package model.business;

import model.*;

import java.io.*;

import java.util.ArrayList;
import java.util.Objects;


public class FileBackedTaskManager extends InMemoryTaskManager implements TaskManager {
    private File file;

    public FileBackedTaskManager(File file) {
        super(Managers.getDefaultHistory());
        this.file = file;
    }

    public static FileBackedTaskManager loadFromFile(File file) {
        final FileBackedTaskManager manager = new FileBackedTaskManager(file);

        try (BufferedReader readFromLine = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = readFromLine.readLine()) != null) {
                if (line.trim().isEmpty()) {
                    continue;
                }

                String[] splitLine = line.split(",");

                TypeTask type = TypeTask.valueOf(splitLine[1]);
<<<<<<< HEAD

=======
<<<<<<< HEAD
>>>>>>> 095d9193a2e27797bb8e58dce86bb8d30a9906e9
                switch (type) {
                    case TASK:
                        CVSTaskFormat.taskFromString(line);
                        break;
                    case EPIC:
                        CVSTaskFormat.epicFromString(line);
                        break;
                    case SUBTASK:
                        CVSTaskFormat.subtaskFromString(line);
                        break;
                    default:
                        throw new ManagerSaveException("Неизвестный тип" + type);
                }
<<<<<<< HEAD

                switch (type) {
                    case TASK:
                        CVSTaskFormat.taskFromString(line);
                        break;
                    case EPIC:
                        CVSTaskFormat.epicFromString(line);
                        break;
                    case SUBTASK:
                        CVSTaskFormat.subtaskFromString(line);
                        break;
                    default:
                        throw new ManagerSaveException("Неизвестный тип" + type);
                }

=======
=======
                    switch (type) {
                        case TASK:
                            CVSTaskFormat.taskFromString(line);
                            break;
                        case EPIC:
                            CVSTaskFormat.epicFromString(line);
                            break;
                        case SUBTASK:
                            CVSTaskFormat.subtaskFromString(line);
                            break;
                        default:
                            throw new ManagerSaveException("Неизвестный тип" + type);
                    }
>>>>>>> c3219c3832d27f7fc6a73153f760e57e3b25130a
>>>>>>> 095d9193a2e27797bb8e58dce86bb8d30a9906e9
            }
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка при чтении задач", e);
        }
        return manager;
    }

    public void save() {
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file))) {

            for (Task task : getTasks()) {
                bufferedWriter.write(CVSTaskFormat.toString(task));
                bufferedWriter.newLine();
            }

            for (Epic epic : getEpics()) {
                bufferedWriter.write(CVSTaskFormat.toString(epic));
                bufferedWriter.newLine();
            }

            for (Subtask subtask : getSubtask()) {
                bufferedWriter.write(CVSTaskFormat.toString(subtask));
                bufferedWriter.newLine();
            }

        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка записи файла файла", e);
        }
    }

    @Override
    public Integer addNewTask(Task task) {
        Integer taskId = super.addNewTask(task);
        save();
        return taskId;
    }

    public Integer addNewEpic(Epic epic) {
        Integer epicId = super.addNewEpic(epic);
        save();
        return epicId;
    }

    @Override
    public Integer addNewSubtask(Subtask subtask) {
        Integer subtaskId = super.addNewSubtask(subtask);
        save();
        return subtaskId;
    }

    @Override
    public ArrayList<Task> getTasks() {
        return super.getTasks();
    }

    @Override
    public ArrayList<Epic> getEpics() {
        return super.getEpics();
    }

    @Override
    public ArrayList<Subtask> getSubtask() {
        return super.getSubtask();
    }

    @Override
    public void cleanTasks() {
        super.cleanTasks();
        save();
    }

    @Override
    public void cleanEpics() {
        super.cleanEpics();
        save();
    }

    @Override
    public void cleanSubtasks(int epicId) {
        super.cleanSubtasks(epicId);
        save();
    }

    @Override
    public Task getTaskId(int taskId) {
        Task task = super.getTaskId(taskId);
        save();
        return task;
    }

    @Override
    public Epic getEpicId(int epicId) {
        Epic epic = super.getEpicId(epicId);
        save();
        return epic;
    }

    @Override
    public Subtask getSubtaskId(int subtaskId) {
        Subtask subtask = super.getSubtaskId(subtaskId);
        save();
        return subtask;
    }

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        save();
    }

    @Override
    public void updateEpic(Epic epic) {
        super.updateEpic(epic);
        save();
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        super.updateSubtask(subtask);
        save();
    }

    @Override
    public void removeByTaskId(int taskId) {
        super.removeByTaskId(taskId);
        save();
    }

    @Override
    public void removeByEpicId(int epicId) {
        super.removeByEpicId(epicId);
        save();
    }

    @Override
    public void removeBySubtaskId(int subtaskId) {
        super.removeBySubtaskId(subtaskId);
        save();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FileBackedTaskManager that = (FileBackedTaskManager) o;


        return Objects.equals(getTasks(), that.getTasks()) &&
                Objects.equals(getEpics(), that.getEpics()) &&
                Objects.equals(getSubtask(), that.getSubtask());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getTasks(), getEpics(), getSubtask());
    }

    @Override
    public String toString() {
        return "FileBackedTaskManager{" +
                "tasks=" + getTasks() +
<<<<<<< HEAD
                ", epics=" + getEpics() +
                ", epics=" + getEpics() +
=======
<<<<<<< HEAD
                ", epics=" + getEpics() +
=======
                ", epics=" + getEpics()+
>>>>>>> c3219c3832d27f7fc6a73153f760e57e3b25130a
>>>>>>> 095d9193a2e27797bb8e58dce86bb8d30a9906e9
                ", subtasks=" + getSubtask() +
                '}';
    }
}