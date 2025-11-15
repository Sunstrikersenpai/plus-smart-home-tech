package ru.yandex.practicum;

import jakarta.persistence.*;
import lombok.*;
import ru.yandex.practicum.interaction.dto.order.OrderState;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Entity
@Table(name = "orders")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID orderId;
    private UUID shoppingCartId;
    private String username;
    private UUID paymentId;
    private UUID deliveryId;

    @ElementCollection
    @CollectionTable(name = "order_products", joinColumns = @JoinColumn(name = "order_id"))
    @MapKeyColumn(name = "product_id")
    @Column(name = "quantity")
    private Map<UUID, Long> products = new HashMap<>();

    @Enumerated(EnumType.STRING)
    private OrderState state;

    private double deliveryWeight;
    private double deliveryVolume;
    private boolean fragile;

    private Double totalPrice;
    private Double deliveryPrice;
    private Double productPrice;
}