/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package my.presentation;

import boundary.PersonFacade;
import entities.Person;
import java.util.List;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.jms.JMSContext;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueReceiver;
import javax.jms.QueueSession;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.InitialContext;

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
        updatePerson = new Person();
    }

    @EJB
    private PersonFacade personFacade;

    private List<Person> allPersons;
    private Person person;
    private Person currentPerson;

    public Person getCurrentPerson() {
        return personFacade.getCurrentUser();
    }
    private Person updatePerson;

    public Person getUpdatePerson() {
        return updatePerson;
    }

    public List<Person> getAllPersons() {
        allPersons = personFacade.findAll();
        return allPersons;
    }

    public Person getPerson() {
        return person;
    }

    public String postPerson() {
        // By default it is impossible for registered accounts to be admins
        person.setAccountLevel(0);
        this.personFacade.create(person);

        return "/index";
    }

    public String updateProfile() {
        personFacade.edit(currentPerson);
        return "/index";
    }

    public String toDetail(String id) {
        Person tempPerson = new Person();
        tempPerson.setId(Long.parseLong(id));
        currentPerson = personFacade.find(tempPerson.getId());
        //set the current person in personfacade
        personFacade.setCurrentPerson(currentPerson);
        return "/html/myProfile";
    }

    public String makeAdmin(String id) {
        Person tempPerson = new Person();
        tempPerson.setId(Long.parseLong(id));
        currentPerson = personFacade.find(tempPerson.getId());
        currentPerson.setAccountLevel(1);
        personFacade.edit(currentPerson);
        return "/html/manageUsers";
    }

    public String removeAdmin(String id) {
        Person tempPerson = new Person();
        tempPerson.setId(Long.parseLong(id));
        currentPerson = personFacade.find(tempPerson.getId());
        currentPerson.setAccountLevel(0);
        personFacade.edit(currentPerson);
        return "/html/manageUsers";
    }

    public String login() {
        if (personFacade.checkLogin(person.getEmail(), person.getPassword())) {
            return "/index";
        } else {
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

    public String logout() {
        this.personFacade.logout();
        return "/index";
    }

//    //send winning mails
//    static final Logger logger = Logger.getLogger("ReceiverBean");
//    @Inject
//    private JMSContext context;
//    @Resource(lookup = "java:comp/jms/webappQueue")
//    private Queue queue;
//
//    public void sendMails() throws Exception{
//        // get the initial context
//       InitialContext context = new InitialContext();
//                                                                           
//      // lookup the queue object
//       Queue queue = (Queue) context.lookup("queue/queue0");
//                                                                           
//       // lookup the queue connection factory
//       QueueConnectionFactory conFactory = (QueueConnectionFactory) context.lookup ("queue/connectionFactory");
//                                                                           
//       // create a queue connection
//       QueueConnection queConn = conFactory.createQueueConnection();
//                                                                           
//       // create a queue session
//       QueueSession queSession = queConn.createQueueSession(false,   
//       Session.AUTO_ACKNOWLEDGE);
// 
//       // create a queue receiver
//       QueueReceiver queReceiver = queSession.createReceiver(queue);
//                                                                           
//       // start the connection
//       queConn.start();
//                                                                           
//       // receive a message
//       TextMessage message = (TextMessage) queReceiver.receive();
//                                                                           
//       // print the message
//       System.out.println("Message Received: " + message.getText());
//                                                                           
//       // close the queue connection
//       queConn.close();
//    }
}
