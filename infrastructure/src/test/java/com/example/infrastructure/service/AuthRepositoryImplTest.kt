package com.example.infrastructure.service

import com.example.domain.model.LoginRequest
import com.example.domain.model.RegisterRequest
import com.example.infrastructure.api.AuthApi
import com.example.infrastructure.api.dto.LoginResponseDto
import com.example.infrastructure.api.dto.RegisterResponseDto
import com.example.infrastructure.local.dao.AuthTokenDao
import com.example.infrastructure.local.entity.AuthToken
import com.google.gson.Gson
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import retrofit2.Response

class AuthRepositoryImplTest {

    private lateinit var authApi: AuthApi
    private lateinit var authTokenDao: AuthTokenDao
    private lateinit var repository: AuthRepositoryImpl

    @Before
    fun setUp() {
        authApi = mockk()
        authTokenDao = mockk(relaxed = true)
        repository = AuthRepositoryImpl(authApi, authTokenDao, Gson())
    }

    // ------------------- LOGIN -------------------

    @Test
    fun `login success saves token in Room and returns LoginResult`() = runTest {
        // Given
        val request = LoginRequest("wdiaz", "secret123")
        val body = LoginResponseDto(
            token = "mock-jwt-token-dotnet-2026",
            username = "wdiaz",
            message = "Autenticación exitosa"
        )
        coEvery { authApi.login(any()) } returns Response.success(body)

        // When
        val result = repository.login(request)

        // Then
        assertTrue(result.isSuccess)
        val value = result.getOrThrow()
        assertEquals("mock-jwt-token-dotnet-2026", value.token)
        assertEquals("wdiaz", value.username)
        assertEquals("Autenticación exitosa", value.message)

        coVerify(exactly = 1) {
            authTokenDao.insertToken(
                AuthToken(token = "mock-jwt-token-dotnet-2026", username = "wdiaz")
            )
        }
    }

    @Test
    fun `login failure returns failure with backend detail`() = runTest {
        val request = LoginRequest("wdiaz", "wrong")
        val errorJson = """
            {"title":"Unauthorized","status":401,"detail":"Credenciales inválidas"}
        """.trimIndent()
        coEvery { authApi.login(any()) } returns Response.error(
            401, errorJson.toResponseBody(null)
        )

        val result = repository.login(request)

        assertTrue(result.isFailure)
        assertEquals("Credenciales inválidas", result.exceptionOrNull()?.message)
        coVerify(exactly = 0) { authTokenDao.insertToken(any()) }
    }

    @Test
    fun `login network exception is wrapped in failure`() = runTest {
        coEvery { authApi.login(any()) } throws RuntimeException("Network down")

        val result = repository.login(LoginRequest("x", "y"))

        assertTrue(result.isFailure)
        assertEquals("Network down", result.exceptionOrNull()?.message)
    }

    // ------------------- REGISTER -------------------

    @Test
    fun `register success returns RegisterResult`() = runTest {
        val request = RegisterRequest(
            identityDocument = "1098765432",
            fullName = "Wandyl Diaz",
            username = "wdiaz",
            email = "wandyl@test.com",
            password = "secret123",
            weight = 75.5,
            height = 1.78
        )
        val body = RegisterResponseDto(
            id = 1,
            username = "wdiaz",
            email = "wandyl@test.com",
            message = "Usuario registrado exitosamente"
        )
        coEvery { authApi.register(any()) } returns Response.success(body)

        val result = repository.register(request)

        assertTrue(result.isSuccess)
        val value = result.getOrThrow()
        assertEquals(1, value.id)
        assertEquals("wdiaz", value.username)
        assertEquals("wandyl@test.com", value.email)
    }

    @Test
    fun `register conflict returns failure with detail`() = runTest {
        val errorJson = """
            {"title":"Conflict","status":409,"detail":"El usuario o correo ya se encuentra registrado."}
        """.trimIndent()
        coEvery { authApi.register(any()) } returns Response.error(
            409, errorJson.toResponseBody(null)
        )

        val result = repository.register(mockk(relaxed = true))

        assertTrue(result.isFailure)
        assertEquals(
            "El usuario o correo ya se encuentra registrado.",
            result.exceptionOrNull()?.message
        )
    }

    // ------------------- TOKEN -------------------

    @Test
    fun `getStoredToken returns token from Room`() = runTest {
        coEvery { authTokenDao.getToken() } returns AuthToken(
            token = "stored-token", username = "wdiaz"
        )

        assertEquals("stored-token", repository.getStoredToken())
    }

    @Test
    fun `getStoredToken returns null when no token is stored`() = runTest {
        coEvery { authTokenDao.getToken() } returns null

        assertEquals(null, repository.getStoredToken())
    }
}