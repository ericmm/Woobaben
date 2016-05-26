package woo.ba.ben.core;


import java.util.HashMap;
import java.util.Map;

public class ClassStructFactory {
    //TODO: allow the eldest Classes to be garbage collected
    private static final int DEFAULT_CACHE_SIZE = 1024;
    private static final Map<String, ClassStruct> CACHE = new HashMap<>(DEFAULT_CACHE_SIZE);

    private ClassStructFactory() {
    }

    public static ClassStruct get(final Class realClass) {
        assert realClass != null;
        ClassStruct classStruct = CACHE.get(realClass);
        if (classStruct == null) {
            classStruct = new ClassStruct(realClass);
            CACHE.put(classStruct.className, classStruct);
        }
        return classStruct;
    }
}
