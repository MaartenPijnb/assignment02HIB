/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package my.presentation;

import boundary.ProductFacade;
import java.util.List;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.bean.ManagedProperty;
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
    @ManagedProperty(value = "#{product}")
    private ProductView productView;

    //must povide the setter method
    public void setMessageBean(ProductView productView) {
        this.productView = productView;
    }

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

    public String filter() {
        List productsApproved = this.productFacade.filter(this.filterProduct);
        this.productView.setProductsApproved(productsApproved);
        return "/index";
    }
}
