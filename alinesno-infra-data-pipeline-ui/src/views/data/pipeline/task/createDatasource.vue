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
         <el-form-item label="数据采集源" prop="dataCollectionTemplate">
            <el-radio-group v-model="form.dataCollectionTemplate">
               <el-radio v-for="item in readerSource" :value="item.dbType" size="large">
                 <i class="fa-solid fa-screwdriver-wrench"></i> {{ item.dbType }}
               </el-radio>
            </el-radio-group> 
         </el-form-item>
         <el-form-item label="采集SQL语句">
            <el-input v-model="form.name" placeholder="请输入SQL语句" />
         </el-form-item>
         <el-form-item label="存量数据读取">
            <el-switch v-model="form.delivery" />
         </el-form-item>


         <el-divider content-position="left">数据目的设置</el-divider>

         <el-form-item label="即时配送">
            <el-switch v-model="form.delivery" />
         </el-form-item>
         <el-form-item label="数据写入源" prop="dataCollectionTemplate">
            <el-radio-group v-model="form.dataCollectionTemplate">
               <el-radio v-for="item in sinkSource" :value="item.dbType" size="large">
                 <i class="fa-solid fa-screwdriver-wrench"></i> {{ item.dbType }}
               </el-radio>
            </el-radio-group> 
         </el-form-item>
         <el-form-item label="资源">
            <el-radio-group v-model="form.resource">
               <el-radio value="赞助商">赞助商</el-radio>
               <el-radio value="场地">场地</el-radio>
            </el-radio-group>
         </el-form-item>

         <el-divider content-position="left">数据插件选择</el-divider>

         <el-form-item label="字段映射">
            <el-button type="primary" bg text @click="handleConfigType(scope.row.id , scope.row.documentType)"> 
               <i class="fa-solid fa-link"></i>&nbsp;配置映射关系
            </el-button>
         </el-form-item>

         <el-form-item label="数据插件">
            <el-checkbox-group v-model="form.type">
               <el-checkbox value="在线活动" name="type">字段映射</el-checkbox> 
               <el-checkbox value="线下活动" name="type">线下活动</el-checkbox> 
               <el-checkbox value="在线活动" name="type">字段映射</el-checkbox> 
               <el-checkbox value="线下活动" name="type">线下活动</el-checkbox> 
               <el-checkbox value="品牌曝光" name="type">品牌曝光</el-checkbox> 
               <el-checkbox value="在线活动" name="type">字段映射</el-checkbox> 
               <el-checkbox value="线下活动" name="type">线下活动</el-checkbox> 
               <el-checkbox value="品牌曝光" name="type">品牌曝光</el-checkbox> 
               <el-checkbox value="品牌曝光" name="type">品牌曝光</el-checkbox> 
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

const data = reactive({
  form: {
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
    max-width: 960px;
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
</style>