package com.alinesno.infra.data.pipeline.enums;

import lombok.Getter;

import java.util.Arrays;
import java.util.List;

/**
 * 支持的插件类型
 */
@Getter
public enum PluginEnum {

    CLEAR_NULL("ClearNull", "清理数据字段空值，随机添加一个数字", "fa-solid fa-user-shield"),
    CONVERT_CASE("ConvertCase", "将数据字段的文本转换为指定大小写", "fa-solid fa-text-height"),
    MERGE_FIELDS("MergeFields", "将多个数据字段合并为一个", "fa-solid fa-plus"),
    ENCRYPT_DATA("EncryptData", "对数据字段进行简单的加密处理", "fa-solid fa-lock"),
    DECRYPT_DATA("DecryptData", "对加密的数据字段进行解密处理", "fa-solid fa-unlock"),
    FORMAT_DATE("FormatDate", "将日期字段格式化为指定格式", "fa-solid fa-calendar-day"),
    TRIM_WHITESPACE("TrimWhitespace", "去除数据字段中的前后空格", "fa-solid fa-space-shuttle"),
    REPLACE_TEXT("ReplaceText", "替换数据字段中的指定文本", "fa-solid fa-exchange"),
    FILL_MISSING("FillMissing", "用指定值填充缺失的数据", "fa-solid fa-fill"),
    FORMAT_DATA("FormatData", "将数据字段格式化为指定的输出格式", "fa-solid fa-pen");

    private final String name;
    private final String desc;
    private final String icon;

    PluginEnum(String name, String desc, String icon) {
        this.name = name;
        this.desc = desc;
        this.icon = icon;
    }

    public static List<PluginEnum> getAllDocumentTypes() {
        return Arrays.asList(PluginEnum.values());
    }

    public static Object[] getAllNames() {
        PluginEnum[] documentTypes = PluginEnum.values();
        Object[] names = new Object[documentTypes.length];
        for (int i = 0; i < documentTypes.length; i++) {
            names[i] = documentTypes[i].getName();
        }
        return names;
    }

    public static String getAllNameStr() {

        StringBuilder names = new StringBuilder();

        for(PluginEnum d : getAllDocumentTypes()){
            names.append(",")
                 .append(d.getName());
        }

        return names.toString();
    }
}
