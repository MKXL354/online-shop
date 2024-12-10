//package com.local.dao.product;
//
//import com.local.model.Product;
//import com.local.model.ProductStatus;
//
//import java.io.Serial;
//import java.io.Serializable;
//import java.util.*;
//import java.util.concurrent.ConcurrentHashMap;
//import java.util.concurrent.atomic.AtomicInteger;
//
//public class ProductDAOMemImpl implements ProductDAO, Serializable {
//    @Serial
//    private static final long serialVersionUID = -3496610368316822489L;
//
//    private ConcurrentHashMap<Integer, Product> products;
//    private AtomicInteger autoGeneratedId;
//
//    public ProductDAOMemImpl() {
//        products = new ConcurrentHashMap<>();
//        autoGeneratedId = new AtomicInteger(1);
//    }
//
//    @Override
//    public Product addProduct(Product product) {
//        int id = autoGeneratedId.getAndIncrement();
//        product.setId(id);
//        products.put(id, product);
//        return new Product(product);
//    }
//
//    @Override
//    public void updateProduct(Product product) {
//        products.computeIfPresent(product.getId(), (k, v) -> {
//            v.setName(product.getName());
//            v.setPrice(product.getPrice());
//            v.setType(product.getType());
//            v.setStatus(product.getStatus());
//            return v;
//        });
//    }
//
//    @Override
//    public Product getProductById(int id) {
//        Product product = products.get(id);
//        if(product != null) {
//            return new Product(product);
//        }
//        return null;
//    }
//
//    @Override
//    public Product getProductByName(String name) {
//        Product product = products.searchValues(16, (p) -> p.getName().equals(name) && p.getStatus() == ProductStatus.AVAILABLE ? p : null);
//        if(product != null){
//            return new Product(product);
//        }
//        return null;
//    }
//
//    @Override
//    public HashSet<Product> getAllProducts() {
//        HashSet<Product> newProducts = new HashSet<>();
//        for(Product product : products.values()) {
//            newProducts.add(new Product(product));
//        }
//        return newProducts;
//    }
//
//    @Override
//    public LinkedHashMap<String, Integer> getProductsSortedBySells() {
//        return getProductsSortedByStatus(ProductStatus.SOLD);
//    }
//
//    @Override
//    public LinkedHashMap<String, Integer> getProductsSortedByCount() {
//        return getProductsSortedByStatus(ProductStatus.AVAILABLE);
//    }
//
//    private LinkedHashMap<String, Integer> getProductsSortedByStatus(ProductStatus productStatus) {
//        HashMap<String, Integer> productsSortedByStatus = new HashMap<>();
//        for(Product product : products.values()) {
//            if(!productsSortedByStatus.containsKey(product.getName())) {
//                if(product.getStatus() == productStatus){
//                    productsSortedByStatus.put(product.getName(), 1);
//                }
//                else{
//                    productsSortedByStatus.put(product.getName(), 0);
//                }
//            }
//            else{
//                if(product.getStatus() == productStatus){
//                    productsSortedByStatus.put(product.getName(), productsSortedByStatus.get(product.getName()) + 1);
//                }
//            }
//        }
//        LinkedHashMap<String, Integer> result = new LinkedHashMap<>();
//        productsSortedByStatus.entrySet().stream().sorted((entry1, entry2) -> entry2.getValue().compareTo(entry1.getValue())).forEachOrdered(entry -> result.put(entry.getKey(), entry.getValue()));
//        return result;
//    }
//}