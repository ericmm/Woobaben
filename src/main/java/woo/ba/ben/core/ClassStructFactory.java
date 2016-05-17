package woo.ba.ben.core;


import java.util.HashMap;
import java.util.Map;

public class ClassStructFactory {
    //TODO: allow the eldest Classes to be garbage collected
    private static final int DEFAULT_CACHE_SIZE = 1024 * 1024;
    private static final Map<Class, ClassStruct> CACHE = new HashMap<>(DEFAULT_CACHE_SIZE);

    private ClassStructFactory() {
    }

    static ClassStruct get(final Class realClass) {
        return CACHE.get(realClass);
    }
}
