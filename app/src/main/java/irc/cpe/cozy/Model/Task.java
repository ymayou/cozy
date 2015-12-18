package irc.cpe.cozy.Model;

/**
 * Created by You on 18/12/2015.
 */
public class Task {
    private String content;
    private boolean status;

    public Task() {
    }

    public Task(boolean status, String content) {
        this.status = status;
        this.content = content;
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
}
