package warehouse;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.interaction.api.WarehouseApi;
import ru.yandex.practicum.interaction.dto.*;

@RestController
@RequiredArgsConstructor
public class WarehouseController implements WarehouseApi {

    private final WarehouseService warehouseService;

    @PutMapping
    public void addNewProductInWarehouse(NewProductInWarehouseRequest request) {
        warehouseService.newProductInWarehouse(request);
    }

    @PostMapping("/add")
    public void addProductToWarehouse(AddProductToWarehouseRequest request) {
        warehouseService.addProductToWarehouse(request);
    }

    @PostMapping("/check")
    public BookedProductsDto checkProductQuantityEnoughForShoppingCart(ShoppingCartDto cartDto) {

        return warehouseService.checkProductQuantityEnoughForShoppingCart(cartDto);
    }

    @GetMapping("/address")
    public AddressDto getWarehouseAddress() {
        return warehouseService.getWarehouseAddress();
    }
}