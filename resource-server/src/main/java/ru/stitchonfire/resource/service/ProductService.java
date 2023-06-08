package ru.stitchonfire.resource.service;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.apache.commons.io.FilenameUtils;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.stitchonfire.resource.dto.ProductDto;
import ru.stitchonfire.resource.dto.ProductShortDto;
import ru.stitchonfire.resource.dto.TypeDto;
import ru.stitchonfire.resource.mapper.ProductMapper;
import ru.stitchonfire.resource.model.Product;
import ru.stitchonfire.resource.repository.ProductRepository;
import ru.stitchonfire.resource.request.ProductCreateRequest;
import ru.stitchonfire.resource.state.ProductState;
import ru.stitchonfire.resource.state.ProductType;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.*;

import static java.nio.file.Files.copy;
import static java.nio.file.Paths.get;


@Service
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProductService {
    ProductRepository productRepository;
    ProductMapper productMapper;
    String DIRECTORY = "D://images/";

    public ResponseEntity<ProductDto> getProductById(String id) {
        return productRepository.findById(UUID.fromString(id))
                .map(product -> ResponseEntity.ok(productMapper.map(product)))
                .orElse(ResponseEntity.notFound().build());
    }

    public ResponseEntity<ProductDto> createProduct(List<MultipartFile> files, ProductCreateRequest request) {
        List<String> fileNames = new ArrayList<>();
        files.forEach(file -> {
            String fileName = UUID.randomUUID() + "." + FilenameUtils.getExtension(file.getOriginalFilename());
            Path path = get(DIRECTORY, fileName).toAbsolutePath().normalize();
            try {
                copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
                fileNames.add(fileName);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        return ResponseEntity.ok(productMapper.map(productRepository.saveAndFlush(
                Product.builder()
                        .productProducer(request.productProducer())
                        .productType(request.productType())
                        .tittle(request.tittle())
                        .productState(request.productState())
                        .n(request.n())
                        .description(request.description())
                        .characteristics(request.characteristics())
                        .characteristicsShort(request.characteristicsShort())
                        .imagesIds(fileNames)
                        .price(request.price())
                        .build()
        )));
    }

    public ResponseEntity<List<ProductShortDto>> getProducts(Optional<String> filter, int page) {
        return filter.map(s -> ResponseEntity.ok(productRepository.findAllWithFilter(ProductState.ACTIVE, s, PageRequest.of(page, 5)).get()
                        .map(productMapper::shortMap).toList()
                )).orElseGet(() -> ResponseEntity.ok(productRepository.findAllByProductState(ProductState.ACTIVE, PageRequest.of(page, 5)).get().map(productMapper::shortMap).toList()));

    }

    public ResponseEntity<List<ProductShortDto>> getProducts(String type, Optional<String> filter, int page) {
        return filter.map(s -> ResponseEntity.ok(productRepository.findAllWithFilterAndProductType(ProductType.valueOf(type.toUpperCase()), ProductState.ACTIVE, s, PageRequest.of(page, 5))
                .get()
                .map(productMapper::shortMap)
                .toList()
        )).orElseGet(() -> ResponseEntity.ok(productRepository.findAllByProductTypeAndProductState(ProductType.valueOf(type.toUpperCase()), ProductState.ACTIVE, PageRequest.of(page, 5)).get().map(productMapper::shortMap).toList()));

    }

    public ResponseEntity<Resource> getFileById(String id) {
        Path path = get(DIRECTORY).toAbsolutePath().normalize().resolve(id);
        if (!Files.exists(path)) {
            return ResponseEntity.notFound().build();
        }

        try {
            Resource resource = new UrlResource(path.toUri());
            return ResponseEntity.ok().contentType(MediaType.parseMediaType(Files.probeContentType(path))).body(resource);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public ResponseEntity<List<TypeDto>> getAvailableTypes() {
        return ResponseEntity.ok(Arrays.stream(ProductType.values()).map(productType -> TypeDto.builder().name(productType.getName()).tp(productType.name()).build()).toList());
    }
}
