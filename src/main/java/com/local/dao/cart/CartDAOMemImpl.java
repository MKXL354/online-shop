package com.local.dao.cart;

import com.local.model.Cart;
import com.local.model.Product;
import com.local.model.User;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class CartDAOMemImpl implements CartDAO {
    private ConcurrentHashMap<Integer, Cart> carts;
    private AtomicInteger autoGeneratedId;

    public CartDAOMemImpl() {
        this.carts = new ConcurrentHashMap<>();
        this.autoGeneratedId = new AtomicInteger(1);
    }

    @Override
    public Cart getActiveCart(User user) {
        Cart cart = carts.searchValues(16, (c) -> c.getUser().getId() == user.getId() && c.getProcessTime() == null ? c : null);
        if(cart != null){
            return new Cart(cart);
        }
        return null;
    }

    @Override
    public HashSet<Cart> getAllActiveCarts() {
        HashSet<Cart> activeCarts = new HashSet<>();
        Cart activeCart;
        for(Cart cart : carts.values()){
            if(cart.getProcessTime() == null){
                activeCart = new Cart(cart);
                activeCarts.add(activeCart);
            }
        }
        return activeCarts;
    }

    @Override
    public Cart addCartToUser(User user) {
        int id = autoGeneratedId.getAndIncrement();
        Cart cart = new Cart(id, user, new HashSet<>(), LocalDateTime.now(), null);
        carts.put(id, cart);
        return new Cart(cart);
    }

    @Override
    public void updateCart(Cart cart) {
        carts.computeIfPresent(cart.getId(), (k, v) -> {
            v.setLastUpdateTime(cart.getLastUpdateTime());
            v.setProcessTime(cart.getProcessTime());
            return v;
        });
    }

    @Override
    public void addProductToCart(Cart cart, Product product) {
        carts.get(cart.getId()).getProducts().add(product);
    }

    @Override
    public void removeProductFromCart(Cart cart, Product product) {
        Set<Product> products = carts.get(cart.getId()).getProducts();
        products.remove(product);
    }
}
