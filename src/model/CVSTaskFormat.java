package model;

public class CVSTaskFormat {

    public static String toString(Task task) {
        return task.getId() + "," + TypeTask.TASK + "," + task.getName() + "," + task.getStatus() + ","
                + task.getDescription();
    }

    public static String toString(Epic epic) {
        return epic.getId() + "," + TypeTask.EPIC + "," + epic.getName() + "," + epic.getStatus() + ","
                + epic.getDescription();
    }

    public static String toString(Subtask subtask) {
        return subtask.getId() + "," + TypeTask.SUBTASK + "," + subtask.getName() + "," + subtask.getStatus() + ","
                + subtask.getDescription() + "," + subtask.getEpicId();
    }

    public static Task taskFromString(String value) {
        String[] split = value.split(",");

        int id = Integer.parseInt(split[0]);
        TypeTask type = TypeTask.valueOf(split[1]);
        String name = split[2].toUpperCase();
        Status status = Status.valueOf(split[3]);
        String description = split[4].toUpperCase();

        Task taskForTxtFile = new Task(name, description);

        taskForTxtFile.setId(id);
        taskForTxtFile.setTypeTask(type);
        taskForTxtFile.setStatus(status);

        return taskForTxtFile;
    }

    public static Epic epicFromString(String value) {
        String[] split = value.split(",");

        int id = Integer.parseInt(split[0]);
        TypeTask type = TypeTask.valueOf(split[1]);
        String name = split[2].toUpperCase();
        Status status = Status.valueOf(split[3]);
        String description = split[4].toUpperCase();

        Epic epicForTxtFile = new Epic(name, description);

        epicForTxtFile.setId(id);
        epicForTxtFile.setTypeTask(type);
        epicForTxtFile.setStatus(status);

        return epicForTxtFile;
    }

    public static Subtask subtaskFromString(String value) {
        String[] split = value.split(",");

        if (split.length != 6) {
            throw new IllegalArgumentException("Invalid format for Subtask: " + value);
        }

        int id = Integer.parseInt(split[0]);
        TypeTask type = TypeTask.valueOf(split[1]);
        String name = split[2].toUpperCase();
        Status status = Status.valueOf(split[3]);
        String description = split[4].toUpperCase();
        int epicId = Integer.parseInt(split[5]);

        Subtask subtaskForTxtFile = new Subtask(name, description, epicId);

        subtaskForTxtFile.setId(id);
        subtaskForTxtFile.setTypeTask(type);
        subtaskForTxtFile.setStatus(status);
        subtaskForTxtFile.setEpicId(epicId);

        return subtaskForTxtFile;
    }
}
