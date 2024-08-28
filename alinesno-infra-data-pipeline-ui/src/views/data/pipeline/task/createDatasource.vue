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

         <!--------------------------------------------------------- 任务配置开始 ---------------------------->

         <el-divider content-position="left">读取源设置</el-divider>

         <!-- 数据采集模板 -->
         <el-form-item label="数据采集源" prop="readerSource">
            <el-radio-group v-model="form.readerSource">
               <el-radio v-for="item in readerSource" 
                  class="database-type" 
                  :key="item.id"
                  :label="item.id"
                  :value="item.id" 
                  size="large">
                    <div style="float: left;">
                      <img style="width:30px; height:30px" :src="'http://data.linesno.com/icons/database/' + item.readerType + '.png'" /> 
                    </div>
                    <div style="float: left;margin-left: 10px;line-height: 1.2rem;">
                        {{ item.readerName }}
                        <div style="font-size: 11px;">
                          {{ item.readerDesc }} 
                      </div>
                    </div>
               </el-radio>
            </el-radio-group> 

         </el-form-item>

         <el-form-item label="配置源" prop="readerSourceProps">
            <el-button type="primary" bg text @click="sourceDrawer = true"> 
               <i class="fa-solid fa-screwdriver-wrench"></i>&nbsp;配置读取源参数
            </el-button>
         </el-form-item>

         <!-- 批数据抽取量 -->
         <el-form-item label="批数据抽取量" prop="batchExtractionAmount">
            <el-input-number :min="1" v-model="form.batchExtractionAmount">
            </el-input-number>
         </el-form-item>

         <el-divider content-position="left">数据插件选择</el-divider>

         <el-form-item label="数据插件" prop="plugins">
            <el-checkbox-group v-model="form.plugins">
                <el-checkbox v-for="item in plugins" 
                  :value="item.name" 
                  :key="item.name"
                  :label="item.name"
                  name="type">
                  <i :class="item.icon"></i>&nbsp;{{ item.desc }}
                </el-checkbox> 
            </el-checkbox-group>
         </el-form-item>

         <el-form-item label="配置源">
            <el-button type="primary" bg text @click="pluginDrawer = true"> 
               <i class="fa-solid fa-screwdriver-wrench"></i>&nbsp;配置插件参数
            </el-button>
         </el-form-item>

         <el-divider content-position="left">数据目的设置</el-divider>

         <el-form-item label="数据写入源" prop="sinkSource">
            <el-radio-group v-model="form.sinkSource">
               <el-radio v-for="item in sinkSource" 
                  class="database-type" 
                  :key="item.id"
                  :label="item.id"
                  :value="item.id" 
                  size="large">
                    <div style="float: left;">
                      <img style="width:30px; height:30px" :src="'http://data.linesno.com/icons/database/' + item.readerType + '.png'" /> 
                    </div>
                    <div style="float: left;margin-left: 10px;line-height: 1.2rem;">
                        {{ item.readerName }}
                        <div style="font-size: 11px;">
                          {{ item.readerDesc }} 
                      </div>
                    </div>
               </el-radio>
            </el-radio-group> 

         </el-form-item>

         <el-form-item label="配置源" props="pluginsProps">
            <el-button type="primary" bg text @click="openSinkDrawer()"> 
               <i class="fa-solid fa-screwdriver-wrench"></i>&nbsp;配置写入取源参数
            </el-button>
         </el-form-item>

         <!-- 批数写入量 -->
         <el-form-item label="批数写入量" prop="batchWriteAmount">
            <el-input-number :min="1" v-model="form.batchWriteAmount">
            </el-input-number>
         </el-form-item>

         <el-form-item label="异常处理" prop="exception">
            <el-radio-group v-model="form.exception">
               <el-radio v-for="item in exceptionHandle" 
                  :key="item.method"
                  :label="item.method"
                  :value="item.method">
                  {{ item.label }}
                </el-radio>
            </el-radio-group>
         </el-form-item>

         <el-form-item label="字段映射" prop="">
            <el-button type="primary" bg text @click="openFieldMappingDrawer()">
               <i class="fa-solid fa-screwdriver-wrench"></i>&nbsp;配置映射关系
            </el-button>
         </el-form-item>
      
         <!--------------------------------------------------------- 任务配置结束 ---------------------------->

         <br/>

         <el-form-item>
            <el-button @click="goBack">上一步</el-button>
            <el-button icon="SuccessFilled" type="primary" @click="submitForm('form')">提交任务 </el-button>
         </el-form-item>
      </el-form>
    </div>

    <!-- 参数 Drawer配置-->
    <el-drawer v-model="sourceDrawer" title="数据源参数配置" :direction="direction" size="50%">
      <!-- 写入插件配置参数 -->
      <SourceParam ref="sourceParamRef" :readerSource="getSinkSource(form.readerSource , 'source')"/>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="sourceDrawer = false">取消</el-button>
          <el-button type="primary" @click="callSourceDrawerParams()">
            确认 
          </el-button>
        </div>
      </template>
    </el-drawer>

    <el-drawer v-model="sinkDrawer" title="写入源参数配置" :direction="direction" size="50%">
      <!-- 写入插件配置参数 -->
      <SinkParam ref="sinkParamRef" :sinkSource="getSinkSource(form.sinkSource , 'sink')"/>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="sinkDrawer = false">取消</el-button>
          <el-button type="primary" @click="callSinkParams()">
            确认 
          </el-button>
        </div>
      </template>
    </el-drawer>

    <el-drawer v-model="pluginDrawer" title="插件源参数配置" :direction="direction" size="50%">
      <!-- 写入插件配置参数 -->
      <PluginParam ref="pluginParamRef" :pluginSource="getPluginItem(form.plugins)" :readerFieldMate="readerFieldMate"/>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="pluginDrawer = false">取消</el-button>
          <el-button type="primary" @click="callPluginsParams()">
            确认 
          </el-button>
        </div>
      </template>
    </el-drawer>

    <el-drawer v-model="centerDialogVisible" title="字段映射关系" :direction="direction" size="50%">
      <FieldMapping ref="fieldMappingRef"  :originalFields="readerFieldMate" :targetFields="sinkFieldMate" />
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="centerDialogVisible = false">取消</el-button>
          <el-button type="primary" @click="callFieldMappingRef()">
            确认 
          </el-button>
        </div>
      </template>
    </el-drawer>

  </div>
</template>

<script setup name="createDatasourceJob">

import {
  getAllSourceReader,
  getAllPlugin,
} from "@/api/data/pipeline/jobBuilder";

import { 
  commitJob 
} from '@/api/data/pipeline/job';

import { 
   fetchTableData,
   fetchTableMetaData
} from '@/api/data/pipeline/readerSource';

import SinkParam from "./params/sinkParam.vue";
import SourceParam from "./params/sourceParam.vue";
import PluginParam from "./params/pluginParam.vue";
import FieldMapping from "./params/fieldMapping.vue";
import { AUTO_ALIGNMENT } from "element-plus/es/components/virtual-list/src/defaults";

const router = useRouter();
const { proxy } = getCurrentInstance();

const sourceParamRef = ref()
const sinkParamRef = ref()
const pluginParamRef = ref()
const fieldMappingRef = ref()  // 映射关系组件

const plugins = ref([])  // 数据插件
const readerSource = ref([])  // 读取源
const sinkSource = ref([])  // 数据目的

const pluginDrawer = ref(false)
const sourceDrawer = ref(false)
const sinkDrawer = ref(false)
const centerDialogVisible = ref(false)

const readerFieldMate = ref([]) // 读取源字段
const sinkFieldMate = ref([]) // 写入源字段

const exceptionHandle = ref([
  {method:'jump' , label:'跳过且记录'},
  {method:'stop' , label:'停止采集'}
])

const data = reactive({
  form: {
      readerSource: '' ,
      readerSourceProps: null ,

      sinkSource: '' ,
      sinkSourceProps: null ,

      plugins: [],
      pluginsProps: [],

      exception: '',

      batchExtractionAmount: 5000, // 批数据抽取量
      batchWriteAmount: 5000, // 批数写入量

      // name: '',
      // region: '',
      // date1: '',
      // dataCollectionTemplate: 'mysql' ,
      // date2: '',
      // delivery: false,
      // type: [],
      // resource: 'project',
      // desc: '', 
  },
  queryParams: {
      pageNum: 1,
      pageSize: 10,
      jobName: undefined,
      dbDesc: undefined
  },
  rules: {
     readerSource: [
         { required: true, message: "请选择采集源", trigger: "blur" }
      ],
      readerSourceProps: [
         { required: true, message: "请配置采集源字段", trigger: "blur" }
      ],
      plugins: [
         { required: true, message: "请选择插件", trigger: "blur" }
      ],
      pluginsProps: [
         { required: true, message: "请配置插件参数", trigger: "blur" }
      ],
      batchExtractionAmount: [
         { required: true, message: "请输入批数据抽取量", trigger: "blur" }
      ],
      batchWriteAmount: [
         { required: true, message: "请输入批数写入量", trigger: "blur" }
      ],
      exception: [
         { required: true, message: "请选择异常处理机制", trigger: "change" }
      ],
      sinkSource: [
         { required: true, message: "请选择写入源", trigger: "blur" },
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

/** 获取到所有数据插件 */
function handleGetAllPlugin() {
  getAllPlugin().then(response => {
    plugins.value = response.data;
  });
}
/** 获取到所有数据源 */
function handleGetAllSourceReader() {
  getAllSourceReader().then(response => {
    const data = response.data;

    readerSource.value = data.filter(item => item.sourceType === 'sink');
    sinkSource.value = data.filter(item => item.sourceType === 'source');

    readerSource.value = data ; 
    sinkSource.value = data ; 
  });
}

/** 提交按钮 */
function submitForm() {
  proxy.$refs["ruleFormRef"].validate(valid => {
     if (valid) {
       console.log(JSON.stringify(form.value, null, 2));

       // 将form数据转换为JSON字符串并存储到localStorage
       localStorage.setItem('jobDatasourceFormData', JSON.stringify(form.value));

       const formDataStr = localStorage.getItem('jobFormData');
       form.value.info = JSON.parse(formDataStr);

       commitJob(form.value).then(response => {
          const jobId = response.data;
          // router.push({ path: '/task/data/pipeline/task/list' });
       });

       proxy.$modal.msgSuccess("提交成功");
     }
  });
};


/** 获取到所有数据源 */
function getSinkSource(id , type) {
  if(type === 'sink'){
    return sinkSource.value.filter(item => item.id=== id)[0];
  }else{
    return readerSource.value.filter(item => item.id=== id)[0];
  }
}

/** 根据获取到plugins数组返回对应的插件对象 */
function getPluginItem(pluginArr) {
  // 使用数组的 filter 方法结合 some 方法来找出所有匹配的插件
  // some 方法会检查每个元素是否存在于 pluginArr 中
  return plugins.value.filter(item => pluginArr.some(name => name === item.name));
}

/** 提交字段映射关系 */
const callFieldMappingRef= () => {
  let mappings = fieldMappingRef.value.submitMapping()
  console.log(JSON.stringify(mappings, null, 2));

  centerDialogVisible.value = false
}

/** 提交源参数配置 */
const callSourceDrawerParams= () => {
  let sourceParam = sourceParamRef.value.submitSourceParam()
  console.log(JSON.stringify(sourceParam, null, 2));

  form.value.readerSourceProps = sourceParam ;
  readerFieldMate.value = sourceParam.readerFieldMate;

  sourceDrawer.value = false
}

/** 提交目标参数配置 */
const callSinkParams = () => {
  let sinkParam = sinkParamRef.value.submitSinkParam()
  console.log(JSON.stringify(sinkParam, null, 2));

  form.value.sinkSourceProps = sinkParam;
  // sinkFieldMate.value = sinkParam.sinkFieldMate;

  // 根据表获取到字段信息
  fetchTableMetaData(form.value.sinkSource,sinkParam.database.targetTableName).then(res => {
    sinkFieldMate.value = res.data ; 
    form.value.sinkSourceProps.sinkFieldMate = res.data;
    sinkDrawer.value = false
  })

}

/** 获取插件参数配置 */
const callPluginsParams = () => {
  let plginParam = pluginParamRef.value.submitPluginParam()
  console.log(JSON.stringify(plginParam , null, 2))

  form.value.pluginsProps = plginParam 

  pluginDrawer.value = false
}

const openSinkDrawer = () => {
  sinkDrawer.value = true

  nextTick(() => {
    // 获取到所有字段信息
    sinkParamRef.value.handleFetchTableData(form.value.sinkSource) ;
  })

}

/** 获取插件参数配置 */
const openFieldMappingDrawer = () => {
  centerDialogVisible.value = true

  nextTick(() => {
    // 获取到所有字段信息
    fieldMappingRef.value.handleMappingData(readerFieldMate.value , sinkFieldMate.value) ;
  })

}

/** 从本地存储加载表单数据 */
function loadFormDataFromStorage() {
  const formDataStr = localStorage.getItem('jobDatasourceFormData');
  if (formDataStr) {
    try {
      const formData = JSON.parse(formDataStr);
      Object.assign(form.value, formData);

      readerFieldMate.value = form.value.readerSourceProps.readerFieldMate
      sinkFieldMate.value = form.value.sinkSourceProps.sinkFieldMate

      console.log('Loaded form data from localStorage:', form.value);
    } catch (error) {
      console.error('Error parsing form data from localStorage:', error);
    }
  } else {
    console.log('No form data found in localStorage.');
  }
}

handleGetAllPlugin();
handleGetAllSourceReader();
loadFormDataFromStorage(); // 调用此方法以加载数据

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

</style>