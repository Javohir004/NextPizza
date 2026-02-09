package uz.jvh.nextpizza.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uz.jvh.nextpizza.dto.request.AddToCartRequest;
import uz.jvh.nextpizza.dto.response.CartItemResponse;
import uz.jvh.nextpizza.dto.response.CartResponse;
import uz.jvh.nextpizza.enomerator.ErrorCode;
import uz.jvh.nextpizza.entity.*;
import uz.jvh.nextpizza.exception.NextPizzaException;
import uz.jvh.nextpizza.repository.CartItemRepository;
import uz.jvh.nextpizza.repository.CartRepository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;
    private final UserService userService;
    private final PizzaService pizzaService;
    private final CartItemRepository cartItemRepository;
    private final DrinkService drinkService;

    @Transactional
    public Cart createCart(Long userId) {
        User byIdE = userService.findByIdE(userId);
       if(cartRepository.existsByUserIdAndIsActiveTrue(byIdE.getId())){
           throw new NextPizzaException
                   (ErrorCode.CART_ALREADY_EXISTS, "User name: " + byIdE.getUsername() + " , Id: " + byIdE.getId());
       }
        Cart cart = Cart.builder()
                .user(byIdE)
                .items(new ArrayList<>())
                .totalPrice(BigDecimal.ZERO)
                .totalItems(0).build();

        return cartRepository.save(cart);
    }

    /**
     * Pizza savatga qo'shish
     */
    public CartResponse addPizzaToCart(Long userId, AddToCartRequest request) {
        Cart cart = getOrCreateCart(userId);
        Pizza pizza = pizzaService.findById(request.getPizzaId());

        // Agar allaqachon savatda bo'lsa - quantity ni oshirish
        CartItem existingItem = findCartItemByPizza(cart, pizza);

        if (existingItem != null) {
            existingItem.setQuantity(existingItem.getQuantity() + request.getQuantity());
            existingItem.setTotalPrice(
                    existingItem.getPrice().multiply(new BigDecimal(existingItem.getQuantity()))
            );
        } else {
            // Yangi CartItem yaratish
            CartItem newItem = CartItem.builder()
                    .cart(cart)
                    .pizza(pizza)
                    .quantity(request.getQuantity())
                    .price(pizza.getPrice())
                    .totalPrice(pizza.getPrice().multiply(new BigDecimal(request.getQuantity())))
                    .build();

            cart.getItems().add(newItem);
        }

        recalculateCart(cart);
        cartRepository.save(cart);

        return toCartResponse(cart);
    }


    /**
     * Drink savatga qo'shish
     */
    public CartResponse addDrinkToCart(Long userId, AddToCartRequest request) {
        Cart cart = getOrCreateCart(userId);
        Drink drink = drinkService.findById(request.getDrinkId());

        CartItem existingItem = findCartItemByDrink(cart, drink);

        if (existingItem != null) {
            existingItem.setQuantity(existingItem.getQuantity() + request.getQuantity());
            existingItem.setTotalPrice(
                    existingItem.getPrice().multiply(new BigDecimal(existingItem.getQuantity()))
            );
        } else {
            CartItem newItem = CartItem.builder()
                    .cart(cart)
                    .drink(drink)
                    .quantity(request.getQuantity())
                    .price(drink.getPrice())
                    .totalPrice(drink.getPrice().multiply(new BigDecimal(request.getQuantity())))
                    .build();

            cart.getItems().add(newItem);
        }

        recalculateCart(cart);
        cartRepository.save(cart);

        return toCartResponse(cart);
    }

    /**
     * Cart item miqdorini o'zgartirish
     */
    public CartResponse updateCartItemQuantity(Long userId, Long cartItemId, Integer newQuantity) {
        Cart cart = getOrCreateCart(userId);

        CartItem item = cart.getItems().stream()
                .filter(i -> i.getId().equals(cartItemId))
                .findFirst()
                .orElseThrow(() -> new NextPizzaException(ErrorCode.CART_ITEM_NOT_FOUND, "ID: " + cartItemId));

        if (newQuantity <= 0) {
            cart.getItems().remove(item);
            cartItemRepository.delete(item);
        } else {
            item.setQuantity(newQuantity);
            item.setTotalPrice(item.getPrice().multiply(new BigDecimal(newQuantity)));
        }

        recalculateCart(cart);
        cartRepository.save(cart);

        return toCartResponse(cart);
    }

    /**
     * Cart itemni o'chirish
     */
    public CartResponse removeCartItem(Long userId, Long cartItemId) {
        Cart cart = getOrCreateCart(userId);

        CartItem item = cart.getItems().stream()
                .filter(i -> i.getId().equals(cartItemId))
                .findFirst()
                .orElseThrow(() -> new NextPizzaException(ErrorCode.CART_ITEM_NOT_FOUND, "ID: " + cartItemId));

        cart.getItems().remove(item);
        cartItemRepository.delete(item);

        recalculateCart(cart);
        cartRepository.save(cart);

        return toCartResponse(cart);
    }

    /**
     * Savatni tozalash
     */
    public void clearCart(Long userId) {
        Cart cart = getOrCreateCart(userId);

        cart.getItems().clear();
        cart.setTotalPrice(BigDecimal.ZERO);
        cart.setTotalItems(0);

        cartRepository.save(cart);
    }

    /**
     * Savat ko'rish
     */
    public CartResponse getCart(Long userId) {
        Cart cart = getOrCreateCart(userId);
        return toCartResponse(cart);
    }

    // ========== Helper Methods ==========

    private CartItem findCartItemByPizza(Cart cart, Pizza pizza) {
        return cart.getItems().stream()
                .filter(item -> item.getPizza() != null && item.getPizza().getId().equals(pizza.getId()))
                .findFirst()
                .orElse(null);
    }

    private CartItem findCartItemByDrink(Cart cart, Drink drink) {
        return cart.getItems().stream()
                .filter(item -> item.getDrink() != null && item.getDrink().getId().equals(drink.getId()))
                .findFirst()
                .orElse(null);
    }

    private void recalculateCart(Cart cart) {
        BigDecimal totalPrice = cart.getItems().stream()
                .map(CartItem::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Integer totalItems = cart.getItems().stream()
                .mapToInt(CartItem::getQuantity)
                .sum();

        cart.setTotalPrice(totalPrice);
        cart.setTotalItems(totalItems);
    }

    @Transactional
    public Cart getOrCreateCart(Long userId) {
        return cartRepository.findByUserIdAndIsActiveTrue(userId)
                .orElseGet(() -> createCart(userId));
    }

    private CartResponse toCartResponse(Cart cart) {
        List<CartItemResponse> itemResponses = cart.getItems().stream()
                .map(this::toCartItemResponse)
                .toList();

        return CartResponse.builder()
                .id(cart.getId())
                .userId(cart.getUser().getId())
                .items(itemResponses)
                .totalPrice(cart.getTotalPrice())
                .totalItems(cart.getTotalItems())
                .build();
    }

    private CartItemResponse toCartItemResponse(CartItem item) {
        return CartItemResponse.builder()
                .id(item.getId())
                .pizzaId(item.getPizza() != null ? item.getPizza().getId() : null)
                .pizzaName(item.getPizza() != null ? item.getPizza().getName() : null)
                .drinkId(item.getDrink() != null ? item.getDrink().getId() : null)
                .drinkName(item.getDrink() != null ? item.getDrink().getDrinkName() : null)
                .quantity(item.getQuantity())
                .price(item.getPrice())
                .totalPrice(item.getTotalPrice())
                .build();
    }
}

