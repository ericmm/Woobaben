package woo.ba.ben.core;

public class ArrayFieldMeta extends FieldMeta {
    protected Class<?> componentType;
    protected int length;
    protected int baseOffset; //ARRAY_INT_BASE_OFFSET, etc...
    protected int indexScale; //ARRAY_INT_INDEX_SCALE, etc...

}
