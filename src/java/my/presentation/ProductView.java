/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package my.presentation;

import boundary.ProductFacade;
import entities.Category;
import entities.Product;

import entities.Status;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.servlet.http.Part;

/**
 *
 * @author maart
 */
@Named(value = "product")
@RequestScoped
public class ProductView {

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

    public ProductView() {
        this.product = new Product();
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
        productsApproved = productFacade.getProductsApproved();
        productsApproved = productFacade.addCurrentHighestBid(productsApproved);
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

    public String toDetail(String id) {
        Product tempProduct = new Product();
        tempProduct.setId(Long.parseLong(id));
        currentProduct = productFacade.find(tempProduct.getId());
        //set the currentproduct in productfacade
        productFacade.setCurrentProduct(currentProduct);
        return "productDetail";
    }

    public String deleteProduct(String id) {
        Product tempProduct = new Product();
        tempProduct.setId(Long.parseLong(id));
        currentProduct = productFacade.find(tempProduct.getId());
        productFacade.remove(currentProduct);
        return "theend";
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
        this.productFacade.create(product);
        return "theend";
    }

    public void setProductsApproved(List<Product> productsApproved) {
        this.productsApproved = productsApproved;
    }
    
    
    

}
