package business;

import com.google.gson.Gson;
import model.Epic;
import model.Subtask;
import model.Task;
import model.business.HistoryManager;
import model.business.Managers;
import model.business.TaskManager;
import model.http.HttpTaskServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


public class HttpTaskServerTest {
    private TaskManager taskManager;
    private HistoryManager historyManager;
    private HttpTaskServer httpTaskServer;
    private Gson gson;

    @BeforeEach
    public void setup() throws IOException {
        this.taskManager = Managers.getDefault();
        this.httpTaskServer = new HttpTaskServer(taskManager);
        this.historyManager = Managers.getDefaultHistory();
        this.gson = Managers.getGson();
        taskManager.cleanTasks();
        taskManager.cleanEpics();

        List<Epic> epics = taskManager.getEpics();
        for (Epic epic : epics) {
            taskManager.cleanSubtasks(epic.getId());
        }

        this.httpTaskServer.start();
    }

    @AfterEach
    public void shutDown() {
        httpTaskServer.stop();
    }

    @Test
    public void shouldAddTask() throws IOException, InterruptedException {
        Task task = new Task("Test 1", "Test 1");
        task.setStartTime(LocalDateTime.of(2024, 9, 12, 10, 5));
        task.setDuration(Duration.ofMinutes(10));

        String jsonTask = gson.toJson(task);

        HttpClient httpClient = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8080/tasks");

        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(uri)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonTask))
                .build();

        HttpResponse<String> httpResponse = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());

        assertEquals(201, httpResponse.statusCode());
    }

    @Test
    public void shouldAddEpic() throws IOException, InterruptedException {
        Epic epic = new Epic("Test №", "Test №");
        epic.setStartTime(LocalDateTime.of(2024, 9, 12, 10, 5));
        epic.setEndTime(LocalDateTime.of(2024, 9, 12, 10, 19));

        String jsonTask = gson.toJson(epic);

        HttpClient httpClient = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8080/epics");

        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(uri)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonTask))
                .build();

        HttpResponse<String> httpResponse = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());

        assertEquals(201, httpResponse.statusCode());
    }

    @Test
    public void shouldAddSubtask() throws IOException, InterruptedException {
        Epic epic = new Epic("Test №1", "Description №1");
        final int epicId = taskManager.addNewEpic(epic);

        Subtask subtask = new Subtask("Test №1", "Description №1", epicId);
        subtask.setStartTime(LocalDateTime.of(2024, 9, 12, 10, 5));
        subtask.setDuration(Duration.ofMinutes(10));

        String jsonTask = gson.toJson(subtask);

        HttpClient httpClient = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8080/subtasks");

        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(uri)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonTask))
                .build();

        HttpResponse<String> httpResponse = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());

        assertEquals(201, httpResponse.statusCode());
    }

    @Test
    public void shouldUpdateTask() throws IOException, InterruptedException {
        Task task = new Task("Test 1", "Test 1");
        task.setStartTime(LocalDateTime.of(2024, 9, 12, 10, 5));
        task.setDuration(Duration.ofMinutes(10));
        taskManager.addNewTask(task);

        task.setName("Test number");
        task.setDescription("Test number 2");

        String jsonTask = gson.toJson(task);

        HttpClient httpClient = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8080/tasks");

        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(uri)
                .PUT(HttpRequest.BodyPublishers.ofString(jsonTask))
                .build();

        HttpResponse<String> httpResponse = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());

        assertEquals(201, httpResponse.statusCode());
        System.out.println("HTTP Response: " + httpResponse.body());
    }

    @Test
    public void shouldUpdateEpic() throws IOException, InterruptedException {
        Epic epic = new Epic("Test №1", "Description №1");
        final int epicId = taskManager.addNewEpic(epic);

        Subtask subtask = new Subtask("Test №1", "Description №1", epicId);
        subtask.setStartTime(LocalDateTime.of(2024, 9, 12, 10, 5));
        subtask.setDuration(Duration.ofMinutes(10));
        taskManager.addNewSubtask(subtask);

        epic.setName("Test number");
        epic.setDescription("Test number 2");

        String jsonEpic = gson.toJson(epic);

        HttpClient httpClient = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8080/epics");

        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(uri)
                .PUT(HttpRequest.BodyPublishers.ofString(jsonEpic))
                .build();

        HttpResponse<String> httpResponse = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());

        assertEquals(201, httpResponse.statusCode());
        System.out.println("HTTP Response: " + httpResponse.body());
    }

    @Test
    public void UpdateSubtask() throws IOException, InterruptedException {
        Epic epic = new Epic("Test №1", "Description №1");
        final int epicId = taskManager.addNewEpic(epic);

        Subtask subtask = new Subtask("Test №1", "Description №1", epicId);
        subtask.setStartTime(LocalDateTime.of(2024, 9, 12, 10, 5));
        subtask.setDuration(Duration.ofMinutes(10));
        taskManager.addNewSubtask(subtask);

        subtask.setName("Test number");
        subtask.setDescription("Test number 2");

        String jsonSubtask = gson.toJson(subtask);

        HttpClient httpClient = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8080/subtasks");

        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(uri)
                .PUT(HttpRequest.BodyPublishers.ofString(jsonSubtask))
                .build();

        HttpResponse<String> httpResponse = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());

        assertEquals(201, httpResponse.statusCode());
        System.out.println("HTTP Response: " + httpResponse.body());
    }

    @Test
    public void shouldGetTasks() throws IOException, InterruptedException {
        Task task = new Task("Test 2", "Test 2");
        task.setStartTime(LocalDateTime.of(2024, 9, 12, 10, 5));
        task.setDuration(Duration.ofMinutes(10));
        int taskId = taskManager.addNewTask(task);
        Task saveTask = taskManager.getTaskId(taskId);

        HttpClient httpClient = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8080/tasks");

        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(uri)
                .GET()
                .build();

        HttpResponse<String> httpResponse = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, httpResponse.statusCode());

        List<Task> taskFromManager = taskManager.getTasks();

        assertNotNull(taskFromManager, "Должна быть одна созданная задача");
        Assertions.assertEquals(1, taskFromManager.size(), "Некорректное количество задач");
        Assertions.assertEquals("Test 2", taskFromManager.get(0).getName(), "Некорректное имя задачи");
    }

    @Test
    public void shouldGetEpics() throws IOException, InterruptedException {
        Epic epic = new Epic("Test №1", "Description №1");
        final int epicId = taskManager.addNewEpic(epic);

        Subtask subtask = new Subtask("Test №1", "Description №1", epicId);
        subtask.setStartTime(LocalDateTime.of(2024, 9, 12, 10, 5));
        subtask.setDuration(Duration.ofMinutes(10));
        taskManager.addNewSubtask(subtask);

        HttpClient httpClient = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8080/epics");

        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(uri)
                .GET()
                .build();

        HttpResponse<String> httpResponse = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, httpResponse.statusCode());

        List<Epic> taskFromManager = taskManager.getEpics();

        assertNotNull(taskFromManager, "Должна быть одна созданная задача");
        Assertions.assertEquals(1, taskFromManager.size(), "Некорректное количество задач");
    }

    @Test
    public void shouldGetSubtasks() throws IOException, InterruptedException {
        Epic epic = new Epic("Test №1", "Description №1");
        final int epicId = taskManager.addNewEpic(epic);

        Subtask subtask = new Subtask("Test №1", "Description №1", epicId);
        subtask.setStartTime(LocalDateTime.of(2024, 9, 12, 10, 5));
        subtask.setDuration(Duration.ofMinutes(10));
        taskManager.addNewSubtask(subtask);

        HttpClient httpClient = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8080/subtasks");

        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(uri)
                .GET()
                .build();

        HttpResponse<String> httpResponse = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, httpResponse.statusCode());

        List<Subtask> taskFromManager = taskManager.getSubtask();

        assertNotNull(taskFromManager, "Должна быть одна созданная задача");
        Assertions.assertEquals(1, taskFromManager.size(), "Некорректное количество задач");
    }

    @Test
    public void shouldGetTasksById() throws IOException, InterruptedException {
        Task task = new Task("Test 3", "Test 3");
        task.setStartTime(LocalDateTime.of(2024, 9, 12, 10, 5));
        task.setDuration(Duration.ofMinutes(10));
        final int taskId = taskManager.addNewTask(task);
        Task saveTask = taskManager.getTaskId(taskId);

        HttpClient httpClient = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8080/tasks/1");

        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(uri)
                .GET()
                .build();

        HttpResponse<String> httpResponse = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, httpResponse.statusCode());
        Assertions.assertEquals(task, saveTask, "Задачи должены быть одинковы");
    }

    @Test
    public void shouldGetEpicById() throws IOException, InterruptedException {
        Epic epic = new Epic("Test №1", "Description №1");
        final int epicId = taskManager.addNewEpic(epic);
        final Epic saveEpic = taskManager.getEpicId(epicId);

        Subtask subtask = new Subtask("Test №1", "Description №1", epicId);
        subtask.setStartTime(LocalDateTime.of(2024, 9, 12, 10, 5));
        subtask.setDuration(Duration.ofMinutes(10));
        taskManager.addNewSubtask(subtask);

        HttpClient httpClient = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8080/epics/1");

        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(uri)
                .GET()
                .build();

        HttpResponse<String> httpResponse = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, httpResponse.statusCode());
        Assertions.assertEquals(epic, saveEpic, "Задачи должены быть одинковы");
    }

    @Test
    public void shouldGetSubtaskById() throws IOException, InterruptedException {
        Epic epic = new Epic("Test №1", "Description №1");
        final int epicId = taskManager.addNewEpic(epic);

        Subtask subtask = new Subtask("Test №1", "Description №1", epicId);
        subtask.setStartTime(LocalDateTime.of(2024, 9, 12, 10, 5));
        subtask.setDuration(Duration.ofMinutes(10));
        final int subtaskId = taskManager.addNewSubtask(subtask);
        final Subtask saveSubtask = taskManager.getSubtaskId(subtaskId);

        HttpClient httpClient = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8080/subtasks/1");

        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(uri)
                .GET()
                .build();

        HttpResponse<String> httpResponse = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, httpResponse.statusCode());
        Assertions.assertEquals(subtask, saveSubtask, "Задачи должены быть одинковы");
    }

    @Test
    public void shouldDeleteTask() throws IOException, InterruptedException {
        Task task = new Task("Test 3", "Test 3");
        task.setStartTime(LocalDateTime.of(2024, 9, 12, 10, 5));
        task.setDuration(Duration.ofMinutes(10));
        int taskId = taskManager.addNewTask(task);
        Task saveTask = taskManager.getTaskId(taskId);

        HttpClient httpClient = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8080/tasks/" + taskId);

        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(uri)
                .DELETE()
                .build();

        HttpResponse<String> httpResponse = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, httpResponse.statusCode());

        Task removeTask = taskManager.getTaskId(taskId);

        Assertions.assertNull(removeTask, "Задача должна быть удалена");
    }

    @Test
    public void shouldDeleteEpic() throws IOException, InterruptedException {
        Epic epic = new Epic("Test №1", "Description №1");
        epic.setStartTime(LocalDateTime.of(2024, 9, 12, 10, 5));
        epic.setDuration(Duration.ofMinutes(10));
        final int epicId = taskManager.addNewEpic(epic);
        final Epic saveEpic = taskManager.getEpicId(epicId);

        HttpClient httpClient = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8080/epics/" + epicId);

        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(uri)
                .DELETE()
                .build();

        HttpResponse<String> httpResponse = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, httpResponse.statusCode());

        Epic removeEpic = taskManager.getEpicId(epicId);

        Assertions.assertNull(removeEpic, "Задача должна быть удалена");
    }

    @Test
    public void shouldDeleteSubtask() throws IOException, InterruptedException {
        Epic epic = new Epic("Test №1", "Description №1");
        final int epicId = taskManager.addNewEpic(epic);

        Subtask subtask = new Subtask("Test №1", "Description №1", epicId);
        subtask.setStartTime(LocalDateTime.of(2024, 9, 12, 10, 5));
        subtask.setDuration(Duration.ofMinutes(10));
        final int subtaskId = taskManager.addNewSubtask(subtask);
        final Subtask saveSubtask = taskManager.getSubtaskId(subtaskId);

        HttpClient httpClient = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8080/subtasks/" + subtaskId);

        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(uri)
                .DELETE()
                .build();

        HttpResponse<String> httpResponse = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, httpResponse.statusCode());

        Subtask removeSubtask = taskManager.getSubtaskId(subtaskId);

        Assertions.assertNull(removeSubtask, "Задача должна быть удалена");
    }

    @Test
    public void shouldGetEpicListSubtasks() throws IOException, InterruptedException {
        Epic epic = new Epic("Test №1", "Description №1");
        final int epicId = taskManager.addNewEpic(epic);

        Subtask subtask = new Subtask("Test №1", "Description №1", epicId);
        subtask.setStartTime(LocalDateTime.of(2024, 9, 12, 10, 5));
        subtask.setDuration(Duration.ofMinutes(10));
        taskManager.addNewSubtask(subtask);

        HttpClient httpClient = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8080/epics/" + epicId + "/subtasks");

        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(uri)
                .GET()
                .build();

        HttpResponse<String> httpResponse = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, httpResponse.statusCode());

        List<Subtask> taskFromManager = taskManager.allListSubtaskByEpic(epicId);

        assertNotNull(taskFromManager, "Должна быть одна созданная задача");
        Assertions.assertEquals(1, taskFromManager.size(), "Некорректное количество задач");
    }

    @Test
    public void getHistoryTask() throws IOException, InterruptedException {
        Task task3 = new Task("Test №3", "Description №3");
        final int getId3 = taskManager.addNewTask(task3);
        final Task savedTask3 = taskManager.getTaskId(getId3);

        Task task4 = new Task("Test №4", "Description №4");
        final int getId4 = taskManager.addNewTask(task4);
        final Task savedTask4 = taskManager.getTaskId(getId4);

        historyManager.add(task3);
        historyManager.add(task4);

        HttpClient httpClient = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8080/history");

        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(uri)
                .GET()
                .build();

        HttpResponse<String> httpResponse = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, httpResponse.statusCode());

        List<Task> allTask = historyManager.getHistory();

        Assertions.assertFalse(allTask.isEmpty(), "История не должна быть пустой");
        Assertions.assertEquals(2, allTask.size(), "Количество задач в истории должно быть 2");
        Assertions.assertTrue(allTask.contains(savedTask3), "История должна содержать задачу 3");
        Assertions.assertTrue(allTask.contains(savedTask4), "История должна содержать задачу 4");
    }

    @Test
    public void shouldGetPrioritizedTask() throws IOException, InterruptedException {
        Task task1 = new Task("Test №1", "Description №1");
        task1.setStartTime(LocalDateTime.of(2024, 9, 12, 10, 5));
        task1.setDuration(Duration.ofMinutes(10));
        taskManager.addNewTask(task1);

        Task task2 = new Task("Test №2", "Description №2");
        task2.setStartTime(LocalDateTime.of(2024, 9, 12, 9, 5));
        task2.setDuration(Duration.ofMinutes(10));
        taskManager.addNewTask(task2);

        HttpClient httpClient = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8080/prioritized");

        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(uri)
                .GET()
                .build();

        HttpResponse<String> httpResponse = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, httpResponse.statusCode());

        List<Task> actualTasks = taskManager.getPrioritizedTasks();

        assertNotNull(actualTasks, "Список задач не должен быть null");
        assertEquals(2, actualTasks.size(), "Некорректное количество задач");
        Assertions.assertFalse(taskManager.validationByIntersection(task1, task2),
                "Task1 и Task2 не должны пересекаться");
    }
}
