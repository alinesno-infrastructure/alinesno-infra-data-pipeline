<template>

    <el-form ref="ruleFormRef" :model="form" :rules="rules" label-width="100px" label-position="top">

        <!-- 七牛参数配置 -->
        <div v-if="props.sinkSource.readerType === 'qiniu'">
            <el-row>
                <el-col :span="24">
                    <el-form-item label="写入Bucket" prop="qiniu.bucketName">
                        <el-input placeholder="请输入存入的Bucket名称" v-model="form.qiniu.bucketName" />
                    </el-form-item>
                </el-col>
                <el-col :span="24">
                    <el-form-item label="写入目录" prop="qiniu.writeDirectory">
                        <el-input placeholder="请输入存入的目录格式" v-model="form.qiniu.writeDirectory" />
                    </el-form-item>
                </el-col>
                <el-col :span="24">
                    <el-form-item label="文件名格式" prop="qiniu.fileNameFormat">
                        <el-input placeholder="请输入存入的文件名格式:如${date}.zip" v-model="form.qiniu.fileNameFormat" />
                    </el-form-item>
                </el-col>
            </el-row>
        </div>

        <!-- Kafka参数配置 -->
        <div v-if="props.sinkSource.readerType === 'kafka'">
            <el-row>
               <el-col :span="24">
                    <el-form-item label="写入Topic">
                        <el-input placeholder="请输入写入Kafka的Topic" />
                    </el-form-item>
                </el-col>
                <el-col :span="24">
                    <el-form-item label="Partition">
                        <el-input placeholder="请输入写入的Partition" />
                    </el-form-item>
                </el-col>
                <el-col :span="24">
                    <el-form-item label="Key序列化">
                        <el-input placeholder="请输入Key的序列化器类名，如：org.apache.kafka.common.serialization.StringSerializer" />
                    </el-form-item>
                </el-col>
                <el-col :span="24">
                    <el-form-item label="Value序列化">
                        <el-input placeholder="请输入Value的序列化器类名，如：org.apache.kafka.common.serialization.StringSerializer" />
                    </el-form-item>
                </el-col>
                <el-col :span="24">
                    <el-form-item label="确认机制">
                        <el-input placeholder="请输入确认机制的值，如：1 或者 all" />
                    </el-form-item>
                </el-col>
                <el-col :span="24">
                    <el-form-item label="请求超时">
                        <el-input placeholder="请输入请求超时时间，单位毫秒" />
                    </el-form-item>
                </el-col>
                <el-col :span="24">
                    <el-form-item label="批量发送大小">
                        <el-input placeholder="请输入批量发送大小，单位字节" />
                    </el-form-item>
                </el-col>
                <el-col :span="24">
                    <el-form-item label="等待时长">
                        <el-input placeholder="请输入等待时间，单位毫秒" />
                    </el-form-item>
                </el-col> 
            </el-row>
        </div>

        <!-- 数据库查询处理 -->
        <div v-if="props.sinkSource.readerType === 'mysql' || 
                props.sinkSource.readerType === 'postgresql' ||
                props.sinkSource.readerType === 'sqlserver' ||
                props.sinkSource.readerType === 'oracle'">

            <el-row>
                <el-col :span="24">
                  <el-form-item label="目标表列表">
                    <DsFetchTableData ref="fetchTableDataRef" style="width:100%" />
                  </el-form-item>
                </el-col>
                <el-col :span="24">
                  <el-form-item label="新建表" prop="database.isGenTable">
                      <el-switch v-model="form.database.isGenTable" />
                  </el-form-item>
                </el-col>
                <el-col :span="24">
                  <el-form-item label="新表名" prop="database.newTableName">
                      <el-input v-model="form.database.newTableName" placeholder="请输入新建表名" />
                  </el-form-item>
                </el-col>
            </el-row>
        </div>
        
    </el-form>

</template>

<script setup name="JobSinkParams">

const props = defineProps({
    sinkSource:{
        type: Number 
    }
})

import { 
   fetchTableData,
} from '@/api/data/pipeline/readerSource';

import DsFetchTableData from './dsFetchTableData.vue';

const { proxy } = getCurrentInstance();

const data = reactive({
  form: {
    qiniu: {
      bucketName: '',
      writeDirectory: '',
      fileNameFormat: ''
    },
    kafka: {
      consumerGroup: '',
      keySerializer: '',
      valueSerializer: '',
      autoCommitInterval: '',
      offsetResetStrategy: ''
    },
    database: {
      targetTableName: '',
      isGenTable: false,
      newTableName: ''
    }
  },
  queryParams: {
     pageNum: 1,
     pageSize: 10,
     jobName: undefined,
     dbDesc: undefined
  },
  rules: {
     'qiniu.bucketName': [{ required: true, message: "Bucket名称不能为空", trigger: "blur" }],
     'qiniu.writeDirectory': [{ required: true, message: "写入目录不能为空", trigger: "blur" }],
     'qiniu.fileNameFormat': [{ required: true, message: "文件名格式不能为空", trigger: "blur" }],

     'kafka.consumerGroup': [{ required: true, message: "消费组不能为空", trigger: "blur" }],
     'kafka.keySerializer': [{ required: true, message: "Key序列化不能为空", trigger: "blur" }],
     'kafka.valueSerializer': [{ required: true, message: "Value序列化不能为空", trigger: "blur" }],
     'kafka.autoCommitInterval': [{ required: true, message: "自动提交间隔不能为空", trigger: "blur" }],
     'kafka.offsetResetStrategy': [{ required: true, message: "偏移量策略不能为空", trigger: "blur" }],

     'database.querySql': [{ required: true, message: "采集SQL语句不能为空", trigger: "blur" }],
     'database.readExistingData': [{ required: false, message: "存量数据读取不能为空", trigger: "change" }]
  }
})

const { form, rules } = toRefs(data);
const earliest = 'earliest';
const latest = 'latest';

const currentSourceId = ref(null) ;
const fetchDataRef = ref(null);
const fetchTableDataRef = ref(null);
const ruleFormRef = ref(null);
const validateFetchData = ref(false) // 是否验证执行SQL
const sinkFieldMate = ref([]) // 读取源字段

const submitSinkParam = () => {

    // if(!validateFetchData.value){
    //     proxy.$modal.msgError("请点击执行确认数据源是否正常.");
    //     return ;
    // }

    let tableName = fetchTableDataRef.value.handleSubmitCurrentRow();
    console.log('tableName = ' + tableName);

    // 设置读取字段信息
    form.value.database.targetTableName = tableName
    let filteredFormData = filterEmptyValues(form.value)
    console.log('--->>> Filtered Form data:', filteredFormData)

    return filteredFormData ;
}

// 辅助函数，用于过滤掉没有值的表单项
const filterEmptyValues = (formData) => {
    const result = {};
    for (const key in formData) {
        if (formData.hasOwnProperty(key)) {
            const value = formData[key];
            if (typeof value === 'object' && value !== null) {
                // 如果是对象，则递归过滤
                result[key] = filterEmptyValues(value);
            } else if (value) {
                // 只保留非空值
                result[key] = value;
            }
        }
    }
    return result;
}

// 验证执行SQL
const handleFetchTableData = (sourceId) => {
    console.log('props.sinkSource = ' + sourceId);

    // currentSourceId.value = sourceId 

    fetchTableData(sourceId).then(response => {
        fetchTableDataRef.value.fillData(response.data) 
    })
}

// 主动暴露方法
defineExpose({ submitSinkParam , handleFetchTableData })

</script>

<style scoped lang="scss">
</style>
