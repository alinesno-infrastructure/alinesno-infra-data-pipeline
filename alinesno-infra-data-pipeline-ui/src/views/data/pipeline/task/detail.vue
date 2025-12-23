<template>
  <el-card>
    <div style="margin-top: 15px">
      <commonInfo :infoform="infoform"></commonInfo>
      <el-button
        type="primary"
        icon="el-icon-arrow-left"
        @click="handleGoBack"
        style="margin: 12px 0px 20px; float: right"
      >
        返回
      </el-button>
    </div>
  </el-card>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import commonInfo from './common/info'
// 导入抽取的任务API
import { getAssignmentDetail } from '@/api/data/pipeline/task'

// 路由实例
const route = useRoute()
const router = useRouter()

// 响应式表单数据
const infoform = reactive({
  id: 0,
  name: "",
  description: "",
  scheduleMode: "MANUAL",
  cronExpression: "",
  sourceConnectionId: 0,
  sourceTypeName: 'MySQL',
  sourceSchema: "",
  runStatus: "",
  tableType: "TABLE",
  includeOrExclude: "",
  sourceTables: [],
  tableNameMapper: [],
  columnNameMapper: [],
  tableNameCase: 'NONE',
  columnNameCase: 'NONE',
  targetConnectionId: 0,
  targetTypeName: 'MySQL',
  targetDropTable: true,
  targetOnlyCreate: false,
  autoSyncMode: 2,
  targetSchema: "",
  batchSize: 5000,
  channelSize: 100,
  targetSyncOption: 'INSERT_UPDATE_DELETE',
  beforeSqlScripts: '',
  afterSqlScripts: '',
  // 补充原代码中赋值的额外字段
  sourceConnectionName: '',
  incrTableColumns: [],
  sourceBeforeSqlScripts: '',
  sourceAfterSqlScripts: '',
  targetConnectionName: '',
  targetAutoIncrement: false,
  targetBeforeSqlScripts: '',
  targetAfterSqlScripts: ''
})

// 加载任务详情
const loadAssignmentDetail = async () => {
  // 校验任务ID是否存在
  if (!route.query.id) {
    ElMessage.warning('任务ID不能为空')
    return
  }

  try {
    // 使用抽取的API方法获取任务详情
    const res = await getAssignmentDetail(route.query.id)
    if (res.code === 200) {
      const detail = res.data
      let varAutoSyncMode = 2
      // 计算自动同步模式
      if (detail.configuration.targetDropTable && detail.configuration.targetOnlyCreate) {
        varAutoSyncMode = 1
      } else if (!detail.configuration.targetDropTable && !detail.configuration.targetOnlyCreate) {
        varAutoSyncMode = 0
      } else {
        varAutoSyncMode = 2
      }
      // 赋值到响应式数据
      Object.assign(infoform, {
        id: detail.id,
        name: detail.name,
        description: detail.description,
        scheduleMode: detail.scheduleMode,
        cronExpression: detail.cronExpression,
        sourceConnectionId: detail.configuration.sourceConnectionId,
        sourceConnectionName: detail.configuration.sourceConnectionName,
        sourceTypeName: detail.configuration.sourceTypeName,
        sourceSchema: detail.configuration.sourceSchema,
        tableType: detail.configuration.tableType,
        includeOrExclude: detail.configuration.includeOrExclude,
        sourceTables: detail.configuration.sourceTables,
        incrTableColumns: detail.configuration.incrTableColumns,
        sourceBeforeSqlScripts: detail.configuration.sourceBeforeSqlScripts,
        sourceAfterSqlScripts: detail.configuration.sourceAfterSqlScripts,
        tableNameMapper: detail.configuration.tableNameMapper,
        columnNameMapper: detail.configuration.columnNameMapper,
        tableNameCase: detail.configuration.tableNameCase,
        columnNameCase: detail.configuration.columnNameCase,
        targetConnectionId: detail.configuration.targetConnectionId,
        targetConnectionName: detail.configuration.targetConnectionName,
        targetTypeName: detail.configuration.targetTypeName,
        targetDropTable: detail.configuration.targetDropTable,
        targetOnlyCreate: detail.configuration.targetOnlyCreate,
        targetAutoIncrement: detail.configuration.targetAutoIncrement,
        autoSyncMode: varAutoSyncMode,
        targetSchema: detail.configuration.targetSchema,
        batchSize: detail.configuration.batchSize,
        channelSize: detail.configuration.channelSize,
        targetSyncOption: detail.configuration.targetSyncOption,
        targetBeforeSqlScripts: detail.configuration.targetBeforeSqlScripts,
        targetAfterSqlScripts: detail.configuration.targetAfterSqlScripts
      })
    } else {
      if (res.data.message) {
        ElMessage.error(`查询任务失败: ${res.data.message}`)
      }
    }
  } catch (error) {
    console.error('加载任务详情失败:', error)
    ElMessage.error('查询任务失败，请稍后重试')
  }
}

// 返回上一页
const handleGoBack = () => {
  router.go(-1)
}

// 生命周期：挂载时加载数据
onMounted(() => {
  loadAssignmentDetail()
})
</script>

<style scoped>
.el-card {
  width: 100%;
  height: 100%;
  overflow: auto;
}
</style>