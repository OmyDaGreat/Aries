//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package util.jAdapterForNativeTTS.engines;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class VoicePreferences {
    private String language;
    private String country;
    private Gender gender;
    private Age age;

    public VoicePreferences() {
        this.reset();
    }

    public void reset() {
        this.language = null;
        this.country = null;
        this.gender = null;
        this.age = null;
    }

    public String toString() {
        return String.format("language='%s', country='%s', gender='%s', age='%s'", this.language != null ? this.language : "", this.country != null ? this.country : "", this.gender != null ? this.gender : "", this.age != null ? this.age : "");
    }

    public enum Age {
        CHILD,
        ADULT;

        Age() {
        }
    }

    public enum Gender {
        FEMALE,
        MALE;

        Gender() {
        }
    }
}
