package model.business;

import model.*;

import java.io.*;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Optional;

public class FileBackedTaskManager extends InMemoryTaskManager implements TaskManager {
    private File file;

    public FileBackedTaskManager(File file) {
        super(Managers.getDefaultHistory());
        this.file = file;
    }

    public static FileBackedTaskManager loadFromFile(File file) {
        final FileBackedTaskManager manager = new FileBackedTaskManager(file);

        try (BufferedReader readFromLine = new BufferedReader(new FileReader(file, StandardCharsets.UTF_8))) {
            readFromLine.readLine();

            while (readFromLine.ready()) {
                String line = readFromLine.readLine();

                String[] split = line.split(",");
                TypeTask type = TypeTask.valueOf(split[1].trim());
                switch (type) {
                    case TASK:
                        Task task = CVSTaskFormat.taskFromString(line);
                        manager.addNewTask(task);
                        break;
                    case EPIC:
                        Epic epic = CVSTaskFormat.epicFromString(line);
                        manager.addNewEpic(epic);
                        break;
                    case SUBTASK:
                        Subtask subtask = CVSTaskFormat.subtaskFromString(line);
                        manager.addNewSubtask(subtask);
                        break;
                    default:
                        throw new ManagerSaveException("Неизвестный тип задач" + type);
                }
            }
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка при чтении задач", e);
        }
        return manager;
    }

    public void save() {
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file, StandardCharsets.UTF_8))) {
            bufferedWriter.write("id,type,name,status,description,epic,duration,startTime");
            bufferedWriter.newLine();

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
        Optional.ofNullable(task)
                .orElseThrow(() -> new ManagerSaveException("Пустая задача"));

        Integer taskId = super.addNewTask(task);
        save();
        return taskId;
    }

    public Integer addNewEpic(Epic epic) {
        Optional.ofNullable(epic)
                .orElseThrow(() -> new ManagerSaveException("Пустойой эпик"));

        Integer epicId = super.addNewEpic(epic);
        save();
        return epicId;
    }

    @Override
    public Integer addNewSubtask(Subtask subtask) {
        Optional.ofNullable(subtask)
                .orElseThrow(() -> new ManagerSaveException("Пустая подзадача"));

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
        return task;
    }

    @Override
    public Epic getEpicId(int epicId) {
        Epic epic = super.getEpicId(epicId);
        return epic;
    }

    @Override
    public Subtask getSubtaskId(int subtaskId) {
        Subtask subtask = super.getSubtaskId(subtaskId);
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
}