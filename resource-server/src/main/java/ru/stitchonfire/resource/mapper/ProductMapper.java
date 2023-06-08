package ru.stitchonfire.resource.mapper;

import org.mapstruct.Mapper;
import ru.stitchonfire.resource.dto.ProductDto;
import ru.stitchonfire.resource.dto.ProductShortDto;
import ru.stitchonfire.resource.model.Product;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    ProductDto map(Product product);

    ProductShortDto shortMap(Product product);
}
