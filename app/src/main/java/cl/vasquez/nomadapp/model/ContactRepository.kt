package cl.vasquez.nomadapp.model

import cl.vasquez.nomadapp.data.Contact
import cl.vasquez.nomadapp.data.ContactDao

/**
 * Capa que separa la lógica de acceso a datos del resto de la app.
 * -> UI ni ViewModel hablan con la base; solo Repository sabe cómo interactuar con los DAOs.
 * -> Repository vincula DAOs con ViewModel.
 * Compose UI
 * -> No ve DAOs
 * -> No ve la db
 * -> No ve ContactRepository
 * -> Se comunica con el ViewModel
 *
 *  ** Cadena completa
 *  UI -> ViewModel -> Repository -> Dao -> Room(SQLite)
 */

//Inyección de dependencias manual -> Repository puede llamar al dao cuando lo necesite
class ContactRepository(private val dao: ContactDao) {

    //guarda un contacto en la db
    suspend fun insert(contact: Contact) = dao.insert(contact)
    //Obtiene todos los contactos
    suspend fun getAll(): List<Contact> = dao.getAll()
    //Elimina un contacto
    suspend fun delete(contact: Contact) = dao.delete(contact)
}
