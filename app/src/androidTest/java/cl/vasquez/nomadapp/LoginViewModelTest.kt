package cl.vasquez.nomadapp

import cl.vasquez.nomadapp.data.SessionManager
import android.app.Application
import cl.vasquez.nomadapp.model.LoginResult
import cl.vasquez.nomadapp.viewmodel.LoginViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.runner.RunWith
import androidx.test.core.app.ApplicationProvider


@RunWith(AndroidJUnit4::class)
@OptIn(ExperimentalCoroutinesApi::class)
class LoginViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var viewModel: LoginViewModel

    @Before
    fun setup() {
        val app = ApplicationProvider.getApplicationContext<Application>()

        // Inicialización obligatoria del singleton
        SessionManager.initialize(app)

        viewModel = LoginViewModel(app)
    }


    // Email vacío
    @Test
    fun `email vacio muestra error`() = runTest {
        viewModel.onEmailChange("")
        viewModel.onPasswordChange("1234")

        viewModel.onLoginClick()
        advanceUntilIdle()

        val state = viewModel.uiState.value
        Assert.assertEquals("El email no puede estar vacío", state.emailError)
    }

    // Password vacío
    @Test
    fun `password vacio muestra error`() = runTest {
        viewModel.onEmailChange("test@test.com")
        viewModel.onPasswordChange("")

        viewModel.onLoginClick()
        advanceUntilIdle()

        val state = viewModel.uiState.value
        Assert.assertEquals("La contraseña no puede estar vacía", state.passwordError)
    }

    // Toggle visibilidad de contraseña
    @Test
    fun `toggle password visibility cambia estado`() = runTest {
        val initialState = viewModel.uiState.value.passwordVisible

        viewModel.togglePasswordVisibility()
        advanceUntilIdle()

        val newState = viewModel.uiState.value.passwordVisible
        Assert.assertNotEquals(initialState, newState)
    }

    // Toggle recordar sesión
    @Test
    fun `toggle remember session cambia estado`() = runTest {
        val initialState = viewModel.uiState.value.rememberSession

        viewModel.toggleRememberSession()
        advanceUntilIdle()

        val newState = viewModel.uiState.value.rememberSession
        Assert.assertNotEquals(initialState, newState)
    }

    // Reset de resultado de login
    @Test
    fun `reset login result vuelve a Idle`() = runTest {
        viewModel.resetLoginResult()
        advanceUntilIdle()

        val state = viewModel.uiState.value
        Assert.assertTrue(state.loginResult is LoginResult.Idle)
    }
}