package bonus.web.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity(name = "test_tbl")
public class AdminModel {

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    private Long id;

    private String text;

    public AdminModel() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
