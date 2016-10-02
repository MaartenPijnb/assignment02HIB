/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package boundary;

import entities.Product;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author maart
 */
@Stateless
public class ProductFacade extends AbstractFacade<Product> {

    @EJB
    private BidFacade bidFacade;

    @PersistenceContext(unitName = "Assignment2_GoedPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    private Product currentProduct;

    public Product getCurrentProduct() {
        return currentProduct;
    }

    public void setCurrentProduct(Product currentProduct) {
        this.currentProduct = currentProduct;
    }
    
    
    public ProductFacade() {
        super(Product.class);
    }

    public List<Product>addCurrentHighestBid(List<Product> productList){
        for (Product product : productList) {
            //checken of product al bids heeft
            if(!product.getBids().isEmpty()){
                
                Object currentHighest = this.getEntityManager().createNamedQuery("Bid.findHighestCurrentBid").setParameter("productID", product.getId()).getSingleResult();
                product.setCurrentHighestBid((double)currentHighest);
            }
            else{
                product.setCurrentHighestBid(product.getStartPrice());
            }
            
            //shit
            
        }
        return productList;
    }
    public List<Product> getProductsPending() {
        List<Product> results = this.getEntityManager().createNamedQuery("Product.findByStatusPending").getResultList();
        return results;
    }
    
    public List<Product> getProductsApproved() {
        List<Product> results = this.getEntityManager().createNamedQuery("Product.findByStatusApproved").getResultList();
        return results;
    }
}
