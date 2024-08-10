package parse;

import static parse.CoroutinesKt.startGUI;
import static parse.CoroutinesKt.startTranscriber;

public class App {
    public static void main(String[] args) {
        startGUI();
        startTranscriber();
    }
}
