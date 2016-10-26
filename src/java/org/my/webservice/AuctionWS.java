/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.my.webservice;

import boundary.BidFacade;
import boundary.ProductFacade;
import entities.Bid;
import entities.MessageAuction;
import entities.Person;
import entities.Product;
import java.util.List;
import javax.ejb.EJB;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.ejb.Stateless;

/**
 *
 * @author Maarten
 */
@WebService(serviceName = "AuctionWS")
@Stateless()
public class AuctionWS {

    /**
     * This is a sample web service operation
     */
    @EJB
    private ProductFacade productFacade;

    @EJB
    private BidFacade bidFacade;

    /**
     * Web service operation
     */
    @WebMethod(operationName = "getActiveAuctions")
    public List<Product> getActiveAuctions() {
        //TODO write your implementation code here:
        return productFacade.getProductsApproved();
    }

    /**
     * Web service operation
     */
    @WebMethod(operationName = "bidForAuction")
    public MessageAuction bidForAuction(@WebParam(name = "bid") Bid bid, @WebParam(name = "user") Person user, @WebParam(name = "product") Product product) {
        //TODO write your implementation code here:
        MessageAuction newMessage = new MessageAuction();
        if (bidFacade.postBid(product, bid, user)) {
            newMessage.setMessageCode("Success: 200");
            newMessage.setMessageText(user.getFullname() + "'s bid has been successfully placed for " + product.getName() + ".");
        } else {
            newMessage.setMessageCode("Error: 406");
            newMessage.setMessageText("The bid for " + product.getName() + " has not been placed for " + user.getFullname() + ".");
        }
        return newMessage;
    }
}
