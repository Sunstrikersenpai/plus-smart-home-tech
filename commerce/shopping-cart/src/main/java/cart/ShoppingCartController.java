package cart;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.interaction.api.ShoppingCartApi;
import ru.yandex.practicum.interaction.dto.ChangeProductQuantityRequest;
import ru.yandex.practicum.interaction.dto.ShoppingCartDto;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/shopping-cart")
@RequiredArgsConstructor
public class ShoppingCartController implements ShoppingCartApi {

    private final ShoppingCartService shoppingCartService;

    @GetMapping
    public ShoppingCartDto getShoppingCart(@RequestParam @NotNull String username) {
        return shoppingCartService.getShoppingCart(username);
    }

    @PutMapping
    public ShoppingCartDto addProductToShoppingCart(
            @RequestParam @NotNull String username,
            @RequestBody @NotNull Map<@NotNull UUID, @Positive Long> products
    ) {
        return shoppingCartService.addProductToShoppingCart(username, products);
    }

    @PostMapping("/remove")
    public ShoppingCartDto removeFromShoppingCart(
            @RequestParam @NotNull String username,
            @RequestBody @NotNull List<@NotNull UUID> productIds
    ) {
        return shoppingCartService.removeFromShoppingCart(username, productIds);
    }

    @PostMapping("/change-quantity")
    public ShoppingCartDto changeProductQuantity(
            @RequestParam @NotNull String username,
            @RequestBody @Valid ChangeProductQuantityRequest request
    ) {
        return shoppingCartService.changeProductQuantity(username, request);
    }

    @DeleteMapping
    public void deactivateCurrentShoppingCart(@RequestParam @NotNull String username) {
        shoppingCartService.deactivateCurrentShoppingCart(username);
    }
}