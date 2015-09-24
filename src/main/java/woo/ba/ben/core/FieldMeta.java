package woo.ba.ben.core;

public class FieldMeta {
    protected String name;
    protected Class<?> type; //can be Enum, Class, Interface...
    protected int modifiers; //static, transient??, instance,
    protected long offset;

//    protected boolean isArray;
//    protected Class<?> componentType;
//    protected int length;
//    protected int baseOffset; //ARRAY_INT_BASE_OFFSET, etc...
//    protected int indexScale; //ARRAY_INT_INDEX_SCALE, etc...

}
