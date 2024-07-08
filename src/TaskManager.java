import java.util.ArrayList;
import java.util.HashMap;


public class TaskManager {
    private static final HashMap<Integer, Task> tasks = new HashMap<>();
    private static final HashMap<Integer, Subtask> subtasks = new HashMap<>();
    private static final HashMap<Integer, Epic> epics = new HashMap<>();

    private int generationId = 0;

    public ArrayList<Task> getTasks() {
        ArrayList<Task> allTasks = new ArrayList<>();
        for (Task task : tasks.values()) {
            allTasks.add(task);
        }
        return allTasks;
    }

    public ArrayList<Epic> getEpics() {
        ArrayList<Epic> allEpics = new ArrayList<>();
        for (Epic epcic : epics.values()) {
            allEpics.add(epcic);
        }
        return allEpics;
    }

    public ArrayList<Subtask> getSubtask() {
        ArrayList<Subtask> allSubtask = new ArrayList<>();
        for (Subtask subtask : subtasks.values()) {
            allSubtask.add(subtask);
        }
        return allSubtask;
    }

    public void cleanTasks() {
        tasks.clear();
    }

    public void cleanEpics() {
        epics.clear();
        subtasks.clear();
    }

    public void cleanSubtasks() {
        subtasks.clear();
    }

    public Task getTaskId(int taskId) {
       return tasks.get(taskId);
    }

    public Subtask getSubtaskId(int subtaskId) {
        return subtasks.get(subtaskId);
    }

    public Epic getEpicId(int epicId) {
       return epics.get(epicId);
    }

    public Integer addNewTask(Task task) {
        final int id = ++generationId;
        task.setId(id);
        tasks.put(id, task);
        return id;
    }

    public Integer addNewEpic(Epic epic) {
        final int id = ++generationId;
        epic.setId(id);
        epics.put(id, epic);
        return id;
    }

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

    public void updateTask(Task task) {
        tasks.put(task.getId(), task);
    }

    public void updateEpic(Epic epic) {
        epics.put(epic.getId(), epic);
    }

    public void updateSubtask(Subtask subtask) {
        subtasks.put(subtask.getId(), subtask);
    }

    public ArrayList<Task> removeByTaskId(int taskId) {
        ArrayList<Task> taskArrayList = new ArrayList<>();
        Task task = tasks.get(taskId);
        if (task == null) {
            System.out.println("Задача с ID" + taskId + "не найден");
        } else {
            tasks.remove(taskId);
        }
        return taskArrayList;
    }

    public ArrayList<Epic> removeByEpicId(int epicId) {
        ArrayList<Epic> epicArrayList = new ArrayList<>();
        Epic epic = epics.get(epicId);
        if (epic == null) {
            System.out.println("Епик с ID" + epicId + " не найден");
        } else {
            ArrayList<Integer> subtaskId = epic.subTaskId;
            for (Integer subtasksId : subtaskId) {
                Subtask subtask = subtasks.remove(subtasksId);
            }
            epics.remove(epicId);
        }
        return epicArrayList;
    }

    public ArrayList<Subtask> removeBySubtaskId(int subtaskId) {
        ArrayList<Subtask> subtaskArrayList = new ArrayList<>();
        Subtask subtask = subtasks.get(subtaskId);
        if (subtask == null) {
            System.out.println("Сабтаск с ID" + subtaskId + " не найден");
        } else {
            subtasks.remove(subtaskId);
        }
        return subtaskArrayList;
    }

    public ArrayList<Subtask> allListSubtaskByEpic(int epicId) {
        ArrayList<Subtask> subtaskArrayList = new ArrayList<>();
        Epic epic = epics.get(epicId);
        if (epic == null) {
            System.out.println("Эписк с ID" + epicId + " не найден");
            return subtaskArrayList;
        }
        for (int subtaskId : epic.subTaskId) {
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
        ArrayList<Integer> subTaskIds = epic.getSubTaskId();

        if (subTaskIds.isEmpty()) {
            epic.setStatus(Status.NEW);
            return;
        }
        for (Integer subtaskId : subTaskIds) {
            Subtask subtask = subtasks.get(subtaskId);
            Status staus = subtask.getStatus();
            if (staus.equals(Status.DONE)) {
                epic.setStatus(Status.DONE);
            } else {
                epic.setStatus(Status.IN_PROGRESS);
            }
        }
    }
}

