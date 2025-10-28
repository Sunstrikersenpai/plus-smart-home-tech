package ru.yandex.practicum.store;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.interaction.dto.ProductDto;
import ru.yandex.practicum.interaction.dto.SetProductQuantityStateRequest;
import ru.yandex.practicum.interaction.enums.ProductCategory;
import ru.yandex.practicum.interaction.enums.ProductState;
import ru.yandex.practicum.interaction.exeception.ProductNotFoundException;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ShoppingStoreServiceImpl implements ShoppingStoreService {
    private final ProductRepository storeRepository;
    private final ProductMapper productMapper;

    @Override
    public Page<ProductDto> getProducts(ProductCategory category, Pageable pageable) {
        Page<Product> products = storeRepository.getProductByProductCategory(category, pageable);
        return products.map(productMapper::toDto);
    }

    @Override
    public ProductDto createNewProduct(ProductDto dto) {
        if (dto.getProductId() != null) {
            throw new IllegalArgumentException("Product id must be null.");
        }
        Product product = productMapper.toEntity(dto);
        product = storeRepository.save(product);

        return productMapper.toDto(product);
    }

    @Override
    public ProductDto updateProduct(ProductDto dto) {
        if (dto.getProductId() == null) {
            throw new IllegalArgumentException("Product ID must not be null");
        }

        Product existing = getProductById(dto.getProductId());
        productMapper.updateEntityFromDto(dto, existing);
        existing = storeRepository.save(existing);

        return productMapper.toDto(existing);
    }

    @Override
    public boolean removeProductFromStore(UUID productId) {
        Product product = getProductById(productId);

        if (product.getProductState() != ProductState.DEACTIVATE) {
            product.setProductState(ProductState.DEACTIVATE);
            storeRepository.save(product);
        }

        return true;
    }

    @Override
    public boolean setProductQuantityState(SetProductQuantityStateRequest request) {
        Product product = getProductById(request.getProductId());

        if (product.getQuantityState() != request.getQuantityState()) {
            product.setQuantityState(request.getQuantityState());
            storeRepository.save(product);
        }

        return product.getQuantityState() == request.getQuantityState();
    }

    @Override
    public ProductDto getProduct(UUID productId) {
        return productMapper.toDto(getProductById(productId));
    }

    private Product getProductById(UUID id) {
        return storeRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product with id " + id + " not found"));
    }
}
