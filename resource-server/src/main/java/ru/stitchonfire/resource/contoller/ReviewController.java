package ru.stitchonfire.resource.contoller;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import ru.stitchonfire.resource.dto.ReviewDto;
import ru.stitchonfire.resource.dto.ReviewsStatDto;
import ru.stitchonfire.resource.model.Order;
import ru.stitchonfire.resource.repository.OrderRepository;
import ru.stitchonfire.resource.request.ReviewCreateRequest;
import ru.stitchonfire.resource.service.ReviewService;

import java.util.List;
import java.util.UUID;

@RestController
@AllArgsConstructor
@RequestMapping("api/v1/reviews")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ReviewController {
    ReviewService reviewService;

    @GetMapping("{id}/stat")
    public ResponseEntity<ReviewsStatDto> getStatById(@PathVariable String id) {
        return reviewService.getStatById(id);
    }

    @GetMapping("{id}")
    public ResponseEntity<List<ReviewDto>> getReviews(@PathVariable String id, @RequestParam int page) {
        return reviewService.getReviewsById(id, page);
    }

    @PostMapping("{id}/create")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ReviewDto> createReview(@PathVariable String id, Authentication authentication, @RequestBody ReviewCreateRequest request) {
        return reviewService.createReview(authentication.getName(), id, request.orderId(), request.text(), request.rating());
    }
}
