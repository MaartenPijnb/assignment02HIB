/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package my.presentation;

import boundary.BidFacade;
import boundary.PersonFacade;
import boundary.ProductFacade;
import entities.Bid;
import entities.Person;
import entities.Product;
import entities.Status;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.Dependent;
import javax.enterprise.context.RequestScoped;
import javax.faces.bean.ManagedProperty;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 *
 * @author maart
 */
@Named(value = "bid")
@RequestScoped
public class BidView {

    @EJB
    private ProductFacade productFacade;

    @EJB
    private PersonFacade personFacade;

    @EJB
    private BidFacade bidFacade;

    private Bid bid;

    private String message;
//    @ManagedProperty(value = "#{product}")
//    private ProductView productBean;

    //must povide the setter method
//    public void setProductBean(ProductView productBean) {
//        this.productBean = productBean;
//    }

    /**
     * Creates a new instance of BidView
     */
    public BidView() {
        this.bid = new Bid();
    }
    public String getMessage(){
        return message;
    }
    public Bid getBid() {
        return bid;
    }

    public String postBid() throws IOException {
        bid.setBidder(personFacade.getCurrentUser());
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        Date date = new Date();
        bid.setMoment(date);
        bid.setProduct(productFacade.getCurrentProduct());
        bid.setIsAccepted(Boolean.FALSE);

        //get current product
        this.bidFacade.create(bid);
        //productBean.toDetail(productFacade.getCurrentProduct().getId().toString());
        message= "Your bidding is registered.";
        return "indexLoggedin";
    }
    
    public String postInstantBuy(){
        Product currentProduct= productFacade.getCurrentProduct();
        
        //make the bid
         bid.setBidder(personFacade.getCurrentUser());
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        Date date = new Date();
        bid.setMoment(date);
        bid.setProduct(productFacade.getCurrentProduct());
        bid.setIsAccepted(Boolean.TRUE);
        bid.setPrice(currentProduct.getSellPrice());

    
        this.bidFacade.create(bid);
        
        
        //change the product status
        
        currentProduct.setStatus(Status.SOLD);
        productFacade.edit(currentProduct);
        message="You succesfully bought the product.";
        return "indexLoggedin";
    }

}