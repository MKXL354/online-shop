package com.local.scheduler.rollback;

import com.local.dao.CartDAO;
import com.local.dao.ProductDAO;

import java.util.concurrent.Executors;

public class ReserveRollbackScheduler extends TaskScheduler{
    private CartDAO cartDAO;
    private ProductDAO productDAO;

    public ReserveRollbackScheduler(int waitBetweenRollbacksMillis, int waitBeforeRollbackMillis, CartDAO cartDAO, ProductDAO productDAO) {
        super(waitBetweenRollbacksMillis, waitBeforeRollbackMillis);
        this.cartDAO = cartDAO;
        this.productDAO = productDAO;
        this.scheduler = Executors.newSingleThreadScheduledExecutor();
    }

    protected void rollback() {
//        try{
//            HashSet<Cart> activeCarts = cartDAO.getAllActiveCarts();
//            for(Cart cart : activeCarts){
//                if(Duration.between(cart.getLastUpdateTime(), LocalDateTime.now()).toMillis() > waitBeforeRollbackMillis){
//                    for(Product product : cart.getProducts().values()){
//                        cartDAO.removeProductFromCart(cart, product);
//                        product.setProductStatus(ProductStatus.AVAILABLE);
//                        productDAO.updateProduct(product);
//                    }
//                    cart.setLastUpdateTime(LocalDateTime.now());
//                    cartDAO.updateCart(cart);
//                }
//            }
//        }
//        catch(DAOException e){
////            TODO: better exception handling. maybe log?
//            e.printStackTrace();
//        }
    }
}
