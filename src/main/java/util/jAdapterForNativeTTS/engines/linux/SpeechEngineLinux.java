//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package util.jAdapterForNativeTTS.engines.linux;

import util.jAdapterForNativeTTS.engines.SpeechEngineAbstract;
import util.jAdapterForNativeTTS.engines.Voice;
import util.jAdapterForNativeTTS.engines.exceptions.ParseException;
import util.jAdapterForNativeTTS.engines.exceptions.SpeechEngineCreationException;

public class SpeechEngineLinux extends SpeechEngineAbstract {

    public SpeechEngineLinux() throws SpeechEngineCreationException {
      super();
    }

    public String getSayExecutable() {
        return "spd-say";
    }

    public String[] getSayOptionsToGetSupportedVoices() {
        return new String[]{"-L"};
    }

    public String[] getSayOptionsToSayText(String text) {
        return new String[]{"-l", this.voice, "-r", String.valueOf(this.rate), text};
    }

    public Voice parse(String line) throws ParseException {
        String[] tokens = line.trim().split("  +");
        if (tokens.length != 3) {
            throw new ParseException(String.format("Unexpected line from %s: %s", this.getSayExecutable(), line));
        } else if (tokens[0].equalsIgnoreCase("NAME")) {
            return null;
        } else {
            Voice voice = new Voice();
            voice.setName(tokens[1]);
            voice.setCulture(tokens[1]);
            voice.setGender("?");
            voice.setAge("?");
            voice.setDescription(tokens[0]);
            return voice;
        }
    }
}
