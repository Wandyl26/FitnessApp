package com.example.domain.service

import com.example.domain.model.LoginRequest
import com.example.domain.model.LoginResult
import com.example.domain.model.RegisterRequest
import com.example.domain.model.RegisterResult
import com.example.domain.repository.AuthRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class AuthServiceTest {

    private lateinit var repository: AuthRepository
    private lateinit var service: AuthService

    @Before
    fun setUp() {
        repository = mockk()
        service = AuthService(repository)
    }

    @Test
    fun `login delegates to repository and returns result`() = runTest {
        // Given
        val request = LoginRequest(username = "wdiaz", password = "secret123")
        val expected = LoginResult(
            token = "mock-jwt-token-dotnet-2026",
            username = "wdiaz",
            message = "Autenticación exitosa"
        )
        coEvery { repository.login(request) } returns Result.success(expected)

        // When
        val result = service.login(request)

        // Then
        assertTrue(result.isSuccess)
        assertEquals(expected, result.getOrNull())
        coVerify(exactly = 1) { repository.login(request) }
    }

    @Test
    fun `login propagates repository failure`() = runTest {
        val request = LoginRequest("wdiaz", "wrong")
        coEvery { repository.login(request) } returns
                Result.failure(Exception("Credenciales inválidas"))

        val result = service.login(request)

        assertTrue(result.isFailure)
        assertEquals("Credenciales inválidas", result.exceptionOrNull()?.message)
    }

    @Test
    fun `register delegates to repository and returns result`() = runTest {
        val request = RegisterRequest(
            identityDocument = "1098765432",
            fullName = "Wandyl Diaz",
            username = "wdiaz",
            email = "wandyl@test.com",
            password = "secret123",
            weight = 75.5,
            height = 1.78
        )
        val expected = RegisterResult(
            id = 1,
            username = "wdiaz",
            email = "wandyl@test.com",
            message = "Usuario registrado exitosamente"
        )
        coEvery { repository.register(request) } returns Result.success(expected)

        val result = service.register(request)

        assertTrue(result.isSuccess)
        assertEquals(expected, result.getOrNull())
        coVerify(exactly = 1) { repository.register(request) }
    }

    @Test
    fun `getStoredToken delegates to repository`() = runTest {
        coEvery { repository.getStoredToken() } returns "token-123"

        val token = service.getStoredToken()

        assertEquals("token-123", token)
    }
}