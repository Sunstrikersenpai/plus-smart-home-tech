package ru.yandex.practicum.store;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.interaction.api.ShoppingStoreApi;
import ru.yandex.practicum.interaction.dto.ProductDto;
import ru.yandex.practicum.interaction.dto.SetProductQuantityStateRequest;
import ru.yandex.practicum.interaction.enums.ProductCategory;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/shopping-store")
@RequiredArgsConstructor
public class ShoppingStoreController implements ShoppingStoreApi {

    private final ShoppingStoreService shoppingStoreService;

    @GetMapping
    public List<ProductDto> getProducts(
            @RequestParam @NotNull ProductCategory category,
            Pageable pageable
    ) {
        return shoppingStoreService.getProducts(category, pageable);
    }

    @PutMapping
    public ProductDto createNewProduct(@RequestBody @Valid ProductDto dto) {
        return shoppingStoreService.createNewProduct(dto);
    }

    @PostMapping
    public ProductDto updateProduct(@RequestBody @Valid ProductDto dto) {
        return shoppingStoreService.updateProduct(dto);
    }

    @PostMapping("/removeProductFromStore")
    public boolean removeProductFromStore(@RequestBody @NotNull UUID productId) {
        return shoppingStoreService.removeProductFromStore(productId);
    }

    @PostMapping("/quantityState")
    public boolean setProductQuantityState(@RequestBody @Valid SetProductQuantityStateRequest request) {
        return shoppingStoreService.setProductQuantityState(request);
    }

    @GetMapping("/{productId}")
    public ProductDto getProduct(@PathVariable @NotNull UUID productId) {
        return shoppingStoreService.getProduct(productId);
    }
}