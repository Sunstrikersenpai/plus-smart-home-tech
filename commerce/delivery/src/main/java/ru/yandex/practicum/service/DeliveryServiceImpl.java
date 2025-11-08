package ru.yandex.practicum.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.DeliveryMapper;
import ru.yandex.practicum.DeliveryRepository;
import ru.yandex.practicum.interaction.dto.delivery.DeliveryDto;
import ru.yandex.practicum.interaction.dto.delivery.DeliveryState;
import ru.yandex.practicum.interaction.dto.order.OrderDto;
import ru.yandex.practicum.interaction.dto.warehouse.ShippedToDeliveryRequest;
import ru.yandex.practicum.interaction.exeception.NoDeliveryFoundException;
import ru.yandex.practicum.model.Address;
import ru.yandex.practicum.model.Delivery;

import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class DeliveryServiceImpl implements DeliveryService {

    private final DeliveryRepository deliveryRepository;
    private final DeliveryMapper deliveryMapper;
    private final OrderClient orderClient;
    private final WarehouseClient warehouseClient;

    private static final Double BASE_COST = 5.0;
    private static final Double WAREHOUSE_ADDRESS_2_SURCHARGE = 2.0;
    private static final Double FRAGILE_SURCHARGE = 0.2;
    private static final Double WEIGHT_SURCHARGE = 0.3;
    private static final Double VOLUME_SURCHARGE = 0.2;
    private static final Double ADDRESS_DELIVERY_SURCHARGE = 0.2;

    @Override
    public DeliveryDto createDelivery(DeliveryDto request) {
        Delivery delivery = deliveryMapper.toEntity(request);
        delivery.setDeliveryState(DeliveryState.CREATED);
        delivery = deliveryRepository.save(delivery);

        return deliveryMapper.toDto(delivery);
    }

    @Override
    public void pickOrderForDelivery(UUID deliveryId) {
        Delivery delivery = findById(deliveryId);

        delivery.setDeliveryState(DeliveryState.IN_PROGRESS);
        deliveryRepository.save(delivery);

        warehouseClient.shippedToDelivery(new ShippedToDeliveryRequest(deliveryId, delivery.getDeliveryId()));

        orderClient.delivery(deliveryId);
    }

    @Override
    public void setDeliverySuccessful(UUID deliveryId) {
        Delivery delivery = findById(deliveryId);

        delivery.setDeliveryState(DeliveryState.DELIVERED);
        deliveryRepository.save(delivery);

        orderClient.complete(deliveryId);
    }

    @Override
    public void setDeliveryFailed(UUID deliveryId) {
        Delivery delivery = findById(deliveryId);

        delivery.setDeliveryState(DeliveryState.FAILED);
        deliveryRepository.save(delivery);

        orderClient.deliveryFailed(deliveryId);
    }

    @Override
    public double getDeliveryCost(OrderDto orderDto) {
        Delivery delivery = findById(orderDto.getDeliveryId());
        Address warehouseAddress = delivery.getFromAddress();
        double deliveryCost = BASE_COST;

        if (warehouseAddress.getStreet().contains("ADDRESS_2")) {
            deliveryCost += deliveryCost * WAREHOUSE_ADDRESS_2_SURCHARGE;
        }

        if (orderDto.isFragile()) {
            deliveryCost += deliveryCost * FRAGILE_SURCHARGE;
        }

        deliveryCost += orderDto.getDeliveryWeight() * WEIGHT_SURCHARGE;
        deliveryCost += orderDto.getDeliveryVolume() * VOLUME_SURCHARGE;

        if (!Objects.equals(warehouseAddress.getStreet(), delivery.getToAddress().getStreet())) {
            deliveryCost += deliveryCost * ADDRESS_DELIVERY_SURCHARGE;
        }

        return deliveryCost;
    }

    private Delivery findById(UUID deliveryId) {
        return deliveryRepository.findById(deliveryId)
                .orElseThrow(() -> new NoDeliveryFoundException("Delivery with id {} not found: " + deliveryId));
    }
}
