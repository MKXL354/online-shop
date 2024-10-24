package com.local.dao.cart;

import com.local.model.Cart;
import com.local.model.Product;
import com.local.model.User;

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
        return carts.searchValues(16, (cart) -> cart.getUser().getId() == user.getId() && cart.getProcessTime() == null ? cart : null);
    }

    @Override
    public Cart addCartToUser(User user) {
        int id = autoGeneratedId.getAndIncrement();
        Cart cart = new Cart(id, user, new HashSet<>(), null);
        carts.put(id, cart);
        return cart;
    }

    @Override
    public void updateCart(Cart cart) {
        carts.put(cart.getId(), cart);
    }

    @Override
    public Product getProductInCartById(int cartId, int productId) {
        Set<Product> products = carts.get(cartId).getProducts();
        for(Product product : products) {
            if(product.getId() == productId) {
                return product;
            }
        }
        return null;
    }

    @Override
    public Set<Product> getProductsInCart(int cartId) {
        return carts.get(cartId).getProducts();
    }

    @Override
    public void addProductToCart(Cart cart, Product product) {
        Set<Product> products = cart.getProducts();
        products.add(product);
    }

    @Override
    public void updateProductInCart(Cart cart, Product product) {
        Set<Product> products = cart.getProducts();
        products.removeIf(p -> p.getId() == product.getId());
        products.add(product);
    }

    @Override
    public void removeProductFromCart(Cart cart, Product product) {
        Set<Product> products = cart.getProducts();
        products.remove(product);
    }
}
