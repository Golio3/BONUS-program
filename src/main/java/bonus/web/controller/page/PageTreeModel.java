package bonus.web.controller.page;

import javax.persistence.*;

@Entity(name = "page_tree")
public class PageTreeModel {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.TABLE)
    private Integer id;

    @Column(name = "TREE_ID")
    private String treeId;

    @Column(name = "TEXT")
    private String text;

    @Column(name = "TYPE")
    private int type;

    @Column(name = "PARENT_ID")
    private long parentId;

    @Column(name = "POSITION")
    private int position;

    public PageTreeModel() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public long getParentId() {
        return parentId;
    }

    public void setParentId(long parentId) {
        this.parentId = parentId;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String getTreeId() {
        return treeId;
    }

    public void setTreeId(String treeId) {
        this.treeId = treeId;
    }
}