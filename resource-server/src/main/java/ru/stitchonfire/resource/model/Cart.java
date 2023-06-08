package ru.stitchonfire.resource.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.*;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Cart {
    @Id
    @Column(name = "id")
    UUID id;

    @MapsId
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "id", foreignKey = @ForeignKey(name = "user-details_cart_fk"))
    UserDetails userDetails;

    @JsonIgnore
    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
    List<Position> positions = new ArrayList<>();

    public void addPosition(Position position) {
        if (!positions.contains(position)) {
            positions.add(position);
            position.setCart(this);
        }
    }

    public void removePosition(Position position) {
        if (positions.contains(position)) {
            positions.remove(position);
            position.setCart(null);
        }
    }

    public Optional<Position> getPositionById(String id) {
        return positions.stream().filter(position -> position.getProduct().getId().toString().equals(id)).findFirst();
    }
}
