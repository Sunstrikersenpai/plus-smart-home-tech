package ru.yandex.practicum.store;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.interaction.enums.ProductCategory;
import ru.yandex.practicum.interaction.enums.ProductState;
import ru.yandex.practicum.interaction.enums.QuantityState;

import java.util.UUID;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID productId;
    private String productName;
    private String description;
    private String imageSrc;
    @Enumerated(EnumType.STRING)
    private QuantityState quantityState;
    @Enumerated(EnumType.STRING)
    private ProductState productState;
    @Enumerated(EnumType.STRING)
    private ProductCategory productCategory;
    private Float price;
}
