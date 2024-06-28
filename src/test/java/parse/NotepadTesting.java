package parse;

import util.Notepad.NotepadProcessor;

public class NotepadTesting {
    public static void main(String[] args) {
        NotepadProcessor np = new NotepadProcessor();
        try {
            np.openNotepad();
            np.writeText("Hello World");
            np.addNewLine();
            np.writeText("This is a test");
            np.saveFileAs("test.txt");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
