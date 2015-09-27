package woo.ba.ben.core;


public class ClassMeta {
    protected Class clazz;
    protected ClassMeta parent;

    private SimpleMap<String, FieldMeta> fieldMap = new SimpleArrayMap<>();

    public void putField(String fieldName, FieldMeta fieldMeta) {
        fieldMap.put(fieldName, fieldMeta);
    }
}
