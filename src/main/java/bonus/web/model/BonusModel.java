package bonus.web.model;

import javax.persistence.*;

@Entity(name = "bonus")
public class BonusModel {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.TABLE)
    private Integer id;

    @Column(name = "NAME")
    private String name;

    @Column(name = "COST")
    private int cost;

    @Lob
    @Column(name = "IMAGE")
    private byte[] image;

    @Transient
    private String errors;

    @Transient
    private String imageStr;

    public BonusModel() {
    }

    public BonusModel(String name, int cost, byte[] image, String errors) {
        this.name = name;
        this.cost = cost;
        this.image = image;
        this.errors = errors;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public String getErrors() {
        return errors;
    }

    public void setErrors(String errors) {
        this.errors = errors;
    }

    public String getImageStr() {
        return imageStr;
    }

    public void setImageStr(String imageStr) {
        this.imageStr = imageStr;
    }
}
