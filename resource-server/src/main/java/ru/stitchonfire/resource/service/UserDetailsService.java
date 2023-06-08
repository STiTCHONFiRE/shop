package ru.stitchonfire.resource.service;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.stitchonfire.resource.dto.PositionDto;
import ru.stitchonfire.resource.dto.UpdateDto;
import ru.stitchonfire.resource.mapper.ProductMapper;
import ru.stitchonfire.resource.model.Cart;
import ru.stitchonfire.resource.model.Position;
import ru.stitchonfire.resource.model.Product;
import ru.stitchonfire.resource.model.UserDetails;
import ru.stitchonfire.resource.repository.ProductRepository;
import ru.stitchonfire.resource.repository.UserDetailsRepository;
import ru.stitchonfire.resource.state.ProductState;

import java.util.*;

@Slf4j
@Service
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserDetailsService {
    UserDetailsRepository userDetailsRepository;
    ProductRepository productRepository;
    ProductMapper productMapper;


    public ResponseEntity<HttpStatus> clearCart(String username) {
        return userDetailsRepository.findByUsername(username)
                .map(userDetails -> {
                    userDetails.setCart(null);
                    userDetailsRepository.save(userDetails);
                    userDetails.setCart(Cart.builder().userDetails(userDetails).build());
                    userDetailsRepository.save(userDetails);
                    return ResponseEntity.ok(HttpStatus.OK);
                }).orElse(ResponseEntity.notFound().build());
    }

    public ResponseEntity<List<PositionDto>> getCart(String username) {
        return userDetailsRepository.findByUsername(username)
                .map(userDetails -> ResponseEntity.ok(userDetails.getCart().getPositions()
                                .stream()
                                .map(position ->
                                        PositionDto.builder()
                                                .n(position.getN())
                                                .isAvailable(position.getProduct().getProductState().equals(ProductState.ACTIVE))
                                                .max(position.getProduct().getN())
                                                .product(productMapper.shortMap(position.getProduct()))
                                                .build())
                                .toList()
                        )
                )
                .orElseGet(() -> {
                    UserDetails userDetails = UserDetails.builder().username(username).build();
                    Cart cart = Cart.builder().userDetails(userDetails).positions(new ArrayList<>()).build();
                    userDetails.setCart(cart);
                    userDetailsRepository.save(userDetails);

                    return ResponseEntity.ok(Collections.emptyList());
                });
    }

    public ResponseEntity<HttpStatus> removeFromCart(String username, String productId) {
        return userDetailsRepository.findByUsername(username).map(userDetails -> {
            Optional<Position> positionOptional = userDetails.getCart().getPositionById(productId);
            return positionOptional.map(position -> {
                userDetails.getCart().removePosition(position);
                userDetailsRepository.save(userDetails);
                return ResponseEntity.ok(HttpStatus.OK);
            }).orElse(ResponseEntity.badRequest().body(HttpStatus.BAD_REQUEST));
        }).orElse(ResponseEntity.notFound().build());
    }

    public ResponseEntity<UpdateDto> updateCart(String username, String productId, Long n) {
        return userDetailsRepository.findByUsername(username)
                .map(userDetails -> userDetails.getCart().getPositionById(productId).map(position -> {
                    if (n < position.getProduct().getN()) {
                        position.setN(n);
                        userDetailsRepository.save(userDetails);
                        return ResponseEntity.ok(
                                UpdateDto.builder()
                                        .n(n)
                                        .isAvailable(position.getProduct().getProductState().equals(ProductState.ACTIVE))
                                        .build()
                        );
                    }

                    if (position.getN().equals(position.getProduct().getN())) {
                        return ResponseEntity.ok(
                                UpdateDto.builder()
                                        .n(position.getN())
                                        .isAvailable(position.getProduct().getProductState().equals(ProductState.ACTIVE))
                                        .build()
                        );
                    }

                    position.setN(position.getProduct().getN());
                    userDetailsRepository.save(userDetails);
                    return ResponseEntity.ok(
                            UpdateDto.builder()
                                    .n(position.getN())
                                    .isAvailable(position.getProduct().getProductState().equals(ProductState.ACTIVE))
                                    .build()
                    );

                }).orElse(ResponseEntity.badRequest().build()))
                .orElse(ResponseEntity.notFound().build());
    }

    public ResponseEntity<HttpStatus> addToCart(String username, String productId) {
        return userDetailsRepository.findByUsername(username).map(userDetails -> {
            Optional<Position> positionOptional = userDetails.getCart().getPositionById(productId);
            if (positionOptional.isPresent()) {
                Position position = positionOptional.get();
                position.setN(position.getN() + 1);
                userDetailsRepository.save(userDetails);

                return ResponseEntity.ok(HttpStatus.OK);
            } else {
                Optional<Product> productOptional = productRepository.findById(UUID.fromString(productId));
                if (productOptional.isPresent()) {
                    Product product = productOptional.get();

                    if (product.getProductState().equals(ProductState.ACTIVE)) {
                        userDetails.getCart().addPosition(Position.builder().product(product).n(1L).build());
                        userDetailsRepository.save(userDetails);
                        return ResponseEntity.ok(HttpStatus.OK);
                    }

                    return ResponseEntity.badRequest().body(HttpStatus.BAD_REQUEST);
                }
            }

            return ResponseEntity.badRequest().body(HttpStatus.BAD_REQUEST);
        }).orElseGet(() -> {
            Optional<Product> productOptional = productRepository.findById(UUID.fromString(productId));
            if (productOptional.isPresent()) {
                Product product = productOptional.get();

                UserDetails userDetails = UserDetails.builder().username(username).build();
                Cart cart = Cart.builder().userDetails(userDetails).positions(new ArrayList<>()).build();
                userDetails.setCart(cart);

                cart.addPosition(Position.builder().product(product).n(1L).build());
                userDetailsRepository.save(userDetails);

                return ResponseEntity.ok(HttpStatus.OK);
            }

            return ResponseEntity.badRequest().body(HttpStatus.BAD_REQUEST);
        });
    }
}
