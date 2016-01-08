package irc.cpe.cozy.Model;

/**
 * Created by You on 18/12/2015.
 */
public class Task {
    private int id;
    private String content;
    private boolean status;
    private int taskNote;

    public Task() {
    }

    public Task(boolean status, String content) {
        this.status = status;
        this.content = content;
    }

    public Task(boolean status, String content, int taskNote) {
        this.status = status;
        this.content = content;
        this.taskNote = taskNote;
    }

    public Task(int id, String content, boolean status, int taskNote) {

        this.id = id;
        this.content = content;
        this.status = status;
        this.taskNote = taskNote;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public int getTaskNote() {
        return taskNote;
    }

    public void setTaskNote(int taskNote) {
        this.taskNote = taskNote;
    }
}
