package org.example;

import java.util.concurrent.TimeUnit;

/**
 * Hello world!
 *
 */
public class App {

    public static void main(String[] args) {
        WebTransportClient client = new WebTransportClient();
        client.addReaderCallback(message -> {
            // Handle the message read from the named pipe
            System.out.println("Callback received message: " + message);
        });
        client.connect();
        for (int i = 0; i < 100; i++) {
            try {
                TimeUnit.SECONDS.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            client.send("Hello from Java!");
        }

        try {
            TimeUnit.DAYS.sleep(100000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
