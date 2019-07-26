package bonus.web.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Entity(name = "users")
public class UsersModel {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.TABLE)
    private Integer id;

    @Column(name = "USERNAME")
    private String username;

    @Column(name = "PASSWORD")
    private String password;

    @Column(name = "ACTIVE")
    private int active;

    @Column(name = "ROLES")
    private String roles = "";

    @Column(name = "PERMISSIONS")
    private String permissions = "";

    @Column(name = "ADDRESS")
    private String address;

    @Column(name = "TEL")
    private Long tel;

    @Column(name = "EMAIL")
    private String email;

    @Column(name = "DATE_START")
    private Date dateStart;

    @Column(name = "LAST_SIGNATURE")
    private Date lastSignature;

    @Column(name = "NEXT_POSSIBLE_SIGNATURE")
    private Date nextPossibleSignature;

    @Column(name = "POINTS")
    private int points;

    @Column(name = "POINTS_ACTUAL")
    private int pointsActual;

    @Transient
    private String errors;

    @Transient
    private String repeatPassword;

    public UsersModel() {
    }

    public UsersModel(String username, String password, int active, String roles, String permissions, String address,
                      Long tel, String email, Date dateStart, Date lastSignature, Date nextPossibleSignature,
                      String errors, String repeatPassword, int points, int pointsActual) {
        this.username = username;
        this.password = password;
        this.active = 1;
        this.roles = roles;
        this.permissions = permissions;
        this.address = address;
        this.tel = tel;
        this.email = email;
        this.dateStart = dateStart;
        this.lastSignature = lastSignature;
        this.nextPossibleSignature = nextPossibleSignature;
        this.errors = errors;
        this.repeatPassword = repeatPassword;
        this.points = points;
        this.pointsActual = pointsActual;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getActive() {
        return active;
    }

    public void setActive(int active) {
        this.active = active;
    }

    public String getRoles() {
        return roles;
    }

    public void setRoles(String roles) {
        this.roles = roles;
    }

    public String getPermissions() {
        return permissions;
    }

    public void setPermissions(String permissions) {
        this.permissions = permissions;
    }

    public List<String> getRoleList(){
        if(this.roles.length() > 0){
            return Arrays.asList(this.roles.split(","));
        }
        return new ArrayList<>();
    }

    public List<String> getPermissionList(){
        if(this.permissions.length() > 0){
            return Arrays.asList(this.permissions.split(","));
        }
        return new ArrayList<>();
    }

    public String getErrors() {
        return errors;
    }

    public void setErrors(String errors) {
        this.errors = errors;
    }

    public String getRepeatPassword() {
        return repeatPassword;
    }

    public void setRepeatPassword(String repeatPassword) {
        this.repeatPassword = repeatPassword;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Long getTel() {
        return tel;
    }

    public void setTel(Long tel) {
        this.tel = tel;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getDateStart() {
        return dateStart;
    }

    public void setDateStart(Date dateStart) {
        this.dateStart = dateStart;
    }

    public Date getLastSignature() {
        return lastSignature;
    }

    public void setLastSignature(Date lastSignature) {
        this.lastSignature = lastSignature;
    }

    public Date getNextPossibleSignature() {
        return nextPossibleSignature;
    }

    public void setNextPossibleSignature(Date nextPossibleSignature) {
        this.nextPossibleSignature = nextPossibleSignature;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public int getPointsActual() {
        return pointsActual;
    }

    public void setPointsActual(int pointsActual) {
        this.pointsActual = pointsActual;
    }
}
