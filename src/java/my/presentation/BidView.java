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
import java.util.List;
import javax.ejb.EJB;
import javax.el.ELContext;
import javax.inject.Named;
import javax.enterprise.context.Dependent;
import javax.enterprise.context.RequestScoped;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.apache.jasper.tagplugins.jstl.ForEach;

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

    @ManagedProperty(value = "#{product}")
    private ProductView productView;

    public ProductView getProductView() {
        return productView;
    }

    public void setProductView(ProductView productView) {
        this.productView = productView;
    }

    private String message;

    public List<Bid> getSelledBids() {
        //get userID from loggedin user
        //List<Bid> test = bidFacade.getSoldBids();
        return bidFacade.getSoldBids();
    }

    public BidView() {
        this.bid = new Bid();
    }

    public String getMessage() {
        return message;
    }

    public Bid getBid() {
        return bid;
    }

    public void postBid() throws IOException {
        Product currentProduct = productFacade.getCurrentProduct();
        Person currentUser = personFacade.getCurrentUser();
        bidFacade.postBid(currentProduct, bid, currentUser);

        // redirect to detail page
        ELContext elContext = FacesContext.getCurrentInstance().getELContext();
        setProductView((ProductView) FacesContext.getCurrentInstance().getApplication().getELResolver().getValue(elContext, null, "product"));
        productView.toDetail(currentProduct.getId().toString());
    }

    public String postInstantBuy() {
        Product currentProduct = productFacade.getCurrentProduct();

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
        message = "You succesfully bought the product.";
        return "/index";
    }

}
