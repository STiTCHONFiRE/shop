package ru.stitchonfire.resource.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.stitchonfire.resource.model.Order;
import ru.stitchonfire.resource.state.OrderState;

import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

public interface OrderRepository extends JpaRepository<Order, UUID> {

    Slice<Order> findByOrderByOrderStateDesc(Pageable pageable);
    Optional<Order> findByIdAndUsername(UUID id, String s);

    Stream<Order> findAllByUsernameAndOrderStateNotAndOrderStateNot(String s, OrderState s1, OrderState s2);

    @Query("select o from Order o where o.username = ?1 and (o.orderState = ?2 or o.orderState = ?3) order by o.creationTimestamp desc")
    Slice<Order> findHistory(String s, OrderState s1, OrderState s2, Pageable pageable);
}
