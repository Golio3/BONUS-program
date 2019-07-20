package bonus.web.controller.page;

import javax.persistence.*;

@Entity(name = "home")
public class HomeModel {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.TABLE)
    private Integer id;

    @Column(name = "HEAD_CONTEXT")
    private String headContext;

    @Column(name = "BODY_CONTEXT")
    private String bodyContext;

    @Column(name = "DOWN_CONTEXT")
    private String downContext;

    @Column(name = "META_TITLE")
    private String metaTitle;

    @Column(name = "META_KEYWORDS")
    private String metaKeywords;

    @Column(name = "META_DESCRIPTION")
    private String metaDescription;

//    @OneToMany(
//            cascade = CascadeType.ALL,
//            orphanRemoval = true
//    )
//
//    @JoinColumn(name = "HOME_ID")
//    private List<FoldersModel> folders = new ArrayList<>();

    public HomeModel() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getHeadContext() {
        return headContext;
    }

    public void setHeadContext(String headContext) {
        this.headContext = headContext;
    }

    public String getBodyContext() {
        return bodyContext;
    }

    public void setBodyContext(String bodyContext) {
        this.bodyContext = bodyContext;
    }

    public String getDownContext() {
        return downContext;
    }

    public void setDownContext(String downContext) {
        this.downContext = downContext;
    }

    public String getMetaTitle() {
        return metaTitle;
    }

    public void setMetaTitle(String metaTitle) {
        this.metaTitle = metaTitle;
    }

    public String getMetaKeywords() {
        return metaKeywords;
    }

    public void setMetaKeywords(String metaKeywords) {
        this.metaKeywords = metaKeywords;
    }

    public String getMetaDescription() {
        return metaDescription;
    }

    public void setMetaDescription(String metaDescription) {
        this.metaDescription = metaDescription;
    }

//    public List<FoldersModel> getFolders() {
//        return folders;
//    }
//
//    public void setFolders(List<FoldersModel> folders) {
//        this.folders = folders;
//    }
}
