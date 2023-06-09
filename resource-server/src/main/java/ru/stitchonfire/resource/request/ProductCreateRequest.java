package ru.stitchonfire.resource.request;

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
        Double price,
        Long n
) {
}
