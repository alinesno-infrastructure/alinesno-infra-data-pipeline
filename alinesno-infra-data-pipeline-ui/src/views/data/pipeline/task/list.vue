<template>
  <div class="app-container">

     <el-row :gutter="20">
        <!--类型数据-->
         <el-col :span="4" :xs="24">
            <div class="head-container">
               <el-input
                  v-model="deptName"
                  placeholder="请输入类型名称"
                  clearable
                  prefix-icon="Search"
                  style="margin-bottom: 20px"
               />
            </div>
            <div class="head-container">
               <el-tree
                  :data="deptOptions"
                  :props="{ label: 'label', children: 'children' }"
                  :expand-on-click-node="false"
                  :filter-node-method="filterNode"
                  ref="deptTreeRef"
                  node-key="id"
                  highlight-current
                  @node-click="handleNodeClick"
               />
                  <!-- default-expand-all -->
            </div>
         </el-col>

        <!--任务数据-->
        <el-col :span="20" :xs="24">
           <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch" label-width="100px">
              <el-form-item label="任务名称" prop="jobName">
                 <el-input v-model="queryParams.jobName" placeholder="请输入任务名称" clearable style="width: 240px" @keyup.enter="handleQuery" />
              </el-form-item>
              <el-form-item label="任务名称" prop="jobName">
                 <el-input v-model="queryParams['condition[jobName|like]']" placeholder="请输入任务名称" clearable style="width: 240px" @keyup.enter="handleQuery" />
              </el-form-item>
              <el-form-item>
                 <el-button type="primary" icon="Search" @click="handleQuery">搜索</el-button>
                 <el-button icon="Refresh" @click="resetQuery">重置</el-button>
              </el-form-item>
           </el-form>

           <el-row :gutter="10" class="mb8">

              <el-col :span="1.5">
                 <el-button type="primary" plain icon="Plus" @click="handleAdd">新增</el-button>
              </el-col>
              <el-col :span="1.5">
                 <el-button type="success" plain icon="Edit" :disabled="single" @click="handleUpdate">修改</el-button>
              </el-col>
              <el-col :span="1.5">
                 <el-button type="danger" plain icon="Delete" :disabled="multiple" @click="handleDelete">删除</el-button>
              </el-col>

              <right-toolbar v-model:showSearch="showSearch" @queryTable="getList" :columns="columns"></right-toolbar>
           </el-row>

           <el-table v-loading="loading" :data="JobList" @selection-change="handleSelectionChange">
              <el-table-column type="selection" width="50" align="center" />
              
              <el-table-column label="图标" align="center" width="60" key="icon">
                 <!-- <template #default="scope">
                    <span style="font-size:25px;color:#3b5998">
                       <i class="fa-solid fa-file-word"></i>   
                    </span>
                 </template> -->
                  <template #default="scope">
                     <div>
                        <img style="width:40px; height:40px" :src="'http://data.linesno.com/icons/database/' + (scope.row.targetDbType).toLowerCase() + '.png'" />
                     </div>
                  </template>
              </el-table-column>


              <!-- 业务字段-->
              <el-table-column label="任务名称" align="left" key="jobName" prop="jobName" v-if="columns[0].visible">
                 <template #default="scope">
                     <div>
                        {{ scope.row.jobName }}
                     </div>
                     <div style="font-size: 13px;color: #a5a5a5;cursor: pointer;" class="text-overflow" v-copyText="scope.row.promptId">
                        {{ scope.row.jobDesc }}
                     </div>
                  </template>
              </el-table-column>
              <el-table-column label="迁移数据量" align="center" key="jobDesc" prop="jobDesc" v-if="columns[1].visible">
                 <template #default="scope">
                     <div style="margin-top: 5px;">
                        <el-button type="primary" text> <i class="fa-solid fa-truck-fast" style="margin-right:5px;"></i> 读: 74299422 条 </el-button>
                     <!-- </div>
                     <div style="margin-top: 5px;"> -->
                        <el-button type="danger" text> <i class="fa-solid fa-feather" style="margin-right:5px"></i> 写: 8742355 条</el-button>
                     </div>
                  </template>
              </el-table-column>
              <el-table-column label="迁移目标" align="center" key="projectCode" prop="projectCode" v-if="columns[2].visible">
                 <template #default="scope">
                     <div style="margin-top: 5px;">
                        <el-button type="primary" text>
                           <img style="margin-right:5px;width:20px; height:20px" :src="'http://data.linesno.com/icons/database/' + (scope.row.sourceDbType).toLowerCase() + '.png'" />  源库: {{ scope.row.sourceDbType }}
                        </el-button>
                     <!-- </div>
                     <div style="margin-top: 5px;"> -->
                        <el-button type="primary" text>
                           <img style="margin-right:5px;width:20px; height:20px" :src="'http://data.linesno.com/icons/database/' + (scope.row.targetDbType).toLowerCase() + '.png'" />  目标: {{ scope.row.targetDbType }}
                        </el-button>
                     </div>
                  </template>
              </el-table-column>

              <el-table-column label="迁移配置" align="center" width="150" key="documentType" prop="documentType" v-if="columns[1].visible" :show-overflow-tooltip="true" >
                 <template #default="scope">
                    <el-button type="primary" bg text @click="handleConfigType(scope.row.id , scope.row.documentType)"> 
                        <i class="fa-solid fa-screwdriver-wrench"></i>&nbsp;迁移参数
                     </el-button>
                 </template>
              </el-table-column>

              <el-table-column label="开启" align="center" width="80" key="hasStatus" prop="hasStatus" v-if="columns[1].visible" :show-overflow-tooltip="true" >
                 <template #default="scope">
                    <el-switch
                       v-model="scope.row.hasStatus"
                       :active-value="0"
                       :inactive-value="1"
                       @change="handleChangStatusField('hasStatus' , scope.row.hasStatus, scope.row.id)"
                    />
                 </template>
              </el-table-column>

              <el-table-column label="执行周期" align="left" prop="addTime" v-if="columns[6].visible" width="200">
                 <template #default="scope">
                     <div>
                        <el-tooltip :content="parseCronExpression(scope.row.jobCron)" placement="top" v-if="scope.row.JobId !== 1">
                         CRON:  {{ scope.row.jobCron }}
                        </el-tooltip>
                     </div>
                     <div style="font-size: 13px;color: #a5a5a5;cursor: pointer;" v-copyText="scope.row.promptId">
                        上次: {{ parseTime(scope.row.addTime) }}
                     </div>
                 </template>
              </el-table-column>

              <!-- 操作字段  -->
              <el-table-column label="操作" align="center" width="200" class-name="small-padding fixed-width">
                 <template #default="scope">
                    <el-tooltip content="修改" placement="top" v-if="scope.row.JobId !== 1">
                       <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)" v-hasPermi="['system:Job:edit']">修改</el-button>
                    </el-tooltip>
                    <el-tooltip content="删除" placement="top" v-if="scope.row.JobId !== 1">
                       <el-button link type="primary" icon="Delete" @click="handleDelete(scope.row)" v-hasPermi="['system:Job:remove']">删除</el-button>
                    </el-tooltip>
                    <el-tooltip content="更多" placement="top" v-if="scope.row.JobId !== 1">
                        <el-dropdown style="margin-top:2px;" :hide-on-click="false" @command="(command) => handleCommand(command, scope.row)">
                              <el-button link type="primary" icon="DArrowRight">更多</el-button>
                              <template #dropdown>
                                 <el-dropdown-menu>
                                    <el-dropdown-item command="handleRunOneTime" icon="CaretRight" style="font-size:13px" v-hasPermi="['monitor:job:changeStatus']">执行一次</el-dropdown-item>
                                    <el-dropdown-item command="handlePauseTrigger" icon="SwitchButton" style="font-size:13px" v-hasPermi="['monitor:job:changeStatus']">暂停任务</el-dropdown-item>
                                    <el-dropdown-item command="handleResumeTrigger" icon="Open" style="font-size:13px" v-hasPermi="['monitor:job:changeStatus']">恢复任务</el-dropdown-item>
                                    <el-dropdown-item command="handleView" icon="View" style="font-size:13px"  v-hasPermi="['monitor:job:query']">任务详细</el-dropdown-item>
                                    <el-dropdown-item command="handleJobLog" icon="Operation" style="font-size:13px"  v-hasPermi="['monitor:job:query']">调度日志</el-dropdown-item>
                                 </el-dropdown-menu>
                              </template>
                        </el-dropdown>
                    </el-tooltip>
                 </template>

              </el-table-column>
           </el-table>
           <pagination v-show="total > 0" :total="total" v-model:page="queryParams.pageNum" v-model:limit="queryParams.pageSize" @pagination="getList" />
        </el-col>
     </el-row>

     <!-- 添加或修改任务配置对话框 -->
     <el-dialog :title="title" v-model="open" width="900px" append-to-body>
        <el-form :model="form" :rules="rules" ref="databaseRef" label-width="100px">
           <el-row>
              <el-col :span="24">
                 <el-form-item label="任务图标" prop="logo">
                    <!-- <el-input v-model="form.logo" placeholder="请输入任务图标" maxlength="255" /> -->

                    <el-upload action="#" list-type="picture-card" :auto-upload="false">
                          <el-icon><Plus /></el-icon>

                          <template #file="{ file }">
                             <div>
                             <img class="el-upload-list__item-thumbnail" :src="file.url" alt="" />
                             <span class="el-upload-list__item-actions">
                                <span
                                   class="el-upload-list__item-preview"
                                   @click="handlePictureCardPreview(file)"
                                >
                                   <el-icon><zoom-in /></el-icon>
                                </span>
                                <span
                                   v-if="!disabled"
                                   class="el-upload-list__item-delete"
                                   @click="handleDownload(file)"
                                >
                                   <el-icon><Download /></el-icon>
                                </span>
                                <span
                                   v-if="!disabled"
                                   class="el-upload-list__item-delete"
                                   @click="handleRemove(file)"
                                >
                                   <el-icon><Delete /></el-icon>
                                </span>
                             </span>
                             </div>
                          </template>
                       </el-upload>

                 </el-form-item>
              </el-col>
              <el-col :span="24">
                 <el-form-item label="任务名称" prop="jobName">
                    <el-input v-model="form.jobName" placeholder="请输入任务名称" maxlength="50" />
                 </el-form-item>
              </el-col>
           </el-row>
           <el-row>
              <el-col :span="24">
                 <el-form-item label="任务介绍" prop="intro">
                    <el-input v-model="form.intro" type="textarea" placeholder="请输入任务介绍" maxlength="255" />
                 </el-form-item>
              </el-col>
           </el-row>
           <el-row>
              <el-col :span="24">
                 <el-form-item label="授权地址" prop="allowUrl">
                    <el-input v-model="form.allowUrl" placeholder="请输入授权地址" maxlength="255" />
                 </el-form-item>
              </el-col>

              <el-col :span="24">
                 <el-form-item label="任务状态" prop="status">
                    <el-radio-group v-model="form.status">
                       <el-radio
                          v-for="dict in sys_normal_disable"
                          :key="dict.value"
                          :label="dict.value"
                       >{{ dict.label }}</el-radio>
                    </el-radio-group>
                 </el-form-item>
              </el-col>

              <el-col :span="24">
                 <!-- <el-form-item label="是否公开" prop="isPublic">
                    <el-input v-model="form.isPublic" placeholder="请输入是否公开" maxlength="1" />
                 </el-form-item> -->

                 <el-form-item label="是否公开" prop="isPublic">
                    <el-radio-group v-model="form.isPublic">
                       <el-radio
                          v-for="dict in sys_normal_disable"
                          :key="dict.value"
                          :label="dict.value"
                       >{{ dict.label }}</el-radio>
                    </el-radio-group>
                 </el-form-item>
              </el-col>
           </el-row>

           <el-row>
              <el-col :span="24">
                 <el-form-item label="备注" prop="description">
                    <el-input v-model="form.description"  placeholder="请输入任务备注"></el-input>
                 </el-form-item>
              </el-col>
           </el-row>
        </el-form>
        <template #footer>
           <div class="dialog-footer">
              <el-button type="primary" @click="submitForm">确 定</el-button>
              <el-button @click="cancel">取 消</el-button>
           </div>
        </template>
     </el-dialog>


     <!-- 文档列表 -->
     <el-dialog :title="title" v-model="openDocumentTypeDialog" width="1024px" append-to-body>

        <TypeList />

        <template #footer>
           <div class="dialog-footer">
              <el-button type="primary" @click="submitDocumentTypeForm">确 定</el-button>
              <el-button @click="openDocumentTypeDialog = false">取 消</el-button>
           </div>
        </template>
     </el-dialog>

  </div>
</template>

<script setup name="Job">

import {
  listJob,
  delJob,
  getJob,
  updateJob,
  addJob,
  runOneTime , 
  resumeTrigger,
  catalogTreeSelect,
  pauseTrigger,
  changStatusField
} from "@/api/data/pipeline/job";

// import TypeList from './channelList.vue'

const router = useRouter();
const { proxy } = getCurrentInstance();

// 定义变量
const JobList = ref([]);
const open = ref(false);
const loading = ref(true);
const showSearch = ref(true);
const ids = ref([]);
const single = ref(true);
const multiple = ref(true);
const total = ref(0);
const title = ref("");
const dateRange = ref([]);
const openCron = ref(false);
const expression = ref("");
const deptOptions = ref(undefined);

// 是否打开配置文档
const openDocumentTypeDialog = ref(false);

// 列显隐信息
const columns = ref([
  { key: 0, label: `任务名称`, visible: true },
  { key: 1, label: `任务描述`, visible: true },
  { key: 2, label: `授权地址`, visible: true },
  { key: 3, label: `类型`, visible: true },
  { key: 4, label: `是否公开`, visible: true },
  { key: 5, label: `状态`, visible: true },
  { key: 6, label: `添加时间`, visible: true }
]);

const data = reactive({
  form: {},
  queryParams: {
     pageNum: 1,
     pageSize: 10,
     jobName: undefined,
     dbDesc: undefined
  },
  rules: {
     jobName: [{ required: true, message: "名称不能为空", trigger: "blur" }] , 
     jdbcUrl: [{ required: true, message: "连接不能为空", trigger: "blur" }],
     dbType: [{ required: true, message: "类型不能为空", trigger: "blur" }] , 
     dbUsername: [{ required: true , message: "用户名不能为空", trigger: "blur"}],
     dbPasswd: [{ required: true, message: "密码不能为空", trigger: "blur" }] , 
     dbDesc: [{ required: true, message: "备注不能为空", trigger: "blur" }] 
  }
});

const { queryParams, form, rules } = toRefs(data);

/** 查询任务列表 */
function getList() {
  loading.value = true;
  listJob(proxy.addDateRange(queryParams.value, dateRange.value)).then(res => {
     loading.value = false;
     JobList.value = res.rows;
     total.value = res.total;
  });
};

/** 搜索按钮操作 */
function handleQuery() {
  queryParams.value.pageNum = 1;
  getList();
};

/** 查询类型下拉树结构 */
function getDeptTree() {
  catalogTreeSelect().then(response => {
    deptOptions.value = response.data;
  });
};

/** 重置按钮操作 */
function resetQuery() {
  dateRange.value = [];
  proxy.resetForm("queryRef");
  queryParams.value.deptId = undefined;
  proxy.$refs.deptTreeRef.setCurrentKey(null);
  handleQuery();
};
/** 删除按钮操作 */
function handleDelete(row) {
  const JobIds = row.id || ids.value;
  proxy.$modal.confirm('是否确认删除任务编号为"' + JobIds + '"的数据项？').then(function () {
     return delJob(JobIds);
  }).then(() => {
     getList();
     proxy.$modal.msgSuccess("删除成功");
  }).catch(() => { });
};

/** 选择条数  */
function handleSelectionChange(selection) {
  ids.value = selection.map(item => item.id);
  single.value = selection.length != 1;
  multiple.value = !selection.length;
};

/** 重置操作表单 */
function reset() {
  form.value = {
     id: undefined,
     deptId: undefined,
     JobName: undefined,
     nickName: undefined,
     password: undefined,
     phonenumber: undefined,
     status: "0",
     remark: undefined,
  };
  proxy.resetForm("databaseRef");
};
/** 取消按钮 */
function cancel() {
  open.value = false;
  reset();
};

/** 新增按钮操作 */
function handleAdd() {
  reset();
//   open.value = true;
//   title.value = "添加任务";
  let path = '/task/data/pipeline/task/create' ;
  router.push({ path: path });
};

/** 修改按钮操作 */
function handleUpdate(row) {
  reset();
  const JobId = row.id || ids.value;
  getJob(JobId).then(response => {
     form.value = response.data;
     open.value = true;
     title.value = "修改任务";
  });
};

/** 查看项目告警空间 */
function handleJobSpace(id){
  let path =  '/project/space/'
  router.push({ path: path + id });
}

/** 提交按钮 */
function submitForm() {
  proxy.$refs["databaseRef"].validate(valid => {
     if (valid) {
        if (form.value.JobId != undefined) {
           updateJob(form.value).then(response => {
              proxy.$modal.msgSuccess("修改成功");
              open.value = false;
              getList();
           });
        } else {
           addJob(form.value).then(response => {
              proxy.$modal.msgSuccess("新增成功");
              open.value = false;
              getList();
           });
        }
     }
  });
};

/** 配置文档类型 */
function handleConfigType(id , documentType){
   let path = '/task/data/pipeline/task/createDatasource' ;
   router.push({ path: path });
}

/** 修改状态 */
const handleChangStatusField = async(field , value , id) => {
   // 判断tags值 这样就不会进页面时调用了
     const res = await changStatusField({
        field: field,
        value: value?1:0,
        id: id
     }).catch(() => { })
     if (res && res.code == 200) {
        // 刷新表格
        getList()
     }
}

 /* 立即执行一次 */
function handleRunOneTime(row) {
   console.log('row = ' + row);
   // proxy.$modal.confirm('确认要立即执行一次"' + row.jobName + '"任务吗？').then(function() {
   //    return runOneTime(row.id).then(res => {
   //       console.info("res = " + res);
   //    });
   // }).then(() => {
   //    proxy.$modal.msgSuccess("执行成功");
   // }).catch(() => {});
   runOneTime(row.id).then(res => {
      console.info("res = " + res);
      proxy.$modal.msgSuccess("执行成功");
   });
} 

/** CRON表达式解析 */
function parseCronExpression(cronExpression) {
  const parts = cronExpression.split(' ');
  const [second, minute, hour, dayOfMonth, month, dayOfWeek] = parts;

  let description = '';

  // 解析秒
  if (second !== '*' && parseInt(second) === 0) {
    description += '每分钟开始';
  }

  // 解析分钟
  if (minute !== '*') {
    if (parseInt(minute) === 0) {
      description += '每小时开始';
    } else {
      description += `每小时的第${minute}分钟`;
    }
  }

  // 解析小时
  if (hour !== '*') {
    const hours = [
      '午夜', '凌晨1点', '凌晨2点', '凌晨3点', '凌晨4点', '凌晨5点', '凌晨6点', '凌晨7点', '凌晨8点', '凌晨9点', '凌晨10点',
      '凌晨11点', '中午', '下午1点', '下午2点', '下午3点', '下午4点', '下午5点', '下午6点', '下午7点', '下午8点',
      '下午9点', '下午10点', '下午11点'
    ];
    description += hours[parseInt(hour)] || `下午${parseInt(hour)}点`;
  }

  // 解析每月的哪一天
  if (dayOfMonth !== '*') {
    description += `每月的第${dayOfMonth}天`;
  }

  // 解析月份
  if (month !== '*') {
    const months = [
      '1月', '2月', '3月', '4月', '5月', '6月',
      '7月', '8月', '9月', '10月', '11月', '12月'
    ];
    description += months[parseInt(month) - 1] || `第${parseInt(month)}个月`;
  }

  // 解析每周的哪一天
  if (dayOfWeek !== '?' && dayOfWeek !== '*') {
    const daysOfWeek = [
      '星期日', '星期一', '星期二', '星期三', '星期四', '星期五', '星期六'
    ];
    description += `在${daysOfWeek[parseInt(dayOfWeek)]}`;
  }

  return description;
}

/** 任务详细信息 */
function handleView(row) {
   getJob(row.id).then(response => {
      // this.form = response.data;
      // this.openView = true;
      console.log(response)
   });
} 

/** 暂停按钮操作 */
function handleResumeTrigger(row){
   resumeTrigger(row.id).then(response => {
      proxy.$modal.msgSuccess("执行成功");
   });
}

/** 恢复按钮操作 */
function handlePauseTrigger(row){
   pauseTrigger(row.id).then(response => {
      proxy.$modal.msgSuccess("执行成功");
   });
}


/** cron表达式按钮操作 */
function handleShowCron() {
   expression.value = this.form.cronExpression;
   openCron.value = true;
}

/** 任务日志列表查询 */
function handleJobLog(row) {
   const jobId = row.id || 0;
   router.push('/monitor/job-log/index/' + jobId)
}

/** 提交配置文档类型 */
function submitDocumentTypeForm(){
  // TODO 待保存任务文档类型
}

/** 下拉事件 */
function handleCommand(command,row){
   switch (command) {
      case "handleRunOneTime":
         handleRunOneTime(row);
         break;
      case "handlePauseTrigger":
         handlePauseTrigger(row);
         break;
      case "handleResumeTrigger":
         handleResumeTrigger(row);
         break;
      case "handleView":
         handleView(row);
         break;
      case "handleJobLog":
         handleJobLog(row);
         break;
      default:
         break;
   }
}

getDeptTree();
getList();

</script>
