<template>
   <div class="app-container">
    <el-page-header @back="goBack" content="配置数据库源"></el-page-header>

    <!--
    <div class="label-title">
      <div class="tip">配置数据库源</div>
      <div class="sub-tip">根据业务场景需求创建数据采集任务，便于数据业务场景开发和分析</div>
    </div>
    -->

    <div class="form-container" >
      <el-form :model="form" :rules="rules" ref="ruleFormRef" label-width="180px">

         <el-divider content-position="left">读取源设置</el-divider>

         <!-- 数据采集模板 -->
         <el-form-item label="数据采集源" prop="readerSource">
            <el-radio-group v-model="form.readerSource">
               <el-radio v-for="item in readerSource" 
                  class="database-type" 
                  :key="item.dbType"
                  :label="item.dbType"
                  :value="item.dbType" 
                  size="large" 
                  border>
                 <i class="fa-solid fa-screwdriver-wrench"></i> {{ item.dbType }}
               </el-radio>
            </el-radio-group> 
         </el-form-item>
         <el-form-item label="采集SQL语句" prop="querySql">
            <el-input v-model="form.querySql" placeholder="请输入SQL语句" />
         </el-form-item>
         <el-form-item label="存量数据读取">
            <el-switch v-model="form.delivery" />
         </el-form-item>

         <el-divider content-position="left">数据目的设置</el-divider>

         <el-form-item label="数据写入源" prop="sinkSource">
            <el-radio-group v-model="form.sinkSource">
               <el-radio v-for="item in sinkSource" 
                  class="database-type" 
                  :key="item.dbType"
                  :label="item.dbType"
                  :value="item.dbType" 
                  size="large" 
                  border>
                 <i class="fa-solid fa-screwdriver-wrench"></i> {{ item.dbType }}
               </el-radio>
            </el-radio-group> 
         </el-form-item>

         <el-form-item label="异常处理">
            <el-radio-group v-model="form.exception">
               <el-radio v-for="item in exceptionHandle" 
                  :key="item.method"
                  :label="item.method"
                  :value="item.method">
                  {{ item.label }}
                </el-radio>
            </el-radio-group>
         </el-form-item>

         <el-divider content-position="left">数据插件选择</el-divider>

         <el-form-item label="字段映射">
            <el-button type="primary" bg text @click="handleConfigType(scope.row.id , scope.row.documentType)"> 
               <i class="fa-solid fa-link"></i>&nbsp;配置映射关系
            </el-button>
         </el-form-item>

         <el-form-item label="数据插件">
            <el-checkbox-group v-model="form.plugins">
                <el-checkbox v-for="item in plugins" 
                  :value="item.pluginName" 
                  :key="item.pluginName"
                  :label="item.pluginName"
                  name="type">
                  <i :class="item.icon"></i>&nbsp;{{ item.pluginDesc }}
                </el-checkbox> <br/>
            </el-checkbox-group>
         </el-form-item>
        
         <br/>

         <el-form-item>
            <el-button @click="goBack">上一步</el-button>
            <el-button icon="SuccessFilled" type="primary" @click="submitForm('form')">提交任务 </el-button>
         </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<script setup name="createDatasourceJob">

const router = useRouter();

const currentLoginStyle = ref('1')
const loginStyleArr = ref([
   {id:'1' , icon:'http://data.linesno.com/icons/flow/style-04.png' , desc:'表数据采集抽取，针对单一数据表进行全量或增量数据的采集'} ,
   {id:'2' , icon:'http://data.linesno.com/icons/flow/style-05.png' , desc:'文件型数据采集，从文件系统中读取文件数据进行解析和加载'} ,
   {id:'3' , icon:'http://data.linesno.com/icons/flow/style-06.png' , desc:'消息数据采集，消息中间件中实时或定时地收集数据'} 
]);

const plugins = ref([
    {
        pluginName: "ClearNull",
        pluginDesc: "清理数据字段空值，随机添加一个数字",
        icon: "fa-solid fa-user-shield"
    },
    {
        pluginName: "ConvertCase",
        pluginDesc: "将数据字段的文本转换为指定大小写",
        icon: "fa-solid fa-text-height"
    },
    {
        pluginName: "RemoveDuplicates",
        pluginDesc: "去除数据字段中的重复项",
        icon: "fa-solid fa-ban"
    },
    {
        pluginName: "FormatDate",
        pluginDesc: "将日期字段格式化为指定格式",
        icon: "fa-solid fa-calendar-day"
    },
    {
        pluginName: "TrimWhitespace",
        pluginDesc: "去除数据字段中的前后空格",
        icon: "fa-solid fa-space-shuttle"
    },
    {
        pluginName: "SplitField",
        pluginDesc: "将数据字段按指定分隔符拆分",
        icon: "fa-solid fa-cut"
    },
    {
        pluginName: "MergeFields",
        pluginDesc: "将多个数据字段合并为一个",
        icon: "fa-solid fa-plus"
    },
    {
        pluginName: "ReplaceText",
        pluginDesc: "替换数据字段中的指定文本",
        icon: "fa-solid fa-exchange"
    },
    {
        pluginName: "FillMissing",
        pluginDesc: "用指定值填充缺失的数据",
        icon: "fa-solid fa-fill"
    },
    {
        pluginName: "NormalizeData",
        pluginDesc: "将数据字段的数值归一化到指定范围",
        icon: "fa-solid fa-chart-line"
    }
])

const readerSource = ref([
  {
    "driverName": "mysql",
    "dbType": "MySQL",
    "dbDriver": "com.mysql.cj.jdbc.Driver",
    "icon": "fa-brands fa-mysql"
  },
  {
    "driverName": "oracle",
    "dbType": "Oracle",
    "dbDriver": "oracle.jdbc.driver.OracleDriver",
    "icon": "fa-brands fa-oracle"
  },
  {
    "driverName": "sqlserver",
    "dbType": "SQLServer",
    "dbDriver": "com.microsoft.sqlserver.jdbc.SQLServerDriver",
    "icon": "fa-brands fa-microsoft"
  },
  {
    "driverName": "postgresql",
    "dbType": "PostgreSQL",
    "dbDriver": "org.postgresql.Driver",
    "icon": "fa-brands fa-postgresql"
  },
  {
    "driverName": "db2",
    "dbType": "DB2",
    "dbDriver": "com.ibm.db2.jcc.DB2Driver",
    "icon": "fa-brands fa-ibm"
  },
  {
    "driverName": "mongodb",
    "dbType": "MongoDB",
    "dbDriver": "com.mongodb.jdbc.MongoDriver",
    "icon": "fa-brands fa-mongodb"
  },
  {
    "driverName": "cassandra",
    "dbType": "Cassandra",
    "dbDriver": "com.datastax.oss.driver.internal.core.cql.CqlRequestHandler",
    "icon": "fa-brands fa-cassandra"
  },
  {
    "driverName": "elasticsearch",
    "dbType": "Elasticsearch",
    "dbDriver": "org.elasticsearch.client.jdbc.ElasticsearchDriver",
    "icon": "fa-brands fa-elastic"
  },
  {
    "driverName": "hbase",
    "dbType": "HBase",
    "dbDriver": "org.apache.hadoop.hbase.jdbc.HBaseDriver",
    "icon": "fa-brands fa-hadoop"
  },
  {
    "driverName": "hive",
    "dbType": "Hive",
    "dbDriver": "org.apache.hive.jdbc.HiveDriver",
    "icon": "fa-brands fa-hive"
  },
  {
    "driverName": "jdbc",
    "dbType": "JDBC",
    "dbDriver": "org.apache.derby.jdbc.EmbeddedDriver",
    "icon": "fa-brands fa-java"
  }]) 

const sinkSource = ref([
   {
    "driverName": "json",
    "dbType": "JSON",
    "dbDriver": "org.json.JSONDriver",
    "icon": "fa-solid fa-file-json"
  },
  {
    "driverName": "hbase",
    "dbType": "HBase",
    "dbDriver": "org.apache.hadoop.hbase.jdbc.HBaseDriver",
    "icon": "fa-brands fa-hadoop"
  },
  {
    "driverName": "xml",
    "dbType": "XML",
    "dbDriver": "org.w3c.dom.jdbc.DomDriver",
    "icon": "fa-solid fa-file-xml"
  },
  {
    "driverName": "csv",
    "dbType": "CSV",
    "dbDriver": "org.apache.commons.csv.CsvDriver",
    "icon": "fa-solid fa-file-csv"
  },
  {
    "driverName": "fixed",
    "dbType": "Fixed",
    "dbDriver": "org.apache.commons.fixed.FixedDriver",
    "icon": "fa-solid fa-file"
  },
  {
    "driverName": "avro",
    "dbType": "Avro",
    "dbDriver": "org.apache.avro.jdbc.AvroDriver",
    "icon": "fa-solid fa-file"
  },
  {
    "driverName": "parquet",
    "dbType": "Parquet",
    "dbDriver": "org.apache.parquet.jdbc.ParquetDriver",
    "icon": "fa-solid fa-file"
  },
  {
    "driverName": "orc",
    "dbType": "ORC",
    "dbDriver": "org.apache.orc.jdbc.OrcDriver",
    "icon": "fa-solid fa-file"
  },
  {
    "driverName": "excel",
    "dbType": "Excel",
    "dbDriver": "org.apache.poi.jdbc.ExcelDriver",
    "icon": "fa-solid fa-file-excel"
  },
  {
    "driverName": "lucene",
    "dbType": "Lucene",
    "dbDriver": "org.apache.lucene.jdbc.LuceneDriver",
    "icon": "fa-solid fa-search"
  }
]) ;

const exceptionHandle = ref([
  {method:'jump' , label:'跳过且记录'},
  {method:'stop' , label:'停止采集'}
])

const data = reactive({
  form: {
      sinkSource: '' ,
      readerSource: '' ,
      querySql: '',
      plugins: [],
      exception: '',

      name: '',
      region: '',
      date1: '',
      dataCollectionTemplate: 'mysql' ,
      date2: '',
      delivery: false,
      type: [],
      resource: 'project',
      desc: '', 
  },
  queryParams: {
      pageNum: 1,
      pageSize: 10,
      jobName: undefined,
      dbDesc: undefined
  },
  rules: {
     taskName: [
         { required: true, message: "请输入任务名称", trigger: "blur" }
      ],
      dataCollectionTemplate: [
         { required: true, message: "请输入数据采集模板", trigger: "blur" }
      ],
      dataQuality: [
         { required: true, message: "请输入数据质量要求", trigger: "blur" }
      ],
      batchExtractionAmount: [
         { required: true, message: "请输入批数据抽取量", trigger: "blur" }
      ],
      batchWriteAmount: [
         { required: true, message: "请输入批数写入量", trigger: "blur" }
      ],
      isAlertEnabled: [
         { required: true, message: "请选择是否启用告警", trigger: "change" }
      ],
      monitorEmail: [
         { required: true, message: "请输入参与人监控邮箱", trigger: "blur" },
         { type: "email", message: "请输入正确的邮箱地址", trigger: ["blur", "change"] }
      ] 
  }
});

const { queryParams, form, rules } = toRefs(data);

/** 查询任务列表 */
function selectStyle(item){
   currentLoginStyle.value = item.id;
   console.log('item = ' + item.id) ;
}

/** 返回 */
function goBack() {
   router.push({path:'/task/data/pipeline/task/create',query:{}});
}


</script>


<style scoped lang="scss">
  .form-container {
    max-width: 80%;
    margin-left: auto;
    margin-right: auto;
    margin-top: 20px;
  }

  .label-title {
    text-align: center;
    max-width: 960px;
    margin-left: auto;
    margin-right: auto;
    margin-top: 10px;

    .tip {
      padding-bottom: 10px;
      font-size: 26px;
      font-weight: bold;
    }

    .sub-tip {
      font-size: 13px;
      text-align: center;
      padding: 10px;
    }
  }

  .image{
    width:100%;
    height: 120px ;
  }

  .select-card {
    border: 1px solid rgb(0, 91, 212) ;
  }

  .database-type {
    width: calc(25% - 10px);
    margin-bottom: 10px;
    margin-right: 10px;
  }
</style>