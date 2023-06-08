package ru.stitchonfire.resource.mapper;

import org.mapstruct.Mapper;
import ru.stitchonfire.resource.dto.ReviewDto;
import ru.stitchonfire.resource.model.Review;

@Mapper(componentModel = "spring")
public interface ReviewMapper {
    ReviewDto map(Review review);
}
