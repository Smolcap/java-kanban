package model.http;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import model.Task;
import model.business.HistoryManager;

import java.io.IOException;
import java.util.List;

public class HistoryHandler extends BaseHttpHandler {

    private final HistoryManager historyManager;

    public HistoryHandler(HistoryManager historyManager, Gson gson) {
        super(null, gson);
        this.historyManager = historyManager;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String resource = "history";

        Endpoints endpoints = getEndpoint(exchange.getRequestURI().getPath(), exchange.getRequestMethod(), resource);

        if (endpoints.equals(Endpoints.GET)) {
            handlerGetHistory(exchange);
        } else {
            sendMethodNotAllowed(exchange, " HTTP-метод не поддерживается " +
                    "сервером для этого ресурса");
        }
    }

    private void handlerGetHistory(HttpExchange exchange) throws IOException {
        try {
            final List<Task> historyList = historyManager.getHistory();
            final String response = gson.toJson(historyList);
            sendText(exchange, response);
        } catch (NotFoundException e) {
            sendNotFound(exchange, "Not Found");
        }
    }

    @Override
    public Endpoints getEndpoint(String requestPath, String requestMethod, String resource) {
        return super.getEndpoint(requestPath, requestMethod, "history");
    }
}
