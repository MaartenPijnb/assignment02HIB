/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package boundary;

import entities.Category;
import entities.Person;
import entities.Product;
import entities.Rating;
import entities.Status;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import other.FilterProduct;

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
        currentProduct = addCurrentHighestBid(currentProduct);
        this.currentProduct = currentProduct;
    }

    public ProductFacade() {
        super(Product.class);
    }

    public List filter(FilterProduct filter) {
        Category category = null;
        boolean categQuery = false;
        String startQuery = "Select p from Product p WHERE p.status = :status ";
        String query = startQuery;

        if (filter.getName() != null || !filter.getName().equals("")) {
            query += "AND LOWER(p.name) LIKE '%" + filter.getName().toLowerCase() + "%' ";
        }
        if (!filter.getCategory().equals("All categories")) {
            /*
            if (!startQuery.equals(query)) {
                query += " AND ";
            }
             */
            categQuery = true;
            category = Category.valueOf(filter.getCategory());
            query += " AND p.category = :category";
        }

        if (categQuery) {
            // Category is specified
            return em.createQuery(query).setParameter("status", Status.APPROVED).setParameter("category", category).getResultList();
        } else {
            // No category is specified
            return em.createQuery(query).setParameter("status", Status.APPROVED).getResultList();
        }

    }

    public Product addCurrentHighestBid(Product product) {

        //check if product already has a bid
        if (!product.getBids().isEmpty()) {

            Object currentHighest = this.getEntityManager().createNamedQuery("Bid.findHighestCurrentBid").setParameter("productID", product.getId()).getSingleResult();
            product.setCurrentHighestBid((double) currentHighest);
        } else {
            product.setCurrentHighestBid(product.getStartPrice());
        }

        return product;
    }

    public Product addRatingToSeller(Product product) {
        Person seller = new Person();
        for (Rating rating : product.getSeller().getRatings()) {
            
        }
        return product;
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
