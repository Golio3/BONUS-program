package bonus.web.model;

import javax.persistence.*;

@Entity(name = "user_bonus")
public class UserBonusModel {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.TABLE)
    private Integer id;

    @Column(name = "NUMBER")
    private Integer number;

    @Column(name = "STATUS")
    private Integer status;

    @ManyToOne(
            fetch = FetchType.LAZY
    )
    @JoinColumn(name = "USER_ID")
    private UsersModel user;

    @ManyToOne(
            fetch = FetchType.LAZY
    )
    @JoinColumn(name = "BONUS_ID")
    private BonusModel bonus;

    @Transient
    private Integer sum;

    public UserBonusModel() {
    }

    public UserBonusModel(Integer number, Integer status, UsersModel user, BonusModel bonus) {
        this.number = number;
        this.status = status;
        this.user = user;
        this.bonus = bonus;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public UsersModel getUser() {
        return user;
    }

    public void setUser(UsersModel user) {
        this.user = user;
    }

    public BonusModel getBonus() {
        return bonus;
    }

    public void setBonus(BonusModel bonus) {
        this.bonus = bonus;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public Integer getSum() {
        return bonus.getCost() * number;
    }

    public void setSum(Integer sum) {
        this.sum = sum;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
