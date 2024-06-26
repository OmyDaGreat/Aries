//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package util.jAdapterForNativeTTS.engines.macos;

import util.jAdapterForNativeTTS.engines.SpeechEngineAbstract;
import util.jAdapterForNativeTTS.engines.Voice;
import util.jAdapterForNativeTTS.engines.exceptions.ParseException;
import util.jAdapterForNativeTTS.engines.exceptions.SpeechEngineCreationException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SpeechEngineMacOS extends SpeechEngineAbstract {
    private static final String maleNames = "Albert,Eddy,Grandpa,Jester,Jacques,Majed,Reed,Rishi,Rocko,Sinji,Alex,Bruce,Carlos,Cem,Daniel,Diego,Felipe,Fred,Henrik,Jorge,Juan,Junior,Juri,Lee,Luca,Maged,Magnus,Markus,Neel,Nicolas,Nicos,Oliver,Oskar,Otoya,Ralph,Tarik,Thomas,Tom,Xander,Yannick,Yuri,";
    private static final String femaleNames = "Amélie,Amira,Daria,Grandma,Lana,Lesya,Linh,Tünde,Meijia,Mónica,Montse,Sandy,Shelley,Tingting,Alva,Agnes,Alice,Allison,Andrea,Angelica,Anna,Amelie,Aurelie,Ava,Catarina,Carmit,Chantal,Claire,Damayanti,Ellen,Ewa,Fiona,Frederica,Ioana,Iveta,Joana,Kanya,Karen,Kate,Kathy,Katja,Klara,Kyoko,Laila,Laura,Lekha,Luciana,Mariska,Milena,Mei-Jia,Melina,Moira,Monica,Nora,Paola,Paulina,Petra,Princess,Samantha,Sara,Satu,Serena,Sin-ji,Soledad,Susan,Tessa,Ting-Ting,Veena,Vicki,Victoria,Yelda,Yuna,Zosia,Zuzana,";

    public SpeechEngineMacOS() throws SpeechEngineCreationException {
        super();
    }

    public String getSayExecutable() {
        return "say";
    }

    public String[] getSayOptionsToGetSupportedVoices() {
        return new String[]{"-v", "?"};
    }

    private String formatRate() {
        return this.rate == 0 ? "" : String.format("[[rate %s]]", (int)Math.round((double)this.rate * 1.3 + 180.0));
    }

    public String[] getSayOptionsToSayText(String text) {
        String[] var10000 = new String[]{"-v", this.voice, null};
        String var10003 = this.formatRate();
        var10000[2] = var10003 + text;
        return var10000;
    }

    public Voice parse(String line) throws ParseException {
        Pattern pattern = Pattern.compile("^(.+?)\\s+([^ ]+)\\s+#.*$");
        Matcher matcher = pattern.matcher(line);
        if (matcher.find() && matcher.groupCount() == 2) {
            String name = matcher.group(1);
            String culture = matcher.group(2);
            Voice voice = new Voice();
            voice.setName(name);
            voice.setCulture(culture);
            String gender = getGender(stripBrackets(name));
            voice.setGender(gender);
            voice.setAge("?");
            voice.setDescription(String.format("%s (%s, %s)", name, culture, gender));
            return voice;
        } else {
            throw new ParseException(String.format("Unexpected line from %s: %s", this.getSayExecutable(), line));
        }
    }

    private static String stripBrackets(String string) {
        Pattern pattern = Pattern.compile("^([^(]+)\\s+\\(.*\\).*$");
        Matcher matcher = pattern.matcher(string);
        return matcher.find() ? matcher.group(1) : string;
    }

    private static String getGender(String name) {
        if (femaleNames.contains(name + ",")) {
            return "female";
        } else {
            return maleNames.contains(name + ",") ? "male" : "?";
        }
    }
}
