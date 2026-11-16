package com.example.fitnessapp.ViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.di.IoDispatcher
import com.example.domain.model.LoginRequest
import com.example.domain.service.AuthService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authService: AuthService,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher

) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    fun login(request: LoginRequest) {
        viewModelScope.launch(ioDispatcher) {
            _uiState.update { it.copy(isLoading = true, error = null) }
            val result = authService.login(request)
            result.fold(
                onSuccess = { loginResult ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            isSuccess = true,
                            username = loginResult.username
                        )
                    }
                },
                onFailure = { e ->
                    _uiState.update { it.copy(isLoading = false, error = e.message) }
                }
            )
        }
    }
}

data class LoginUiState(
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val error: String? = null,
    val username: String = ""
)