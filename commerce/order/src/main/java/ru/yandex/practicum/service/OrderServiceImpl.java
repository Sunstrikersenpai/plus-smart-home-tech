package ru.yandex.practicum.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.Order;
import ru.yandex.practicum.OrderMapper;
import ru.yandex.practicum.OrderRepository;
import ru.yandex.practicum.interaction.dto.delivery.DeliveryDto;
import ru.yandex.practicum.interaction.dto.order.CreateNewOrderRequest;
import ru.yandex.practicum.interaction.dto.order.OrderDto;
import ru.yandex.practicum.interaction.dto.order.OrderState;
import ru.yandex.practicum.interaction.dto.order.ProductReturnRequest;
import ru.yandex.practicum.interaction.dto.payment.PaymentDto;
import ru.yandex.practicum.interaction.dto.warehouse.AssemblyProductsForOrderRequest;
import ru.yandex.practicum.interaction.dto.warehouse.BookedProductsDto;
import ru.yandex.practicum.interaction.dto.warehouse.ShippedToDeliveryRequest;
import ru.yandex.practicum.interaction.exeception.NoOrderFoundException;
import ru.yandex.practicum.interaction.exeception.NotAuthorizedUserException;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final WarehouseClient warehouseClient;
    private final PaymentClient paymentClient;
    private final DeliveryClient deliveryClient;

    @Override
    public List<OrderDto> getClientOrders(String username) {
        validateUsername(username);
        return OrderMapper.toDto(orderRepository.findByUsername(username));
    }

    @Override
    public OrderDto createOrder(String username, CreateNewOrderRequest request) {
        validateUsername(username);

        BookedProductsDto bookedProducts = warehouseClient
                .checkProductQuantityEnoughForShoppingCart(request.getShoppingCart());

        Order order = OrderMapper.toEntity(username, request.getShoppingCart(), bookedProducts);
        order.setState(OrderState.NEW);
        order = orderRepository.save(order);

        OrderDto dto = OrderMapper.toDto(order);
        dto.setProductPrice(paymentClient.getProductCost(dto));

        DeliveryDto deliveryDto = DeliveryDto.builder()
                .orderId(order.getOrderId())
                .fromAddress(warehouseClient.getWarehouseAddress())
                .toAddress(request.getDeliveryAddress())
                .build();
        deliveryDto = deliveryClient.createDelivery(deliveryDto);

        dto.setDeliveryId(deliveryDto.getDeliveryId());
        dto.setDeliveryPrice(deliveryClient.getDeliveryCost(dto));
        dto.setTotalPrice(paymentClient.getTotalCost(dto));

        PaymentDto payment = paymentClient.createPayment(dto);
        dto.setPaymentId(payment.getPaymentId());
        dto.setState(OrderState.ON_PAYMENT);

        order.setDeliveryId(dto.getDeliveryId());
        order.setProductPrice(dto.getProductPrice());
        order.setDeliveryPrice(dto.getDeliveryPrice());
        order.setTotalPrice(dto.getTotalPrice());
        order.setPaymentId(dto.getPaymentId());
        order.setState(dto.getState());

        orderRepository.save(order);

        return dto;
    }


    @Override
    public OrderDto productReturn(ProductReturnRequest request) {
        Order order = findById(request.getOrderId());
        order.setState(OrderState.PRODUCT_RETURNED);
        return OrderMapper.toDto(orderRepository.save(order));
    }

    @Override
    public OrderDto payment(UUID orderId) {
        Order order = findById(orderId);
        order.setState(OrderState.PAID);
        return OrderMapper.toDto(orderRepository.save(order));
    }

    @Override
    public OrderDto paymentFailed(UUID orderId) {
        Order order = findById(orderId);
        order.setState(OrderState.PAYMENT_FAILED);
        return OrderMapper.toDto(orderRepository.save(order));
    }

    @Override
    public OrderDto delivery(UUID orderId) {
        Order order = findById(orderId);

        warehouseClient.shippedToDelivery(
                new ShippedToDeliveryRequest(order.getOrderId(), order.getDeliveryId()));

        order.setState(OrderState.ON_DELIVERY);
        orderRepository.save(order);

        return OrderMapper.toDto(order);
    }

    @Override
    public OrderDto deliveryFailed(UUID orderId) {
        Order order = findById(orderId);
        order.setState(OrderState.DELIVERY_FAILED);
        return OrderMapper.toDto(orderRepository.save(order));
    }

    @Override
    public OrderDto assembly(UUID orderId) {
        Order order = findById(orderId);

        warehouseClient.assemblyProductsForOrder(
                new AssemblyProductsForOrderRequest(orderId, order.getProducts()));

        order.setState(OrderState.ASSEMBLED);
        orderRepository.save(order);

        return OrderMapper.toDto(order);
    }

    @Override
    public OrderDto assemblyFailed(UUID orderId) {
        Order order = findById(orderId);
        order.setState(OrderState.ASSEMBLY_FAILED);
        return OrderMapper.toDto(orderRepository.save(order));
    }

    @Override
    public OrderDto complete(UUID orderId) {
        Order order = findById(orderId);
        order.setState(OrderState.COMPLETED);
        return OrderMapper.toDto(orderRepository.save(order));
    }

    @Override
    public OrderDto calculateTotalCost(UUID orderId) {
        Order order = findById(orderId);
        double total = paymentClient.getTotalCost(OrderMapper.toDto(order));
        order.setTotalPrice(total);
        return OrderMapper.toDto(orderRepository.save(order));
    }

    @Override
    public OrderDto calculateDeliveryCost(UUID orderId) {
        Order order = findById(orderId);
        double deliveryCost = deliveryClient.getDeliveryCost(OrderMapper.toDto(order));
        order.setDeliveryPrice(deliveryCost);
        return OrderMapper.toDto(orderRepository.save(order));
    }

    private void validateUsername(String username) {
        if (username == null || username.isBlank()) {
            throw new NotAuthorizedUserException("User name can not be blank");
        }
    }

    private Order findById(UUID orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new NoOrderFoundException("Order not found: " + orderId));
    }
}