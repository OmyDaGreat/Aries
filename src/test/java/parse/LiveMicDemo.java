package parse;

import ai.picovoice.leopard.LeopardException;

import java.awt.*;
import java.io.IOException;

public class LiveMicDemo {
	
	public static void main(String[] args) throws IOException, InterruptedException, AWTException, LeopardException {
		LiveMic.startRecognition();
	}
	
}
