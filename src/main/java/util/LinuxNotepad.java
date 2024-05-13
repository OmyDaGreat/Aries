package util;

import java.io.IOException;

public class LinuxNotepad {
    public static void main(String[] args) {
        try {
            ProcessBuilder processBuilder = new ProcessBuilder("notepadqq");
            Process process = processBuilder.start();
            int exitCode = process.waitFor();
            System.out.println("Exited with code: " + exitCode);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
