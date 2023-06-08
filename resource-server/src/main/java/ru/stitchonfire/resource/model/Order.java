package ru.stitchonfire.resource.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import ru.stitchonfire.resource.state.OrderState;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "order_table")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Order {
    @Id
    @GeneratedValue(generator = "uuid3")
    @GenericGenerator(name = "uuid3", strategy = "org.hibernate.id.UUIDGenerator")
    UUID id;

    @Column
    @Enumerated(EnumType.STRING)
    OrderState orderState;

    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "order")
    List<OrderPosition> orderPositions = new ArrayList<>();

    @Column(nullable = false, updatable = false)
    String username;

    @CreationTimestamp
    Instant creationTimestamp;
}
