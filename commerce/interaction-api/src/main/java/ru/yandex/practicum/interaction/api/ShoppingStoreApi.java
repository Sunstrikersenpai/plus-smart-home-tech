package ru.yandex.practicum.interaction.api;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.interaction.dto.ProductDto;
import ru.yandex.practicum.interaction.enums.ProductCategory;
import ru.yandex.practicum.interaction.enums.QuantityState;

import java.util.List;
import java.util.UUID;

public interface ShoppingStoreApi {

    @GetMapping("/api/v1/shopping-store")
    List<ProductDto> getProducts(@RequestParam ProductCategory category, Pageable pageable);

    @PutMapping("/api/v1/shopping-store")
    ProductDto createNewProduct(@RequestBody  @Valid ProductDto dto);

    @PostMapping("/api/v1/shopping-store")
    ProductDto updateProduct(@RequestBody  @Valid ProductDto dto);

    @PostMapping("/api/v1/shopping-store/removeProductFromStore")
    boolean removeProductFromStore(@RequestBody @NotNull UUID productId);

    @PostMapping("/api/v1/shopping-store/quantityState")
    boolean setProductQuantityState(@RequestParam UUID productId, @RequestParam QuantityState quantityState);

    @GetMapping("/api/v1/shopping-store/{productId}")
    ProductDto getProduct(@PathVariable @NotNull UUID productId);
}