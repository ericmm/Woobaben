package woo.ba.ben.core;


import java.util.ArrayList;
import java.util.List;

public class ClassMeta {
    protected Class<?> clazz;
    protected ClassMeta parent;

    private SimpleMap<String, FieldMeta> fieldMap = new SimpleArrayMap<>();
    private List<String> fieldNames = new ArrayList<>();

    public void putField(String fieldName, FieldMeta fieldMeta) {
        fieldMap.put(fieldName, fieldMeta);
        fieldNames.add(fieldName);
    }
}
