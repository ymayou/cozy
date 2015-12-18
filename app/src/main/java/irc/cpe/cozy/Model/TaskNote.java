package irc.cpe.cozy.Model;

import java.util.List;

/**
 * Created by You on 18/12/2015.
 */
public class TaskNote {
    private int id;
    private String name;
    private List<Task> tasks;
    private int folder;

    public TaskNote() {
    }

    public TaskNote(String name, List<Task> tasks, int folder) {
        this.name = name;
        this.tasks = tasks;
        this.folder = folder;
    }

    public TaskNote(int id, String name, List<Task> tasks, int folder) {
        this.id = id;
        this.name = name;
        this.tasks = tasks;
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

    public List<Task> getTasks() {
        return tasks;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }

    public int getFolder() {
        return folder;
    }

    public void setFolder(int folder) {
        this.folder = folder;
    }
}
