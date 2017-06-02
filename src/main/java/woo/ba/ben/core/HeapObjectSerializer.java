package woo.ba.ben.core;


import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import static woo.ba.ben.core.ClassStruct.classStruct;

public class HeapObjectSerializer {

    public void write(final OutputStream out, final Object object) throws IOException {
        if (out == null || object == null) {
            return;
        }

        final Map<Integer, Object> objectMap = new HashMap<>();
        if (object.getClass().isArray()) {
//            writeArray(out, object, objectMap);
        }
        writeObject(out, object, objectMap);
//        Thread.getAllStackTraces();
//        Thread.currentThread().getStackTrace();
    }

    private void writeObject(final OutputStream out, final Object originalObj, final Map<Integer, Object> objectMap) {
        if (originalObj == null) {
            return;
        }
        final ClassStruct classStruct = classStruct(originalObj.getClass());
        if (!classStruct.hasInstanceFields()) {
            return;
        }

//        Object attributeInOriginalObj, attributeInTargetObj;
//        for (final FieldStruct fieldStruct : classStruct.getSortedInstanceFields()) {
//            if (fieldStruct.type.isPrimitive()) {
//                copyPrimitive(originalObj, targetObject, fieldStruct);
//            } else {
//                attributeInOriginalObj = UNSAFE.getObject(originalObj, fieldStruct.offset);
//                if (fieldStruct.type.isArray()) {
//                    attributeInTargetObj = copyArray(attributeInOriginalObj, objectMap);
//                } else {
//                    attributeInTargetObj = copyObject(attributeInOriginalObj, objectMap);
//                }
//                UNSAFE.putObject(targetObject, fieldStruct.offset, attributeInTargetObj);
//            }
//        }
//        return targetObject;
    }


}
