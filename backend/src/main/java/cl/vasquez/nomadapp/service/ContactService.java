package cl.vasquez.nomadapp.service;

import cl.vasquez.nomadapp.dto.ContactRequest;
import cl.vasquez.nomadapp.entity.Contact;
import cl.vasquez.nomadapp.entity.User;
import cl.vasquez.nomadapp.repository.ContactRepository;
import cl.vasquez.nomadapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ContactService {

    @Autowired
    private ContactRepository contactRepository;

    @Autowired
    private UserRepository userRepository;

    public Contact createContact(ContactRequest request, Long userId) {
        Optional<User> userOpt = userRepository.findById(userId);

        if (userOpt.isEmpty()) {
            throw new RuntimeException("Usuario no encontrado");
        }

        Contact contact = Contact.builder()
                .name(request.getName())
                .email(request.getEmail())
                .phone(request.getPhone())
                .address(request.getAddress())
                .user(userOpt.get())
                .build();

        return contactRepository.save(contact);
    }

    public Optional<Contact> getContactById(Long id) {
        return contactRepository.findById(id);
    }

    public List<Contact> getUserContacts(Long userId) {
        return contactRepository.findByUserIdOrderByCreatedAtDesc(userId);
    }

    public Contact updateContact(Long contactId, ContactRequest request, Long userId) {
        Optional<Contact> contactOpt = contactRepository.findById(contactId);

        if (contactOpt.isEmpty()) {
            throw new RuntimeException("Contacto no encontrado");
        }

        Contact contact = contactOpt.get();

        // Verificar que el usuario sea el propietario
        if (!contact.getUser().getId().equals(userId)) {
            throw new RuntimeException("No tienes permiso para editar este contacto");
        }

        contact.setName(request.getName());
        contact.setEmail(request.getEmail());
        contact.setPhone(request.getPhone());
        contact.setAddress(request.getAddress());
        contact.setUpdatedAt(LocalDateTime.now());

        return contactRepository.save(contact);
    }

    public void deleteContact(Long contactId, Long userId) {
        Optional<Contact> contactOpt = contactRepository.findById(contactId);

        if (contactOpt.isEmpty()) {
            throw new RuntimeException("Contacto no encontrado");
        }

        Contact contact = contactOpt.get();

        // Verificar que el usuario sea el propietario
        if (!contact.getUser().getId().equals(userId)) {
            throw new RuntimeException("No tienes permiso para eliminar este contacto");
        }

        contactRepository.delete(contact);
    }
}
