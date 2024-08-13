# alinesno-infra-data-kettle
提供数据集成的功能，用于将不同数据源的数据进行整合和转换。

## 数据集成示例

请求url:

http://localhost:30202/v1/api/data/pipeline/runPipeline

请求body: 

```json
{
    "name": "订单数据同步",
    "describe": "存储服务数据迁移到数据仓库中.",
    "context": {},
    "fileMap": [],
    "plugins": [
        {
            "code": "security",
            "name": "安全过滤应用"
        },
        {
            "code": "null_filter",
            "name": "去掉为空数据"
        }
    ],
    "reader": {
        "driverClass": "com.mysql.cj.jdbc.Driver",
        "jdbcUrl": "jdbc:mysql://localhost:3306/society_demo?serverTimezone=GMT%2B8&zeroDateTimeBehavior=CONVERT_TO_NULL",
        "name": "mysql",
        "password": "adminer",
        "querySql": "select * from kfc_info limit 5000000",
        "type": "mysql",
        "username": "root"
    },
    "settings": {
        "cron": "*/30 * * * * ?"
    },
    "writer": {
        "driverClass": "com.mysql.cj.jdbc.Driver",
        "jdbcUrl": "jdbc:mysql://localhost:3306/dev_alinesno_infra_data_pipeline_v100?serverTimezone=GMT%2B8&zeroDateTimeBehavior=CONVERT_TO_NULL",
        "name": "clickhouse",
        "password": "adminer",
        "type": "clickhouse",
        "username": "root",
        "writeModel": "append"
    }
}
```

## 鸣谢

- 集成学习参考资料[Quartz应用与集群原理分析](https://tech.meituan.com/2014/08/31/mt-crm-quartz.html)
- 集成定时任务框架[quartz](https://github.com/kagkarlsson/db-scheduler)