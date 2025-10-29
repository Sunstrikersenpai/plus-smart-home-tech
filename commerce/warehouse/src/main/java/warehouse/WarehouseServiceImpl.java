package warehouse;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.interaction.dto.*;
import ru.yandex.practicum.interaction.enums.QuantityState;
import ru.yandex.practicum.interaction.exeception.NoSpecifiedProductInWarehouseException;
import ru.yandex.practicum.interaction.exeception.ProductInShoppingCartLowQuantityInWarehouse;
import ru.yandex.practicum.interaction.exeception.SpecifiedProductAlreadyInWarehouseException;
import warehouse.model.Dimension;
import warehouse.model.ProductInWarehouse;

import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class WarehouseServiceImpl implements WarehouseService {

    private final ProductInWarehouseRepository warehouseRepository;
    private final ShoppingStoreClient shoppingStoreClient;

    @Override
    public void newProductInWarehouse(NewProductInWarehouseRequest request) {
        boolean exists = warehouseRepository.existsByProductId(request.getProductId());
        if (exists) {
            throw new SpecifiedProductAlreadyInWarehouseException(
                    "Product already registered in warehouse: " + request.getProductId());
        }

        ProductInWarehouse entity = ProductInWarehouse.builder()
                .productId(request.getProductId())
                .weight(request.getWeight())
                .fragile(request.isFragile())
                .dimension(new Dimension(
                        request.getDimension().getWidth(),
                        request.getDimension().getHeight(),
                        request.getDimension().getDepth()
                ))
                .quantity(0L)
                .build();

        warehouseRepository.save(entity);
    }

    @Override
    public void addProductToWarehouse(AddProductToWarehouseRequest request) {
        ProductInWarehouse entity = warehouseRepository.findByProductId(request.getProductId())
                .orElseThrow(() -> new NoSpecifiedProductInWarehouseException(
                        "Product not found: " + request.getProductId()));

        entity.setQuantity(entity.getQuantity() + request.getQuantity());
        warehouseRepository.save(entity);
        updateProductQuantityState(entity);
    }

    @Override
    public BookedProductsDto checkProductQuantityEnoughForShoppingCart(ShoppingCartDto cartDto) {
        log.info("Warehouse check incoming: {}", cartDto.getProducts());

        double totalWeight = 0;
        double totalVolume = 0;
        boolean fragileFound = false;


        for (Map.Entry<UUID, Long> entry : cartDto.getProducts().entrySet()) {
            UUID productId = entry.getKey();
            long requestedQty = entry.getValue();

            ProductInWarehouse product = warehouseRepository.findByProductId(productId)
                    .orElseThrow(() -> new NoSpecifiedProductInWarehouseException(
                            "Product not found: " + productId));

            log.info("Checking product {}: requested={}, available={}",
                    productId, requestedQty, product.getQuantity());

            if (product.getQuantity() < requestedQty) {
                throw new ProductInShoppingCartLowQuantityInWarehouse(
                        "LowQuantityInWarehouse for product: " + productId);
            }

            totalWeight += product.getWeight() * requestedQty;
            totalVolume += product.getDimension().getVolume() * requestedQty;
            if (product.isFragile()) fragileFound = true;

        }

        return BookedProductsDto.builder()
                .deliveryWeight(totalWeight)
                .deliveryVolume(totalVolume)
                .fragile(fragileFound)
                .build();
    }

    private void updateProductQuantityState(ProductInWarehouse item) {
        QuantityState state;

        if (item.getQuantity() == 0)
            state = QuantityState.ENDED;
        else if (item.getQuantity() < 10)
            state = QuantityState.FEW;
        else if (item.getQuantity() <= 100)
            state = QuantityState.ENOUGH;
        else
            state = QuantityState.MANY;

        SetProductQuantityStateRequest request = new SetProductQuantityStateRequest(item.getProductId(), state);

        try {
            shoppingStoreClient.setProductQuantityState(item.getProductId(), request.getQuantityState());
        } catch (Exception e) {
            log.warn("Failed to update quantity for product {}", item.getProductId(), e);
        }
    }

    @Override
    public AddressDto getWarehouseAddress() {
        return AddressDto.builder()
                .country("Russia")
                .city("Moscow")
                .street("Pushkina")
                .house("1")
                .flat(null)
                .build();
    }
}