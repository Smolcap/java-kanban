package model.business;

import model.Epic;
import model.Subtask;
import model.Task;

import java.util.ArrayList;

public interface TaskManager {

    ArrayList<Task> getTasks();

    ArrayList<Epic> getEpics();

    ArrayList<Subtask> getSubtask();

    void cleanTasks();

    void cleanEpics();

    void cleanSubtasks(int epicId);

    Task getTaskId(int taskId);

    Subtask getSubtaskId(int subtaskId);

    Epic getEpicId(int epicId);

    Integer addNewTask(Task task);

    Integer addNewEpic(Epic epic);

    Integer addNewSubtask(Subtask subtask);

    void updateTask(Task task);

    void updateEpic(Epic epic);

    void updateSubtask(Subtask subtask);

    void removeByTaskId(int taskId);

    void removeByEpicId(int epicId);

    void removeBySubtaskId(int subtaskId);

}
