package parse;

import lombok.extern.log4j.Log4j2;

import javax.swing.UIManager;
import java.util.Arrays;

@Log4j2
public class GetLookAndFeel {
    public static void main(String[] a) {
        Arrays.stream(UIManager.getInstalledLookAndFeels()).forEach(info -> log.info(info.getClassName()));
    }
}
