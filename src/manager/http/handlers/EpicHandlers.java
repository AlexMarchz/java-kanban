package manager.http.handlers;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import manager.TaskManager;
import data.Epic;
import java.io.IOException;

public class EpicHandlers extends BaseHttpHandler {
    public EpicHandlers(TaskManager taskManager, Gson gson) {
        super(taskManager, gson);
    }

    @Override
    public void safeHandle(HttpExchange exchange) throws IOException {
        Endpoint endpoint = getEndpoint(exchange);
        String[] split = exchange.getRequestURI().getPath().split("/");
        switch (endpoint) {
            case GET -> sendText(exchange, gson.toJson(taskManager.showAllEpics()));
            case GET_BY_ID -> sendText(exchange, gson.toJson(taskManager.findEpicById(Integer.parseInt(split[2]))));
            case GET_SUBS_BY_EPIC_ID ->
                    sendText(exchange, gson.toJson(taskManager.findAllSubtaskByEpicId(Integer.parseInt(split[2]))));
            case POST -> {
                epic = gson.fromJson(getTaskFromRequestBody(exchange), Epic.class);
                taskManager.addEpic(epic);
                writeResponse(exchange, 201, "Задача добавлена");
            }
            case POST_BY_ID -> {
                epic = gson.fromJson(getTaskFromRequestBody(exchange), Epic.class);
                taskManager.updateEpic(epic);
                sendText(exchange, "");
            }
            case DELETE_BY_ID -> {
                epic = taskManager.findEpicById(Integer.parseInt(split[2]));
                taskManager.removeEpicById(epic.getId());
                writeResponse(exchange, 204, "");
            }
            case UNKNOWN -> sendNotFound(exchange);
        }
    }
}