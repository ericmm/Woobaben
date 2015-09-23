package woo.ba.ben.core;

public class FieldMeta {
    protected String name;
    protected Class<?> type; //can be Enum, Class, Interface...
    protected int modifiers; //static, transient??, instance,
    protected long offset;

}
