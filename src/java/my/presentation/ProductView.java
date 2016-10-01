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

    public ProductView() {
        this.product = new Product();
    }

    @PostConstruct
    public void init() {
        allCategories = Category.values();
        allProducts = productFacade.findAll();
    }

    private Part file; // +getter+setter

    public Part getFile() {
        return file;
    }

    public void setFile(Part file) {
        this.file = file;
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
    public String toDetail(String id){
        Product tempProduct = new Product();
        tempProduct.setId(Long.parseLong(id));
        currentProduct = productFacade.find(tempProduct.getId());
        return "html/productDetail";
    }
    public String postProduct() {
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        Date date = new Date();
        product.setMoment(date);
        product.setStatus(Status.PENDING); //hardcoded?
        this.productFacade.create(product);
        return "theend";
    }
}