package manager.http.adapters;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LocalDateTimeAdapter extends TypeAdapter<LocalDateTime> {
    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");

    @Override
    public void write(JsonWriter jsonWriter, LocalDateTime localDateTime) throws IOException {
        if (localDateTime != null) {
            jsonWriter.value(localDateTime.format(dateTimeFormatter));
            return;
        }
        jsonWriter.nullValue();
    }

    @Override
    public LocalDateTime read(JsonReader jsonReader) throws IOException {
        try {
            return LocalDateTime.parse(jsonReader.nextString(), dateTimeFormatter);
        } catch (Exception e) {
            throw new IOException("Ошибка десериализации LocalDateTime: " + e.getMessage(), e);
        }
    }
}

