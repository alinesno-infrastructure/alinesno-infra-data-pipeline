package com.alinesno.infra.data.pipeline.datasource.enums;

public enum JavaType {

    // 基本数值类型
    BYTE("byte", "字节类型"),
    SHORT("short", "短整数类型"),
    INT("int", "整数类型"),
    LONG("long", "长整数类型"),
    FLOAT("float", "单精度浮点数类型"),
    DOUBLE("double", "双精度浮点数类型"),
    CHAR("char", "字符类型"),
    BOOLEAN("boolean", "布尔类型"),

    // 复杂类型
    STRING("String", "字符串类型"),
    ARRAY("Array", "数组类型"),
    JSON("Json", "JSON类型"),
    MAP("Map", "映射类型");

    private final String type;
    private final String description;

    JavaType(String type, String description) {
        this.type = type;
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public String getDescription() {
        return description;
    }

    public static JavaType fromType(String type) {
        for (JavaType javaType : JavaType.values()) {
            if (javaType.getType().equals(type)) {
                return javaType;
            }
        }
        return null; // 或者抛出异常，表示无法识别的类型
    }
}
