package parse;

import co.touchlab.kermit.Logger;
import co.touchlab.kermit.Severity;

import static parse.CoroutinesKt.startGUI;
import static parse.CoroutinesKt.startTranscriber;

public class App {
    public static void main(String[] args) {
        Logger.Companion.setMinSeverity(Severity.Verbose);
        startGUI();
        startTranscriber();
    }
}
