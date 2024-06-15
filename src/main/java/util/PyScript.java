package util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import lombok.experimental.UtilityClass;
import lombok.extern.log4j.Log4j2;

@Log4j2
@UtilityClass
public class PyScript {

  public static void run() throws IOException, InterruptedException {
    ProcessBuilder builder = new ProcessBuilder("python", "src/main/java/util/pypkg/ai.py");
    builder.redirectErrorStream(true);
    Process process = builder.start();

    BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
    String line;
    while ((line = reader.readLine()) != null) {
      log.info(line);
    }
    process.waitFor();
  }
}
