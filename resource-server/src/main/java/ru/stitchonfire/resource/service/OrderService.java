package ru.stitchonfire.resource.service;

import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.stitchonfire.resource.dto.OrderDto;
import ru.stitchonfire.resource.dto.OrderForAdminDto;
import ru.stitchonfire.resource.dto.OrderPositionDto;
import ru.stitchonfire.resource.mapper.OrderMapper;
import ru.stitchonfire.resource.mapper.ProductMapper;
import ru.stitchonfire.resource.mapper.ReviewMapper;
import ru.stitchonfire.resource.model.Cart;
import ru.stitchonfire.resource.model.Order;
import ru.stitchonfire.resource.model.OrderPosition;
import ru.stitchonfire.resource.repository.OrderRepository;
import ru.stitchonfire.resource.repository.UserDetailsRepository;
import ru.stitchonfire.resource.state.OrderState;
import ru.stitchonfire.resource.state.ProductState;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OrderService {
    OrderRepository orderRepository;
    UserDetailsRepository userDetailsRepository;
    ProductMapper productMapper;
    OrderMapper orderMapper;

    public ResponseEntity<HttpStatus> updateOrder(String id, OrderState orderState) {
        return orderRepository.findById(UUID.fromString(id)).map(order -> {
            if (!order.getOrderState().equals(OrderState.CANCELED) && !order.getOrderState().equals(OrderState.DONE)) {
                order.setOrderState(orderState);
                orderRepository.save(order);

                return ResponseEntity.ok(HttpStatus.OK);
            }

            return ResponseEntity.badRequest().body(HttpStatus.BAD_REQUEST);
        }).orElse(ResponseEntity.notFound().build());
    }

    @Transactional
    public ResponseEntity<List<OrderForAdminDto>> getAllOrders(int page) {
        return ResponseEntity.ok(orderRepository.findByOrderByOrderStateDesc(PageRequest.of(page, 5)).get()
                .map(order ->
                        OrderForAdminDto.builder()
                                .orderState(order.getOrderState())
                                .id(order.getId())
                                .instant(order.getCreationTimestamp())
                                .products(
                                        order.getOrderPositions()
                                                .stream()
                                                .map(orderPosition -> OrderPositionDto.builder()
                                                        .serialCode(orderPosition.getSerialCode())
                                                        .product(productMapper.shortMap(orderPosition.getProduct()))
                                                        .n(orderPosition.getN())
                                                        .build()
                                                )
                                                .toList()
                                )
                                .build()
                ).toList()
        );
    }

    public ResponseEntity<OrderDto> getOrderById(String orderId, String username) {
        return orderRepository.findByIdAndUsername(UUID.fromString(orderId), username)
                .map(order -> ResponseEntity.ok(orderMapper.map(order)
                        /*OrderDto.builder()
                                .orderState(order.getOrderState())
                                .id(order.getId())
                                .instant(order.getCreationTimestamp())
                                .products(
                                        order.getOrderPositions()
                                                .stream()
                                                .map(orderPosition -> OrderPositionDto.builder()
                                                        .serialCode(orderPosition.getSerialCode())
                                                        .product(productMapper.shortMap(orderPosition.getProduct()))
                                                        .review(reviewMapper.map(orderPosition.getReview()))
                                                        .n(orderPosition.getN())
                                                        .build()
                                                )
                                                .toList()
                                )
                                .build()*/
                ))
                .orElse(ResponseEntity.notFound().build());
    }

    @Transactional
    public ResponseEntity<List<OrderDto>> getActiveOrders(String username) {
        return ResponseEntity.ok(orderRepository.findAllByUsernameAndOrderStateNotAndOrderStateNot(username, OrderState.DONE, OrderState.CANCELED)
                .map(orderMapper::mapIgnoreReview
                        /*OrderDto.builder()
                                .orderState(order.getOrderState())
                                .id(order.getId())
                                .instant(order.getCreationTimestamp())
                                .products(
                                        order.getOrderPositions()
                                                .stream()
                                                .map(orderPosition -> OrderPositionDto.builder()
                                                        .serialCode(orderPosition.getSerialCode())
                                                        .product(productMapper.shortMap(orderPosition.getProduct()))
                                                        .n(orderPosition.getN())
                                                        .build()
                                                )
                                                .toList()
                                )
                                .build() */
                )
                .toList());
    }

    public ResponseEntity<List<OrderDto>> getHistoryOrders(String username, int page) {
        return ResponseEntity.ok(orderRepository.findHistory(username, OrderState.DONE, OrderState.CANCELED, PageRequest.of(page, 3)).get()
                .map(orderMapper::mapIgnoreReview
                        /*OrderDto.builder()
                                .orderState(order.getOrderState())
                                .id(order.getId())
                                .instant(order.getCreationTimestamp())
                                .products(
                                        order.getOrderPositions()
                                                .stream()
                                                .map(orderPosition -> OrderPositionDto.builder()
                                                        .serialCode(orderPosition.getSerialCode())
                                                        .product(productMapper.shortMap(orderPosition.getProduct()))
                                                        .n(orderPosition.getN())
                                                        .build()
                                                )
                                                .toList()
                                )
                                .build() */
                )
                .toList());
    }

    public ResponseEntity<OrderDto> createOrder(String username) {
        return userDetailsRepository.findByUsername(username).map(userDetails -> {
            if (userDetails.getCart().getPositions().isEmpty()) {
                return ResponseEntity.badRequest().body((OrderDto) null);
            }

            Order order = Order.builder()
                    .orderState(OrderState.FORMATION)
                    .username(username)
                    .build();

            order.setOrderPositions(
                    userDetails.getCart().getPositions()
                            .stream()
                            .filter(position -> position.getProduct().getProductState().equals(ProductState.ACTIVE))
                            .map(position -> {
                                if (position.getProduct().getN() <= position.getN()) {
                                    OrderPosition orderPosition = OrderPosition.builder()
                                            .product(position.getProduct())
                                            .order(order)
                                            .serialCode(UUID.randomUUID().toString())
                                            .n(position.getProduct().getN())
                                            .build();

                                    position.getProduct().setProductState(ProductState.OUT_OF_STOCK);
                                    position.getProduct().setN(0L);

                                    return orderPosition;
                                }

                                position.getProduct().setN(position.getProduct().getN() - position.getN());

                                return OrderPosition.builder()
                                        .product(position.getProduct())
                                        .order(order)
                                        .n(position.getN())
                                        .build();
                            })
                            .toList()
            );

            if (order.getOrderPositions().isEmpty()) {
                return ResponseEntity.badRequest().body((OrderDto) null);
            }

            Order orderSaved = orderRepository.saveAndFlush(order);

            userDetails.setCart(null);
            userDetailsRepository.save(userDetails);
            userDetails.setCart(Cart.builder().userDetails(userDetails).build());
            userDetailsRepository.save(userDetails);
            return ResponseEntity.ok(orderMapper.mapIgnoreReview(orderSaved)
                    /*OrderDto.builder()
                            .orderState(orderSaved.getOrderState())
                            .id(orderSaved.getId())
                            .instant(order.getCreationTimestamp())
                            .products(
                                    orderSaved.getOrderPositions()
                                            .stream()
                                            .map(orderPosition -> OrderPositionDto.builder()
                                                    .serialCode(orderPosition.getSerialCode())
                                                    .product(productMapper.shortMap(orderPosition.getProduct()))
                                                    .n(orderPosition.getN())
                                                    .build()
                                            )
                                            .toList()
                            )
                            .build()*/
            );
        }).orElse(ResponseEntity.badRequest().build());
    }
}
