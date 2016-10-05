/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package my.presentation;

import boundary.PersonFacade;
import boundary.RatingFacade;
import entities.Person;
import entities.Rating;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;

/**
 *
 * @author maart
 */
@Named(value = "rating")
@RequestScoped
public class RatingView {

    @EJB
    private RatingFacade ratingFacade;

    @EJB
    private PersonFacade personFacade;

    
    
    /**
     * Creates a new instance of RatingView
     */
    private Rating rating;

    public RatingView() {
        this.rating = new Rating();
    }

    public Rating getRating() {
        return rating;
    }

    public String postRating(Long personReceiverID) {
        // Person currentPerson = new Person();
        // get receiver person
        rating.setReceiver(personFacade.find(personReceiverID));
        //get current user
        rating.setGiver(personFacade.getCurrentUser());
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        Date date = new Date();
        rating.setMoment(date);
        
        ratingFacade.create(rating);
        return "/index";
    }
}
