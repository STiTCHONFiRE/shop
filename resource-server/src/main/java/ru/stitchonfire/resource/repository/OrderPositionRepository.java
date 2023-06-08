package ru.stitchonfire.resource.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.stitchonfire.resource.model.OrderPosition;

import java.util.Optional;
import java.util.UUID;

public interface OrderPositionRepository extends JpaRepository<OrderPosition, UUID> {
    Optional<OrderPosition> findByOrder_IdAndProduct_IdAndOrder_Username(UUID id1, UUID id2, String username);
}
