package warehouse;

import org.springframework.data.jpa.repository.JpaRepository;
import warehouse.model.ProductInWarehouse;

import java.util.Optional;
import java.util.UUID;

public interface ProductInWarehouseRepository extends JpaRepository<ProductInWarehouse, Long> {
    Optional<ProductInWarehouse> findByProductId(UUID productId);

    boolean existsByProductId(UUID productId);
}
