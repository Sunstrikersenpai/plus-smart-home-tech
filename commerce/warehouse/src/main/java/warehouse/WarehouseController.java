package warehouse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.interaction.api.WarehouseApi;
import ru.yandex.practicum.interaction.dto.*;

@RestController
@RequiredArgsConstructor
@Slf4j
public class WarehouseController implements WarehouseApi {

    private final WarehouseService warehouseService;

    @Override
    public void addNewProductInWarehouse(NewProductInWarehouseRequest request) {
        warehouseService.newProductInWarehouse(request);
    }

    @Override
    public void addProductToWarehouse(AddProductToWarehouseRequest request) {
        warehouseService.addProductToWarehouse(request);
    }

    @Override
    public BookedProductsDto checkProductQuantityEnoughForShoppingCart(ShoppingCartDto cartDto) {
        log.info("Received check request with {} products", cartDto.getProducts().size());
        return warehouseService.checkProductQuantityEnoughForShoppingCart(cartDto);
    }

    @Override
    public AddressDto getWarehouseAddress() {
        return warehouseService.getWarehouseAddress();
    }
}