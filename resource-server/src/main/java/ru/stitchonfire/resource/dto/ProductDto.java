package ru.stitchonfire.resource.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import ru.stitchonfire.resource.state.ProductState;
import ru.stitchonfire.resource.state.ProductType;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProductDto {
    UUID id;
    ProductState productState;
    ProductType productType;
    String productProducer;
    String tittle;
    String characteristics;
    String characteristicsShort;
    String description;
    Long price;
    List<String> imagesIds;
    Instant creationTimestamp;

    public String getProductType() {
        return this.productType.getName();
    }
}
