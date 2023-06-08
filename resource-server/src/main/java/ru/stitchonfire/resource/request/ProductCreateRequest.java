package ru.stitchonfire.resource.request;

import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import org.springframework.web.multipart.MultipartFile;
import ru.stitchonfire.resource.state.ProductState;
import ru.stitchonfire.resource.state.ProductType;

import java.util.List;

public record ProductCreateRequest(
        List<MultipartFile> files,
        ProductType productType,
        ProductState productState,
        String productProducer,
        String tittle,
        String characteristicsShort,
        String characteristics,
        String description,
        Long price,
        Long n
) {
}
