package ru.yandex.practicum.store;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.interaction.api.ShoppingStoreApi;
import ru.yandex.practicum.interaction.dto.ProductDto;
import ru.yandex.practicum.interaction.dto.SetProductQuantityStateRequest;
import ru.yandex.practicum.interaction.enums.ProductCategory;
import ru.yandex.practicum.interaction.enums.QuantityState;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/shopping-store")
@RequiredArgsConstructor
public class ShoppingStoreController implements ShoppingStoreApi {

    private final ShoppingStoreService shoppingStoreService;

    @GetMapping
    public List<ProductDto> getProducts(
            ProductCategory category,
            Pageable pageable
    ) {
        return shoppingStoreService.getProducts(category, pageable);
    }

    @PutMapping
    public ProductDto createNewProduct(ProductDto dto) {
        return shoppingStoreService.createNewProduct(dto);
    }

    @PostMapping
    public ProductDto updateProduct( ProductDto dto) {
        return shoppingStoreService.updateProduct(dto);
    }

    @PostMapping("/removeProductFromStore")
    public boolean removeProductFromStore(  UUID productId) {
        return shoppingStoreService.removeProductFromStore(productId);
    }

    @PostMapping("/quantityState")
    public boolean setProductQuantityState(UUID productId, QuantityState quantityState) {
        SetProductQuantityStateRequest request = SetProductQuantityStateRequest.builder()
                .productId(productId)
                .quantityState(quantityState)
                .build();
        return shoppingStoreService.setProductQuantityState(request);
    }

    @GetMapping("/{productId}")
    public ProductDto getProduct( UUID productId) {
        return shoppingStoreService.getProduct(productId);
    }
}