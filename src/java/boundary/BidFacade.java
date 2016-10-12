/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package boundary;

import entities.Bid;
import entities.Person;
import entities.Product;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author maart
 */
@Stateless
public class BidFacade extends AbstractFacade<Bid> {

    @EJB
    private PersonFacade personFacade;

    @PersistenceContext(unitName = "Assignment2_GoedPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public BidFacade() {
        super(Bid.class);
    }

    public List<Bid> getSoldBids() {
        //get userID from loggedin user
        List<Bid> soldBids = new ArrayList<>();
        Person currentUser = personFacade.getCurrentUser();
        currentUser.getBids().stream().forEach((item) -> {
            //check if bid is sold then add it to the bid list
            if (item.getIsAccepted()) {
                soldBids.add(item);
            }

        });
        return soldBids;
    }

    public void postBid(Product product, Bid bid, Person user) {
        Double bidPrice = bid.getPrice();
        if (bidPrice > product.getStartPrice() && bidPrice < product.getSellPrice() && bidPrice > product.getCurrentHighestBid()) {
            bid.setBidder(user);
            DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm");
            Date date = new Date();
            bid.setMoment(date);
            bid.setProduct(product);
            bid.setIsAccepted(Boolean.FALSE);
            this.create(bid);
            FacesContext
                    .getCurrentInstance()
                    .addMessage(
                            "test",
                            new FacesMessage(
                                    FacesMessage.SEVERITY_ERROR,
                                    "Bid was placed!",
                                    "Bid was placed!"));
        }
        else {
            FacesContext
                    .getCurrentInstance()
                    .addMessage(
                            "test",
                            new FacesMessage(
                                    FacesMessage.SEVERITY_ERROR,
                                    "Your bid is not accepted! Your bid has to be higher than the minimumprice and the current highest bid or lower than the instant buy price.",
                                    "Your bid is not accepted! Your bid has to be higher than the minimumprice and the current highest bid or lower than the instant buy price."));
        }
    }
}
