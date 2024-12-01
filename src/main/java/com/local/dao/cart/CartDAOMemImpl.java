package com.local.dao.cart;

import com.local.model.Cart;
import com.local.model.Product;
import com.local.model.User;
import com.local.util.transaction.TransactionManager;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class CartDAOMemImpl implements CartDAO {
    private ConcurrentHashMap<Integer, Cart> carts;
    private AtomicInteger autoGeneratedId;
    private TransactionManager transactionManager;

    public CartDAOMemImpl() {
        this.carts = new ConcurrentHashMap<>();
        this.autoGeneratedId = new AtomicInteger(1);
        this.transactionManager = TransactionManager.getInstance();
    }

//    TODO: maybe an actual status field instead of time?
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
                if(transactionManager.isTransactionStarted()){
                    transactionManager.lockResource(Cart.class, cart.getId());
                }
                activeCart = new Cart(cart);
                activeCarts.add(activeCart);
            }
        }
        return activeCarts;
    }

    @Override
    public Cart addCartToUser(User user) {
        int id = autoGeneratedId.getAndIncrement();
        if(transactionManager.isTransactionStarted()){
            transactionManager.lockResource(Cart.class, id);
            transactionManager.addRestorable(() -> carts.remove(id));
        }
        Cart cart = new Cart(id, user, new HashMap<>(), LocalDateTime.now(), null);
        carts.putIfAbsent(id, cart);
        return new Cart(cart);
    }

    @Override
    public void updateCart(Cart cart) {
        int cartId = cart.getId();
        if(transactionManager.isTransactionStarted()){
            transactionManager.lockResource(Cart.class, cartId);
            transactionManager.addRestorable(() -> carts.put(cartId, cart));
        }
        carts.computeIfPresent(cartId, (k, v) -> {
            v.setLastUpdateTime(cart.getLastUpdateTime());
            v.setProcessTime(cart.getProcessTime());
            return v;
        });
    }

    @Override
    public void addProductToCart(Cart cart, Product product) {
        int cartId = cart.getId();
        int productId = product.getId();
        Map<Integer, Product> products = carts.get(cartId).getProducts();
        if(transactionManager.isTransactionStarted()){
            transactionManager.lockResource(Product.class, productId);
            transactionManager.addRestorable(() -> products.remove(productId, product));
        }
        products.putIfAbsent(productId, product);
    }

    @Override
    public void removeProductFromCart(Cart cart, Product product) {
        int cartId = cart.getId();
        int productId = product.getId();
        Map<Integer, Product> products = carts.get(cartId).getProducts();
        if(transactionManager.isTransactionStarted()){
            transactionManager.lockResource(Cart.class, cartId);
            transactionManager.addRestorable(() -> products.put(productId, product));
        }
        products.remove(productId);
    }
}
