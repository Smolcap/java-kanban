package model.business;

import model.Epic;
import model.Status;
import model.Subtask;
import model.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class InMemoryTaskManager implements TaskManager {
    private static final Map<Integer, Task> tasks = new HashMap<>();
    private static final Map<Integer, Subtask> subtasks = new HashMap<>();
    private static final Map<Integer, Epic> epics = new HashMap<>();

    private int generationId = 0;

    private HistoryManager historyManager = Managers.getDefaultHistory();

    public InMemoryTaskManager(HistoryManager historyManager) {
        this.historyManager = historyManager;
    }
    @Override
    public ArrayList<Task> getTasks() {
        return new ArrayList<>(tasks.values());
    }

    @Override
    public ArrayList<Epic> getEpics() {
        return new ArrayList<>(epics.values());
    }

    @Override
    public ArrayList<Subtask> getSubtask() {
        return new ArrayList<>(subtasks.values());
    }

    @Override
    public void cleanTasks() {
        tasks.clear();
    }

    @Override
    public void cleanEpics() {
        epics.clear();
        subtasks.clear();
    }

    @Override
    public void cleanSubtasks(int epicId) {
        subtasks.clear();
        updateEpicStatus(epicId);
    }

    @Override
    public Task getTaskId(int taskId) {
        final Task task = tasks.get(taskId);
        historyManager.add(task);
        return task;
    }

    @Override
    public Subtask getSubtaskId(int subtaskId) {
        final Subtask subtask = subtasks.get(subtaskId);
        historyManager.add(subtask);
        return subtask;
    }

    @Override
    public Epic getEpicId(int epicId) {
        final Epic epic = epics.get(epicId);
        historyManager.add(epic);
        return epic;
    }

    @Override
    public Integer addNewTask(Task task) {
        final int id = ++generationId;
        task.setId(++generationId);
        tasks.put(id, task);
        return id;
    }

    @Override
    public Integer addNewEpic(Epic epic) {
        final int id = ++generationId;
        epic.setId(id);
        epics.put(id, epic);
        return id;
    }

    @Override
    public Integer addNewSubtask(Subtask subtask) {
        Epic epic = epics.get(subtask.getEpicId());
        if (epic == null) {
            System.out.println("Нет такой задачи" + subtask.getEpicId());
        }
        final int id = ++generationId;
        subtask.setId(id);
        subtasks.put(id, subtask);
        return id;
    }

    @Override
    public void updateTask(Task task) {
        ArrayList<Task> updateTask = new ArrayList<>(tasks.values());
        if (!tasks.containsKey(task.getId())) {
            System.out.println("Ошибка обновления задачи");
        } else {
            removeByTaskId(task.getId());
            tasks.put(task.getId(), task);
        }
    }

    @Override
    public void updateEpic(Epic epic) {
        ArrayList<Task> updateEpic = new ArrayList<>(epics.values());
        if (!epics.containsKey(epic.getId())) {
            System.out.println("Ошибка обновления задачи");
        } else {
            removeByEpicId(epic.getId());
            epics.put(epic.getId(), epic);
        }
        updateEpicStatus(epic.getId());
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        ArrayList<Subtask> updateSubtask = new ArrayList<>(subtasks.values());
        if (!subtasks.containsKey(subtask.getEpicId())) {
            System.out.println("Ошибка обновления задачи");
        } else {
            removeBySubtaskId(subtask.getId());
            subtasks.put(subtask.getId(), subtask);
        }
        updateEpicStatus(subtask.getEpicId());
    }

    @Override
    public void removeByTaskId(int taskId) {
        Task task = tasks.get(taskId);
        if (!tasks.containsKey(taskId)) {
            System.out.println("Задача с ID" + taskId + "не найден");
        } else {
            tasks.remove(taskId);
        }
    }

    @Override
    public void removeByEpicId(int epicId) {
        Epic epic = epics.get(epicId);
        if (!epics.containsKey(epicId)) {
            System.out.println("Епик с ID" + epicId + " не найден");
        } else {
            ArrayList<Integer> subtaskId = epic.getSubtaskId();
            for (Integer subtasksId : subtaskId) {
                Subtask subtask = subtasks.remove(subtasksId);
            }
            epics.remove(epicId);
        }
    }

    @Override
    public void removeBySubtaskId(int subtaskId) {
        Subtask subtask = subtasks.get(subtaskId);
        if (!subtasks.containsKey(subtaskId)) {
            System.out.println("Сабтаск с ID" + subtaskId + " не найден");
        } else {
            subtasks.remove(subtaskId);
        }
        updateEpicStatus(subtask.getEpicId());
    }

    public ArrayList<Subtask> allListSubtaskByEpic(int epicId) {
        ArrayList<Subtask> subtaskArrayList = new ArrayList<>();
        Epic epic = epics.get(epicId);
        if (epic == null) {
            System.out.println("Эписк с ID" + epicId + " не найден");
            return subtaskArrayList;
        }
        for (int subtaskId : epic.getSubtaskId()) {
            Subtask subtask = subtasks.get(subtaskId);
            if (subtask != null) {
                subtaskArrayList.add(subtask);
            }
        }
        return subtaskArrayList;
    }

    static void updateEpicStatus(int epicId) {
        Epic epic = epics.get(epicId);
        if (epic == null) {
            System.out.println("Эпик с ID " + epicId + " не найден.");
            return;
        }
        ArrayList<Integer> subTaskIds = epic.getSubtaskId();

        if (subTaskIds.isEmpty()) {
            epic.setStatus(Status.NEW);
            return;
        }
        for (Integer subtaskId : subTaskIds) {
            Subtask subtask = subtasks.get(subtaskId);
            Status status = subtask.getStatus();
            if (status.equals(Status.DONE)) {
                epic.setStatus(Status.DONE);
            } else {
                epic.setStatus(Status.IN_PROGRESS);
            }
        }
    }
}

