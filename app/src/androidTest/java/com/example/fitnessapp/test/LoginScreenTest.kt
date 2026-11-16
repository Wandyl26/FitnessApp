package com.example.fitnessapp.test

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.example.fitnessapp.view.Screen.LoginScreen
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class LoginScreenTest {

    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun showsTitleAndFields() {
        composeRule.setContent {
            LoginScreen(
                onNavigateToRegister = {},
                onLoginSuccess = {},
                viewModel = androidx.hilt.navigation.compose.hiltViewModel()
            )
        }

        composeRule.onNodeWithText("Bienvenido").assertIsDisplayed()
        composeRule.onNodeWithText("Usuario").assertIsDisplayed()
        composeRule.onNodeWithText("Contraseña").assertIsDisplayed()
        composeRule.onNodeWithText("Iniciar Sesión").assertIsDisplayed()
    }

    @Test
    fun clickingRegisterText_triggersCallback() {
        var navigated = false
        composeRule.setContent {
            LoginScreen(
                onNavigateToRegister = { navigated = true },
                onLoginSuccess = {},
                viewModel = androidx.hilt.navigation.compose.hiltViewModel()
            )
        }

        composeRule.onNodeWithText("¿No tienes cuenta? Regístrate").performClick()
        assertTrue(navigated)
    }
}