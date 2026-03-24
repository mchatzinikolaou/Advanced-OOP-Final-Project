package com.logistics.service;

import com.logistics.dto.OrderDetailResponse;
import com.logistics.model.Order;
import com.logistics.model.OrderStatus;
import com.logistics.repository.OrderRepository;
import com.logistics.strategy.ShippingStrategy;
import com.logistics.strategy.ShippingStrategyFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
public class OrderService {

    private static final Logger logger = LoggerFactory.getLogger(OrderService.class);

    private final OrderRepository orderRepository;
    private final ShippingStrategyFactory strategyFactory;

    @Value("${logistics.order.processing-delay-ms}")
    private long processingDelayMs;

    @Value("${logistics.simulation.inventory-check-failure-rate}")
    private double inventoryCheckFailureRate;

    @Value("${logistics.simulation.payment-failure-rate}")
    private double paymentFailureRate;

    @Autowired
    public OrderService(OrderRepository orderRepository, ShippingStrategyFactory strategyFactory) {
        this.orderRepository = orderRepository;
        this.strategyFactory = strategyFactory;
    }

    /**
     * Creates an order synchronously and returns it with PENDING status
     */
    @Transactional
    public Order createOrder(Order order) {
        order.setStatus(OrderStatus.PENDING);
        Order savedOrder = orderRepository.save(order);
        logger.info("[Main-Thread] Order {} created with status PENDING", savedOrder.getId());
        return savedOrder;
    }

    /**
     * Processes the order asynchronously (heavy processing in background)
     */
    @Async
    @Transactional
    public CompletableFuture<Void> processOrderAsync(Long orderId) {
        logger.info("[Async-Thread-{}] Starting async processing for order {}",
            Thread.currentThread().getName(), orderId);

        try {

            Thread.sleep(processingDelayMs);

            Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found: " + orderId));
            order.setStatus(OrderStatus.PROCESSING);
            // Simulate heavy processing with sleep

            // Update status to PROCESSING
            orderRepository.save(order);

            // Perform inventory check (simulated)
            boolean inventoryAvailable = checkInventory(order);
            logger.info("[Async-Thread-{}] Inventory check for order {}: {}",
                Thread.currentThread().getName(), orderId, inventoryAvailable);

            // Calculate shipping cost using Strategy Pattern
            ShippingStrategy strategy = strategyFactory.getStrategy(order.getShippingType());
            double cost = strategy.calculateCost(order.getWeight());
            order.setCost(cost);

            logger.info("[Async-Thread-{}] Cost calculated for order {}: {}€ using {} strategy",
                Thread.currentThread().getName(), orderId, cost, strategy.getStrategyName());

            // Simulate payment processing
            boolean paymentSuccessful = processPayment(order);
            logger.info("[Async-Thread-{}] Payment processing for order {}: {}",
                Thread.currentThread().getName(), orderId, paymentSuccessful);

            // Update final status
            if (inventoryAvailable && paymentSuccessful) {
                order.setStatus(OrderStatus.COMPLETED);
            } else {
                order.setStatus(OrderStatus.DELAYED);
            }

            orderRepository.save(order);
            logger.info("[Async-Thread-{}] Order {} processing completed with status: {}",
                Thread.currentThread().getName(), orderId, order.getStatus());

        } catch (InterruptedException e) {
            logger.error("[Async-Thread-{}] Processing interrupted for order {}",
                Thread.currentThread().getName(), orderId, e);
            Thread.currentThread().interrupt();
        } catch (Exception e) {
            logger.error("[Async-Thread-{}] Error processing order {}",
                Thread.currentThread().getName(), orderId, e);
        }

        return CompletableFuture.completedFuture(null);
    }

    private boolean checkInventory(Order order) {
        // Simulate inventory check
        return Math.random() > inventoryCheckFailureRate;
    }

    private boolean processPayment(Order order) {
        // Simulate payment processing
        return Math.random() > paymentFailureRate;
    }

    public List<OrderDetailResponse> getAllOrders() {
        return orderRepository.findAll().stream()
            .map(this::convertToDetailResponse)
            .collect(Collectors.toList());
    }

    public OrderDetailResponse getOrderById(Long id) {
        Order order = orderRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Order not found: " + id));
        return convertToDetailResponse(order);
    }

    private OrderDetailResponse convertToDetailResponse(Order order) {
        return new OrderDetailResponse(
            order.getId(),
            order.getCustomerName(),
            order.getWeight(),
            order.getDestination(),
            order.getShippingType(),
            order.getStatus().toString(),
            order.getCost()
        );
    }
}
