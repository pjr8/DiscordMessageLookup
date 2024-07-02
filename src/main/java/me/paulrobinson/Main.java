package me.paulrobinson;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class Main {

    private static final ArrayList<Message> messages = new ArrayList<>();

    public static void main(String[] args) {

        String folder = "C:\\Users\\paulr\\Desktop\\desktop\\discord data\\messages";
        File file = new File(folder);
        Object[] folders = Arrays.stream(Objects.requireNonNull(file.listFiles())).toArray();

        for (Object messageFolder : folders) {
            File messageJSON = new File(messageFolder.toString() + "\\messages.json");
            if (messageJSON.exists()) {
                try {
                    BufferedReader reader = new BufferedReader(new FileReader(messageJSON));
                    JsonArray jsonArray = new Gson().fromJson(new JsonReader(reader), JsonArray.class);
                    for (int i = 0; i < jsonArray.size(); i++) {
                        JsonObject messageData = jsonArray.get(i).getAsJsonObject();
                        Message message = new Message();
                        message.id = messageData.get("ID").getAsString();
                        message.timestamp = messageData.get("Timestamp").getAsString();
                        message.contents = messageData.get("Contents").getAsString();
                        message.attachments = messageData.get("Attachments").getAsString();
                        message.messageFolder = messageFolder.toString();
                        messages.add(message);
                    }
                } catch (FileNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        messages.sort(Comparator.comparing(o -> o.timestamp));

        AtomicInteger counter = new AtomicInteger(0);

        boolean message = true;

        if (message) {
            messages.forEach(s -> {
                if (s.contents.length() > 1) {
                    if (s.contents.contains("hi")) {
                        System.out.println(s);
                        counter.getAndIncrement();
                    }
                }
            });
        } else {
            messages.forEach(s -> {
                if (s.attachments.length() > 1) {
                    if (s.attachments.contains(".png")) {
                        System.out.println(s);
                        counter.getAndIncrement();
                    }
                }
            });
        }
        System.out.println("Count: " + counter.get());
    }

    public static class Message {
        public String id;
        public String timestamp;
        public String contents;
        public String attachments;
        public String messageFolder;

        @Override
        public String toString() {
            return "ID: " + id + " | Timestamp: " + timestamp + " | Contents: " + contents + " | Attachments: " + attachments + " | Folder: " + messageFolder;
        }
    }
}