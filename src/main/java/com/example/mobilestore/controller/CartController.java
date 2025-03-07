package com.example.mobilestore.controller;

import com.example.mobilestore.dto.CartDTO;
import com.example.mobilestore.dto.CartItemDTO;
import com.example.mobilestore.service.CartService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
public class CartController {
    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping("/{userId}")
    public ResponseEntity<CartDTO> getCartByUserId(@PathVariable int userId) {
        return ResponseEntity.ok(cartService.getCartByUserId(userId));
    }

    // ✅ 2. Thêm sản phẩm vào giỏ hàng
    @PostMapping("/{userId}/add")
    public ResponseEntity<CartDTO> addToCart(@PathVariable int userId, @RequestBody CartItemDTO cartItemDTO) {
        return ResponseEntity.ok(cartService.addToCart(userId, cartItemDTO));
    }

    // ✅ 3. Cập nhật số lượng sản phẩm
    @PutMapping("/update/{cartItemId}")
    public ResponseEntity<CartDTO> updateCartItem(@PathVariable int cartItemId, @RequestParam int quantity) {
        return ResponseEntity.ok(cartService.updateCartItem(cartItemId, quantity));
    }

}
