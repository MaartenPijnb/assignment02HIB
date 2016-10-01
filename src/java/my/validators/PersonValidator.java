/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package my.validators;

/**
 *
 * @author Maarten
 */
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

@FacesValidator("PersonValidator")
public class PersonValidator implements Validator {

    public PersonValidator() {

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
    }
}
