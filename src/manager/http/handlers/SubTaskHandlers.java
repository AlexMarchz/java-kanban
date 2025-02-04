package manager.http.handlers;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import manager.TaskManager;
import data.SubTask;
import java.io.IOException;

public class SubTaskHandlers extends BaseHttpHandler {
    public SubTaskHandlers(TaskManager taskManager, Gson gson) {
        super(taskManager, gson);
    }

    @Override
    public void safeHandle(HttpExchange exchange) throws IOException {
        Endpoint endpoint = getEndpoint(exchange);
        String[] split = exchange.getRequestURI().getPath().split("/");
        switch (endpoint) {
            case GET -> sendText(exchange, gson.toJson(taskManager.showAllSubTasks()));
            case GET_BY_ID -> sendText(exchange, gson.toJson(taskManager.findSubTaskById(Integer.parseInt(split[2]))));
            case POST -> {
                subtask = gson.fromJson(getTaskFromRequestBody(exchange), SubTask.class);
                taskManager.addSubTask(subtask);
                writeResponse(exchange, 201, "Задача добавлена");
            }
            case POST_BY_ID -> {
                subtask = gson.fromJson(getTaskFromRequestBody(exchange), SubTask.class);
                taskManager.updateSubTask(subtask);
                sendText(exchange, "");
            }
            case DELETE_BY_ID -> {
                subtask = taskManager.findSubTaskById(Integer.parseInt(split[2]));
                taskManager.removeSubTaskById(subtask.getId());
                writeResponse(exchange, 204, "");
            }
            case UNKNOWN -> sendNotFound(exchange);
        }
    }
}