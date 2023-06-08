package ru.stitchonfire.resource.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.stitchonfire.resource.model.UserDetails;

import java.util.Optional;
import java.util.UUID;

public interface UserDetailsRepository extends JpaRepository<UserDetails, UUID> {
    Optional<UserDetails> findByUsername(String s);
}
