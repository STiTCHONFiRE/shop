package ru.stitchonfire.resource.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import ru.stitchonfire.resource.state.ReviewRating;

import java.time.Instant;
import java.util.UUID;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Review {
    @Id
    @GeneratedValue(generator = "uuid5")
    @GenericGenerator(name = "uuid5", strategy = "org.hibernate.id.UUIDGenerator")
    UUID id;

    @Column(nullable = false)
    String reviewText;

    @Column
    @Enumerated(EnumType.ORDINAL)
    ReviewRating reviewRating;

    @OneToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn(
            name = "order_position_id",
            referencedColumnName = "id",
            nullable = false,
            foreignKey = @ForeignKey(
                    name = "review_order_position_ok"
            )
    )
    OrderPosition orderPosition;

    @CreationTimestamp
    Instant creationTimestamp;
}
