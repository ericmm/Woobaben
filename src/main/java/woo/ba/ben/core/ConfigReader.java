package woo.ba.ben.core;


import com.google.common.io.ByteSource;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import static com.google.common.io.Resources.asByteSource;
import static com.google.common.io.Resources.getResource;
import static java.lang.Long.parseLong;

class ConfigReader {
    private static final Properties PROPERTIES = new Properties();

    static {
        final ByteSource byteSource = asByteSource(getResource("config.properties"));
        try (InputStream inputStream = byteSource.openBufferedStream()) {
            PROPERTIES.load(inputStream);

            PROPERTIES.list(System.out);
        } catch (IOException e) {
            throw new RuntimeException("openBufferedStream failed!", e);
        }
    }

    private ConfigReader() {
    }

    static String getProperty(final String key) {
        return PROPERTIES.getProperty(key);
    }

    static long getPropertyAsLong(final String key) {
        return parseLong(PROPERTIES.getProperty(key));
    }
}
