package parse;

import util.WinAppDriver;
public class WinAppDriverTesting {

    public static void main(String[] args) throws InterruptedException {
        // Call the openApp method from WinAppDriver
        WinAppDriver.openApp("C:\\Program Files (x86)\\Windows Application Driver\\WinAppDriver.exe","http://127.0.0.1:4723");
    }
}
