//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package util.jAdapterForNativeTTS.util.os;

import lombok.experimental.UtilityClass;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@UtilityClass
public class ProcessHelper {

    public static void startApplication(String executable) throws IOException {
        startApplication(executable, (String)null);
    }

    private static ProcessBuilder getProcessBuilder(String executable, String... args) {
        ArrayList<String> command = new ArrayList<>();
        command.add(executable);
        if (args != null) {
            command.addAll(Arrays.asList(args));
        }

        return new ProcessBuilder(command);
    }

    public static Process startApplication(String executable, String... args) throws IOException {
        ProcessBuilder builder = getProcessBuilder(executable, args);
        return builder.inheritIO().start();
    }

    public static List<String> startApplicationAndGetOutput(String executable, String... args) throws IOException, InterruptedException {
        ProcessBuilder builder = getProcessBuilder(executable, args);
        Process process = builder.start();
        ArrayList<String> output = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

        String line;
        try {
            while((line = reader.readLine()) != null) {
                output.add(line);
            }
        } catch (Exception var9) {
            try {
                reader.close();
            } catch (Exception var8) {
                var9.addSuppressed(var8);
            }

            throw var9;
        }

        reader.close();
        process.waitFor();
        return output;
    }
}
