package com.alinesno.infra.data.pipeline.api;

import com.alibaba.fastjson.JSON;
import lombok.Data;
import lombok.ToString;

import java.util.List;
import java.util.Map;

@ToString
@Data
public class JobConfig {
    private String readerSource;
    private ReaderSinkSourceProps readerSourceProps;
    private String sinkSource;
    private ReaderSinkSourceProps sinkSourceProps;
    private List<String> plugins;
    private PluginsProps pluginsProps;
    private String exception;
    private int batchExtractionAmount;
    private int batchWriteAmount;
    private Info info;

    @Data
    public static class ReaderSinkSourceProps {
        private Map<String, Object> qiniu;
        private Map<String, Object> kafka;
        private Database database;
        private ReaderSinkFieldMeta[] readerSinkFieldMate;
    }

    @Data
    public static class ReaderSinkFieldMeta {
        private String name;
        private String type;
        private String comment;
        private String defaultValue;
        private String nullable;
        private String primaryKey;
        private String autoIncrement;
        private String unique;
        private String originalField;
        private String targetField;
    }

    @Data
    public static class Database {
        private String querySql;
        private String targetTableName;
    }

    @Data
    public static class PluginsProps {
        private ClearNull clearNull;
        private ConvertCase convertCase;
        private EncryptData encryptData;
        private ReplaceText replaceText;
        private TrimWhitespace trimWhitespace;
        private MergeFields mergeFields;
        private DecryptData decryptData;
        private FormatDate formatDate;
        private FillMissing fillMissing;
        private FormatData formatData;

        @Data
        public static class ClearNull {
            private List<String> fields;
            private int randomType;
        }

        @Data
        public static class ConvertCase {
            private List<String> fields;
            private String type;
        }

        @Data
        public static class EncryptData {
            private List<String> fields;
            private String algorithm;
        }

        @Data
        public static class ReplaceText {
            private List<String> fields;
            private String originalText;
            private String replacementText;
        }

        @Data
        public static class TrimWhitespace {
            private boolean allFields;
            private List<String> fields;
        }

        @Data
        public static class MergeFields {
            private List<String> fields;
            private int mergeMethod;
        }

        @Data
        public static class DecryptData {
            private List<String> fields;
            private String decryptionKey;
            private String algorithm;
        }

        @Data
        public static class FormatDate {
            private List<String> fields;
            private String dateFormat;
        }

        @Data
        public static class FillMissing {
            private boolean allFields;
            private List<String> fields;
            private String fillValue;
        }

        @Data
        public static class FormatData {
            private List<String> fields;
            private String format;
        }
    }

    @Data
    public static class Info {
        private String taskName;
        private Map<String, String> context;
        private String dataCollectionTemplate;
        private int dataQuality;
        private boolean isAlertEnabled;
        private String monitorEmail;
        private String cronExpression;
        private String startTime;
        private String endTime;
    }

}