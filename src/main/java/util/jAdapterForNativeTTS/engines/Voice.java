//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package util.jAdapterForNativeTTS.engines;

import lombok.Getter;
import lombok.Setter;
import util.jAdapterForNativeTTS.engines.VoicePreferences.Age;
import util.jAdapterForNativeTTS.engines.VoicePreferences.Gender;
import java.util.Locale;

@Setter
@Getter
public class Voice {
    private String name;
    private String culture;
    private String gender;
    private String age;
    private String description;

    public String toString() {
        return String.format("name='%s', culture='%s', gender='%s', age='%s', description='%s'", this.name, this.culture, this.gender, this.age, this.description);
    }

    public String toJSONString() {
        return String.format("{\"name\" : \"%s\", \"culture\" : \"%s\", \"gender\" : \"%s\", \"age\" : \"%s\", \"description\" : \"%s\"}", this.name, this.culture, this.gender, this.age, this.description);
    }

    private VoicePreferences toVoicePreferences() {
        VoicePreferences voicePreferences = new VoicePreferences();
        String[] cultureTokens = this.culture.toLowerCase(Locale.US).replace("_", "-").split("-");
        if (cultureTokens.length > 0) {
            voicePreferences.setLanguage(cultureTokens[0]);
        }

        if (cultureTokens.length > 1) {
            voicePreferences.setCountry(cultureTokens[1].toUpperCase(Locale.US));
        }

        if (this.gender.toLowerCase(Locale.US).equals("male")) {
            voicePreferences.setGender(Gender.MALE);
        } else if (this.gender.toLowerCase(Locale.US).equals("female")) {
            voicePreferences.setGender(Gender.FEMALE);
        }

        if (this.age.toLowerCase(Locale.US).equals("adult")) {
            voicePreferences.setAge(Age.ADULT);
        } else if (this.age.toLowerCase(Locale.US).equals("child")) {
            voicePreferences.setAge(Age.CHILD);
        }

        return voicePreferences;
    }

    public boolean matches(VoicePreferences voicePreferences) {
        boolean match = true;
        VoicePreferences myVoicePreferences = this.toVoicePreferences();
        if (voicePreferences.getLanguage() != null && myVoicePreferences.getLanguage() != null) {
            match = voicePreferences.getLanguage().equalsIgnoreCase(myVoicePreferences.getLanguage());
        }

        if (!match) {
            return false;
        } else {
            if (voicePreferences.getCountry() != null && myVoicePreferences.getCountry() != null) {
                match = voicePreferences.getCountry().equalsIgnoreCase(myVoicePreferences.getCountry());
            }

            if (!match) {
                return false;
            } else {
                if (voicePreferences.getGender() != null && myVoicePreferences.getGender() != null) {
                    match = voicePreferences.getGender().equals(myVoicePreferences.getGender());
                }

                if (!match) {
                    return false;
                } else {
                    if (voicePreferences.getAge() != null && myVoicePreferences.getAge() != null) {
                        match = voicePreferences.getAge().equals(myVoicePreferences.getAge());
                    }

                    return match;
                }
            }
        }
    }
}
