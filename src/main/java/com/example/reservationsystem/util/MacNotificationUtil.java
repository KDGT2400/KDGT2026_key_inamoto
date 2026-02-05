package com.example.reservationsystem.util;

public class MacNotificationUtil {

    public static void notify(String title, String message) {
        try {
            String script = String.format(
                "display notification \"%s\" with title \"%s\"",
                message.replace("\"", "\\\""),
                title.replace("\"", "\\\"")
            );

            new ProcessBuilder("osascript", "-e", script).start();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
