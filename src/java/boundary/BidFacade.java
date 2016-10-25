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
import javax.jms.DeliveryMode;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;
import javax.jms.TopicConnection;
import javax.jms.TopicConnectionFactory;
import javax.jms.TopicPublisher;
import javax.jms.TopicSession;
import javax.naming.InitialContext;
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

    public boolean postBid(Product product, Bid bid, Person user) {
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
            return true;
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
            return false;
        }
    }
    
    public void sentMessage() throws Exception{
        // get the initial context
       InitialContext ctx = new InitialContext();
                                                                           
       // lookup the topic object
       Topic topic = (Topic) ctx.lookup("topic/topic0");
                                                                           
       // lookup the topic connection factory
       TopicConnectionFactory connFactory = (TopicConnectionFactory) ctx.
           lookup("topic/connectionFactory");
                                                                           
       // create a topic connection
       TopicConnection topicConn = connFactory.createTopicConnection();
                                                                           
       // create a topic session
       TopicSession topicSession = topicConn.createTopicSession(false, 
           Session.AUTO_ACKNOWLEDGE);
                                                                           
       // create a topic publisher
       TopicPublisher topicPublisher = topicSession.createPublisher(topic);
       topicPublisher.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
                                                                           
       // create the "Hello World" message
       TextMessage message = topicSession.createTextMessage();
       message.setText("Hello World");
                                                                           
       // publish the messages
       topicPublisher.publish(message);
                                                                           
       // print what we did
       System.out.println("Message published: " + message.getText());
                                                                           
       // close the topic connection
       topicConn.close();
    }
}
