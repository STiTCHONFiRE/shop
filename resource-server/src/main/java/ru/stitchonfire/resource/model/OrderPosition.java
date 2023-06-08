package ru.stitchonfire.resource.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.GenericGenerator;

import java.util.UUID;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderPosition {
    @Id
    @GeneratedValue(generator = "uuid3")
    @GenericGenerator(name = "uuid3", strategy = "org.hibernate.id.UUIDGenerator")
    UUID id;

    @OneToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn(name = "product_id", referencedColumnName = "id", nullable = false, foreignKey = @ForeignKey(name = "position_product_fk"))
    Product product;

    @Column
    Long n;

    @Column
    String serialCode;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(
            name = "order_id",
            referencedColumnName = "id",
            nullable = false,
            foreignKey = @ForeignKey(
                    name = "order_order_position_fk"
            )
    )
    Order order;

    @OneToOne(mappedBy = "orderPosition", cascade = CascadeType.ALL, orphanRemoval = true)
    Review review;
}
