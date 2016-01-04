package irc.cpe.cozy.Model;

/**
 * Created by You on 18/12/2015.
 */
public class Folder {
    private int id;
    private String name;

    public Folder() {
    }

    public Folder(String name) {
        this.name = name;
    }

    public Folder(int id, String name) {
        this.id = id;
        this.name = name;
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
}
