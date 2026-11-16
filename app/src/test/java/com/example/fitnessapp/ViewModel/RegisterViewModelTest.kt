package com.example.fitnessapp.ViewModel

import com.example.domain.model.RegisterRequest
import com.example.domain.model.RegisterResult
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
class RegisterViewModelTest {

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
    fun `register success sets isSuccess true`() = runTest {
        val fakeRepo = FakeAuthRepository(
            registerResult = Result.success(
                RegisterResult(1, "wdiaz", "wandyl@test.com", "Usuario registrado exitosamente")
            )
        )
        val viewModel = RegisterViewModel(
            AuthService(fakeRepo),
            ioDispatcher = TODO()
        )

        viewModel.register(
            RegisterRequest(
                identityDocument = "1098765432",
                fullName = "Wandyl Diaz",
                username = "wdiaz",
                email = "wandyl@test.com",
                password = "secret123",
                weight = 75.5,
                height = 1.78
            )
        )
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertTrue(state.isSuccess)
        assertFalse(state.isLoading)
        assertNull(state.error)
    }

    @Test
    fun `register conflict sets error detail`() = runTest {
        val fakeRepo = FakeAuthRepository(
            registerResult = Result.failure(
                Exception("El usuario o correo ya se encuentra registrado.")
            )
        )
        val viewModel = RegisterViewModel(
            AuthService(fakeRepo),
            ioDispatcher = TODO()
        )

        viewModel.register(
            RegisterRequest(
                identityDocument = "1",
                fullName = "Nombre",
                username = "wdiaz",
                email = "w@t.com",
                password = "p",
                weight = 1.0,
                height = 1.0
            )
        )
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertFalse(state.isSuccess)
        assertEquals("El usuario o correo ya se encuentra registrado.", state.error)
    }
}