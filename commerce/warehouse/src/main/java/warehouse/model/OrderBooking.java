package warehouse.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Map;
import java.util.UUID;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderBooking {

    @Id
    private UUID orderId;

    @Column(nullable = false)
    private boolean delivered;

    @Column
    private UUID deliveryId;

    @ElementCollection
    @CollectionTable(name = "order_booking_items", joinColumns = @JoinColumn(name = "order_id"))
    @MapKeyColumn(name = "product_id")
    @Column(name = "quantity")
    private Map<UUID, Long> reservedProducts;
}