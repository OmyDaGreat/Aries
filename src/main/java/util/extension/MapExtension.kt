package util.extension

import lombok.experimental.UtilityClass
import java.util.function.Function

/**
   * Checks if a map contains a key with the specified first element.
   *
   * @param key The first element of the pair to search for in the map's keys.
   * @param <A> The type of the first element in the pair.
   * @param <B> The type of the second element in the pair, also the type of the key to search for.
   * @param <V> The type of the map's values.
   * @return true if the map contains at least one key with the specified second element, false otherwise.
  </V></B></A> */
  fun <A, B, V> Map<Pair<A, B>, V>.containsKeyFirst(key: A): Boolean {
    return keys.stream().anyMatch { pair: Pair<A, B> -> pair.first == key }
  }

  /**
   * Checks if a map contains a key with the specified second element.
   *
   * @param key The second element of the pair to search for in the map's keys.
   * @param <A> The type of the first element in the pair.
   * @param <B> The type of the second element in the pair, also the type of the key to search for.
   * @param <V> The type of the map's values.
   * @return true if the map contains at least one key with the specified second element, false otherwise.
  </V></B></A> */
  fun <A, B, V> Map<Pair<A, B>, V>.containsKeySecond(key: B): Boolean {
    return keys.stream().anyMatch { pair: Pair<A, B> -> pair.second == key }
  }

  /**
   * Retrieves the value associated with a key that has the specified first element.
   * If multiple keys match, the value associated with the first matching key is returned.
   *
   * @param key The first element of the pair to search for in the map's keys.
   * @param <A> The type of the first element in the pair.
   * @param <B> The type of the second element in the pair, also the type of the key to search for.
   * @param <V> The type of the map's values.
   * @return The value associated with the first key that has the specified second element.
   * @throws NoSuchElementException if no key with the specified second element is found.
  </V></B></A> */
  @Throws(NoSuchElementException::class)
  fun <A, B, V> Map<Pair<A, B>, V>.getFirst(key: A): V {
    return this.entries.firstOrNull { it.key.first == key }?.value
      ?: throw NoSuchElementException("No value associated with key having first element $key")
  }

  /**
   * Retrieves the value associated with a key that has the specified second element.
   * If multiple keys match, the value associated with the first matching key is returned.
   *
   * @param key The second element of the pair to search for in the map's keys.
   * @param <A> The type of the first element in the pair.
   * @param <B> The type of the second element in the pair, also the type of the key to search for.
   * @param <V> The type of the map's values.
   * @return The value associated with the first key that has the specified second element.
   * @throws NoSuchElementException if no key with the specified second element is found.
  </V></B></A> */
  @Throws(NoSuchElementException::class)
  fun <A, B, V> Map<Pair<A, B>, V>.getSecond(key: B): V {
    return this.entries.firstOrNull { it.key.second == key }?.value
      ?: throw NoSuchElementException("No value associated with key having second element $key")
  }