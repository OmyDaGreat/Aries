package parse;

import ai.rev.speechtotext.RevAiWebSocketListener;
import ai.rev.speechtotext.StreamingClient;
import ai.rev.speechtotext.models.streaming.*;
import lombok.extern.log4j.Log4j2;
import okhttp3.Response;
import okio.ByteString;
import org.jetbrains.annotations.NotNull;

import javax.sound.sampled.*;
import java.nio.ByteBuffer;

@Log4j2
public class RevAiStreaming {
	
	private static final String ACCESS_TOKEN = "026oWNQohGMo-O91YeDeWdRzgFlcHU0OwizYCxJ0v8JA1uyJmdVLtw5MNtCFWleWlShsMG6LtFVisTyNzj4V471APkZ5U";
	
	public void streamFromMicrophone() throws InterruptedException, LineUnavailableException {
		// Configure the streaming content type
		StreamingClient streamingClient = getStreamingClient(ACCESS_TOKEN);
		
		// Configure audio format
		AudioFormat format = new AudioFormat(16000, 16, 1, true, false);
		DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);
		
		if (!AudioSystem.isLineSupported(info)) {
			log.error("Line not supported");
			System.exit(0);
		}
		
		// Open the line with the specified format
		TargetDataLine line = (TargetDataLine) AudioSystem.getLine(info);
		line.open();
		line.start();
		
		// Set the number of bytes to send in each message
		int chunk = 8000;
		byte[] buffer = new byte[chunk];
		
		// Stream the audio in the configured chunk size
		while (true) {
			int bytesRead = line.read(buffer, 0, chunk);
			if (bytesRead == -1) break;
			
			streamingClient.sendAudioData(ByteString.of(ByteBuffer.wrap(buffer, 0, bytesRead)));
		}
		
		// Wait to make sure all responses are received
		Thread.sleep(5000);
		
		// Close the WebSocket
		streamingClient.close();
		
		// Close the line
		line.close();
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