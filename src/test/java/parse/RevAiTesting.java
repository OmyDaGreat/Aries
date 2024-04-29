package parse;

import javax.sound.sampled.LineUnavailableException;

public class RevAiTesting {

	public static void main(String[] args) throws LineUnavailableException, InterruptedException {
		RevAiStreaming streaming = new RevAiStreaming();
		streaming.streamFromMicrophone();
	}
}
