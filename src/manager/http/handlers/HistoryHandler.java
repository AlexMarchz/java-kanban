package manager.http.handlers;
import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import manager.TaskManager;
import java.io.IOException;
public class HistoryHandler extends BaseHttpHandler {
    public HistoryHandler(TaskManager taskManager, Gson gson) {
        super(taskManager, gson);
    }

    @Override
    public void safeHandle(HttpExchange exchange) throws IOException {
        String requestMethod = exchange.getRequestMethod();
        String[] split = exchange.getRequestURI().getPath().split("/");
        if (requestMethod.equals("GET") && split[1].equals("history")) {
            try {
                sendText(exchange, gson.toJson(taskManager.getHistory()));
            } catch (Exception e) {
                writeResponse(exchange, 500, "");
            }
        } else {
            writeResponse(exchange, 400, "");
        }
    }
}