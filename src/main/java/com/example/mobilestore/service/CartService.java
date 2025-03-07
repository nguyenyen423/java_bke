package com.example.mobilestore.service;

import com.example.mobilestore.dto.CartDTO;
import com.example.mobilestore.dto.CartItemDTO;
import com.example.mobilestore.entity.Cart;
import com.example.mobilestore.entity.CartItem;
import com.example.mobilestore.entity.Product;
import com.example.mobilestore.exception.NotFoundException;
import com.example.mobilestore.repository.CartItemRepository;
import com.example.mobilestore.repository.CartRepository;
import com.example.mobilestore.repository.ProductRepository;
import com.example.mobilestore.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CartService {
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    public CartService(CartRepository cartRepository,
                       CartItemRepository cartItemRepository,
                       ProductRepository productRepository,
                       UserRepository userRepository) {
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
    }

    public CartDTO addToCart(int userId, CartItemDTO cartItemDTO) {
        Cart cart = cartRepository.findByUserId(userId).orElseGet(() -> {
            Cart newCart = new Cart();
            newCart.setUser(userRepository.findById(userId)
                    .orElseThrow(() -> new NotFoundException(userId)));
            newCart.setTotal(0);
            return cartRepository.save(newCart);
        });

        Product product = productRepository.findById(cartItemDTO.getProductId())
                .orElseThrow(() -> new NotFoundException(userId));

        CartItem cartItem = new CartItem();
        cartItem.setCart(cart);
        cartItem.setProduct(product);
        cartItem.setQuantity(cartItemDTO.getQuantity());
        cartItem.setPrice(product.getPrice() * cartItemDTO.getQuantity());
        cartItemRepository.save(cartItem);

        cart.setTotal(cart.getTotal() + cartItem.getPrice());
        cartRepository.save(cart);

        return getCartByUserId(userId);
    }

    public CartDTO getCartByUserId(int userId) {
        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new NotFoundException(userId));

        CartDTO cartDTO = new CartDTO();
        cartDTO.setUserId(cart.getUser().getId());
        cartDTO.setTotal(cart.getTotal());

        List<CartItemDTO> itemDTOs = cart.getCartItems().stream().map(item -> {
            CartItemDTO dto = new CartItemDTO();
            dto.setProductId(item.getProduct().getId());
            dto.setProductName(item.getProduct().getName());
            dto.setQuantity(item.getQuantity());
            dto.setPrice(item.getPrice());
            return dto;
        }).collect(Collectors.toList());

        cartDTO.setItems(itemDTOs);
        return cartDTO;
    }

    public CartDTO updateCartItem(int cartItemId, int quantity) {
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new NotFoundException(cartItemId));

        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than 0");
        }

        cartItem.setQuantity(quantity);
        cartItem.setPrice(cartItem.getProduct().getPrice() * quantity); // Cập nhật giá

        cartItemRepository.save(cartItem);

        // Trả về giỏ hàng sau khi cập nhật
        return getCartByUserId(cartItem.getCart().getUser().getId());
    }
}
