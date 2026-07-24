package com.ecom.ecom.services;

import com.ecom.ecom.dto.OrderResponse;
import com.ecom.ecom.models.*;
import com.ecom.ecom.repositories.OrderRepository;
import com.ecom.ecom.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final CartService cartService;
    private final UserRepository userRepository;

    public Optional<OrderResponse> createOrder(String userId) {

        //validate for cart item

        List<CartItem> cartItems=cartService.getCart(userId);
        if(cartItems.isEmpty()){
            return Optional.empty();
        }

        //validate for user

        Optional<User> userOptional= userRepository.findById(Long.valueOf(userId));
        if(userOptional.isEmpty()){
            return Optional.empty();
        }
        User user = userOptional.get();

        //Calculate total price;

        BigDecimal totalPrice = cartItems.stream()
                .map(CartItem::getPrice)
                .reduce(BigDecimal.ZERO,BigDecimal::add);

        //create order;

        Order order = new Order();
        order.setUser(user);
        order.setStatus(OrderStatus.CONFIRMED);
        order.setTotalAmount(totalPrice);
        List<OrderItem> orderItems = cartItems.stream()
                .map(item -> new OrderItem(
                        null,
                        item.getProduct(),
                        item.getQuantity(),
                        item.getPrice(),
                        order
                ))
                .toList();

        order.setItems(orderItems);
        Order savedOrder = orderRepository.save(order);


        //clear the cart;

        cartService.clearCart(userId);

        return Optional.of(mapToOrderResponse(savedOrder));


    }

    private OrderResponse mapToOrderResponse(Order order) {

        return new OrderResponse(
                order.getId(),
                order.getTotalAmount(),
                order.getStatus(),
                order.getItems().stream()
                        .map(orderItem -> new OrderItemDTO(
                                orderItem.getId(),
                                orderItem.getProduct().getId(),
                                orderItem.getQuantity(),
                                orderItem.getPrice(),
                                orderItem.getPrice().multiply(new BigDecimal(orderItem.getQuantity()))


                        ))
                        .toList(),
                order.getCreatedAt(),
                order.getUpdatedAt()

        );
    }
}
