/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package my.presentation;

import boundary.PersonFacade;
import boundary.ProductFacade;
import entities.Category;
import entities.Person;
import entities.Product;
import entities.Rating;

import entities.Status;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.Part;
import other.FilterProduct;

/**
 *
 * @author maart
 */
@Named(value = "product")
@RequestScoped
public class ProductView {

    @EJB
    private PersonFacade personFacade;

    @EJB
    private ProductFacade productFacade;

    /**
     * Creates a new instance of ProductView
     */
    //getting al the enumaration values
    private Category[] allCategories;
    private Status[] allStatus;
    private List<Product> allProducts;
    private Product currentProduct;
    private Product product;
    private List<Product> productsPending;
    private List<Product> productsApproved;
    private FilterProduct filterProduct;

    // For filtering
    private boolean isFilter = false;

    public ProductView() {
        this.product = new Product();
        this.filterProduct = new FilterProduct();
    }

    @PostConstruct
    public void init() {
        allCategories = Category.values();
//        allProducts = productFacade.findAll();
//        allProducts = productFacade.addCurrentHighestBid(allProducts);
    }

    private Part file; // +getter+setter

    public Part getFile() {
        return file;
    }

    public void setFile(Part file) {
        this.file = file;
    }

    public List<Product> getProductsPending() {
        productsPending = productFacade.getProductsPending();
        return productsPending;
    }

    public List<Product> getProductsApproved() {

        if (!isFilter) {
            productsApproved = productFacade.getProductsApproved();
        } else {
            productsApproved = productFacade.filter(this.filterProduct);
        }

        // productsApproved = productFacade.addCurrentHighestBid(productsApproved);
        return productsApproved;
    }

    public void save() throws IOException {
        //file.write("../img/halokes.jpg");
    }

    public Status[] getAllStatus() {
        return allStatus;
    }

    public Category[] getAllCategories() {
        return allCategories;
    }

    public List<Product> getAllProducts() {
        return allProducts;
    }

    public Product getProduct() {
        return product;
    }

    public Product getCurrentProduct() {
        return currentProduct;
    }

    public FilterProduct getFilterProduct() {
        return filterProduct;
    }

    public void setFilterProduct(FilterProduct filterProduct) {
        this.filterProduct = filterProduct;
    }

    public String toDetail(String id) {
        
        // Check if user is logged in, if the user session exists redirect to detail page. If not redirect to index.
        FacesContext context = FacesContext.getCurrentInstance();
        Person user = (Person) context.getExternalContext().getSessionMap().get("user");

        if (user == null) {
            return "/index";
        } else {
            Product tempProduct = new Product();
            tempProduct.setId(Long.parseLong(id));
            currentProduct = productFacade.find(tempProduct.getId());
            //set the currentproduct in productfacade
            productFacade.setCurrentProduct(currentProduct);
            productFacade.addRatingToSeller(currentProduct);
            return "/html/productDetail";
        }

    }

    public String deleteProduct(String id) {
        Product tempProduct = new Product();
        tempProduct.setId(Long.parseLong(id));
        currentProduct = productFacade.find(tempProduct.getId());
        productFacade.remove(currentProduct);
        return "/index";
    }

    public String approveProduct(String id) {
        Product tempProduct = new Product();
        tempProduct.setId(Long.parseLong(id));
        currentProduct = productFacade.find(tempProduct.getId());
        currentProduct.setStatus(Status.APPROVED);
        productFacade.edit(currentProduct);
        return "/html/approveEntries";
    }

    public String postProduct() {
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        Date date = new Date();
        product.setMoment(date);
        product.setStatus(Status.PENDING); //hardcoded?
        //get currentuser
        product.setSeller(personFacade.getCurrentUser());
        this.productFacade.create(product);
        return "/index";
    }

    public void setProductsApproved(List<Product> productsApproved) {
        this.productsApproved = productsApproved;
    }

    public void filter() {
        isFilter = true;
    }

}
