package com.example.store.service.impl;

import com.example.store.dto.order.OrderDto;
import com.example.store.dto.order.OrderPatchRequestDto;
import com.example.store.dto.order.OrderRequestDto;
import com.example.store.dto.orderitem.OrderItemDto;
import com.example.store.exception.EntityNotFoundException;
import com.example.store.exception.UnauthorizedModificationException;
import com.example.store.mapper.OrderItemMapper;
import com.example.store.mapper.OrderMapper;
import com.example.store.model.CartItem;
import com.example.store.model.Order;
import com.example.store.model.OrderItem;
import com.example.store.model.Status;
import com.example.store.model.User;
import com.example.store.repository.order.OrderRepository;
import com.example.store.repository.orderstatus.OrderStatusRepository;
import com.example.store.repository.user.UserRepository;
import com.example.store.service.CartItemService;
import com.example.store.service.OrderItemService;
import com.example.store.service.OrderService;
import com.example.store.service.ShoppingCartService;
import jakarta.transaction.Transactional;
import java.math.BigDecimal;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final ShoppingCartService shoppingCartService;
    private final CartItemService cartItemService;
    private final OrderMapper orderMapper;
    private final OrderItemService orderItemService;
    private final OrderItemMapper orderItemMapper;
    private final OrderStatusRepository orderStatusRepository;

    @Override
    @Transactional
    public OrderDto save(OrderRequestDto requestDto, String email) {
        addShippingAddressIfExist(email, requestDto.shippingAddress());
        Order order = new Order();
        order.setUser(getUserByEmail(email));
        order.setStatus(orderStatusRepository.findStatusByStatus(Status.StatusName.PENDING));
        order.setShippingAddress(requestDto.shippingAddress());
        order.setTotal(BigDecimal.ZERO);
        orderRepository.save(order);
        Set<OrderItem> orderItems = getOrderItemsFromCartByEmail(email, order);
        order.setTotal(getTotal(orderItems));
        order.getOrderItems().addAll(orderItemService.saveAll(orderItems));
        shoppingCartService.cleanCart(email);
        return orderMapper.toDto(order);
    }

    @Override
    public OrderDto updateStatus(OrderPatchRequestDto requestDto, Long orderId) {
        Order order = findOrderById(orderId);
        String statusValue = requestDto.status();
        Status.StatusName statusName = Status.StatusName.valueOf(statusValue);
        order.setStatus(orderStatusRepository.findStatusByStatus(statusName));
        return orderMapper.toDto(orderRepository.save(order));
    }

    @Override
    public OrderItemDto getOrderItemByOrderItemId(Long orderId, Long orderItemId, String email) {
        return getOrderItemsByOrderId(orderId, email).stream()
                .filter(orderItemDto -> orderItemDto.getId().equals(orderItemId))
                .findFirst()
                .orElseThrow(
                        () -> new EntityNotFoundException(
                                "Can't find order item with id: " + orderItemId
                                + " in order with id: " + orderId)
                );
    }

    @Override
    public List<OrderDto> findAll(String email, Pageable pageable) {
        List<Order> allByUserEmail = orderRepository.findAllByUserEmail(email, pageable);
        if (allByUserEmail.isEmpty()) {
            throw new EntityNotFoundException("Cant find orders where user email: " + email);
        }
        return allByUserEmail.stream()
                .map(orderMapper::toDto)
                .toList();
    }

    @Override
    public List<OrderItemDto> getOrderItemsByOrderId(Long orderId, String email) {
        Order orderById = findOrderById(orderId);
        if (!orderById.getUser().getEmail().equals(email)) {
            throw new UnauthorizedModificationException("User with email: " + email
                    + " can't get order items where order id: " + orderId);
        }
        return orderById.getOrderItems().stream()
                .map(orderItemMapper::toDto)
                .toList();

    }

    private Order findOrderById(Long orderId) {
        return orderRepository.findOrderById(orderId).orElseThrow(
                () -> new EntityNotFoundException("Can't find order with id: " + orderId)
        );
    }

    private BigDecimal getTotal(Set<OrderItem> orderItems) {
        return orderItems.stream()
                .map(OrderItem::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private Set<OrderItem> getOrderItemsFromCartByEmail(String email, Order order) {
        Set<CartItem> cartItems = cartItemService.getAllByUserEmail(email);
        return cartItems.stream()
                .map(cartItem -> orderMapper.toOrder(cartItem, order))
                .collect(Collectors.toSet());
    }

    private void addShippingAddressIfExist(String email, String shippingAddress) {
        User user = getUserByEmail(email);
        if (user.getShippingAddress() == null) {
            user.setShippingAddress(shippingAddress);
            userRepository.save(user);
        }
    }

    private User getUserByEmail(String email) {
        return userRepository.findUserByEmail(email).orElseThrow(
                () -> new EntityNotFoundException("Cant find user with email: " + email)
        );
    }
}
