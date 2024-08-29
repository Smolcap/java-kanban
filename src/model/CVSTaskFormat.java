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
        String statusString = split[3].trim();

        Status status = Status.NEW;

        try {
            status = Status.valueOf(statusString);
        } catch (ManagerSaveException e) {
            throw new ManagerSaveException("Неверный статус" + status);
        }

        String description = capitalize(split[4].toUpperCase());
        Integer epicId = null;

        if (split.length > 5) {
            if (!split[5].isEmpty()) {
                epicId = Integer.parseInt(split[5]);
            }
        }

        switch (type) {
            case TASK:
                Task task = new Task(name, description);
                task.setId(id);
                task.setStatus(status);
                return task;
            case EPIC:
                Epic epic = new Epic(name, description);
                epic.setId(id);
                epic.setStatus(status);
                return epic;
            case SUBTASK:
                if (epicId == null) {
                    throw new ManagerSaveException("Эпик Id не указан для подзадачи" + value);
                }
                Subtask subtask = new Subtask(name, description, epicId);
                subtask.setId(id);
                subtask.setStatus(status);
                return subtask;
            default:
                throw new ManagerSaveException("Неизвестный тип" + type);
        }
    }
}
