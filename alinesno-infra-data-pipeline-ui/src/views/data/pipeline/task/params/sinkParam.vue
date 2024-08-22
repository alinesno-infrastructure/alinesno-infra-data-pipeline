<template>

    <el-form ref="ruleFormRef" label-width="100px">

        <!-- 七牛参数配置 -->
        <div v-if="props.sinkSource.readerType === 'qiniu'">
            <el-row>
                <el-col :span="24">
                    <el-form-item label="写入Bucket">
                        <el-input placeholder="请输入存入的Bucket名称" />
                    </el-form-item>
                </el-col>
                <el-col :span="24">
                    <el-form-item label="写入目录">
                        <el-input placeholder="请输入存入的目录格式" />
                    </el-form-item>
                </el-col>
                <el-col :span="24">
                    <el-form-item label="文件名格式">
                        <el-input placeholder="请输入存入的文件名格式:如${date}.zip" />
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
                    <el-form-item label="Kafka参数">
                        <el-input type="textarea" :rows="4" resize="none" placeholder="写入的KafkaConfig参数,使用key=value处理" />
                    </el-form-item>
                </el-col>
            </el-row>
        </div>
        
    </el-form>

</template>

<script setup name="JobSinkParams">

const props = defineProps({
    sinkSource:{
        type: Object,
        default: {
            readerType: null 
        }
    }
})
</script>

<style scoped lang="scss">
</style>


