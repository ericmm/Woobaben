package woo.ba.ben.bean;


import woo.ba.ben.core.ArrayBackedHashMap;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

public class BeanSerializer {

    public void write(final OutputStream out, final Object object) throws IOException {
        if (out == null || object == null) {
            return;
        }

        final Map<Integer, Object> objectMap = new ArrayBackedHashMap<>();
        if (object.getClass().isArray()) {
//            writeArray(out, object, objectMap);
        }
//        writeObject(out, object, objectMap);
    }


}
