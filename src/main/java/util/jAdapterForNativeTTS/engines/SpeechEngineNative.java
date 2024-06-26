//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package util.jAdapterForNativeTTS.engines;

import lombok.experimental.UtilityClass;
import util.jAdapterForNativeTTS.engines.exceptions.SpeechEngineCreationException;
import util.jAdapterForNativeTTS.engines.linux.SpeechEngineLinux;
import util.jAdapterForNativeTTS.engines.macos.SpeechEngineMacOS;
import util.jAdapterForNativeTTS.engines.windows.SpeechEngineWindows;
import java.util.Locale;

@UtilityClass
public class SpeechEngineNative {
    private static SpeechEngine engine = null;

    public static SpeechEngine getInstance() throws SpeechEngineCreationException {
        if (engine == null) {
            String osName = System.getProperty("os.name");
            String osNameForComparison = osName.replaceAll("\\s", "").toLowerCase(Locale.US);
            if (osNameForComparison.startsWith("windows")) {
                engine = new SpeechEngineWindows();
            } else if (osNameForComparison.startsWith("macos")) {
                engine = new SpeechEngineMacOS();
            } else {
                if (!osNameForComparison.startsWith("linux")) {
                    throw new SpeechEngineCreationException(String.format("Operating System '%s' is not supported.", osName));
                }

                engine = new SpeechEngineLinux();
            }
        }

        return engine;
    }
}
