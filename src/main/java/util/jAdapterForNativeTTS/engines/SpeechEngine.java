//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package util.jAdapterForNativeTTS.engines;

import util.jAdapterForNativeTTS.engines.exceptions.ParseException;
import java.io.IOException;
import java.util.List;

public interface SpeechEngine {
    String getSayExecutable();

    String[] getSayOptionsToSayText(String var1);

    String[] getSayOptionsToGetSupportedVoices();

    Voice parse(String var1) throws ParseException;

    void findAvailableVoices() throws IOException, InterruptedException;

    List<Voice> getAvailableVoices();

    List<ParseException> getParseExceptions();

    Voice findVoiceByPreferences(VoicePreferences var1);

    void setRate(int var1) throws IllegalArgumentException;

    void setVoice(String var1);

    Process say(String var1) throws IOException;

    void stopTalking();
}
