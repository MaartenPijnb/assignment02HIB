/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package my.validators;

import boundary.PersonFacade;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

/**
 *
 * @author Maarten
 */
@Named(value = "EmailValidator")
@RequestScoped
public class EmailValidator implements Validator {

    /**
     * Creates a new instance of EmailValidator
     */
    
    @EJB
    private PersonFacade personFacade;
    
    public EmailValidator() {
    }
    
    @Override
    public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {
        String text = (String) value;
        if (text == null || text.equals("") ) {

            FacesMessage msg
                    = new FacesMessage("No value was entered! Please enter a value.",
                            "No value was entered! Please enter a value.");
            msg.setSeverity(FacesMessage.SEVERITY_ERROR);
            throw new ValidatorException(msg);
        }
        else if (this.personFacade.checkIfMailExists(text)) {
            FacesMessage msg
                    = new FacesMessage("This email address is already registered! Please enter another one.",
                            "This email address is already registered! Please enter another one.");
            msg.setSeverity(FacesMessage.SEVERITY_ERROR);
            throw new ValidatorException(msg);
        }
    }
}
