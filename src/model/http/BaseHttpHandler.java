package model.http;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import model.business.TaskManager;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class BaseHttpHandler {
    private static final int SUCCESS_CODE = 200;
    private static final int CREATE_CODE = 201;
    private static final int NOT_FOUND_CODE = 404;
    private static final int HAS_INTERACTIONS_CODE = 406;
    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;

    protected Gson gson;
    protected TaskManager taskManager;

    public BaseHttpHandler(TaskManager taskManager, Gson gson) {
        this.taskManager = taskManager;
        this.gson = gson;
    }

    protected <T> T getReadRequestBody(HttpExchange exchange, Class<T> taskClass) throws IOException {
        InputStream inputStream = exchange.getRequestBody();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            StringBuilder requestBody = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                requestBody.append(line);
            }
            return gson.fromJson(requestBody.toString(), taskClass);
        }
    }

    protected void sendResponse(HttpExchange httpExchange, String responseText, int responseCode) throws IOException {
        byte[] responseBytes = responseText.getBytes(DEFAULT_CHARSET);
        try (OutputStream os = httpExchange.getResponseBody()) {
            httpExchange.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
            httpExchange.sendResponseHeaders(responseCode, responseBytes.length);
            os.write(responseBytes);
        } finally {
            httpExchange.close();
        }
    }

    protected void sendText(HttpExchange httpExchange, String responseTextSuccess) throws IOException {
        sendResponse(httpExchange, responseTextSuccess, SUCCESS_CODE);
    }

    protected void sendCreate(HttpExchange httpExchange, String responseTextSuccess) throws IOException {
        sendResponse(httpExchange, responseTextSuccess, CREATE_CODE);
    }

    protected void sendNotFound(HttpExchange httpExchange, String responseTextNotFound) throws IOException {
        sendResponse(httpExchange, responseTextNotFound, NOT_FOUND_CODE);
    }

    protected void sendHasInteractions(HttpExchange httpExchange, String responseTextHasInteractions)
            throws IOException {
        sendResponse(httpExchange, responseTextHasInteractions, HAS_INTERACTIONS_CODE);
    }

    protected Endpoints getEndpoint(String requestPath, String requestMethod, String resource) {
        final String[] pathParts = requestPath.split("/");

        if (pathParts.length == 2 && pathParts[1].equals(resource)) {
            return getBaseEndpoint(requestMethod);
        } else if (pathParts.length == 3 && pathParts[1].equals(resource)) {
            return getIdEndpoint(requestMethod, pathParts[2]);
        } else if (isSubtaskRequest(pathParts)) {
            return Endpoints.GET_SUBTASK;
        }

        return Endpoints.UNKNOWN;
    }

    private Endpoints getBaseEndpoint(String requestMethod) {
        return switch (requestMethod) {
            case "GET" -> Endpoints.GET;
            case "POST" -> Endpoints.POST;
            case "PUT" -> Endpoints.PUT_UPDATE_TASK;
            case "DELETE" -> Endpoints.DELETE;
            default -> Endpoints.UNKNOWN;
        };
    }

    private Endpoints getIdEndpoint(String requestMethod, String idPart) {
        if (isNumbers(idPart)) {
            return switch (requestMethod) {
                case "GET" -> Endpoints.GET_BY_ID;
                case "DELETE" -> Endpoints.DELETE_BY_ID;
                default -> Endpoints.UNKNOWN;
            };
        }
        return Endpoints.UNKNOWN;
    }

    private boolean isSubtaskRequest(String[] pathParts) {
        return pathParts.length == 4 && pathParts[3].equals("subtasks");
    }

    private boolean isNumbers(String str) {
        if (str == null) {
            return false;
        }
        try {
            Integer.parseInt(str);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }
}
