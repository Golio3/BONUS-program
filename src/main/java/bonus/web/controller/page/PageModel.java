package bonus.web.controller.page;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity(name = "page")
public class PageModel {

    @Id
    @Column(name = "ID")
    private Integer id;

    @Column(name = "TITLE")
    private String title;

    @Column(name = "URL")
    private String url;

    @Column(name = "CONTEXT")
    private String context;

    @Column(name = "COMMENTS")
    private String comments;

    @Column(name = "META_TITLE")
    private String metaTitle;

    @Column(name = "META_KEYWORDS")
    private String metaKeywords;

    @Column(name = "META_DESCRIPTION")
    private String metaDescription;

    @Column(name = "RENDER_CONTACT_FORM")
    private boolean renderContactForm;

    @Column(name = "RENDER_ATTACHMENT")
    private boolean renderAttachment;

    @Lob
    @Column(name = "IMAGE")
    private byte[] image;

    @Lob
    @Column(name = "ATTACHMENT")
    private byte[] attachment;

    @Column(name = "ATTACHMENT_NAME")
    private String attachmentName;

    @Column(name = "ATTACHMENT_TYPE")
    private String attachmentType;

    @OneToMany(
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @JoinColumn(name = "PAGE_ID")
    private List<TogglesModel> toggles = new ArrayList<>();

    public PageModel() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
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

    public boolean isRenderContactForm() {
        return renderContactForm;
    }

    public void setRenderContactForm(boolean renderContactForm) {
        this.renderContactForm = renderContactForm;
    }

    public boolean isRenderAttachment() {
        return renderAttachment;
    }

    public void setRenderAttachment(boolean renderAttachment) {
        this.renderAttachment = renderAttachment;
    }

    public List<TogglesModel> getToggles() {
        return toggles;
    }

    public void setToggles(List<TogglesModel> toggles) {
        this.toggles = toggles;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public byte[] getAttachment() {
        return attachment;
    }

    public void setAttachment(byte[] attachment) {
        this.attachment = attachment;
    }

    public String getAttachmentName() {
        return attachmentName;
    }

    public void setAttachmentName(String attachmentName) {
        this.attachmentName = attachmentName;
    }

    public String getAttachmentType() {
        return attachmentType;
    }

    public void setAttachmentType(String attachmentType) {
        this.attachmentType = attachmentType;
    }
}
