<template>
   <div class="app-container">

    <el-page-header @back="goBack" content="读取源配置"></el-page-header>
    <div class="label-title">
      <div class="tip">创建数据读取源</div>
      <div class="sub-tip">根据业务场景需求创建数据采集任务，便于数据业务场景开发和分析</div>
    </div>
    <div class="form-container" >
         <el-form ref="databaseRef" :model="form" :rules="rules" label-width="100px">

            <el-form-item label="源类型" prop="readerType">
               <el-radio-group v-model="form.readerType">
                  <el-radio key="R" label="R" value="R"  size="large">读取(reader)</el-radio>
                  <el-radio key="S" label="S" value="S"  size="large">写入(sink)</el-radio>
               </el-radio-group> 
            </el-form-item>

            <!-- 数据采集模板 -->
            <el-form-item label="数据采集源" prop="readerSource" v-if="form.readerType == 'R'">
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

            <el-form-item label="数据写入源" prop="sinkSource" v-if="form.readerType == 'S'">
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


            <el-form-item label="描述" prop="readerDesc">
               <el-input v-model="form.readerDesc" placeholder="读取源描述"/>
            </el-form-item>

            <el-form-item label="连接" prop="readerUrl">
               <el-input v-model="form.readerUrl" placeholder="jdbc:mysql://localhost:3306/readerName?useUnicode=true&characterEncoding=utf8&characterSetResults=utf8&useSSL=false&serverTimezone=GMT"/>
            </el-form-item>

            <el-form-item label="用户名" prop="readerUsername">
               <el-input v-model="form.readerUsername" auto-complete="new-password" placeholder="读取源用户名"/>
            </el-form-item>

            <el-form-item label="密码" prop="readerPasswd">
               <el-input type="password" auto-complete="new-password" v-model="form.readerPasswd" placeholder="读取源密码"/>
            </el-form-item>

            </el-form>

            <div class="dialog-footer" style="float:right;margin-top:20px">
               <!-- <el-button type="primary" @click="submitForm">确 定</el-button>
               <el-button @click="cancel">取 消</el-button> -->
               <el-button type="text" @click="validateDburl">请先点击验证读取源是否连通</el-button>
               <el-button type="primary" @click="submitForm" :disabled="!btnChangeEnable">确 定</el-button>
               <el-button @click="cancel">取 消</el-button>
            </div>
      </div>
   </div>
</template>

<script setup name="ReaderSource">

import {
   listReaderSource,
   delReaderSource,
   getReaderSource,
   checkDbConfig,
   updateReaderSource,
   addReaderSource
} from "@/api/data/pipeline/readerSource";

import {
  getAllSourceReader,
} from "@/api/data/pipeline/jobBuilder";

const router = useRouter();
const { proxy } = getCurrentInstance();

// 定义变量
const ReaderSourceList = ref([]);
const open = ref(false);
const loading = ref(true);
const showSearch = ref(true);
const ids = ref([]);
const readerDescs = ref([]);
const single = ref(true);
const multiple = ref(true);
const total = ref(0);
const title = ref("");
const dateRange = ref([]);
const btnChangeEnable = ref(true)

const readerSource = ref([])  // 读取源
const sinkSource = ref([])  // 数据目的

const postOptions = ref([]);
const roleOptions = ref([]);

// 列显隐信息
const columns = ref([
   { key: 0, label: `应用名称`, visible: true },
   { key: 1, label: `应用描述`, visible: true },
   { key: 2, label: `表数据量`, visible: true },
   { key: 3, label: `类型`, visible: true },
   { key: 4, label: `应用地址`, visible: true },
   { key: 5, label: `状态`, visible: true },
   { key: 6, label: `更新时间`, visible: true }
]);

const data = reactive({
   form: {
      readerType: 'R'
   },
   queryParams: {
      pageNum: 1,
      pageSize: 10,
      readerName: undefined,
      readerDesc: undefined
   },
   rules: {
      readerName: [{ required: true, message: "名称不能为空", trigger: "blur" }] , 
      readerUrl: [{ required: true, message: "连接不能为空", trigger: "blur" }],
      readerType: [{ required: true, message: "类型不能为空", trigger: "blur" }] , 
      readerUsername: [{ required: true , message: "用户名不能为空", trigger: "blur"}],
      readerPasswd: [{ required: true, message: "密码不能为空", trigger: "blur" }] , 
      readerDesc: [{ required: true, message: "备注不能为空", trigger: "blur" }] 
   }
});

const { queryParams, form, rules } = toRefs(data);

/** 查询应用列表 */
function getList() {
   loading.value = true;
   listReaderSource(proxy.addDateRange(queryParams.value, dateRange.value)).then(res => {
      loading.value = false;
      ReaderSourceList.value = res.rows;
      total.value = res.total;
   });
};

/** 获取到所有数据源 */
function handleGetAllSourceReader() {
  getAllSourceReader().then(response => {
    const data = response.data;

    readerSource.value = data.filter(item => item.sourceType === 'sink');
    sinkSource.value = data.filter(item => item.sourceType === 'source');

    readerSource.value = data ; 
    sinkSource.value = data ; 

    console.log('sinkSource = ' + sinkSource.value)
  });
}

/** 返回 */
function goBack() {
   router.push({path:'/migration/data/pipeline/readerSource/index',query:{}});
}

/** 提交按钮 */
function validateDburl() {
   proxy.$refs["databaseRef"].validate(valid => {
      if (valid) {
         if(!form.readerType){
            form.value.readerType = 'MySQL' ;
         }

         checkDbConfig(form.value).then(resp=>{
            if (resp.data.accepted){
               proxy.$modal.msgSuccess("读取源校验成功");
               btnChangeEnable.value = true ;
            }else{
               proxy.$modal.msgSuccess(resp.msg);
            }
         })

      }
   });
};

/** 提交按钮 */
function submitForm() {
   proxy.$refs["databaseRef"].validate(valid => {
      if (valid) {
         if (form.value.id != undefined) {
            updateReaderSource(form.value).then(response => {
               proxy.$modal.msgSuccess("修改成功");
               open.value = false;
               getList();
            });
         } else {
            addReaderSource(form.value).then(response => {
               proxy.$modal.msgSuccess("新增成功");
               open.value = false;
               getList();
            });
         }
      }
   });
};

handleGetAllSourceReader();

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