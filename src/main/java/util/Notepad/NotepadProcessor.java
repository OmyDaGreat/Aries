package util.Notepad;

import lombok.extern.log4j.Log4j2;
import util.Platform;

import java.awt.*;
import java.io.IOException;

@Log4j2
public class NotepadProcessor {
    String platformMissing = "Unable to determine platform";
    Notepad notepad;

    public NotepadProcessor() {
        Platform p = Platform.Companion.detectPlatform();
        switch (p) {
            case WINDOWS:
                notepad = new WinNotepad();
                break;
            case LINUX:
                notepad = new LinuxNotepad();
                break;
            case MAC:
                notepad = new MacNotepad();
                break;
            case UNKNOWN:
                log.error(platformMissing);
        }
    }

    public void openNotepad() throws IOException, InterruptedException, AWTException {
        notepad.openNotepad();
    }

    public void writeText(String text) {
        notepad.writeText(text);
    }

    public void deleteText() {
        notepad.deleteText();
    }

    public void addNewLine() {
        notepad.addNewLine();
    }

    public void saveFileAs(String name) {
        notepad.saveFileAs(name);
    }

    public void openNewFile() {
        notepad.openNewFile();
    }

    public void closeFile() {
        notepad.closeFile();
    }

    public void closeNotepad() throws InterruptedException {
        notepad.closeNotepad();
    }
}