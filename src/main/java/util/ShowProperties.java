package util;

import lombok.experimental.UtilityClass;

@UtilityClass
class ShowProperties {
    public static void main(String[] args) {
        System.getProperties().list(System.out);
    }
}