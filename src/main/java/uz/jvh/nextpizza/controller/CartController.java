package uz.jvh.nextpizza.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import uz.jvh.nextpizza.dto.request.cart.AddDrinkToCartRequest;
import uz.jvh.nextpizza.dto.request.cart.AddPizzaToCartRequest;
import uz.jvh.nextpizza.entity.User;
import org.springframework.web.bind.annotation.*;
import uz.jvh.nextpizza.dto.request.cart.UpdateCartItemRequest;
import uz.jvh.nextpizza.dto.response.cart.CartResponse;
import uz.jvh.nextpizza.service.CartService;

@RequestMapping("/api/cart")
@RestController
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @PostMapping("/create-cart/{id}")
    public ResponseEntity<CartResponse> createCart(@PathVariable("id") Long id) {
       return ResponseEntity.ok(cartService.createCart(id));
    }

    /**
     * Mening savatim
     * GET /api/cart
     */
    @GetMapping
    public ResponseEntity<CartResponse> getMyCart( @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(cartService.getCardByUserId(user.getId()));
    }

    /**
     * Pizza savatga qo'shish
     * POST /api/cart/add-pizza
     */
    @PostMapping("/add-pizza")
    public ResponseEntity<CartResponse> addPizzaToCart( @AuthenticationPrincipal User user,
                                                       @Valid @RequestBody AddPizzaToCartRequest request) {
        return ResponseEntity.ok(cartService.addPizzaToCart(user.getId(), request));
    }

    /**
     * Drink savatga qo'shish
     * POST /api/cart/add-drink
     */
    @PostMapping("/add-drink")
    public ResponseEntity<CartResponse> addDrinkToCart(@AuthenticationPrincipal User user,
                                                       @Valid @RequestBody AddDrinkToCartRequest request) {
        return ResponseEntity.ok(cartService.addDrinkToCart(user.getId(), request));
    }

    /**
     * Cart item miqdorini o'zgartirish
     * PUT /api/cart/item/{itemId}
     */
    @PutMapping("/item/{itemId}")
    public ResponseEntity<CartResponse> updateCartItemQuantity( @AuthenticationPrincipal User user,
                                                               @PathVariable Long itemId,
                                                               @Valid @RequestBody UpdateCartItemRequest request) {
        return ResponseEntity.ok(cartService.updateCartItemQuantity(user.getId(), itemId, request.getQuantity()));
    }

    /**
     * Cart itemni o'chirish
     * DELETE /api/cart/item/{itemId}
     */
    @DeleteMapping("/item/{itemId}")
    public ResponseEntity<CartResponse> removeCartItem(@AuthenticationPrincipal User user,
                                                       @PathVariable Long itemId) {
        return ResponseEntity.ok(cartService.removeCartItem(user.getId(), itemId));
    }

    /**
     * Savatni tozalash
     * DELETE /api/cart/clear
     */
    @DeleteMapping("/clear")
    public ResponseEntity<Void> clearCart( @AuthenticationPrincipal User user) {
        cartService.clearCart(user.getId());
        return ResponseEntity.noContent().build();
    }

}
