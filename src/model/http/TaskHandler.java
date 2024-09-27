package model.http;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import model.Task;
import model.business.TaskManager;

import java.io.IOException;
import java.util.List;


public class TaskHandler extends BaseHttpHandler implements HttpHandler {

    public TaskHandler(TaskManager taskManager, Gson gson) {
        super(taskManager, gson);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String resource = "tasks";

        Endpoints endpoints = getEndpoint(exchange.getRequestURI().getPath(), exchange.getRequestMethod(), resource);

        switch (endpoints) {
            case GET -> handleGetTasks(exchange);
            case GET_BY_ID -> handleGetTasksById(exchange);
            case POST -> handleAddTask(exchange);
            case PUT_UPDATE_TASK -> handleUpdateTask(exchange);
            case DELETE -> handleDeleteTask(exchange);
            case DELETE_BY_ID -> handleDeleteTaskById(exchange);
        }
    }

    private void handleGetTasks(HttpExchange exchange) throws IOException {
        try {
            List<Task> listTasks = taskManager.getTasks();
            String response = gson.toJson(listTasks);
            System.out.println("Получен список задач: " + response);
            sendText(exchange, response);
        } catch (NotFoundException e) {
            sendNotFound(exchange, "Not Found");
        }
    }

    private void handleGetTasksById(HttpExchange exchange) throws IOException {
        String path = exchange.getRequestURI().getPath();
        String idTask = path.substring(path.lastIndexOf("/") + 1);

        try {
            int taskId = Integer.parseInt(idTask);
            Task createTask = taskManager.getTaskId(taskId);
            String response = gson.toJson(createTask);
            sendText(exchange, response);
        } catch (NotFoundException e) {
            sendNotFound(exchange, "Not Found");
        }
    }

    private void handleAddTask(HttpExchange exchange) throws IOException {
        try {
            Task createTask = getReadRequestBody(exchange, Task.class);
            Integer createTaskId = taskManager.addNewTask(createTask);
            String response = gson.toJson(createTaskId);
            sendCreate(exchange, "Задача создана с ID: " + response);
        } catch (NotFoundException e) {
            sendHasInteractions(exchange, "Not Acceptable");
        }
    }

    private void handleUpdateTask(HttpExchange exchange) throws IOException {
        try {
            Task updateTask = getReadRequestBody(exchange, Task.class);
            if (updateTask.getId() != null) {
                taskManager.updateTask(updateTask);
                sendCreate(exchange, "Success update");
            }
        } catch (NotFoundException e) {
            sendHasInteractions(exchange, "Not Acceptable");
        }
    }

    private void handleDeleteTask(HttpExchange exchange) throws IOException {
        try {
            taskManager.cleanTasks();
            System.out.println("Задачи успешно удалены");
            sendText(exchange, "Success");
        } catch (NotFoundException e) {
            sendNotFound(exchange, "Not Found");
        }
    }

    private void handleDeleteTaskById(HttpExchange exchange) throws IOException {
        String path = exchange.getRequestURI().getPath();
        String id = path.substring(path.lastIndexOf("/") + 1);
        try {
            int taskId = Integer.parseInt(id);
            taskManager.removeByTaskId(taskId);
            sendText(exchange, "Success remove " + taskId);
        } catch (NotFoundException e) {
            sendNotFound(exchange, "Not Found");
        }
    }

    @Override
    protected Endpoints getEndpoint(String requestPath, String requestMethod, String resource) {
        return super.getEndpoint(requestPath, requestMethod, "tasks");
    }
}
