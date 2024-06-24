package parse;

import io.github.jonelo.jAdapterForNativeTTS.engines.*;
import io.github.jonelo.jAdapterForNativeTTS.engines.exceptions.SpeechEngineCreationException;
import java.util.List;

public class GetAvailableVoices {
  public static void main(String[] args) throws SpeechEngineCreationException {
    List<Voice> voices = SpeechEngineNative.getInstance().getAvailableVoices();
    for (Voice voice : voices) {
      System.out.println(voice);
    }
  }
}
