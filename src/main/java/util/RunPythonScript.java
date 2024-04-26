package util;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import lombok.extern.log4j.Log4j2;

@Log4j2
public class RunPythonScript {
    public static void main(String[] args) {
        try {
            // Specify the path to the Python script
            String pythonScriptPath = "src/main/java/util/pleasework.py";

            // Create a ProcessBuilder instance
            ProcessBuilder pb = new ProcessBuilder("python", pythonScriptPath);

            // Start the process
            Process process = pb.start();

            // Read the output from the Python script
            BufferedReader stdInput = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String s;
            while ((s = stdInput.readLine()) != null) {
                System.out.println(s);
            }

            // Wait for the process to complete
            process.waitFor();

            // Close the input stream
            stdInput.close();

            log.info("Done.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}