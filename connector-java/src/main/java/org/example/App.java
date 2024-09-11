package org.example;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * Hello world!
 *
 */
public class App {
    private static final String READ_PIPE_PATH = "/tmp/from_python_named_pipe";
    private static final String WRITE_PIPE_PATH = "/tmp/to_python_named_pipe";

    public static void main(String[] args) {
        try {
            listenToPipe();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void listenToPipe() throws IOException {
        System.out.println("Listening to named pipe: " + READ_PIPE_PATH);
        try (BufferedReader reader = new BufferedReader(new FileReader(READ_PIPE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println("Received: " + line);
            }
        }
    }
}
