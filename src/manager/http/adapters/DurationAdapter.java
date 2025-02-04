package manager.http.adapters;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.time.Duration;

public class DurationAdapter extends TypeAdapter<Duration> {
    @Override
    public void write(JsonWriter jsonWriter, Duration duration) throws IOException {
        if (duration != null) {
            jsonWriter.value(duration.toString());
            return;
        }
        jsonWriter.nullValue();
    }

    @Override
    public Duration read(JsonReader jsonReader) throws IOException {
        try {
            return Duration.parse(jsonReader.nextString());
        } catch (Exception e) {
            throw new IOException("Ошибка десериализации Duration: " + e.getMessage(), e);
        }
    }
}