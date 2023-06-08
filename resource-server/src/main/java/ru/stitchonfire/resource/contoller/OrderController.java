package ru.stitchonfire.resource.contoller;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import ru.stitchonfire.resource.dto.OrderDto;
import ru.stitchonfire.resource.service.OrderService;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("api/v1/orders")
@PreAuthorize("isAuthenticated()")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OrderController {
    OrderService orderService;

    @GetMapping
    public ResponseEntity<List<OrderDto>> getActiveOrders(Authentication authentication) {
        return orderService.getActiveOrders(authentication.getName());
    }

    @GetMapping("history")
    public ResponseEntity<List<OrderDto>> getHistoryOrders(@RequestParam int page, Authentication authentication) {
        return orderService.getHistoryOrders(authentication.getName(), page);
    }

    @GetMapping("{id}")
    public ResponseEntity<OrderDto> getOrderById(@PathVariable String id, Authentication authentication) {
        return orderService.getOrderById(id, authentication.getName());
    }

    @PostMapping("create")
    public ResponseEntity<OrderDto> createOrder(Authentication authentication) {
        return orderService.createOrder(authentication.getName());
    }
}
