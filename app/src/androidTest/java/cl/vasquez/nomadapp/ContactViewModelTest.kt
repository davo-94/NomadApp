package cl.vasquez.nomadapp

import android.app.Application
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import cl.vasquez.nomadapp.viewmodel.ContactViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(AndroidJUnit4::class)
class ContactViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var viewModel: ContactViewModel

    @Before
    fun setup() {
        val app = ApplicationProvider.getApplicationContext<Application>()
        viewModel = ContactViewModel(app)
    }

    @Test
    fun `validarContacto retorna false con datos invalidos`() {
        val result = viewModel.validarContacto(
            nombre = "",
            correo = "correo-malo",
            mensaje = ""
        )

        assertFalse(result)
    }

    @Test
    fun `validarContacto retorna true con datos validos`() {
        val result = viewModel.validarContacto(
            nombre = "Juan Perez",
            correo = "juan@test.com",
            mensaje = "Hola, este es un mensaje valido"
        )

        assertTrue(result)
    }

}
