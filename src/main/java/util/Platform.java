package util;

import lombok.Getter;

@Getter
public enum Platform {
    WINDOWS_X64_CHROME("Windows x64 Chrome"),
    WINDOWS_X64_FIREFOX("Windows x64 Firefox"),
    WINDOWS_X64_EDGE("Windows x64 Edge"),
    LINUX_X64_CHROME("Linux x64 Chrome"),
    LINUX_X64_FIREFOX("Linux x64 Firefox"),
    LINUX_X64_EDGE("Linux x64 Edge"),
    MAC_CHROME("Mac Chrome"),
    MAC_FIREFOX("Mac Firefox"),
    MAC_EDGE("Mac Edge"),
    UNKNOWN("Unknown");

    private final String displayName;

    Platform(String displayName) {
        this.displayName = displayName;
    }

    public static Platform detectPlatform(String osAndBrowser) {
        return switch (osAndBrowser) {
            case "windows-x64-chrome" -> WINDOWS_X64_CHROME;
            case "windows-x64-firefox" -> WINDOWS_X64_FIREFOX;
            case "windows-x64-edge" -> WINDOWS_X64_EDGE;
            case "linux-x64-chrome" -> LINUX_X64_CHROME;
            case "linux-x64-firefox" -> LINUX_X64_FIREFOX;
            case "linux-x64-edge" -> LINUX_X64_EDGE;
            case "mac-chrome" -> MAC_CHROME;
            case "mac-firefox" -> MAC_FIREFOX;
            case "mac-edge" -> MAC_EDGE;
            default -> UNKNOWN;
        };
    }
}
