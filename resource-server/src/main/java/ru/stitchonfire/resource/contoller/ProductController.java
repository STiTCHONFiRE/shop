package ru.stitchonfire.resource.contoller;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.stitchonfire.resource.dto.ProductDto;
import ru.stitchonfire.resource.dto.ProductShortDto;
import ru.stitchonfire.resource.dto.TypeDto;
import ru.stitchonfire.resource.request.ProductCreateRequest;
import ru.stitchonfire.resource.service.ProductService;

import java.util.List;
import java.util.Optional;

@RestController
@AllArgsConstructor
@RequestMapping("api/v1/products")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProductController {
    ProductService productService;

    @PreAuthorize("hasAuthority('admin')")
    @PostMapping(value = "create", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseEntity<ProductDto> createProduct(@ModelAttribute ProductCreateRequest request) {
        return productService.createProduct(request.files(), request);
    }

    @GetMapping("/details/{id}")
    public ResponseEntity<ProductDto> getProduct(@PathVariable String id) {
        return productService.getProductById(id);
    }

    @GetMapping("public/images/{id}")
    public ResponseEntity<Resource> getImageById(@PathVariable String id) {
        return productService.getFileById(id);
    }

    @GetMapping("types")
    public ResponseEntity<List<TypeDto>> getAvailableTypes() {
        return productService.getAvailableTypes();
    }

    @GetMapping
    public ResponseEntity<List<ProductShortDto>> getProducts(@RequestParam Optional<String> filter, @RequestParam int page) {
        return productService.getProducts(filter, page);
    }

    @GetMapping("{type}")
    public ResponseEntity<List<ProductShortDto>> getProductsByType(@PathVariable String type, @RequestParam Optional<String> filter, @RequestParam int page) {
        return productService.getProducts(type, filter, page);
    }
}
