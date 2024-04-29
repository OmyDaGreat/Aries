package parse;

import ai.rev.speechtotext.RevAiWebSocketListener;
import ai.rev.speechtotext.StreamingClient;
import ai.rev.speechtotext.models.streaming.*;
import lombok.Cleanup;
import lombok.extern.log4j.Log4j2;
import okhttp3.Response;
import okio.ByteString;
import org.jetbrains.annotations.NotNull;

import javax.sound.sampled.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.nio.ByteBuffer;

@Log4j2
public class RevAiStreaming {
	
	private static final String ACCESS_TOKEN = "026oWNQohGMo-O91YeDeWdRzgFlcHU0OwizYCxJ0v8JA1uyJmdVLtw5MNtCFWleWlShsMG6LtFVisTyNzj4V471APkZ5U";
	private volatile boolean stopStreaming = false;
	
	public void streamFromMicrophone() throws InterruptedException, LineUnavailableException {
		// Configure the streaming content type
		@Cleanup StreamingClient streamingClient = getStreamingClient(ACCESS_TOKEN);
		
		// Configure audio format
		AudioFormat format = new AudioFormat(16000, 16, 1, true, false);
		DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);
		
		if (!AudioSystem.isLineSupported(info)) {
			log.error("Line not supported");
			System.exit(0);
		}
		
		// Open the line with the specified format
		@Cleanup TargetDataLine line = (TargetDataLine) AudioSystem.getLine(info);
		line.open();
		line.start();
		
		// Set the number of bytes to send in each message
		int chunk = 4000;
		byte[] buffer = new byte[chunk];
		
		// Start a new thread to listen for keyboard input
		Thread keyboardListener = new Thread(() -> {
			try {
				while (!stopStreaming) {
					// Check for the Space key press
					if (System.in.available() > 0) {
						int key = System.in.read();
						if (key == ' ') {
							stopStreaming = true;
						}
					}
					Thread.sleep(100); // Sleep to reduce CPU usage
				}
			} catch (IOException | InterruptedException e) {
				e.printStackTrace();
			}
		});
		keyboardListener.start();
		
		while (!stopStreaming) {
			int bytesRead = line.read(buffer, 0, chunk);
			if (bytesRead == -1) break;
			streamingClient.sendAudioData(ByteString.of(ByteBuffer.wrap(buffer, 0, bytesRead)));
		}
		
		System.exit(0);
	}
	
	private static @NotNull StreamingClient getStreamingClient(String accessToken) {
		StreamContentType streamContentType = new StreamContentType();
		streamContentType.setContentType("audio/x-raw"); // audio content type
		streamContentType.setLayout("interleaved"); // layout
		streamContentType.setFormat("S16LE"); // format
		streamContentType.setRate(16000); // sample rate
		streamContentType.setChannels(1); // channels
		
		// Set up the SessionConfig with any optional parameters
		SessionConfig sessionConfig = new SessionConfig();
		sessionConfig.setMetaData("Streaming from the Java SDK");
		sessionConfig.setFilterProfanity(true);
		
		// Initialize your client with your access token
		StreamingClient streamingClient = new StreamingClient(accessToken);
		
		// Initialize your WebSocket listener
		WebSocketListener webSocketListener = new WebSocketListener();
		
		// Begin the streaming session
		streamingClient.connect(webSocketListener, streamContentType, sessionConfig);
		return streamingClient;
	}
	
	// Your WebSocket listener for all streaming responses
	private static class WebSocketListener implements RevAiWebSocketListener {
		
		@Override
		public void onConnected(ConnectedMessage message) {
			log.info(message);
		}
		
		@Override
		public void onHypothesis(Hypothesis hypothesis) {
			log.info(hypothesis.toString());
		}
		
		@Override
		public void onError(Throwable t, Response response) {
			log.info(response);
		}
		
		@Override
		public void onClose(int code, String reason) {
			log.info(reason);
		}
		
		@Override
		public void onOpen(Response response) {
			log.info(response.toString());
		}
	}
}