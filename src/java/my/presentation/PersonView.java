/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package my.presentation;

import boundary.PersonFacade;
import entities.Person;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

/**
 *
 * @author Maarten
 */
@Named(value = "PersonView")
@RequestScoped
public class PersonView {

    

    /**
     * Creates a new instance of PersonView
     */
    public PersonView() {
        this.person = new Person();
    }
    
    @EJB
    private PersonFacade personFacade;
    
    
    private Person person;

    public Person getPerson() {
        return person;
    }
    
    public String postPerson(){
        // By default it is impossible for registered accounts to be admins
        person.setAccountLevel(0);
        this.personFacade.create(person);
        
        return "/index";
    }
    
    public String login(){
        if (personFacade.checkLogin(person.getEmail(), person.getPassword())) {
            return "/index";
        }
        else {
            FacesContext
                    .getCurrentInstance()
                    .addMessage(
                            "loginButton",
                            new FacesMessage(
                                    FacesMessage.SEVERITY_ERROR,
                                    "WRONG EMAIL ADDRESS OR PASSWORD ENTERED!",
                                    "WRONG EMAIL ADDRESS OR PASSWORD ENTERED!"));
            return null;
        }
    }
    
    public String logout(){
        this.personFacade.logout();
        return "/index";
    }
    
}
