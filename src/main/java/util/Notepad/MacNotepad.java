package util.Notepad;

import java.io.IOException;

public class MacNotepad implements Notepad {

    public void openNotepad() {
        try {
            String scriptPath = "src/main/resources/openNotepad.scpt"; // Adjusted for a typical script extension
            // String scriptPath = "C:/Users/rishthewizard/Documents/GitHub/ParseButPro/src/main/resources/openNotepad.scpt"; // Adjusted for a typical script extension
            ProcessBuilder pb = new ProcessBuilder("osascript", scriptPath);
            pb.start().waitFor(); // Execute the script and wait for it to finish
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            Thread.currentThread().interrupt();
        }
    }

    public void writeText(String text) {
        // TODO: Implement this method
    }

    public void deleteText() {
        // TODO: Implement this method
    }

    public void addNewLine() {
        // TODO: Implement this method
    }

    public void saveFileAs(String name) {
        // TODO: Implement this method
    }

    public void openNewFile() {
        // TODO: Implement this method
    }

    public void closeFile() {
        // TODO: Implement this method
    }

    public void closeNotepad() {
        // TODO: Implement this method
    }
}