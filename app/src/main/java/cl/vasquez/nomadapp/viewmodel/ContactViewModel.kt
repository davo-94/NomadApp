package cl.vasquez.nomadapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import cl.vasquez.nomadapp.data.AppDatabase
import cl.vasquez.nomadapp.data.Contact
import cl.vasquez.nomadapp.model.ContactRepository
import cl.vasquez.nomadapp.utils.ValidationUtils
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
class ContactViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: ContactRepository
    private val _contacts = MutableStateFlow<List<Contact>>(emptyList())
    val contacts: StateFlow<List<Contact>> = _contacts

    init {
        val dao = AppDatabase.getDatabase(application).contactDao()
        repository = ContactRepository(dao)
        loadContacts()
    }
    // Validación usando ValidationUtils
    fun validarContacto(nombre: String, correo: String, mensaje: String): Boolean {
        return ValidationUtils.isValidNombre(nombre) &&
                ValidationUtils.isValidEmail(correo) &&
                ValidationUtils.isValidMensaje(mensaje)
    }
    //Guarda el contacto si pasa las validaciones
    fun addContact(nombre: String, correo: String, pais: String, mensaje: String) {
        viewModelScope.launch {
            if (validarContacto(nombre, correo, mensaje)) {
                repository.insert(
                    Contact(
                        nombre = nombre,
                        correo = correo,
                        pais = pais,
                        mensaje = mensaje
                    )
                )
                loadContacts()
            }
        }
    }
    private fun loadContacts() {
        viewModelScope.launch {
            _contacts.value = repository.getAll()
        }
    }
    fun deleteContact(contact: Contact) {
        viewModelScope.launch {
            repository.delete(contact)
            loadContacts()
        }
    }
}
