package cl.vasquez.nomadapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import cl.vasquez.nomadapp.data.AppDatabase
import cl.vasquez.nomadapp.model.LoginRepository
import cl.vasquez.nomadapp.model.LoginResult
import cl.vasquez.nomadapp.data.User
import cl.vasquez.nomadapp.data.UserDao
import cl.vasquez.nomadapp.data.SessionManager
import cl.vasquez.nomadapp.utils.ValidationUtils
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class LoginUiState(
    val email: String = "",
    val password: String = "",
    val emailError: String? = null,
    val passwordError: String? = null,
    val isLoading: Boolean = false,
    val loginResult: LoginResult = LoginResult.Idle,
    val passwordVisible: Boolean = false,
    val rememberSession: Boolean = false
)

class LoginViewModel(application: Application): AndroidViewModel(application) {

    private val userDao: UserDao = AppDatabase.getDatabase(application).userDao()
    private val repository = LoginRepository(userDao)

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()
    // modo de prueba local (no usa DB)
    private var testMode: Boolean = false

    // usuarios de prueba en memoria
    private val testUsers = listOf(
        User(email = "admin@nomadapp.com", password = "123456", role = "admin"),
        User(email = "user@nomadapp.com", password = "password", role = "guest")
    )

    fun onEmailChange(email: String) {
        _uiState.value = _uiState.value.copy(email = email, emailError = null)
    }

    fun onPasswordChange(password: String) {
        _uiState.value = _uiState.value.copy(password = password, passwordError = null)
    }

    fun togglePasswordVisibility() {
        _uiState.value = _uiState.value.copy(passwordVisible = !_uiState.value.passwordVisible)
    }

    fun toggleRememberSession() {
        _uiState.value = _uiState.value.copy(rememberSession = !_uiState.value.rememberSession)
    }

    private fun validateFields(): Boolean {
        val emailError = ValidationUtils.getEmailErrorMessage(_uiState.value.email)
        val passwordError = ValidationUtils.getPasswordErrorMessage(_uiState.value.password)

        _uiState.value = _uiState.value.copy(emailError = emailError, passwordError = passwordError)
        return emailError == null && passwordError == null
    }

    fun onLoginClick() {
        if (!validateFields()) return

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, loginResult = LoginResult.Loading)
            val result = if (testMode) {
                // autenticar contra lista en memoria
                val u = testUsers.find { it.email.equals(_uiState.value.email, ignoreCase = true) && it.password == _uiState.value.password }
                if (u != null) LoginResult.Success(u) else LoginResult.Error("Email o contraseña incorrectos (modo prueba)")
            } else {
                repository.login(_uiState.value.email, _uiState.value.password)
            }
            
            // Guardar sesión si el login fue exitoso
            if (result is LoginResult.Success) {
                SessionManager.saveUserSession(result.user.email, result.user.role, _uiState.value.rememberSession)
            }
            
            _uiState.value = _uiState.value.copy(isLoading = false, loginResult = result)
        }
    }

    fun toggleTestMode() {
        testMode = !testMode
        // show a small message in email field to indicate mode
        _uiState.value = _uiState.value.copy(emailError = if (testMode) "Modo prueba activo" else null)
    }

    fun resetLoginResult() {
        _uiState.value = _uiState.value.copy(loginResult = LoginResult.Idle)
    }

    fun onLogoutClick() {
        viewModelScope.launch {
            SessionManager.logout()
            resetLoginResult()
        }
    }
}
