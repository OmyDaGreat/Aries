package util.extension;

import kotlin.Pair;
import lombok.experimental.UtilityClass;

import java.util.Map;
import java.util.NoSuchElementException;

/**
 * Provides utility methods for working with Maps that have composite keys.
 * This class is designed to enhance operations on maps where keys are instances of Pair<A, B>.
 */
@UtilityClass
public class MapExtension {

  /**
   * Checks if a map contains a key with the specified first element.
   *
   * @param map The map to search, where keys are pairs of type {@link Pair}.
   * @param key The first element of the pair to search for in the map's keys.
   * @param <A> The type of the first element in the pair.
   * @param <B> The type of the second element in the pair, also the type of the key to search for.
   * @param <V> The type of the map's values.
   * @return true if the map contains at least one key with the specified second element, false otherwise.
   */
  public static <A, B, V> boolean containsKeyFirst(Map<Pair<A, B>, V> map, A key) {
    return map.keySet().stream().anyMatch(pair -> pair.getFirst().equals(key));
  }

  /**
   * Checks if a map contains a key with the specified second element.
   *
   * @param map The map to search, where keys are pairs of type {@link Pair}.
   * @param key The second element of the pair to search for in the map's keys.
   * @param <A> The type of the first element in the pair.
   * @param <B> The type of the second element in the pair, also the type of the key to search for.
   * @param <V> The type of the map's values.
   * @return true if the map contains at least one key with the specified second element, false otherwise.
   */
  public static <A, B, V> boolean containsKeySecond(Map<Pair<A, B>, V> map, B key) {
    return map.keySet().stream().anyMatch(pair -> pair.getSecond().equals(key));
  }

  /**
   * Retrieves the value associated with a key that has the specified first element.
   * If multiple keys match, the value associated with the first matching key is returned.
   *
   * @param map The map to search, where keys are pairs of type {@link Pair}.
   * @param key The first element of the pair to search for in the map's keys.
   * @param <A> The type of the first element in the pair.
   * @param <B> The type of the second element in the pair, also the type of the key to search for.
   * @param <V> The type of the map's values.
   * @return The value associated with the first key that has the specified second element.
   * @throws NoSuchElementException if no key with the specified second element is found.
   */
  public static <A, B, V> V getFirst(Map<Pair<A, B>, V> map, A key) throws NoSuchElementException {
    return map.entrySet().stream()
            .filter(entry -> entry.getKey().getFirst().equals(key))
            .map(Map.Entry::getValue)
            .findFirst().orElseThrow();
  }

  /**
   * Retrieves the value associated with a key that has the specified second element.
   * If multiple keys match, the value associated with the first matching key is returned.
   *
   * @param map The map to search, where keys are pairs of type {@link Pair}.
   * @param key The second element of the pair to search for in the map's keys.
   * @param <A> The type of the first element in the pair.
   * @param <B> The type of the second element in the pair, also the type of the key to search for.
   * @param <V> The type of the map's values.
   * @return The value associated with the first key that has the specified second element.
   * @throws NoSuchElementException if no key with the specified second element is found.
   */
  public static <A, B, V> V getSecond(Map<Pair<A, B>, V> map, B key) throws NoSuchElementException {
    return map.entrySet().stream()
            .filter(entry -> entry.getKey().getSecond().equals(key))
            .map(Map.Entry::getValue)
            .findFirst().orElseThrow();
  }
}