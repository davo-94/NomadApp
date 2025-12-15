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

/**
 * LoginViewModel hereda de AndroidViewModel, que da acceso al applicationContext
 * a través de getApplication.
 * -> Este ViewModel necesita contexto de la app para inicializar la db.
 */
class LoginViewModel(application: Application): AndroidViewModel(application) {

    //Se necesita el contexto de la app para crear la base de datos.
    //Se crea el UserDao. El ViewModel lo pasa al Repository.
    private val userDao: UserDao = AppDatabase.getDatabase(application).userDao()
    private val repository = LoginRepository(userDao)

    //_uiState -> mutable, solo el ViewModel lo puede cambiar
    //uiState -> Solo lectra, la Screen lo observa pero no lo puede modificar.
    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

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
        var hasError = false
        
        if (_uiState.value.email.isBlank()) {
            _uiState.value = _uiState.value.copy(emailError = "El email no puede estar vacío")
            hasError = true
        } else {
            _uiState.value = _uiState.value.copy(emailError = null)
        }
        
        if (_uiState.value.password.isBlank()) {
            _uiState.value = _uiState.value.copy(passwordError = "La contraseña no puede estar vacía")
            hasError = true
        } else {
            _uiState.value = _uiState.value.copy(passwordError = null)
        }
        
        return !hasError
    }

    fun onLoginClick() {
        if (!validateFields()) return

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, loginResult = LoginResult.Loading)
            val result = repository.login(
                _uiState.value.email,
                _uiState.value.password)

            
            // Guardar sesión si el login fue exitoso
            if (result is LoginResult.Success) {
                SessionManager.saveUserSession(result.user.email, result.user.role, _uiState.value.rememberSession)
            }
            
            _uiState.value = _uiState.value.copy(isLoading = false, loginResult = result)
        }
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
