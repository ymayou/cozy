package irc.cpe.cozy.Model;

/**
 * Created by You on 18/12/2015.
 */
public class Folder {
    private int id;
    private String name;
    private int parentFolder;

    public Folder() {
    }

    public Folder(String name, int parentFolder) {
        this.name = name;
        this.parentFolder = parentFolder;
    }

    public Folder(int id, String name, int parentFolder) {
        this.id = id;
        this.name = name;
        this.parentFolder = parentFolder;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getParentFolder() {
        return parentFolder;
    }

    public void setParentFolder(int parentFolder) {
        this.parentFolder = parentFolder;
    }
}
