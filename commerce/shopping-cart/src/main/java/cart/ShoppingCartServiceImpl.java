package cart;

import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.interaction.dto.ChangeProductQuantityRequest;
import ru.yandex.practicum.interaction.dto.ShoppingCartDto;
import ru.yandex.practicum.interaction.exeception.NoProductsInShoppingCartException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class ShoppingCartServiceImpl implements ShoppingCartService {

    private final ShoppingCartRepository shoppingCartRepository;
    private final ShoppingCartMapper shoppingCartMapper;
    private final WarehouseClient warehouseClient;

    @Override
    public ShoppingCartDto getShoppingCart(String username) {
        return shoppingCartMapper.toDto(getOrCreateCart(username));
    }

    @Transactional
    @Override
    public ShoppingCartDto addProductToShoppingCart(String username, Map<UUID, Long> products) {
        ShoppingCart cart = getOrCreateCart(username);
        ShoppingCartDto tempCart = shoppingCartMapper.toDto(cart);

        products.forEach((id, qty) ->
                tempCart.getProducts().merge(id, qty, Long::sum));

        try {
            warehouseClient.checkProductQuantityEnoughForShoppingCart(tempCart);
        } catch (Exception e) {
            throw new NoProductsInShoppingCartException("Not enough items in warehouse");
        }

        products.forEach((id, qty) -> cart.getProducts()
                .merge(id, qty, Long::sum));

        shoppingCartRepository.save(cart);
        return shoppingCartMapper.toDto(cart);
    }

    @Transactional
    @Override
    public ShoppingCartDto removeFromShoppingCart(String username, List<UUID> productIds) {
        ShoppingCart cart = getOrCreateCart(username);
        if (cart.getProducts().isEmpty())
            throw new NoProductsInShoppingCartException("Cart is empty");

        productIds.forEach(id -> cart.getProducts().remove(id));
        shoppingCartRepository.save(cart);
        return shoppingCartMapper.toDto(cart);
    }

    @Transactional
    @Override
    public ShoppingCartDto changeProductQuantity(String username, ChangeProductQuantityRequest request) {
        ShoppingCart cart = getOrCreateCart(username);
        UUID productId = request.getProductId();

        if (!cart.getProducts().containsKey(productId))
            throw new NoProductsInShoppingCartException("Product not found in cart");

        long newQuantity = request.getNewQuantity();

        ShoppingCartDto tempCart = shoppingCartMapper.toDto(cart);
        log.info("Requesting warehouse check: {}", tempCart.getProducts());
        if (newQuantity <= 0) {
            tempCart.getProducts().remove(productId);
        } else {
            tempCart.getProducts().put(productId, newQuantity);
        }

        try {
            warehouseClient.checkProductQuantityEnoughForShoppingCart(tempCart);
        } catch (FeignException.BadRequest e) {
            throw new NoProductsInShoppingCartException(
                    "Requested quantity exceeds warehouse stock for product: " + productId);
        }

        if (newQuantity <= 0) {
            cart.getProducts().remove(productId);
        } else {
            cart.getProducts().put(productId, newQuantity);
        }

        shoppingCartRepository.save(cart);
        return shoppingCartMapper.toDto(cart);
    }

    @Transactional
    @Override
    public void deactivateCurrentShoppingCart(String username) {
        shoppingCartRepository.findByUsernameAndActiveTrue(username)
                .ifPresent(cart -> {
                    cart.setActive(false);
                    shoppingCartRepository.save(cart);
                });
    }

    private ShoppingCart getOrCreateCart(String username) {
        return shoppingCartRepository.findByUsernameAndActiveTrue(username)
                .orElseGet(() -> shoppingCartRepository.save(
                        ShoppingCart.builder()
                                .username(username)
                                .products(new HashMap<>())
                                .active(true)
                                .build()
                ));
    }
}