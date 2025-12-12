package cl.vasquez.nomadapp.repository;

import cl.vasquez.nomadapp.entity.Contact;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContactRepository extends JpaRepository<Contact, Long> {
    List<Contact> findByUserIdOrderByCreatedAtDesc(Long userId);
}
