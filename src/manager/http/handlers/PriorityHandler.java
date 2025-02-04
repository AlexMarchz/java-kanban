package manager.http.handlers;
import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import manager.TaskManager;
import java.io.IOException;
public class PriorityHandler extends BaseHttpHandler {
    public PriorityHandler(TaskManager taskManager, Gson gson) {
        super(taskManager, gson);
    }

    @Override
    public void safeHandle(HttpExchange exchange) throws IOException {
        String requestMethod = exchange.getRequestMethod();
        String[] split = exchange.getRequestURI().getPath().split("/");
        if (requestMethod.equals("GET") && split[1].equals("priority")) {
            try {
                sendText(exchange, gson.toJson(taskManager.getPriorityTasks()));
            } catch (Exception e) {
                writeResponse(exchange, 500, "");
            }
        } else {
            writeResponse(exchange, 400, "");
        }
    }
}
