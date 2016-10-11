/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package boundary;

import entities.Person;
import java.util.List;
import javax.ejb.Stateless;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Maarten
 */
@Stateless
public class PersonFacade extends AbstractFacade<Person> {

    @PersistenceContext(unitName = "Assignment2_GoedPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
    private Person currentUser;
    private Person currentPerson;

    public void setCurrentPerson(Person currentPerson) {
        this.currentPerson = currentPerson;
    }

    public Person getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(Person currentUser) {
        this.currentUser = currentUser;
    }

    public PersonFacade() {
        super(Person.class);
    }

    public Boolean checkLogin(String email, String password) {
        FacesContext context = FacesContext.getCurrentInstance();
        List results = this.getEntityManager().createNamedQuery("Person.checkLogin").setParameter("email", email).setParameter("password", password).getResultList();
        if (!results.isEmpty()) {
            Person user = (Person) results.get(0);
            this.currentUser = user;
            context.getExternalContext().getSessionMap().put("user", user);
            return true;
        } else {
            return false;
        }
    }

    public void logout() {
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
    }

    public Boolean checkIfMailExists(String email) {
        List results = this.getEntityManager().createNamedQuery("Person.findByEmail").setParameter("email", email).getResultList();
        if (results.isEmpty()) {
            return false;
        } else {
            return true;
        }

    }
    
}
