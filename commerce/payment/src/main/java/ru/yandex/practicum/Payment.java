package ru.yandex.practicum;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "payments")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID paymentId;

    private UUID orderId;

    private Double totalPayment;

    private Double deliveryTotal;

    private Double feeTotal;

    @Enumerated(EnumType.STRING)
    private PaymentState paymentState;
}
