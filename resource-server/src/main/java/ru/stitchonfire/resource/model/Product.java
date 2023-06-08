package ru.stitchonfire.resource.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import ru.stitchonfire.resource.converter.ListToStringConverter;
import ru.stitchonfire.resource.state.ProductState;
import ru.stitchonfire.resource.state.ProductType;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Product {
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
    UUID id;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    ProductState productState;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    ProductType productType;

    @Column(nullable = false)
    String productProducer;

    @Column(nullable = false)
    String tittle;

    @Column(nullable = false, columnDefinition = "text")
    String characteristicsShort;

    @Column(nullable = false, columnDefinition = "text")
    String characteristics;

    @Column(nullable = false, columnDefinition = "text")
    String description;

    @Column(nullable = false)
    Long price;

    @Column(nullable = false)
    Long n;

    @Convert(converter = ListToStringConverter.class)
    List<String> imagesIds;

    @CreationTimestamp
    Instant creationTimestamp;
}
