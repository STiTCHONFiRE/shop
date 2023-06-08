package ru.stitchonfire.resource.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.stitchonfire.resource.model.Review;
import ru.stitchonfire.resource.state.ReviewRating;

import java.util.Optional;
import java.util.UUID;

public interface ReviewRepository extends JpaRepository<Review, UUID> {
    Optional<Review> findByProduct_IdAndUsername(UUID id, String username);

    boolean existsByProduct_Id(UUID id);

    @Query("select count(r) from Review r where r.product.id = ?1")
    Long countOfReviews(UUID id);

    @Query("select count(r) from Review r where r.reviewRating = ?2 and r.product.id = ?1")
    Long countOfRating(UUID id, ReviewRating reviewRating);

    @Query("select r from Review r where r.product.id = ?1 order by r.creationTimestamp desc")
    Slice<Review> findByProduct_Id(UUID id, Pageable pageable);
}
