package model;

import model.business.ManagerSaveException;

import java.time.DateTimeException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class CVSTaskFormat {
    static DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("HH:mm|dd.MM.yyyy");

    public static String capitalize(String input) {
        if (input == null || input.isEmpty()) {
            return input;
        }
        return Character.toUpperCase(input.charAt(0)) + input.substring(1).toLowerCase();
    }

    public static String toString(Task task) {
        LocalDateTime startTimeForTask = task.getStartTime();
        String formattedStartTime;
        if (startTimeForTask != null) {
            formattedStartTime = startTimeForTask.format(inputFormatter);
        } else {
            formattedStartTime = "";
        }

        return task.getId() + "," + TypeTask.TASK + "," + task.getName() + "," + task.getStatus() + ","
                + task.getDescription() + "," + task.duration.toMinutes() + ","
                + startTimeForTask;
    }

    public static String toString(Epic epic) {
        LocalDateTime startTimeForEpic = epic.getStartTime();
        String formattedStartTime;
        if (startTimeForEpic != null) {
            formattedStartTime = startTimeForEpic.format(inputFormatter);
        } else {
            formattedStartTime = "";
        }

        return epic.getId() + "," + TypeTask.EPIC + "," + epic.getName() + "," + epic.getStatus() + ","
                + epic.getDescription() + "," + epic.duration.toMinutes() + ","
                + startTimeForEpic;
    }

    public static String toString(Subtask subtask) {
        LocalDateTime startTimeForSubtask = subtask.getStartTime();
        String formattedStartTime;
        if (startTimeForSubtask != null) {
            formattedStartTime = startTimeForSubtask.format(inputFormatter);
        } else {
            formattedStartTime = "";
        }

        return subtask.getId() + "," + TypeTask.SUBTASK + "," + subtask.getName() + "," + subtask.getStatus() + ","
                + subtask.getDescription() + "," + subtask.getEpicId() + "," + subtask.duration.toMinutes() + ","
                + startTimeForSubtask;
    }

    public static Task taskFromString(String value) {
        String[] split = value.split(",");

        int id = Integer.parseInt(split[0]);
        TypeTask typeForTask = TypeTask.valueOf(split[1]);
        String nameForTask = capitalize(split[2].toUpperCase());
        Status statusForTask = Status.valueOf(split[3].trim());
        String descriptionForTask = capitalize(split[4].toUpperCase());
        long totalMinutes = Long.parseLong(split[5]);
        Duration durationForTask = Duration.ofMinutes(totalMinutes);

        String startDateStr = split[6];
        LocalDateTime startTimeForTask = null;
        try {
            if (!"null".equals(startDateStr) && !startDateStr.isEmpty()) {
                startTimeForTask = LocalDateTime.parse(startDateStr, inputFormatter);
            }
        } catch (DateTimeException e) {
            throw new ManagerSaveException("Ошибка парсинга строки");
        }

        Task task = new Task(nameForTask, descriptionForTask);
        task.setId(id);
        task.setStatus(statusForTask);
        task.setType(typeForTask);
        task.setDuration(durationForTask);
        task.setStartTime(startTimeForTask);

        return task;
    }

    public static Epic epicFromString(String value) {
        String[] split = value.split(",");

        int id = Integer.parseInt(split[0]);
        TypeTask typeForEpic = TypeTask.valueOf(split[1]);
        String nameForEpic = capitalize(split[2].toUpperCase());
        Status statusForEpic = Status.valueOf(split[3].trim());
        String descriptionForEpic = capitalize(split[4].toUpperCase());
        long totalMinutesForEpic = Long.parseLong(split[5]);
        Duration durationForEpic = Duration.ofMinutes(totalMinutesForEpic);

        String startDateStr = split[6];
        LocalDateTime startTimeForEpic = null;
        try {
            if (!"null".equals(startDateStr) && !startDateStr.isEmpty()) {
                startTimeForEpic = LocalDateTime.parse(startDateStr, inputFormatter);
            }
        } catch (DateTimeException e) {
            throw new ManagerSaveException("Ошибка парсинга строки");
        }

        Epic epic = new Epic(nameForEpic, descriptionForEpic);
        epic.setId(id);
        epic.setStatus(statusForEpic);
        epic.setType(typeForEpic);
        epic.setDuration(durationForEpic);
        epic.setStartTime(startTimeForEpic);

        return epic;
    }

    public static Subtask subtaskFromString(String value) {
        String[] split = value.split(",");

        int id = Integer.parseInt(split[0]);
        TypeTask typeForSubtask = TypeTask.valueOf(split[1]);
        String nameForSubtask = capitalize(split[2].toUpperCase());
        Status statusForSubtask = Status.valueOf(split[3].trim());
        String descriptionForSubtask = capitalize(split[4].toUpperCase());
        int epicId = Integer.parseInt(split[5]);
        long totalMinutesForSubtask = Long.parseLong(split[6]);
        Duration durationForSubtask = Duration.ofMinutes(totalMinutesForSubtask);

        String startDateStr = split[7];
        LocalDateTime startTimeForSubtask = null;
        try {
            if (!"null".equals(startDateStr) && !startDateStr.isEmpty()) {
                startTimeForSubtask = LocalDateTime.parse(startDateStr, inputFormatter);
            }
        } catch (DateTimeException e) {
            throw new ManagerSaveException("Ошибка парсинга строки");
        }

        Subtask subtask = new Subtask(nameForSubtask, descriptionForSubtask, epicId);
        subtask.setId(id);
        subtask.setStatus(statusForSubtask);
        subtask.setType(typeForSubtask);
        subtask.setDuration(durationForSubtask);
        subtask.setStartTime(startTimeForSubtask);

        return subtask;
    }
}


