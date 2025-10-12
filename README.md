# alinesno-infra-data-pipeline
提供数据集成的功能，用于将不同数据源的数据进行整合和转换。

## 性能要求
 
性能指标：

- 定时任务支持不少于2000个
- 单表数据迁移不少于20亿
- 文件数据迁移不少于20TB
- 消息消费迁移不少于100亿条

## 鸣谢

- 集成学习参考资料[Quartz应用与集群原理分析](https://tech.meituan.com/2014/08/31/mt-crm-quartz.html)
- 集成定时任务框架[quartz](https://github.com/kagkarlsson/db-scheduler)