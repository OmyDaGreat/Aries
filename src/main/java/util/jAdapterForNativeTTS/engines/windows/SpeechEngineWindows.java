//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package util.jAdapterForNativeTTS.engines.windows;

import util.jAdapterForNativeTTS.engines.SpeechEngineAbstract;
import util.jAdapterForNativeTTS.engines.Voice;
import util.jAdapterForNativeTTS.engines.exceptions.ParseException;
import util.jAdapterForNativeTTS.engines.exceptions.SpeechEngineCreationException;

public class SpeechEngineWindows extends SpeechEngineAbstract {
    private static final String CODE_TOKEN_TTS_NAME = "##TTS_NAME##";
    private static final String CODE_TOKEN_RATE = "##RATE##";
    private static final String CODE_TOKEN_TEXT = "##TEXT##";
    private static final String POWER_SHELL_CODE_SAY =
            String.join("",
                    "Add-Type -AssemblyName System.Speech;",
                    "$speak = New-Object System.Speech.Synthesis.SpeechSynthesizer;",
                    "$speak.SelectVoice('", CODE_TOKEN_TTS_NAME, "');",
                    "$speak.Rate=", CODE_TOKEN_RATE,";",
                    "$speak.Speak('", CODE_TOKEN_TEXT, "');");
    private static final String POWER_SHELL_CODE_SUPPORTED_VOICES =
            String.join("",
                    "Add-Type -AssemblyName System.Speech;",
                    "$speak = New-Object System.Speech.Synthesis.SpeechSynthesizer;",
                    "$speak.GetInstalledVoices() | ",
                    "Select-Object -ExpandProperty VoiceInfo | ",
                    "Select-Object -Property Name, Culture, Gender, Age, Description | ",
                    "ConvertTo-Csv -NoTypeInformation | ",
                    "Select-Object -Skip 1;");

    public SpeechEngineWindows() throws SpeechEngineCreationException {
        super();
    }

    public String getSayExecutable() {
        return "PowerShell";
    }

    private int recalcRate(int rate) {
        return (int)Math.round(rate / 10.0);
    }

    public String[] getSayOptionsToSayText(String text) {
        String escapedText = text.replace("'", "''''");
        String code = POWER_SHELL_CODE_SAY.replace(CODE_TOKEN_TTS_NAME, this.voice).replace(CODE_TOKEN_RATE, Integer.toString(this.recalcRate(this.rate))).replace(CODE_TOKEN_TEXT, escapedText);
        return new String[]{"-Command", String.join("", "\"", code, "\"")};
    }

    public String[] getSayOptionsToGetSupportedVoices() {
        return new String[]{"-Command", String.join("", "\"", POWER_SHELL_CODE_SUPPORTED_VOICES, "\"")};
    }

    private String trimQuotes(String line) {
        return line.replaceAll("^\"|\"$", "");
    }

    public Voice parse(String csvLine) throws ParseException {
        String[] tokens = csvLine.split(",");
        if (tokens.length != 5) {
            throw new ParseException(String.format("Invalid csv line: %s", csvLine));
        } else {
            Voice voice = new Voice();
            voice.setName(this.trimQuotes(tokens[0]));
            voice.setCulture(this.trimQuotes(tokens[1]));
            voice.setGender(this.trimQuotes(tokens[2]));
            voice.setAge(this.trimQuotes(tokens[3]));
            voice.setDescription(String.format("%s (%s, %s)", voice.getName(), voice.getCulture().replace('-', '_'), voice.getGender()));
            return voice;
        }
    }
}
