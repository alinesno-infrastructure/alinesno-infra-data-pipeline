<template>

    <el-form ref="ruleFormRef" :model="form" :rules="rules" label-width="120px" label-position="top">

        <!-- 七牛参数配置 -->
        <div v-if="props.readerSource.readerType === 'qiniu'">
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
        <div v-if="props.readerSource.readerType === 'kafka'">
            <el-row>
                <el-col :span="24">
                    <el-form-item label="消费组" prop="kafka.consumerGroup">
                        <el-input placeholder="请输入消费者组ID" v-model="form.kafka.consumerGroup" />
                    </el-form-item>
                </el-col>
                <el-col :span="24">
                    <el-form-item label="Key序列化" prop="kafka.keySerializer">
                        <el-input placeholder="请输入Key的反序列化器类名，如：org.apache.kafka.common.serialization.StringDeserializer" v-model="form.kafka.keySerializer" />
                    </el-form-item>
                </el-col>
                <el-col :span="24">
                    <el-form-item label="Value序列化" prop="kafka.valueSerializer">
                        <el-input placeholder="请输入Value的反序列化器类名，如：org.apache.kafka.common.serialization.StringDeserializer" v-model="form.kafka.valueSerializer" />
                    </el-form-item>
                </el-col>
                <el-col :span="24">
                    <el-form-item label="自动提交" prop="kafka.autoCommitInterval">
                        <el-input placeholder="请输入自动提交间隔，单位毫秒" v-model="form.kafka.autoCommitInterval" />
                    </el-form-item>
                </el-col>
                <el-col :span="24">
                    <el-form-item label="偏移量策略" prop="kafka.offsetResetStrategy">
                        <el-radio-group v-model="form.kafka.offsetResetStrategy">
                            <el-radio :label="earliest">earliest</el-radio>
                            <el-radio :label="latest">latest</el-radio>
                        </el-radio-group>
                    </el-form-item>
                </el-col> 
            </el-row>
        </div>

        <!-- 数据库查询处理 -->
        <div v-if="props.readerSource.readerType === 'mysql' || 
                props.readerSource.readerType === 'postgresql' ||
                props.readerSource.readerType === 'sqlserver' ||
                props.readerSource.readerType === 'oracle'">

            <el-row>
                <el-col :span="24">
                    <el-form-item label="采集SQL语句" prop="database.querySql">
                        <el-input type="textarea" 
                            :rows="5" 
                            resize="none"
                            placeholder="请输入SQL语句" v-model="form.database.querySql" />
                    </el-form-item>
                </el-col>
                <el-col :span="24">
                    <el-form-item label="存量数据读取" prop="database.readExistingData">
                        <el-switch v-model="form.database.readExistingData" />
                    </el-form-item>
                </el-col>
            </el-row>
        </div>

    </el-form>
</template>

<script setup name="JobReaderParams">
// import { reactive, ref, toRefs } from 'vue';

const props = defineProps({
    readerSource: {
        type: Object,
        default: () => ({ readerType: null })
    }
});

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
      querySql: '',
      readExistingData: false
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
});

const { form, rules } = toRefs(data);
const earliest = 'earliest';
const latest = 'latest';

const ruleFormRef = ref(null);

const submitSourceParam = () => {

    const filteredFormData = filterEmptyValues(form.value);
    console.log('Filtered Form data:', filteredFormData);

    return filteredFormData ;
};

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
};

// 主动暴露方法
defineExpose({ submitSourceParam })

</script>

<style scoped lang="scss">
</style>