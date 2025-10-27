package ru.yandex.practicum.store;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.interaction.dto.ProductDto;
import ru.yandex.practicum.interaction.dto.SetProductQuantityStateRequest;
import ru.yandex.practicum.interaction.enums.ProductCategory;
import ru.yandex.practicum.interaction.enums.ProductState;
import ru.yandex.practicum.interaction.exeception.ProductNotFoundException;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class ShoppingStoreServiceImpl implements ShoppingStoreService {
    private final ProductRepository storeRepository;
    private final ProductMapper productMapper;

    @Override
    public List<ProductDto> getProducts(ProductCategory category, Pageable pageable) {
        Page<Product> products = storeRepository.getProductByProductCategory(category, pageable);

        return products.map(productMapper::toDto).getContent();
    }

    @Override
    public ProductDto createNewProduct(ProductDto dto) {
        if (storeRepository.existsByProductId(dto.getProductId())) {
            throw new IllegalArgumentException("Product already exists: " + dto.getProductId());
        }
        Product product = storeRepository.save(productMapper.toEntity(dto));
        return productMapper.toDto(product);
    }

    @Override
    public ProductDto updateProduct(ProductDto dto) {
        Product existing = getProductById(dto.getProductId());
        productMapper.updateEntityFromDto(dto, existing);
        storeRepository.save(existing);

        return productMapper.toDto(existing);
    }

    @Override
    public boolean removeProductFromStore(UUID productId) {
        Product product = getProductById(productId);
        product.setProductState(ProductState.DEACTIVATE);
        storeRepository.save(product);

        return true;
    }

    @Override
    public boolean setProductQuantityState(SetProductQuantityStateRequest request) {
        Product product = getProductById(request.getProductId());
        product.setQuantityState(request.getQuantityState());
        storeRepository.save(product);

        return true;
    }

    @Override
    public ProductDto getProduct(UUID productId) {
        return productMapper.toDto(getProductById(productId));
    }

    private Product getProductById(UUID id) {
        return storeRepository.getByProductId(id)
                .orElseThrow(() -> new ProductNotFoundException("Product with id " + id + " not found"));
    }
}