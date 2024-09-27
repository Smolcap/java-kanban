package model.http;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import model.Epic;
import model.Subtask;
import model.business.TaskManager;

import java.io.IOException;
import java.util.List;

public class EpicHandler extends BaseHttpHandler implements HttpHandler {

    public EpicHandler(TaskManager taskManager, Gson gson) {
        super(taskManager, gson);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String resource = "epics";

        Endpoints endpoints = getEndpoint(exchange.getRequestURI().getPath(), exchange.getRequestMethod(), resource);

        switch (endpoints) {
            case GET -> handleGetEpics(exchange);
            case GET_BY_ID -> handleGetEpicById(exchange);
            case GET_SUBTASK -> handleGetEpicSubtasks(exchange);
            case POST -> handleAddEpic(exchange);
            case PUT_UPDATE_TASK -> handleUpdateEpic(exchange);
            case DELETE -> handleDeleteEpic(exchange);
            case DELETE_BY_ID -> handleDeleteEpicById(exchange);
        }
    }

    private void handleGetEpics(HttpExchange exchange) throws IOException {
        try {
            List<Epic> listEpic = taskManager.getEpics();
            String response = gson.toJson(listEpic);
            System.out.println("Получен список задач: " + response);
            sendText(exchange, response);
        } catch (NotFoundException e) {
            sendNotFound(exchange, "Not Found");
        }
    }

    private void handleGetEpicById(HttpExchange exchange) throws IOException {
        String pathToEpic = exchange.getRequestURI().getPath();
        String idEpic = pathToEpic.substring(pathToEpic.lastIndexOf("/") + 1);

        try {
            int parseEpicId = Integer.parseInt(idEpic);
            Epic createEpic = taskManager.getEpicId(parseEpicId);
            String response = gson.toJson(createEpic);
            sendText(exchange, response);
        } catch (NotFoundException e) {
            sendNotFound(exchange, "Not Found");
        }
    }

    private void handleAddEpic(HttpExchange exchange) throws IOException {
        try {
            Epic createEpic = getReadRequestBody(exchange, Epic.class);
            Integer createEpicId = taskManager.addNewEpic(createEpic);
            String response = gson.toJson(createEpicId);
            sendCreate(exchange, "Эпик создан с ID: " + response);
        } catch (NotFoundException e) {
            sendHasInteractions(exchange, "Not Acceptable");
        }
    }

    private void handleUpdateEpic(HttpExchange exchange) throws IOException {
        try {
            Epic createEpic = getReadRequestBody(exchange, Epic.class);
            if (createEpic.getId() != null) {
                taskManager.updateEpic(createEpic);
                sendCreate(exchange, "Success update");
            }
        } catch (NotFoundException e) {
            sendHasInteractions(exchange, "Not Acceptable");
        }
    }

    private void handleDeleteEpic(HttpExchange exchange) throws IOException {
        try {
            taskManager.cleanEpics();
            System.out.println("Задачи успешно удалены");
            sendText(exchange, "Success");
        } catch (NotFoundException e) {
            sendNotFound(exchange, "Not Found");
        }
    }

    private void handleDeleteEpicById(HttpExchange exchange) throws IOException {
        String path = exchange.getRequestURI().getPath();
        String idEpic = path.substring(path.lastIndexOf("/") + 1);
        try {
            int epicId = Integer.parseInt(idEpic);
            taskManager.removeByEpicId(epicId);
            sendText(exchange, "Success remove " + epicId);
        } catch (NotFoundException e) {
            sendNotFound(exchange, "Not Found");
        }
    }

    private void handleGetEpicSubtasks(HttpExchange exchange) throws IOException {
        String pathToEpic = exchange.getRequestURI().getPath();
        String[] pathParts = pathToEpic.split("/");

        if (pathParts.length < 4 || !pathParts[1].equals("epics") || !pathParts[3].equals("subtasks")) {
            sendNotFound(exchange, "Некорректный путь");
            return;
        }

        String idEpic = pathParts[2];

        try {
            int parseEpicId = Integer.parseInt(idEpic);
            List<Subtask> listSubtaskByEpic = taskManager.allListSubtaskByEpic(parseEpicId);
            String response = gson.toJson(listSubtaskByEpic);
            sendText(exchange, response);
        } catch (NotFoundException e) {
            sendNotFound(exchange, "Not Found");
        } catch (Exception e) {
            sendResponse(exchange, "Internal Server Error", 500);
        }
    }


    @Override
    public Endpoints getEndpoint(String requestPath, String requestMethod, String resource) {
        return super.getEndpoint(requestPath, requestMethod, "epics");
    }
}
