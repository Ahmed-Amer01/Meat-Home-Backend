package com.example.meat_home.service;
import org.springframework.security.core.context.SecurityContextHolder;
import com.example.meat_home.dto.Order.CreateOrderDto;
import com.example.meat_home.dto.Order.OrderDto;
import com.example.meat_home.dto.Order.UpdateOrderDto;
import com.example.meat_home.entity.*;
import com.example.meat_home.repository.CustomerRepository;
import com.example.meat_home.repository.OrderRepository;
import com.example.meat_home.repository.ProductRepository;
import com.example.meat_home.util.OrderMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final CustomerRepository customerRepository;
    private final ProductRepository productRepository;

    /**
     * Retrieves an Order by its ID.
     *
     * @param id the ID of the order
     * @return the mapped {@link OrderDto}, or {@code null} if not found
     */
    public OrderDto getOrderById(Long id) {
        Order order = orderRepository.findById(id)
                .orElse(null);
        return orderMapper.toDto(order);
    }

    /**
     * Retrieves all Orders from the database.
     *
     * @return a list of mapped {@link OrderDto}
     */
    public List<OrderDto> getOrders() {
   return orderRepository.findAll()
                .stream()
                .map(orderMapper::toDto)
                .toList();
    }

    public List<OrderDto> getOrdersForAuthCustomer() {
        // ✅ Extract email from SecurityContext (set by JWT filter)
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return orderRepository.findByCustomerEmail(email).stream().map(orderMapper::toDto).toList();
    }

    /**
     * Creates a new Order from the provided {@link CreateOrderDto}.
     * Steps:
     * <ul>
     *   <li>Validates that the DTO is not null and contains product IDs.</li>
     *   <li>Fetches the associated {@link Customer} and {@link Product}s from the database.</li>
     *   <li>Initializes the order with a default status of {@link StatusEnum#Preparing}.</li>
     *   <li>Saves the new order to the database (with cascading for {@link OrderStatusChange}).</li>
     *   <li>Maps and returns the saved {@link Order} as {@link OrderDto}.</li>
     * </ul>
     *
     * @param dto the DTO containing order creation data
     * @return the created order mapped to {@link OrderDto}, or {@code null} if validation fails
     */
    @Transactional
    public OrderDto createOrder(CreateOrderDto dto) {
        if (dto == null || dto.getProducts_id().isEmpty()) return null;

        // ✅ Extract email from SecurityContext (set by JWT filter)
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Customer customer = customerRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        List<Product> products = productRepository.findAllById(dto.getProducts_id());

        Order order = Order.builder()
                    .products(products)
                    .customer(customer)
                    .build();

        OrderStatusChange os = OrderStatusChange.builder().createdAt(LocalDateTime.now()).status(StatusEnum.Preparing).order(order).build();

        order.getOrderStatusChanges().add(os);
        orderRepository.save(order);

        return orderMapper.toDto(order);
    }

    /**
     * Deletes an Order by its ID.
     *
     * @param id the ID of the order to delete
     * @return {@code true} if the order was found and deleted,
     *         {@code false} if no order with the given ID exists
     */
    public Boolean deleteOrder(Long id) {
        if(!orderRepository.existsById(id)) return false;
        orderRepository.deleteById(id);
        return true;
    }

    /**
     * Partially updates an existing Order.
     * Supports:
     * <ul>
     *   <li>Replacing the product list if product IDs are provided.</li>
     *   <li>Appending a new status change if a status is provided.</li>
     * </ul>
     *
     * @param id  the ID of the order to update
     * @param dto the update data containing product IDs and/or status
     * @return the updated {@link OrderDto}, or {@code null} if the order was not found
     */
    @Transactional
    public OrderDto updateOrderPatch(Long id, UpdateOrderDto dto) {

        Order order = orderRepository.findById(id).orElse(null);
        if (order == null) return null;

        if (dto.getProducts_id() != null && !dto.getProducts_id().isEmpty()) {
            List<Product> products = productRepository.findAllById(dto.getProducts_id());
            order.setProducts(products);
        }

        if (dto.getStatus() != null) {
            OrderStatusChange os = OrderStatusChange.builder()
                    .status(dto.getStatus())
                    .createdAt(LocalDateTime.now())
                    .order(order)
                    .build();
            order.getOrderStatusChanges().add(os);
        }

        orderRepository.save(order);
        return orderMapper.toDto(order);
    }

}
