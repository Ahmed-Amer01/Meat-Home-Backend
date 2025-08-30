package com.example.meat_home.service;
import org.springframework.security.core.context.SecurityContextHolder;
import com.example.meat_home.dto.Order.CreateOrderDto;
import com.example.meat_home.dto.Order.OrderDto;
import com.example.meat_home.dto.Order.UpdateOrderDto;
import com.example.meat_home.entity.*;
import com.example.meat_home.repository.*;
import com.example.meat_home.util.OrderMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.*;
import java.util.stream.Collectors;
import com.example.meat_home.entity.StatusEnum;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final CustomerRepository customerRepository;
    private final ProductRepository productRepository;
    private final OrderStatusChangeRepository orderStatusChangeRepository;

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
    
    /**
     * Retrieves filtered orders based on the provided criteria.
     *
     * @param customerId Filter by customer ID (optional)
     * @param status Filter by order status (optional)
     * @param startDate Filter by start date (inclusive, optional)
     * @param endDate Filter by end date (inclusive, optional)
     * @return list of filtered orders as {@link OrderDto}
     */
    public List<OrderDto> getFilteredOrders(Long customerId, StatusEnum status, 
                                          LocalDateTime startDate, LocalDateTime endDate) {
        return orderRepository.findWithFilters(customerId, status, startDate, endDate)
                .stream()
                .map(orderMapper::toDto)
                .toList();
    }

    public List<OrderDto> getOrdersByStatus(StatusEnum status) {
        return orderRepository.findAll().stream()
                .filter(order -> order.getOrderStatusChanges().stream()
                        .anyMatch(change -> change.getStatus() == status))
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
     * Cancels an order by its ID.
     * Only orders that are not already delivered or cancelled can be cancelled.
     *
     * @param id the ID of the order to cancel
     * @return the cancelled order as {@link OrderDto}, or {@code null} if not found or cannot be cancelled
     */
    @Transactional
    public OrderDto cancelOrder(Long id) {
        Order order = orderRepository.findById(id).orElse(null);
        if (order == null) {
            return null;
        }

        // Check if order is already delivered or cancelled
        boolean isAlreadyFinal = order.getOrderStatusChanges().stream()
                .anyMatch(change -> change.getStatus() == StatusEnum.Delivered || 
                                 change.getStatus() == StatusEnum.Cancelled);
        
        if (isAlreadyFinal) {
            return null; // Cannot cancel already delivered or cancelled orders
        }

        // Create cancellation status change
        OrderStatusChange statusChange = OrderStatusChange.builder()
                .order(order)
                .status(StatusEnum.Cancelled)
                .build();
        
        orderStatusChangeRepository.save(statusChange);
        order.getOrderStatusChanges().add(statusChange);
        order = orderRepository.save(order);
        
        return orderMapper.toDto(order);
    }

    @Transactional
    public OrderDto updateOrderStatus(Long orderId, StatusEnum newStatus) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        // Create status change record
        OrderStatusChange statusChange = OrderStatusChange.builder()
                .order(order)
                .status(newStatus)
                .build();
        orderStatusChangeRepository.save(statusChange);

        // Update order's status history
        order.getOrderStatusChanges().add(statusChange);
        order = orderRepository.save(order);

        return orderMapper.toDto(order);
    }

    /**
     * Updates an order with new product quantities and/or status.
     * Only orders that are not already delivered or cancelled can be updated.
     *
     * @param id the ID of the order to update
     * @param dto the update data containing products with quantities and/or status
     * @return the updated order as {@link OrderDto}, or {@code null} if not found or cannot be updated
     */
    @Transactional
    public OrderDto updateOrderPatch(Long id, UpdateOrderDto dto) {
        Order order = orderRepository.findById(id).orElse(null);
        if (order == null) {
            return null;
        }

        // Check if order is already delivered or cancelled
        boolean isFinalStatus = order.getOrderStatusChanges().stream()
                .anyMatch(change -> change.getStatus() == StatusEnum.Delivered || 
                                 change.getStatus() == StatusEnum.Cancelled);
        
        if (isFinalStatus) {
            return null; // Cannot update already delivered or cancelled orders
        }

        // Update products if provided
        if (dto.getProducts() != null && !dto.getProducts().isEmpty()) {
            // Get all product IDs from the request
            List<Long> productIds = new ArrayList<>(dto.getProducts().keySet());
            
            // Find all products that exist in the database
            List<Product> products = productRepository.findAllById(productIds);
            
            // Create a map of product ID to Product for quick lookup
            Map<Long, Product> productMap = products.stream()
                    .collect(Collectors.toMap(Product::getId, product -> product));
            
            // Clear existing products and add the new ones with quantities
            order.getProducts().clear();
            
            // Add each product the specified number of times based on quantity
            for (Map.Entry<Long, Integer> entry : dto.getProducts().entrySet()) {
                Long productId = entry.getKey();
                Integer qty = entry.getValue();
                Product product = productMap.get(productId);

                if (product != null && qty != null && qty > 0) {
                    order.getProducts().addAll(Collections.nCopies(qty, product));
                }
            }

        }

        // Update status if provided
        if (dto.getStatus() != null) {
            OrderStatusChange statusChange = OrderStatusChange.builder()
                    .status(dto.getStatus())
                    .order(order)
                    .build();
            orderStatusChangeRepository.save(statusChange);
            order.getOrderStatusChanges().add(statusChange);
        }

        order = orderRepository.save(order);
        return orderMapper.toDto(order);
    }
}
