package ru.yandex.practicum.interaction.api;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.interaction.dto.ChangeProductQuantityRequest;
import ru.yandex.practicum.interaction.dto.ShoppingCartDto;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Validated
@RequestMapping("/api/v1/shopping-cart")
public interface ShoppingCartApi {

    @GetMapping
    ShoppingCartDto getShoppingCart(@RequestParam @NotNull String username);

    @PutMapping
    ShoppingCartDto addProductToShoppingCart(
            @RequestParam String username,
            @RequestBody Map<@NotNull UUID, @Positive Long> products
    );

    @PostMapping("/remove")
    ShoppingCartDto removeFromShoppingCart(
            @RequestParam String username,
            @RequestBody List<@NotNull UUID> productIds
    );

    @PostMapping("/change-quantity")
    ShoppingCartDto changeProductQuantity(
            @RequestParam @NotNull String username,
            @RequestBody @Valid ChangeProductQuantityRequest request
    );

    @DeleteMapping
    void deactivateCurrentShoppingCart(@RequestParam @NotNull String username);
}