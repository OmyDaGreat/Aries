package util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import lombok.experimental.UtilityClass;
import lombok.extern.log4j.Log4j2;

@Log4j2
@UtilityClass
public class FilePrinter {

  public static void print(String fileName) {
    Path filePath = Paths.get(fileName);
    try {
      String fileContent = Files.readString(filePath);
      log.info("\n\n{}", fileContent);
    } catch (IOException e) {
      log.error("Failed to read file: {}", e.getMessage());
    }
  }
}
