<template>
  <div class="app-container">
    <!-- 查询条件 -->
    <el-form
      :model="queryParams"
      ref="queryRef"
      :inline="true"
      v-show="showSearch"
      label-width="108px"
    >
      <el-form-item label="数据源名称" prop="name">
        <el-input
          v-model="queryParams.name"
          placeholder="请输入数据源名称"
          clearable
          style="width: 240px"
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="数据库类型" prop="type">
        <el-select
          v-model="queryParams.type"
          placeholder="请选择数据库类型"
          clearable
          style="width: 240px"
        >
          <el-option
            v-for="item in databaseTypeOptions"
            :key="item.value"
            :label="item.label"
            :value="item.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="Search" @click="handleQuery">搜索</el-button>
        <el-button icon="Refresh" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <!-- 操作按钮 -->
    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="Plus"
          @click="selectDataSource"
          v-hasPermi="['admin:connection:add']"
        >创建</el-button>
      </el-col>
      <right-toolbar v-model:showSearch="showSearch" @queryTable="getList" />
    </el-row>

    <!-- 数据表格 -->
    <el-table
      ref="tableRef"
      v-loading="loading"
      :data="dataSourceList"
      style="width: 100%"
    >
      <el-table-column type="index" label="序号" width="55" align="center" />
      <el-table-column
        label="数据源名称"
        align="center"
        prop="name"
        :show-overflow-tooltip="true"
      />
      <el-table-column
        label="数据库类型"
        align="center"
        prop="type"
        :show-overflow-tooltip="true"
      />
      <el-table-column
        label="驱动版本"
        align="center"
        prop="version"
        :show-overflow-tooltip="true"
      />
      <el-table-column label="用户名" align="center" prop="username" :show-overflow-tooltip="true" />
      <el-table-column label="创建时间" align="center" prop="createTime" width="180" />
      <el-table-column label="操作" align="center" width="300" class-name="small-padding fixed-width">
        <template #default="scope">
          <el-button
            type="text"
            icon="Position"
            @click="handleTest(scope.row)"
            v-hasPermi="['admin:connection:test']"
          >测试</el-button>
          <el-button
            type="text"
            icon="Edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['admin:connection:edit']"
          >编辑</el-button>
          <el-button
            type="text"
            icon="View"
            @click="handleDetail(scope.row)"
            v-hasPermi="['admin:connection:query']"
          >详情</el-button>
          <el-button
            type="text"
            icon="Delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['admin:connection:remove']"
          >删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 分页 -->
    <pagination
      v-show="total > 0"
      :total="total"
      v-model:page="queryParams.pageNum"
      v-model:limit="queryParams.pageSize"
      @pagination="getList"
    />
  </div>
</template>

<script setup name="ConnectionList">
import { listDataSource, deleteDataSource, testDataSource } from '@/api/data/pipeline/dataSource'
import { getDatabaseTypes } from '@/api/data/pipeline/dataSource'

const { proxy } = getCurrentInstance()

// 全局状态
const dataSourceList = ref([])
const loading = ref(true)
const showSearch = ref(true)
const total = ref(0)
const databaseTypeOptions = ref([])

// 查询参数
const data = reactive({
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    name: undefined,
    type: undefined
  }
})

const { queryParams } = toRefs(data)

// 加载数据库类型
function loadDatabaseTypes() {
  getDatabaseTypes().then(response => {
    if (response?.code === 200) {
      databaseTypeOptions.value = response.data.map(item => ({
        value: item,
        label: item
      }))
    }
  })
}

// 查询列表
function getList() {
  loading.value = true
  listDataSource(queryParams.value).then(response => {
    if (response?.code === 200) {
      const pagination = response.pagination
      dataSourceList.value = response.data || []
      total.value = pagination.total
    }
    loading.value = false
  }).catch(() => {
    loading.value = false
  })
}

// 搜索
function handleQuery() {
  queryParams.value.pageNum = 1
  getList()
}

// 重置
function resetQuery() {
  proxy.resetForm('queryRef')
  queryParams.value.pageNum = 1
  handleQuery()
}

// 测试连接
function handleTest(row) {
  testDataSource(row.id).then(response => {
    if (response?.code === 200) {
      proxy.$modal.msgSuccess('测试连接成功！')
    } else {
      proxy.$modal.msgError('测试失败：' + (response?.message || ''))
    }
  })
}

// 删除
function handleDelete(row) {
  proxy.$modal.confirm(`是否确认删除数据源 "${row.name}"？`).then(() => {
    return deleteDataSource(row.id)
  }).then(() => {
    getList()
    proxy.$modal.msgSuccess('删除成功')
  }).catch(() => {})
}

// 跳转
const router = useRouter()
function selectDataSource() {
  router.push('/data/pipeline/source/select')
}
function handleUpdate(row) {
  router.push({ path: '/data/pipeline/source/update', query: { id: row.id } })
}
function handleDetail(row) {
  router.push({ path: '/data/pipeline/source/detail', query: { id: row.id } })
}

// 初始化
onMounted(() => {
  loadDatabaseTypes()
  getList()
})
</script>

<style scoped>
/* 若依标准样式已由全局 CSS 覆盖，此处无需额外样式 */
</style>