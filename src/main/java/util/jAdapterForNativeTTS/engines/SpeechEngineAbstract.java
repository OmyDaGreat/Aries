//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package util.jAdapterForNativeTTS.engines;

import util.jAdapterForNativeTTS.engines.exceptions.ParseException;
import util.jAdapterForNativeTTS.engines.exceptions.SpeechEngineCreationException;
import util.jAdapterForNativeTTS.util.os.ProcessHelper;
import lombok.Getter;
import lombok.Setter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public abstract class SpeechEngineAbstract implements SpeechEngine {
    @Setter
    protected String voice;
    protected Process process;
    @Getter
    protected List<Voice> availableVoices;
    protected int rate;
    @Getter
    protected List<ParseException> parseExceptions;

    protected SpeechEngineAbstract() throws SpeechEngineCreationException {
        this.parseExceptions = new ArrayList<>();

        try {
            this.findAvailableVoices();
        } catch (InterruptedException | IOException var2) {
            Thread.currentThread().interrupt();
            throw new SpeechEngineCreationException(var2.getMessage());
        }

        if (this.availableVoices.isEmpty()) {
            throw new SpeechEngineCreationException(String.format("Not even one voice has been found. There were %s parse error(s).%n", this.getParseExceptions().size()));
        } else {
            this.voice = null;
            this.rate = 0;
        }
    }

    protected SpeechEngineAbstract(String voice) throws SpeechEngineCreationException {
        this();
        this.voice = voice;
    }

    public void findAvailableVoices() throws IOException, InterruptedException {
        ArrayList<String> list = (ArrayList<String>) ProcessHelper.startApplicationAndGetOutput(this.getSayExecutable(), this.getSayOptionsToGetSupportedVoices());
        ArrayList<Voice> voices = new ArrayList<>();

        for (String line : list) {
            try {
                Voice v = this.parse(line);
                if (v != null) {
                    voices.add(v);
                }
            } catch (ParseException var6) {
                this.parseExceptions.add(var6);
            }
        }

        this.availableVoices = voices;
    }

    public Voice findVoiceByPreferences(VoicePreferences voicePreferences) {
        Iterator<Voice> var2 = this.availableVoices.iterator();

        Voice voice2;
        do {
            if (!var2.hasNext()) {
                return null;
            }

            voice2 = var2.next();
        } while(!voice2.matches(voicePreferences));

        return voice2;
    }

    public Process say(String text) throws IOException {
        if (this.voice != null) {
            this.process = ProcessHelper.startApplication(this.getSayExecutable(), this.getSayOptionsToSayText(text));
            return this.process;
        } else {
            return null;
        }
    }

    public void stopTalking() {
        if (this.process != null) {
            this.process.destroy();
            this.process = null;
        }

    }

    public void setRate(int rate) throws IllegalArgumentException {
        if (rate >= -100 && rate <= 100) {
            this.rate = rate;
        } else {
            throw new IllegalArgumentException("Invalid range for rate, valid range is [-100..100]");
        }
    }
}
