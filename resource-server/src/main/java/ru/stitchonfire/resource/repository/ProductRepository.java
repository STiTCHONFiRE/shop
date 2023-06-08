package ru.stitchonfire.resource.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.stitchonfire.resource.model.Product;
import ru.stitchonfire.resource.state.ProductState;
import ru.stitchonfire.resource.state.ProductType;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProductRepository extends JpaRepository<Product, UUID> {

    Optional<Product> findProductByIdAndProductState(UUID id, ProductState state);
    Slice<Product> findAllByProductState(ProductState state, Pageable pageable);

    @Query("select p from Product p where p.productState = ?1 and (lower(p.tittle) like lower(concat('%', ?2, '%')) or lower(p.productProducer) like lower(concat('%', ?2, '%')))")
    Slice<Product> findAllWithFilter(ProductState state, String filter, Pageable pageable);

    Slice<Product> findAllByProductTypeAndProductState(ProductType productType, ProductState productState, Pageable pageable);

    @Query("select p from Product p where p.productType = ?1 and p.productState = ?2 and (lower(p.tittle) like lower(concat('%', ?3, '%')) or lower(p.productProducer) like lower(concat('%', ?3, '%')))")
    Slice<Product> findAllWithFilterAndProductType(ProductType productType, ProductState state, String f, Pageable pageable);
}
