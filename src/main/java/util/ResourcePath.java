package util;

import lombok.experimental.UtilityClass;

import java.net.URISyntaxException;
import java.util.Objects;

@UtilityClass
public class ResourcePath {
    public static String getResourcePath(String path) throws URISyntaxException {
        return Objects.requireNonNull(ResourcePath.class.getClassLoader().getResource(path)).toURI().toString();
    }
}
