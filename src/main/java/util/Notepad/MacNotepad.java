package util.Notepad;

import java.io.IOException;

public class MacNotepad {

    public void openNotepad() {
        try {
            // Assuming you meant to execute an AppleScript command or script file
            // Replace "/path/to/your/script.scpt" with the actual path to your AppleScript file
            // If you indeed have an AppleScript executable script to open the Notes app or similar
            String scriptPath = "/Users/rishthewizard/Documents/GitHub/ParseButPro/src/main/resources/openNotepad.scpt"; // Adjusted for a typical script extension
            ProcessBuilder pb = new ProcessBuilder("osascript", scriptPath);
            pb.start().waitFor(); // Execute the script and wait for it to finish
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        MacNotepad notepad = new MacNotepad();
        notepad.openNotepad();
    }
}