package ru.yandex.practicum.interaction.api;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.interaction.dto.ChangeProductQuantityRequest;
import ru.yandex.practicum.interaction.dto.ShoppingCartDto;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface ShoppingCartApi {

    @GetMapping("/api/v1/shopping-cart")
    ShoppingCartDto getShoppingCart(@RequestParam String username);

    @PutMapping("/api/v1/shopping-cart")
    ShoppingCartDto addProductToShoppingCart(
            @RequestParam String username,
            @RequestBody Map<UUID, Long> products
    );

    @PostMapping("/api/v1/shopping-cart/remove")
    ShoppingCartDto removeFromShoppingCart(
            @RequestParam String username,
            @RequestBody List<UUID> productIds
    );

    @PostMapping("/api/v1/shopping-cart/change-quantity")
    ShoppingCartDto changeProductQuantity(
            @RequestParam @NotNull String username,
            @RequestBody @Valid ChangeProductQuantityRequest request
    );

    @DeleteMapping("/api/v1/shopping-cart")
    void deactivateCurrentShoppingCart(@RequestParam String username);
}