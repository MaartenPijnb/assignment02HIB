/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package boundary;

import entities.Product;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author maart
 */
@Stateless
public class ProductFacade extends AbstractFacade<Product> {

    @PersistenceContext(unitName = "Assignment2_GoedPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ProductFacade() {
        super(Product.class);
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
