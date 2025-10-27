package ru.yandex.practicum.store;

import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.yandex.practicum.interaction.enums.ProductCategory;

import java.util.Optional;
import java.util.UUID;

public interface ProductRepository extends JpaRepository<Product, UUID> {
    Page<Product> getProductByProductCategory(ProductCategory category, org.springframework.data.domain.Pageable pageable);

    Optional<Product> getByProductId(UUID productId);

    boolean existsByProductId(@NotNull UUID productId);
}
