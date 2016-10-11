/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package boundary;

import entities.Bid;
import entities.Person;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
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
        List<Bid>soldBids = new ArrayList<>();
        Person currentUser = personFacade.getCurrentUser();
        currentUser.getBids().stream().forEach((item) -> {
            //check if bid is sold then add it to the bid list
            if(item.getIsAccepted()){
                soldBids.add(item);
            }
            
        });
        return soldBids;
    }
}
