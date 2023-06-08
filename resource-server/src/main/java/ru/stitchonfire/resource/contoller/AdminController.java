package ru.stitchonfire.resource.contoller;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import ru.stitchonfire.resource.dto.OrderDto;
import ru.stitchonfire.resource.dto.OrderForAdminDto;
import ru.stitchonfire.resource.service.OrderService;
import ru.stitchonfire.resource.state.OrderState;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("api/v1/admin")
@PreAuthorize("hasAuthority('admin')")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AdminController {
    OrderService orderService;

    @GetMapping("orders")
    public ResponseEntity<List<OrderForAdminDto>> getOrders(@RequestParam int page) {
        return orderService.getAllOrders(page);
    }

    @PostMapping("orders/{id}")
    public ResponseEntity<HttpStatus> updateOrder(@PathVariable String id, @RequestParam String orderState) {
        return this.orderService.updateOrder(id, OrderState.valueOf(orderState));
    }
}
