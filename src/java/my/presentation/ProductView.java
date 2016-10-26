/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package my.presentation;

import boundary.BidFacade;
import boundary.PersonFacade;
import boundary.ProductFacade;
import entities.Bid;
import entities.Category;
import entities.Person;
import entities.Product;
import javax.naming.InitialContext;
import entities.Status;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.jms.DeliveryMode;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueSender;
import javax.jms.QueueSession;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.InitialContext;
import javax.servlet.http.Part;
import other.FilterProduct;

/**
 *
 * @author maart
 */
@Named(value = "product")
@RequestScoped
public class ProductView {

    @EJB
    private BidFacade bidFacade;

    @EJB
    private PersonFacade personFacade;

    @EJB
    private ProductFacade productFacade;

    /**
     * Creates a new instance of ProductView
     */
    //getting al the enumaration values
    private Category[] allCategories;
    private Status[] allStatus;
    private List<Product> allProducts;
    private Product currentProduct;
    private Product product;
    private List<Product> productsPending;
    private List<Product> productsApproved;
    private List<Product> productsApprovedWithoutDate;
    private FilterProduct filterProduct;

    // For filtering
    private boolean isFilter = false;

    public ProductView() {
        this.product = new Product();
        this.filterProduct = new FilterProduct();
    }

    @PostConstruct
    public void init() {
        allCategories = Category.values();
//        try {
//           // notifyButyer();
////        allProducts = productFacade.findAll();
////        allProducts = productFacade.addCurrentHighestBid(allProducts);
//        } catch (Exception ex) {
//            Logger.getLogger(ProductView.class.getName()).log(Level.SEVERE, null, ex);
//        }
    }

    private Part file; // +getter+setter

    public Part getFile() {
        return file;
    }

    public void setFile(Part file) {
        this.file = file;
    }

    public List<Product> getProductsPending() {
        productsPending = productFacade.getProductsPending();
        return productsPending;
    }

    public List<Product> getProductsApprovedWithoutDate() {
        productsApprovedWithoutDate = productFacade.getProductsApprovedWithoutDate();
        return productsApprovedWithoutDate;
    }

    public List<Product> getProductsApproved() {

        if (!isFilter) {
            productsApproved = productFacade.getProductsApproved();
        } else {
            productsApproved = productFacade.filter(this.filterProduct);
        }

        // productsApproved = productFacade.addCurrentHighestBid(productsApproved);
        return productsApproved;
    }

    public void save() throws IOException {
        //file.write("../img/halokes.jpg");
    }

    public Status[] getAllStatus() {
        return allStatus;
    }

    public Category[] getAllCategories() {
        return allCategories;
    }

    public List<Product> getAllProducts() {
        return allProducts;
    }

    public Product getProduct() {
        return product;
    }

    public Product getCurrentProduct() {
        return currentProduct;
    }

    public FilterProduct getFilterProduct() {
        return filterProduct;
    }

    public void setFilterProduct(FilterProduct filterProduct) {
        this.filterProduct = filterProduct;
    }

    public String toDetail(String id) {

        // Check if user is logged in, if the user session exists redirect to detail page. If not redirect to index.
        FacesContext context = FacesContext.getCurrentInstance();
        Person user = (Person) context.getExternalContext().getSessionMap().get("user");

        if (user == null) {
            return "/html/login";
        } else {
            Product tempProduct = new Product();
            tempProduct.setId(Long.parseLong(id));
            currentProduct = productFacade.find(tempProduct.getId());
            //set the currentproduct in productfacade
            productFacade.setCurrentProduct(currentProduct);
            //productFacade.addRatingToSeller(currentProduct);
            return "/html/productDetail";
        }

    }

    public String deleteProduct(String id) {
        Product tempProduct = new Product();
        tempProduct.setId(Long.parseLong(id));
        List<Bid> bids = productFacade.getBids(tempProduct);
        for (Bid bid : bids) {
            bidFacade.remove(bid);
        }
        currentProduct = productFacade.find(tempProduct.getId());
        productFacade.remove(currentProduct);
        return "/index";
    }

    public String approveProduct(String id) {
        Product tempProduct = new Product();
        tempProduct.setId(Long.parseLong(id));
        currentProduct = productFacade.find(tempProduct.getId());
        currentProduct.setStatus(Status.APPROVED);
        productFacade.edit(currentProduct);
        return "/html/approveEntries";
    }

    public String postProduct() {
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        Date date = new Date();
        product.setMoment(date);
        product.setStatus(Status.PENDING); //hardcoded?
        //get currentuser
        product.setSeller(personFacade.getCurrentUser());
        this.productFacade.create(product);
        return "/index";
    }

    public void setProductsApproved(List<Product> productsApproved) {
        this.productsApproved = productsApproved;
    }

    public void filter() {
        isFilter = true;
    }

    //notifybuyer

//    public void notifyButyer() throws Exception {
//      // get the initial context
//       InitialContext context = new InitialContext();
//                                                               
//       // lookup the queue object
//       Queue queue = (Queue) context.lookup("queue/queue0");
//                                                                           
//       // lookup the queue connection factory
//       QueueConnectionFactory conFactory = (QueueConnectionFactory) context.lookup ("queue/connectionFactory");
//                                                                           
//       // create a queue connection
//       QueueConnection queConn = conFactory.createQueueConnection();
//                                                                           
//       // create a queue session
//       QueueSession queSession = queConn.createQueueSession(false, Session.DUPS_OK_ACKNOWLEDGE);
//                                                                           
//       // create a queue sender
//       QueueSender queSender = queSession.createSender(queue);
//       queSender.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
//                                                                           
//       // create a simple message to say "Hello World"
//       TextMessage message = queSession.createTextMessage("Hello World");
//                                                     
//       // send the message
//       queSender.send(message);
//                                                                          
//       // print what we did
//       System.out.println("Message Sent: " + message.getText());
//                                                                           
//       // close the queue connection
//       queConn.close();
//    }

}
