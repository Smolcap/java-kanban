package model.http;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import model.Subtask;
import model.business.TaskManager;

import java.io.IOException;
import java.util.List;

public class SubtaskHandler extends BaseHttpHandler {

    public SubtaskHandler(TaskManager taskManager, Gson gson) {
        super(taskManager, gson);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String resource = "subtasks";

        Endpoints endpoints = getEndpoint(exchange.getRequestURI().getPath(), exchange.getRequestMethod(), resource);

        switch (endpoints) {
            case GET -> handleGetSubtasks(exchange);
            case GET_BY_ID -> handleGetSubtaskById(exchange);
            case POST -> handleAddSubtask(exchange);
            case PUT_UPDATE_TASK -> handleUpdateSubtask(exchange);
            case DELETE_BY_ID -> handleDeleteSubtaskById(exchange);
            default -> sendMethodNotAllowed(exchange, " HTTP-метод не поддерживается " +
                    "сервером для этого ресурса");
        }
    }

    private void handleGetSubtasks(HttpExchange exchange) throws IOException {
        try {
            final List<Subtask> listSubtask = taskManager.getSubtask();
            final String response = gson.toJson(listSubtask);
            System.out.println("Получен список задач: " + response);
            sendText(exchange, response);
        } catch (NotFoundException e) {
            sendNotFound(exchange, "Not Found");
        }
    }

    private void handleGetSubtaskById(HttpExchange exchange) throws IOException {
        String pathToSubtask = exchange.getRequestURI().getPath();
        String idSubtask = pathToSubtask.substring(pathToSubtask.lastIndexOf("/") + 1);

        try {
            int parseSubtaskId = Integer.parseInt(idSubtask);
            Subtask createSubtask = taskManager.getSubtaskId(parseSubtaskId);
            String response = gson.toJson(createSubtask);
            sendText(exchange, response);
        } catch (NotFoundException e) {
            sendNotFound(exchange, "Not Found");
        }
    }

    private void handleAddSubtask(HttpExchange exchange) throws IOException {
        try {
            Subtask createSubtask = getReadRequestBody(exchange, Subtask.class);
            Integer createSubtaskId = taskManager.addNewSubtask(createSubtask);
            String response = gson.toJson(createSubtaskId);
            sendCreate(exchange, "Подзадача создана с ID: " + response);
        } catch (NotFoundException e) {
            sendHasInteractions(exchange, "Not Acceptable");
        }
    }

    private void handleUpdateSubtask(HttpExchange exchange) throws IOException {
        try {
            Subtask createSubtask = getReadRequestBody(exchange, Subtask.class);
            if (createSubtask.getId() != null) {
                taskManager.updateSubtask(createSubtask);
                sendCreate(exchange, "Success update");
            }
        } catch (NotFoundException e) {
            sendHasInteractions(exchange, "Not Acceptable");
        }
    }

    private void handleDeleteSubtaskById(HttpExchange exchange) throws IOException {
        String path = exchange.getRequestURI().getPath();
        String idSubtask = path.substring(path.lastIndexOf("/") + 1);
        try {
            int subtaskId = Integer.parseInt(idSubtask);
            taskManager.removeBySubtaskId(subtaskId);
            sendText(exchange, "Success remove " + subtaskId);
        } catch (NotFoundException e) {
            sendNotFound(exchange, "Not Found");
        }
    }

    @Override
    public Endpoints getEndpoint(String requestPath, String requestMethod, String resource) {
        return super.getEndpoint(requestPath, requestMethod, "subtasks");
    }
}
