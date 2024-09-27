package model.http;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import model.Task;
import model.business.TaskManager;

import java.io.IOException;
import java.util.List;

public class PrioritizedHandler extends BaseHttpHandler implements HttpHandler {

    public PrioritizedHandler(TaskManager taskManager, Gson gson) {
        super(taskManager, gson);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String resource = "prioritized";
        Endpoints endpoints = getEndpoint(exchange.getRequestURI().getPath(), exchange.getRequestMethod(), resource);

        if (endpoints.equals(Endpoints.GET)) {
            handlerGetPrioritizedTasks(exchange);
        }
    }

    private void handlerGetPrioritizedTasks(HttpExchange exchange) throws IOException {
        try {
            List<Task> prioritizedTask = taskManager.getPrioritizedTasks();
            String response = gson.toJson(prioritizedTask);
            sendText(exchange, response);
        } catch (NotFoundException e) {
            sendNotFound(exchange, "Not Found");
        }
    }

    @Override
    protected Endpoints getEndpoint(String requestPath, String requestMethod, String resource) {
        return super.getEndpoint(requestPath, requestMethod, "prioritized");
    }
}
