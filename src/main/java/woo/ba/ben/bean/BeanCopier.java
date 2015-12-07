package woo.ba.ben.bean;

import sun.misc.Unsafe;
import woo.ba.ben.core.ClassStruct;
import woo.ba.ben.core.ClassStructFactory;
import woo.ba.ben.core.FieldStruct;
import woo.ba.ben.core.UnsafeFactory;

import java.util.List;

import static sun.misc.Unsafe.ARRAY_BYTE_BASE_OFFSET;

public class BeanCopier {
    private static final Unsafe UNSAFE = UnsafeFactory.get();

    public Object copyBean(final Object originalObj) throws InstantiationException {
        if (originalObj == null || originalObj instanceof Class) {
            return originalObj;
        }

        final Class<?> objClass = originalObj.getClass();
        final ClassStruct classStruct = ClassStructFactory.get(objClass);

        final Object objectInstance = createInstance(objClass);
        if (!classStruct.hasInstanceFields()) {
            return objectInstance;
        }

        final long startInstanceOffset = classStruct.getInstanceFieldStartOffset();
        final long instanceFieldsSize = classStruct.getInstanceFieldEndOffset() - startInstanceOffset;
        final byte[] buffer = new byte[(int) instanceFieldsSize];
        UNSAFE.copyMemory(originalObj, startInstanceOffset, buffer, ARRAY_BYTE_BASE_OFFSET, instanceFieldsSize);

        copyValuesFromBuffer(objectInstance, classStruct, buffer);

        return objectInstance;
    }

    private void copyValuesFromBuffer(final Object objectInstance, final ClassStruct classStruct, final byte[] buffer) {
        final List<FieldStruct> sortedInstanceFields = classStruct.getSortedInstanceFields();
        final long firstOffset = sortedInstanceFields.get(0).offset;
        for (final FieldStruct fieldStruct : sortedInstanceFields) {
            if (fieldStruct.type == byte.class) {
                UNSAFE.putByte(objectInstance, fieldStruct.offset, readByte(buffer, (int) (fieldStruct.offset - firstOffset)));
            } else if (fieldStruct.type == boolean.class) {
                UNSAFE.putBoolean(objectInstance, fieldStruct.offset, readBoolean(buffer, (int) (fieldStruct.offset - firstOffset)));
            } else if (fieldStruct.type == char.class) {
                UNSAFE.putChar(objectInstance, fieldStruct.offset, readChar(buffer, (int) (fieldStruct.offset - firstOffset)));
            }
        }
    }

    private char readChar(final byte[] buffer, final int offset) {
        int ch1 = buffer[offset] & 0xff;
        int ch2 = buffer[offset + 1] & 0xff;
        return (char) ((ch1 << 8) + ch2 );
//        return 0;
    }

    private boolean readBoolean(final byte[] buffer, final int offset) {
        return buffer[offset] != 0;

    }

    private byte readByte(final byte[] buffer, final int offset) {
        return buffer[offset];
//        return (byte) (buffer[offset] & 0xff); TODO: need to & 0xff?
    }

    private Object createInstance(final Class<?> objClass) throws InstantiationException {
        final Object objectInstance = UNSAFE.allocateInstance(objClass);
        if (!UNSAFE.shouldBeInitialized(objClass)) {
            UNSAFE.ensureClassInitialized(objClass);
        }
        return objectInstance;
    }
}
