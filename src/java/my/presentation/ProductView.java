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
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.Dependent;

/**
 *
 * @author maart
 */
@Named(value = "product")
@Dependent
public class ProductView {

    @EJB
    private ProductFacade productFacade;

    /**
     * Creates a new instance of ProductView
     */
    //getting al the enumaration values
    private Category[] allCategories;
    private Status[] allStatus;
    private Product product;

    public ProductView() {
        this.product = new Product();
    }

    @PostConstruct
    public void init() {
        allCategories = Category.values();
        allStatus = Status.values();
    }

    public Status[] getAllStatus() {
        return allStatus;
    }

    public Category[] getAllCategories() {
        return allCategories;
    }

    public Product getProduct() {
        return product;
    }

    public String postProduct() throws ParseException {
        // DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        // Date date = new Date();
        //product.setMoment(date);
        this.productFacade.create(product);
        return "theend";
    }
}
