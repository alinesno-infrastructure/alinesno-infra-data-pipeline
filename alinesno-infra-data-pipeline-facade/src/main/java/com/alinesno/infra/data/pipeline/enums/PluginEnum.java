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
    REMOVE_DUPLICATES("RemoveDuplicates", "去除数据字段中的重复项", "fa-solid fa-ban"),
    FORMAT_DATE("FormatDate", "将日期字段格式化为指定格式", "fa-solid fa-calendar-day"),
    TRIM_WHITESPACE("TrimWhitespace", "去除数据字段中的前后空格", "fa-solid fa-space-shuttle"),
    SPLIT_FIELD("SplitField", "将数据字段按指定分隔符拆分", "fa-solid fa-cut"),
    MERGE_FIELDS("MergeFields", "将多个数据字段合并为一个", "fa-solid fa-plus"),
    REPLACE_TEXT("ReplaceText", "替换数据字段中的指定文本", "fa-solid fa-exchange"),
    FILL_MISSING("FillMissing", "用指定值填充缺失的数据", "fa-solid fa-fill"),
    NORMALIZE_DATA("NormalizeData", "将数据字段的数值归一化到指定范围", "fa-solid fa-chart-line"),
    ENCRYPT_DATA("EncryptData", "对数据字段进行简单的加密处理", "fa-solid fa-lock"),
    DECRYPT_DATA("DecryptData", "对加密的数据字段进行解密处理", "fa-solid fa-unlock"),
    FORMAT_DATA("FormatData", "将数据字段格式化为指定的输出格式", "fa-solid fa-pen"),
    CLEAN_DATA("CleanData", "清洗数据字段中的异常值", "fa-solid fa-broom"),
    MAP_VALUES("MapValues", "将数据字段中的值映射到另一组值", "fa-solid fa-map"),
    CONVERT_TYPE("ConvertType", "将数据字段的值转换为另一种数据类型", "fa-solid fa-exchange-alt"),
    REPLACE_PATTERN("ReplacePattern", "替换数据字段中的特定模式或值", "fa-solid fa-asterisk"),
    VALIDATE_DATA("ValidateData", "验证数据字段中的数据是否符合预期格式", "fa-solid fa-check"),
    MERGE_DATA("MergeData", "将来自不同源的数据合并到一起", "fa-solid fa-project-diagram");

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
