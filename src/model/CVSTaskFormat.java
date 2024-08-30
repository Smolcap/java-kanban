package model;

public class CVSTaskFormat {

    public static String capitalize(String input) {
        if (input == null || input.isEmpty()) {
            return input;
        }
        return Character.toUpperCase(input.charAt(0)) + input.substring(1).toLowerCase();
    }

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
        String name = capitalize(split[2].toUpperCase());
        Status status = Status.valueOf(split[3].trim());
        String description = capitalize(split[4].toUpperCase());

        Task task = new Task(name, description);
        task.setId(id);
        task.setStatus(status);

        return task;
    }


    public static Epic epicFromString(String value) {
        String[] split = value.split(",");

        int id = Integer.parseInt(split[0]);
        TypeTask type = TypeTask.valueOf(split[1]);
        String name = capitalize(split[2].toUpperCase());
        Status status = Status.valueOf(split[3].trim());
        String description = capitalize(split[4].toUpperCase());

        Epic epic = new Epic(name, description);
        epic.setId(id);
        epic.setStatus(status);

        return epic;
    }

    public static Subtask subtaskFromString(String value) {
        String[] split = value.split(",");

        int id = Integer.parseInt(split[0]);
        TypeTask type = TypeTask.valueOf(split[1]);
        String name = capitalize(split[2].toUpperCase());
        Status status = Status.valueOf(split[3].trim());
        String description = capitalize(split[4].toUpperCase());
        int epicId = Integer.parseInt(split[5]);

        Subtask subtask = new Subtask(name, description, epicId);
        subtask.setId(id);
        subtask.setStatus(status);

        return subtask;
    }
}


