package utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigUtil {
    private static final Properties properties = new Properties();
    private ConfigUtil() {}
    static {
        try (InputStream input =
                     ConfigUtil.class.getClassLoader()
                             .getResourceAsStream("config.properties")) {
            if (input == null) {
                throw new RuntimeException("Could not find config.properties file.");
            }

            properties.load(input);

        } catch (IOException e) {
            throw new RuntimeException("Could not read config.properties file.", e);
        }

    }

    public static String get(String key) { return properties.getProperty(key); }
}
