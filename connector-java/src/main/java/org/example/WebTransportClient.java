package org.example;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Hello world!
 *
 */
public class WebTransportClient {
    private static final String READ_PIPE_PATH = "/tmp/from_python_named_pipe";
    private static final String WRITE_PIPE_PATH = "/tmp/to_python_named_pipe";
    private ReaderCallback readerCallback;
    private final BlockingQueue<String> messageQueue = new LinkedBlockingQueue<>();

    static interface ReaderCallback {
        void onMessage(String message);
    }

    public void connect() {
        new Thread(this::listenToPipe).start();
        new Thread(this::writeToPipe).start();
    }

    public void send(String message) {
        System.out.println("Queueing message: " + message);
        messageQueue.offer(message);
    }

    public void addReaderCallback(ReaderCallback callback) {
        readerCallback = callback;
    }

    private void listenToPipe() {
        System.out.println("Listening to named pipe: " + READ_PIPE_PATH);
        try (BufferedReader reader = new BufferedReader(new FileReader(READ_PIPE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (readerCallback != null) readerCallback.onMessage(line);
                System.out.println("Received: " + line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void writeToPipe() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(WRITE_PIPE_PATH))) {
            while (true) {
                String message = messageQueue.take(); // Blocks until a message is available
                System.out.println("Sending message: " + message);
                writer.write(message);
                writer.newLine();
                writer.flush();
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
