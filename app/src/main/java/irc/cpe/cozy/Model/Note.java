package irc.cpe.cozy.Model;

/**
 * Created by You on 18/12/2015.
 */
public class Note {
    private int id;
    private String name;
    private String content;
    private int folder;

    public Note() {
    }

    public Note(String name, String content, int folder) {
        this.name = name;
        this.content = content;
        this.folder = folder;
    }

    public Note(int id, String name, String content, int folder) {
        this.id = id;
        this.name = name;
        this.content = content;
        this.folder = folder;
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getFolder() {
        return folder;
    }

    public void setFolder(int folder) {
        this.folder = folder;
    }
}
