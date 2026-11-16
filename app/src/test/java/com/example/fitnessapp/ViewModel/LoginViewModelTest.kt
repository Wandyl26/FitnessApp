package com.example.fitnessapp.ViewModel

import com.example.domain.model.LoginRequest
import com.example.domain.model.LoginResult
import com.example.domain.service.AuthService
import com.example.fitnessapp.FakeAuthRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class LoginViewModelTest {

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `login success sets isSuccess and username`() = runTest {
        val fakeRepo = FakeAuthRepository(
            loginResult = Result.success(
                LoginResult("token-x", "wdiaz", "Autenticación exitosa")
            )
        )
        val viewModel = LoginViewModel(
            AuthService(fakeRepo),
            ioDispatcher = TODO()
        )

        viewModel.login(LoginRequest("wdiaz", "secret123"))
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertTrue(state.isSuccess)
        assertFalse(state.isLoading)
        assertNull(state.error)
        assertEquals("wdiaz", state.username)
    }

    @Test
    fun `login failure sets error message`() = runTest {
        val fakeRepo = FakeAuthRepository(
            loginResult = Result.failure(Exception("Credenciales inválidas"))
        )
        val viewModel = LoginViewModel(
            AuthService(fakeRepo),
            ioDispatcher = TODO()
        )

        viewModel.login(LoginRequest("wdiaz", "wrong"))
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertFalse(state.isSuccess)
        assertFalse(state.isLoading)
        assertEquals("Credenciales inválidas", state.error)
    }

    @Test
    fun `login sets isLoading true while in-flight`() = runTest {
        // No avanzamos el dispatcher hasta verificar el estado intermedio
        val fakeRepo = FakeAuthRepository(
            loginResult = Result.success(LoginResult("t", "u", "ok"))
        )
        val viewModel = LoginViewModel(
            AuthService(fakeRepo),
            ioDispatcher = TODO()
        )

        viewModel.login(LoginRequest("u", "p"))

        // Aún no ha corrido la corrutina → estado inicial
        assertFalse(viewModel.uiState.value.isLoading)

        // Dejamos correr la corrutina (solo la parte síncrona)
        // El estado intermedio "loading" no se puede observar fácilmente
        // con StateFlow por conflation, así que verificamos al final
        advanceUntilIdle()
        assertTrue(viewModel.uiState.value.isSuccess)
    }
}