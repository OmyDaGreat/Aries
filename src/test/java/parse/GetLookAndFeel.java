package parse;

import java.util.Arrays;
import javax.swing.UIManager;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class GetLookAndFeel {
  public static void main(String[] a) {
    Arrays.stream(UIManager.getInstalledLookAndFeels())
        .forEach(info -> log.info(info.getClassName()));
  }
}
