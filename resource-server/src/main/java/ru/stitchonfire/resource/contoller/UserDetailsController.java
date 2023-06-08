package ru.stitchonfire.resource.contoller;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import ru.stitchonfire.resource.dto.PositionDto;
import ru.stitchonfire.resource.dto.UpdateDto;
import ru.stitchonfire.resource.request.AddProductToCartRequest;
import ru.stitchonfire.resource.request.RemoveProductFromCartRequest;
import ru.stitchonfire.resource.request.UpdateCartRequest;
import ru.stitchonfire.resource.service.UserDetailsService;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("api/v1/users")
@PreAuthorize("isAuthenticated()")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserDetailsController {
    UserDetailsService userDetailsService;

    @PutMapping("cart/add")
    public ResponseEntity<HttpStatus> addProductToCart(@RequestBody AddProductToCartRequest request, Authentication authentication) {
        return userDetailsService.addToCart(authentication.getName(), request.productId());
    }

    @GetMapping("cart")
    public ResponseEntity<List<PositionDto>> getCart(Authentication authentication) {
        return userDetailsService.getCart(authentication.getName());
    }

    @DeleteMapping("cart/remove")
    public ResponseEntity<HttpStatus> removeProductFromCart(@RequestBody RemoveProductFromCartRequest request, Authentication authentication) {
        return userDetailsService.removeFromCart(authentication.getName(), request.id());
    }

    @DeleteMapping("cart/clear")
    public ResponseEntity<HttpStatus> clearCart(Authentication authentication) {
        return userDetailsService.clearCart(authentication.getName());
    }

    @PostMapping("cart/update")
    public ResponseEntity<UpdateDto> updateCart(@RequestBody UpdateCartRequest request, Authentication authentication) {
        return userDetailsService.updateCart(authentication.getName(), request.id(), request.n());
    }
}
