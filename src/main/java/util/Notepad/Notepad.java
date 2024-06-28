package util.Notepad;

import java.awt.*;
import java.io.IOException;

public interface Notepad {
    void openNotepad() throws IOException, InterruptedException, AWTException;
    void writeText(String text);
    void deleteText();
    void addNewLine();
    void saveFileAs(String name);
    void openNewFile();
    void closeFile();
    void closeNotepad() throws InterruptedException;
}