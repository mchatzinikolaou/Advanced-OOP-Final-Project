package com.logistics.controller;

import com.logistics.dto.OrderRequest;
import com.logistics.dto.OrderResponse;
import com.logistics.model.Order;
import com.logistics.service.OrderService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private static final Logger logger = LoggerFactory.getLogger(OrderController.class);

    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<OrderResponse> createOrder(@Valid @RequestBody OrderRequest request) {
        long startTime = System.currentTimeMillis();

        logger.info("[Main-Thread] Received order request from customer: {}", request.getCustomerName());

        // Create order entity
        Order order = new Order(
            request.getCustomerName(),
            request.getWeight(),
            request.getDestination(),
            request.getShippingType()
        );

        // Save order synchronously (returns immediately)
        Order savedOrder = orderService.createOrder(order);

        // Trigger async processing (non-blocking)
        orderService.processOrderAsync(savedOrder.getId());

        long endTime = System.currentTimeMillis();
        logger.info("[Main-Thread] Response returned in {}ms", (endTime - startTime));

        OrderResponse response = new OrderResponse(
            savedOrder.getId(),
            savedOrder.getStatus().toString(),
            "Order accepted and is being processed"
        );

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
    }

    /**
     * GET /orders - Get all orders
     */
    @GetMapping
    public ResponseEntity<List<Order>> getAllOrders() {
        List<Order> orders = orderService.getAllOrders();
        return ResponseEntity.ok(orders);
    }

    /**
     * GET /orders/{id} - Get order by ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<Order> getOrderById(@PathVariable Long id) {
        Order order = orderService.getOrderById(id);
        return ResponseEntity.ok(order);
    }
}
