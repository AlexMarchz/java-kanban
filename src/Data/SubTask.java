package Data;

import Status.Status;

import java.util.ArrayList;

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
