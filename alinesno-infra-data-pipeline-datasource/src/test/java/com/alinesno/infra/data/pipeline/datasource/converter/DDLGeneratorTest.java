package com.alinesno.infra.data.pipeline.datasource.converter;

import com.alinesno.infra.data.pipeline.datasource.ddl.TableField;
import com.alinesno.infra.data.pipeline.datasource.enums.JavaType;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DDLGeneratorTest {

    @Test
    void genDbDDL() {
        List<TableField> tableFields = new ArrayList<>();

        // 添加字段信息
        tableFields.add(new TableField("id", "用户ID", JavaType.INT, 11).setPrimaryKey(true));
        tableFields.add(new TableField("username", "用户名", JavaType.STRING, 255));
        tableFields.add(new TableField("password", "密码", JavaType.STRING, 255));
        tableFields.add(new TableField("email", "邮箱", JavaType.STRING, 255));

        String mysqlDDL = DDLGenerator.genDbDDL(tableFields, "mysql", "user", "InnoDB", "utf8");
        String clickhouseDDL = DDLGenerator.genDbDDL(tableFields, "clickhouse", "user", "", "");

        System.out.println("MySQL建表语句：---->>>>>>>>>>>>>>>>>>>>>>>\n" + mysqlDDL);
        System.out.println("ClickHouse建表语句：>>>>>>>>>>>>>>>>>>>>>>\n" + clickhouseDDL);
    }
}