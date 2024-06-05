package parse;

import java.awt.*;
import java.io.IOException;
import util.LiveMic;

public class LiveMicDemo {
	
	public static void main(String[] args) throws IOException, InterruptedException, AWTException {
		LiveMic.startRecognition();
	}
	
}
