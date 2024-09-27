package model.business;

import model.Epic;
import model.Status;
import model.Subtask;
import model.Task;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class InMemoryTaskManager implements TaskManager {
    private final Map<Integer, Task> tasks = new HashMap<>();
    private final Map<Integer, Subtask> subtasks = new HashMap<>();
    private final Map<Integer, Epic> epics = new HashMap<>();

    private int generationId = 0;

    private HistoryManager historyManager = Managers.getDefaultHistory();

    public InMemoryTaskManager(HistoryManager historyManager) {
        this.historyManager = historyManager;
    }

    @Override
    public List<Task> getPrioritizedTasks() {
        TreeSet<Task> taskSet = tasks.values().stream()
                .filter(Objects::nonNull)
                .filter(task -> task.getStartTime() != null)
                .collect(Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(Task::getStartTime))));

        TreeSet<Epic> epicSet = epics.values().stream()
                .filter(Objects::nonNull)
                .filter(epic -> epic.getStartTime() != null)
                .collect(Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(Epic::getStartTime))));

        TreeSet<Subtask> subtaskSet = subtasks.values().stream()
                .filter(Objects::nonNull)
                .filter(subtask -> subtask.getStartTime() != null)
                .collect(Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(Subtask::getStartTime))));

        List<Task> prioritizedTasks = new ArrayList<>();

        prioritizedTasks.addAll(taskSet);
        prioritizedTasks.addAll(epicSet);
        prioritizedTasks.addAll(subtaskSet);

        return prioritizedTasks;
    }

    @Override
    public boolean validationByIntersection(Task existingTask, Task newSubtask) {
        LocalDateTime existingStart = existingTask.getStartTime();
        LocalDateTime existingEnd = existingStart.plus(existingTask.getDuration());

        LocalDateTime newStart = newSubtask.getStartTime();
        LocalDateTime newEnd = newStart.plus(newSubtask.getDuration());

        return (newStart.isBefore(existingEnd) && newEnd.isAfter(existingStart));
    }

    public void add(Task task) {
        boolean intersectionsForTask = getPrioritizedTasks().stream()
                .anyMatch(createdTask -> validationByIntersection(createdTask, task));
        if (intersectionsForTask) {
            throw new ManagerSaveException("Задача пересекается с одной из существующих задач");
        }
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
        List<Integer> subtaskIds = epics.get(epicId).getSubtaskId();
        subtasks.keySet().removeIf(subtaskId -> subtaskIds.contains(subtaskId));
    }

    @Override
    public Task getTaskId(int taskId) {
        Optional<Task> optionalTask = Optional.ofNullable(tasks.get(taskId));
        optionalTask.ifPresent(task -> historyManager.add(task));
        return optionalTask.orElse(null);
    }

    @Override
    public Subtask getSubtaskId(int subtaskId) {
        Optional<Subtask> optionalSubtask = Optional.ofNullable(subtasks.get(subtaskId));
        optionalSubtask.ifPresent(subtask -> historyManager.add(subtask));
        return optionalSubtask.orElse(null);
    }

    @Override
    public Epic getEpicId(int epicId) {
        Optional<Epic> optionalEpic = Optional.ofNullable(epics.get(epicId));
        optionalEpic.ifPresent(epic -> historyManager.add(epic));
        return optionalEpic.orElse(null);
    }

    @Override
    public Integer addNewTask(Task task) {
        add(task);
        final int id = ++generationId;
        task.setId(id);
        tasks.put(id, task);
        System.out.println("Задача успешно добавлена");
        return id;
    }

    @Override
    public Integer addNewEpic(Epic epic) {
        add(epic);
        final int id = ++generationId;
        epic.setId(id);
        epics.put(id, epic);
        updateEpicDuration(epic);
        return id;
    }

    @Override
    public Integer addNewSubtask(Subtask subtask) {
        Epic epic = epics.get(subtask.getEpicId());
        if (epic == null) {
            throw new ManagerSaveException("Нет такого эпика" + subtask.getEpicId());
        }
        add(subtask);
        final int id = ++generationId;
        subtask.setId(id);
        subtasks.put(id, subtask);
        epic.addSubtaskId(id);
        updateEpicDuration(epic);
        return id;
    }

    @Override
    public void updateTask(Task task) {
        Optional<Task> optionalUpdateTask = Optional.ofNullable(tasks.get(task.getId()));

        optionalUpdateTask.ifPresentOrElse(existingTask -> {
                    existingTask.setName(task.getName());
                    existingTask.setDescription(task.getDescription());

                    if (task.getStartTime() != null) {
                        existingTask.setStartTime(task.getStartTime());
                    }
                    if (task.getDuration() != null) {
                        existingTask.setDuration(task.getDuration());
                    }

                    System.out.println("Задача с ID " + task.getId() + " успешно обновлена.");
                },
                () -> System.out.println("Задача не была обновлена"));
    }

    @Override
    public void updateEpic(Epic epic) {
        Optional<Epic> optionalUpdateEpic = Optional.ofNullable(epics.get(epic.getId()));

        optionalUpdateEpic.ifPresentOrElse(exsistingEpic -> {
                    exsistingEpic.setName(epic.getName());
                    exsistingEpic.setDescription(epic.getDescription());

                    if (epic.getStartTime() != null) {
                        exsistingEpic.setStartTime(epic.getStartTime());
                    }
                    if (epic.getEndTime() != null) {
                        exsistingEpic.setEndTime(epic.getEndTime());
                    }
                    System.out.println("Эпик с ID " + epic.getId() + " успешно обновлен.");
                    updateEpicStatus(epic.getId());
                },
                () -> System.out.println("Эпик не был обновлён"));
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        Optional<Subtask> optionalUpdateSubtask = Optional.ofNullable(subtasks.get(subtask.getId()));

        optionalUpdateSubtask.ifPresentOrElse(exsistingSubtask -> {
                    exsistingSubtask.setName(subtask.getName());
                    exsistingSubtask.setDescription(subtask.getDescription());

                    if (subtask.getStartTime() != null) {
                        exsistingSubtask.setStartTime(subtask.getStartTime());
                    }
                    if (subtask.getDuration() != null) {
                        exsistingSubtask.setDuration(subtask.getDuration());
                    }
                    System.out.println("Подзадача с ID " + subtask.getId() + " успешно обновлена.");

                    updateEpicStatus(subtask.getEpicId());

                    updateEpicDuration(epics.get(subtask.getEpicId()));
                },
                () -> System.out.println("Подзадача не была обновлена"));
    }

    @Override
    public void removeByTaskId(int taskId) {
        Optional<Task> removeTask = Optional.ofNullable(tasks.get(taskId));
        removeTask.ifPresentOrElse(task -> {
                    historyManager.remove(taskId);
                    tasks.remove(taskId);
                    System.out.println("Задача была успешно удалена");
                },
                () -> System.out.println("Задача c ID " + taskId + " не удалена ")
        );
    }

    @Override
    public void removeByEpicId(int epicId) {
        Optional<Epic> removeEpic = Optional.ofNullable(epics.get(epicId));
        removeEpic.ifPresentOrElse(epic -> {
                    List<Integer> subtaskIds = epic.getSubtaskId();

                    subtaskIds.forEach(subtaskId -> {
                        historyManager.remove(subtaskId);
                        subtasks.remove(subtaskId);
                    });

                    epics.remove(epicId);
                    historyManager.remove(epicId);
                    System.out.println("Эпик с ID " + epicId + " был успешно удален");
                },
                () -> System.out.println("Эпик с ID " + epicId + " не найден"));
    }

    @Override
    public void removeBySubtaskId(int subtaskId) {
        Optional<Subtask> removeSubtask = Optional.ofNullable(subtasks.get(subtaskId));

        removeSubtask.ifPresentOrElse(subtask -> {
                    historyManager.remove(subtaskId);
                    subtasks.remove(subtaskId);

                    epics.values().forEach(epic -> {
                        epic.removeSubtaskId(subtaskId);
                        updateEpicStatus(epic.getId());
                        updateEpicDuration(epic);
                    });
                    System.out.println("Подзадача с ID " + subtaskId + " была успешно удалена");
                },
                () -> System.out.println("Подзадача с ID " + subtaskId + " не найдена"));
    }

    @Override
    public List<Subtask> allListSubtaskByEpic(int epicId) {
        Optional<Epic> optionalSubtask = Optional.ofNullable(epics.get(epicId));

        List<Integer> subtasksList = optionalSubtask.get().getSubtaskId();
        return subtasksList.stream()
                .map(subtaskId -> subtasks.get(subtaskId))
                .collect(Collectors.toList());
    }

    public void updateEpicStatus(int epicId) {
        Optional<Epic> optionalEpic = Optional.ofNullable(epics.get(epicId));

        optionalEpic.ifPresentOrElse(epic -> {
            List<Integer> subTaskIds = epic.getSubtaskId();
            List<Status> subtaskStatus = subTaskIds.stream()
                    .map(subTaskId -> Optional.ofNullable(subtasks.get(subTaskId)))
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .map(Subtask::getStatus)
                    .collect(Collectors.toList());

            boolean allMatchStatusDone = subtaskStatus.stream().allMatch(status -> status.equals(Status.DONE));
            boolean anyInProgressOrNew = subtaskStatus.stream().anyMatch(status ->
                    status.equals(Status.IN_PROGRESS) || status.equals(Status.NEW));

            if (allMatchStatusDone) {
                epic.setStatus(Status.DONE);
            } else if (anyInProgressOrNew) {
                epic.setStatus(Status.IN_PROGRESS);
            } else {
                epic.setStatus(Status.NEW);
            }
        }, () -> {
            System.out.println("Эпик с ID " + epicId + " не найден.");
        });
    }

    public void updateEpicDuration(Epic epic) {
        Optional<Epic> optionalUpdateDuration = Optional.ofNullable(epics.get(epic.getId()));

        optionalUpdateDuration.ifPresent(epicCalculated -> {
            List<Integer> subtaskId = epic.getSubtaskId();
            List<Duration> subtaskDuration = subtaskId.stream()
                    .map(subtaskIds -> Optional.ofNullable(subtasks.get(subtaskIds)))
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .map(Subtask::getDuration)
                    .toList();

            Duration totalDuration = subtaskDuration.stream()
                    .reduce(Duration.ZERO, Duration::plus);

            LocalDateTime start = subtaskId.stream()
                    .map(subtaskIds -> Optional.ofNullable(subtasks.get(subtaskIds)))
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .map(Subtask::getStartTime)
                    .filter(Objects::nonNull)
                    .min(LocalDateTime::compareTo)
                    .orElse(null);

            LocalDateTime end = subtaskId.stream()
                    .map(subtaskIds -> Optional.ofNullable(subtasks.get(subtaskIds)))
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .map(Subtask::getEndTime)
                    .filter(Objects::nonNull)
                    .max(LocalDateTime::compareTo)
                    .orElse(null);

            epicCalculated.setDuration(totalDuration);
            epicCalculated.setStartTime(start);
            epicCalculated.setEndTime(end);
        });
    }
}

