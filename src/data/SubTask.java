package data;

public class SubTask extends Task {

    private int epicSubTasksId;

    public SubTask(String description, String name, int epicId) {
        super(description, name);
        this.epicSubTasksId = epicId;
    }

    public int getEpicId() {
        return epicSubTasksId;
    }

}
