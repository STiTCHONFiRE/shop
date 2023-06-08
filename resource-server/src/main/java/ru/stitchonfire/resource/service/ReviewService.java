package ru.stitchonfire.resource.service;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.stitchonfire.resource.dto.ReviewDto;
import ru.stitchonfire.resource.dto.ReviewsStatDto;
import ru.stitchonfire.resource.mapper.ReviewMapper;
import ru.stitchonfire.resource.model.OrderPosition;
import ru.stitchonfire.resource.model.Review;
import ru.stitchonfire.resource.repository.OrderPositionRepository;
import ru.stitchonfire.resource.repository.ReviewRepository;
import ru.stitchonfire.resource.state.ReviewRating;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ReviewService {
    OrderPositionRepository orderPositionRepository;
    ReviewRepository reviewRepository;
    ReviewMapper reviewMapper;

    public ResponseEntity<ReviewsStatDto> getStatById(String id) {
        UUID uuid = UUID.fromString(id);
        if (reviewRepository.existsByProduct_Id(uuid)) {
            return ResponseEntity.ok(ReviewsStatDto.builder()
                    .n(reviewRepository.countOfReviews(uuid))
                    .pc(new Long[]{
                            reviewRepository.countOfRating(uuid, ReviewRating.VERY_BAD),
                            reviewRepository.countOfRating(uuid, ReviewRating.BAD),
                            reviewRepository.countOfRating(uuid, ReviewRating.NORMAL),
                            reviewRepository.countOfRating(uuid, ReviewRating.GOOD),
                            reviewRepository.countOfRating(uuid, ReviewRating.VERY_GOOD)
                    })
                    .build()
            );
        }

        return ResponseEntity.notFound().build();
    }

    public ResponseEntity<List<ReviewDto>> getReviewsById(String id, int page) {
        return ResponseEntity.ok(reviewRepository.findByProduct_Id(UUID.fromString(id), PageRequest.of(page, 5))
                .get()
                .map(reviewMapper::map)
                .toList()
        );
    }

    public ResponseEntity<ReviewDto> createReview(String username, String id, String orderId, String text, int rating) {
        return orderPositionRepository.findByOrder_IdAndProduct_IdAndOrder_Username(UUID.fromString(orderId), UUID.fromString(id), username).map(orderPosition -> {
            orderPosition.setReview(
                    Review.builder()
                            .reviewText(text)
                            .username(username)
                            .reviewRating(ReviewRating.values()[rating])
                            .dateOfPurchase(orderPosition.getOrder().getCreationTimestamp())
                            .product(orderPosition.getProduct())
                            .orderPosition(orderPosition)
                            .build()
            );

            OrderPosition orderPositionSaved = orderPositionRepository.saveAndFlush(orderPosition);
            return ResponseEntity.ok(reviewMapper.map(orderPositionSaved.getReview()));
        }).orElse(ResponseEntity.notFound().build());
    }
}
