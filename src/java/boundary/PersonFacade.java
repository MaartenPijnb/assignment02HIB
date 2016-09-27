/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package boundary;

import entities.Person;
import javax.ejb.Stateless;
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

    public PersonFacade() {
        super(Person.class);
    }
    
    public Boolean checkLogin(){
        
        return true;
    }
    
}
