<template>
  <div class="app-container">
    <div class="sys-log-page">
      <el-table 
        v-loading="loading"
        :header-cell-style="{ background: '#eef1f6', color: '#606266' }"
        loading-text="拼命加载中"
        loading-spinner="el-icon-loading"
        loading-background="rgba(0, 0, 0, 0.8)"
        :data="lists"
        stripe
      >
        <!-- 展开列 -->
        <el-table-column type="expand">
          <template v-slot="props">
            <el-form 
              label-position="left"
              inline
              class="demo-table-expand"
            >
              <el-form-item label="日志编号:">
                <span>{{ props.row.id }}</span>
              </el-form-item>
              <el-form-item label="日志时间:">
                <span>{{ props.row.createTime }}</span>
              </el-form-item>
              <el-form-item label="操作用户:">
                <span>{{ props.row.username }}</span>
              </el-form-item>
              <el-form-item label="请求IP地址:">
                <span>{{ props.row.ipAddress }}</span>
              </el-form-item>
              <el-form-item label="操作模块:">
                <span>{{ props.row.moduleName }}</span>
              </el-form-item>
              <el-form-item label="操作描述:">
                <span>{{ props.row.content }}</span>
              </el-form-item>
              <el-form-item label="处理耗时(ms):">
                <span>{{ props.row.elapseSeconds }}</span>
              </el-form-item>
              <el-form-item label="请求路径:">
                <span>{{ props.row.urlPath }}</span>
              </el-form-item>
              <el-form-item label="异常状态:">
                <span></span>
              </el-form-item>
              <el-form-item label="异常日志:">
                <el-input 
                  type="textarea"
                  style="font-size: 12px; width: 700px"
                  :autosize="{ minRows: 2, maxRows: 5 }"
                  v-model="props.row.exception"
                  readonly
                ></el-input>
              </el-form-item>
            </el-form>
          </template>
        </el-table-column>

        <!-- 表格列定义 -->
        <el-table-column 
          prop="createTime"
          label="日志时间"
          min-width="15%"
          :show-overflow-tooltip="true"
        ></el-table-column>
        <el-table-column 
          prop="username"
          label="操作用户"
          min-width="10%"
          :show-overflow-tooltip="true"
        ></el-table-column>
        <el-table-column 
          prop="ipAddress"
          label="请求IP"
          min-width="10%"
          :show-overflow-tooltip="true"
        ></el-table-column>
        <el-table-column 
          prop="moduleName"
          label="操作类型"
          min-width="10%"
          :show-overflow-tooltip="true"
        ></el-table-column>
        <el-table-column 
          prop="elapseSeconds"
          label="耗时(ms)"
          min-width="10%"
          :show-overflow-tooltip="true"
        ></el-table-column>
        <el-table-column 
          prop="urlPath"
          label="请求路径"
          min-width="20%"
          :show-overflow-tooltip="true"
        ></el-table-column>
        <el-table-column 
          prop="failed"
          label="异常"
          :formatter="boolFormat"
          min-width="10%"
          :show-overflow-tooltip="true"
        ></el-table-column>
      </el-table>

      <!-- 分页组件 -->
      <div class="pagination-wrapper">
        <el-pagination 
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          :current-page="currentPage"
          :page-sizes="[5, 10, 20, 40]"
          :page-size="pageSize"
          layout="total, sizes, prev, pager, next, jumper"
          :total="totalCount"
          size="small"
        ></el-pagination>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { getSysLogList } from '@/api/data/pipeline/sysLogApi' // 导入API方法

// 响应式数据
const loading = ref(true)
const lists = ref([])
const currentPage = ref(1)
const pageSize = ref(10)
const totalCount = ref(0)

// 加载日志数据
const loadData = async () => {
  loading.value = true
  try {
    const res = await getSysLogList(2, currentPage.value, pageSize.value)
    if (res.code === 200) {
      // 更新分页和列表数据
      currentPage.value = res.pagination.page
      pageSize.value = res.pagination.size
      totalCount.value = res.pagination.total
      lists.value = res.data
    } else {
      ElMessage.error(`加载数据失败: ${res.message || '未知错误'}`)
    }
  } catch (error) {
    ElMessage.error(`数据加载错误: ${error.message}`)
  } finally {
    loading.value = false
  }
}

// 布尔值格式化（是否异常）
const boolFormat = (row) => {
  return row.failed ? '是' : '否'
}

// 分页大小变更
const handleSizeChange = (newPageSize) => {
  pageSize.value = newPageSize
  loadData()
}

// 当前页变更
const handleCurrentChange = (newCurrentPage) => {
  currentPage.value = newCurrentPage
  loadData()
}

// 查看详情（保留原方法，可扩展）
const handleDetail = (index, row) => {
  ElMessage.info(`查看日志详情 ${index} - ${row.id}`)
}

// 生命周期：组件挂载时加载数据
onMounted(() => {
  loadData()
})
</script>

<style lang="scss" scoped>
.sys-log-page {
  margin-top: 15px;
  width: 100%;

  .el-table {
    width: 100%;
    height: 100%;
  }

  // 展开行表单样式
  .demo-table-expand {
    font-size: 0;

    label {
      width: 90px;
      color: #99a9bf;
    }

    .el-form-item {
      margin-right: 0;
      margin-bottom: 0;
      width: 50%;
      padding: 4px 0;
    }

    .el-textarea {
      font-size: 12px;
      width: 700px;

      ::v-deep .el-textarea__inner {
        resize: vertical;
      }
    }
  }

  // 分页组件样式
  .pagination-wrapper {
    margin-top: 15px;
    text-align: right;

    .el-pagination {
      --el-pagination-font-size: 12px;
    }
  }
}
</style>