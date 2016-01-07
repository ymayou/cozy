package irc.cpe.cozy.Model;

/**
 * Created by You on 01/01/2016.
 */
public class Explorer {
    private int id;
    private String name;
    private Class type;

    public Explorer() {
    }

    public Explorer(int id, String name, Class type) {
        this.id = id;
        this.name = name;
        this.type = type;
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

    public Class getType() {
        return type;
    }

    public void setType(Class type) {
        this.type = type;
    }
}
