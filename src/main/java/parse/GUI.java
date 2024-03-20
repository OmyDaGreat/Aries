package parse;

import org.apache.pivot.wtk.*;
import org.apache.pivot.collections.*;

import lombok.var;

public class GUI implements Application {
    private Window window = null;

    @Override
    public void startup(Display display, Map<String, String> properties) {
        window = new Window();
        window.setTitle("Apache Pivot Base");
        window.setMaximized(true);

        var label = new Label("Hello, Apache Pivot!");
        window.setContent(label);

        window.open(display);
    }

    @Override
    public boolean shutdown(boolean optional) {
        if (window != null) {
            window.close();
        }
        return false;
    }

    @Override
    public void suspend() {
        // Implement any logic needed when the application is suspended
    }

    @Override
    public void resume() {
        // Implement any logic needed when the application is resumed
    }

    public static void main(String[] args) {
        try {
            DesktopApplicationContext.main(GUI.class, args);
        } catch (Throwable err) {
            err.printStackTrace();
        }
    }
}
