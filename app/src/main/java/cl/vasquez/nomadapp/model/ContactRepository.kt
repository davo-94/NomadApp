package cl.vasquez.nomadapp.model

import cl.vasquez.nomadapp.data.Contact
import cl.vasquez.nomadapp.data.ContactDao

class ContactRepository(private val dao: ContactDao) {
    suspend fun insert(contact: Contact) = dao.insert(contact)
    suspend fun getAll(): List<Contact> = dao.getAll()
    suspend fun delete(contact: Contact) = dao.delete(contact)
}
