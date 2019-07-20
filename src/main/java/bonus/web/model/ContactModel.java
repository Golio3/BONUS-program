package bonus.web.model;

import javax.persistence.*;

@Entity(name = "contact")
public class ContactModel {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.TABLE)
    private int id;

    @Column(name = "CONTEXT")
    private String context;

    @Column(name = "CONTACT_US")
    private String contactUs;

    public ContactModel() {
    }

    public ContactModel(int id, String context, String contactUs) {
        this.id = id;
        this.context = context;
        this.contactUs = contactUs;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public String getContactUs() {
        return contactUs;
    }

    public void setContactUs(String contactUs) {
        this.contactUs = contactUs;
    }
}
