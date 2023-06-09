package ru.stitchonfire.resource.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.stitchonfire.resource.state.ProductState;
import ru.stitchonfire.resource.state.ProductType;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProductShortDto {
    UUID id;
    ProductState productState;
    ProductType productType;
    String productProducer;
    String tittle;
    String characteristicsShort;
    Double price;
    List<String> imagesIds;
    Instant creationTimestamp;
    public String getProductType() {
        return productType.getName();
    }
}
