/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package my.presentation;

import boundary.ProductFacade;
import entities.Product;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import other.FilterProduct;

/**
 *
 * @author Maarten
 */
@Named(value = "FilterView")
@RequestScoped
public class FilterView {

    /**
     * Creates a new instance of FilterView
     */
    
    private FilterProduct filterProduct;
    
    public FilterView() {
        this.filterProduct = new FilterProduct();
    }
    
    @EJB
    private ProductFacade productFacade;

    public FilterProduct getFilterProduct() {
        return filterProduct;
    }

    public void setFilterProduct(FilterProduct filterProduct) {
        this.filterProduct = filterProduct;
    }
    
    public String filter(){
        List products = this.productFacade.filter(this.filterProduct);
        return "indexLoggedin";
    }
}
