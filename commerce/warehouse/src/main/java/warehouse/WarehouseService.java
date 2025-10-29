package warehouse;

import ru.yandex.practicum.interaction.dto.*;

public interface WarehouseService {

    void newProductInWarehouse(NewProductInWarehouseRequest request);

    void addProductToWarehouse(AddProductToWarehouseRequest request);

    BookedProductsDto checkProductQuantityEnoughForShoppingCart(ShoppingCartDto cartDto);

    AddressDto getWarehouseAddress();
}