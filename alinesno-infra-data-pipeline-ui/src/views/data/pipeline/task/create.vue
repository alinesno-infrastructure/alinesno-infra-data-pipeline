<template>
   <div class="app-container">
    <el-page-header @back="goBack" content="任务配置"></el-page-header>
    <div class="label-title">
      <div class="tip">创建数据采集任务</div>
      <div class="sub-tip">根据业务场景需求创建数据采集任务，便于数据业务场景开发和分析</div>
    </div>
    <div class="form-container" >
      <el-form :model="form" :rules="rules" ref="databaseRef" label-width="180px">

         <!-- 任务名称 -->
         <el-form-item label="任务名称" prop="taskName">
            <el-input v-model="form.taskName" placeholder="请输入任务名称"></el-input>
         </el-form-item>

         <!-- 数据采集模板 -->
         <el-form-item label="数据采集模板" prop="dataCollectionTemplate">
            <el-row>
               <el-col :span="7" v-for="(o, index) in loginStyleArr" :key="index" :offset="index > 0 ? 1 : 0">
               <el-card :body-style="{ padding: '0px !important' }" :class="currentLoginStyle == o.id ? 'select-card' : ''" shadow="never">
                  <img :src="o.icon" class="image">
                  <div style="padding: 14px; line-height: 1.4rem; padding: 8px;">
                     <span>{{ o.desc }}</span>
                     <div class="bottom clearfix">
                     <el-button @click="selectStyle(o)" type="text" class="button">选择</el-button>
                     </div>
                  </div>
               </el-card>
               </el-col>
            </el-row>
         </el-form-item>

         <!-- 数据质量 -->
         <el-form-item label="数据质量" prop="dataQuality">
            <el-switch v-model="form.dataQuality" :active-value="1" :inactive-value="0"></el-switch>
         </el-form-item>

         <!-- 批数据抽取量 -->
         <el-form-item label="批数据抽取量" prop="batchExtractionAmount">
            <el-input-number :min="1" size="large" v-model="form.batchExtractionAmount">
            </el-input-number>
         </el-form-item>

         <!-- 批数写入量 -->
         <el-form-item label="批数写入量" prop="batchWriteAmount">
            <el-input-number :min="1" size="large" v-model="form.batchWriteAmount">
            </el-input-number>
         </el-form-item>

         <!-- CRON表达式 -->
         <el-form-item label="CRON表达式" prop="cronExpression">
            <el-input v-model="form.cronExpression" placeholder="请输入CRON表达式"></el-input>
         </el-form-item>

         <!-- 是否告警 -->
         <el-form-item label="起止时间">
            <el-col :span="11">
               <el-date-picker
               v-model="form.date1"
               type="date"
               placeholder="选择日期"
               style="width: 100%"
               />
            </el-col>
            <el-col :span="2" class="text-center">
               <span class="text-gray-500">-</span>
            </el-col>
            <el-col :span="11">
               <el-time-picker
               v-model="form.date2"
               placeholder="选择时间"
               style="width: 100%"
               />
            </el-col>
         </el-form-item>

         <!-- 参与人监控邮箱 -->
         <el-form-item label="参与人监控邮箱" prop="monitorEmail">
            <el-input v-model="form.monitorEmail" placeholder="请输入参与人监控邮箱"></el-input>
         </el-form-item>

        <br/>

        <el-form-item>
          <el-button icon="Right" type="primary" @click="createDatasource">
            下一步
          </el-button>
          <el-button @click="resetForm">
            重置
          </el-button>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<script setup name="createJob">

const router = useRouter();

const loginStyleArr = ref([
   {id:'1' , icon:'http://data.linesno.com/icons/flow/style-04.png' , desc:'表数据采集抽取，针对单一数据表进行全量或增量数据的采集'} ,
   {id:'2' , icon:'http://data.linesno.com/icons/flow/style-05.png' , desc:'文件型数据采集，从文件系统中读取文件数据进行解析和加载'} ,
   {id:'3' , icon:'http://data.linesno.com/icons/flow/style-06.png' , desc:'消息数据采集，消息中间件中实时或定时地收集数据'} 
]);
const currentLoginStyle = ref('1')

const data = reactive({
  form: {
      taskName: "", // 任务名称
      dataCollectionTemplate: "", // 数据采集模板
      dataQuality: "", // 数据质量
      batchExtractionAmount: 5000, // 批数据抽取量
      batchWriteAmount: 5000, // 批数写入量
      isAlertEnabled: false, // 是否告警
      monitorEmail: "" // 参与人监控邮箱
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
   router.push({path:'/task/data/pipeline/task/list',query:{}});
}

/** 创建数据源 */
function createDatasource(){
  let path = '/task/data/pipeline/task/createDatasource' ;
  router.push({ path: path });
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