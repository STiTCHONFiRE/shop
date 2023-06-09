package ru.stitchonfire.resource.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.stitchonfire.resource.dto.ReviewDto;
import ru.stitchonfire.resource.model.Review;

@Mapper(componentModel = "spring")
public interface ReviewMapper {
    @Mapping(target = "username", source = "review.orderPosition.order.username")
    @Mapping(target = "dateOfPurchase", source = "review.orderPosition.order.creationTimestamp")
    ReviewDto map(Review review);
}
