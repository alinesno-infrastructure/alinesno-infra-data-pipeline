<template>
  <div class="task-list-container">
    <el-card shadow="hover">

      <!-- 操作按钮区域 -->
      <el-row :gutter="10" class="operation-bar">
        <el-col :span="12">
      <!-- 搜索区域 -->
      <div class="search-area">
        <el-input
          placeholder="请输入任务名称关键字搜索"
          v-model="keyword"
          clearable
          @change="searchByKeyword"
          class="search-input"
        >
          <template #append>
            <el-button @click="searchByKeyword" icon="Search"></el-button>
          </template>
        </el-input>
      </div>

        </el-col>
        <el-col :span="12">
          <el-button
            icon="SwitchButton"
            :disabled="isSelected"
            plain
            @click="batchStart"
          >
            启动
          </el-button>
          <el-button
            icon="VideoPause"
            :disabled="isSelected"
            plain
            @click="batchStop"
          >
            停用
          </el-button>
          <el-button
            :disabled="isSelected"
            plain
            @click="batchExport"
          >
            导出
          </el-button>
          <el-button
            type="primary"
            icon="DocumentAdd"
            @click="handleCreate"
          >
            创建任务
          </el-button>
        </el-col>
      </el-row>

      <!-- 表格区域 -->
      <el-table
        :header-cell-style="{ background: '#eef1f6', color: '#606266' }"
        :data="tableData"
        @selection-change="handleSelectionChange"
        v-loading="loading"
        class="task-table"
      >
        <el-table-column prop="id" label="编号" type="selection" min-width="6%"></el-table-column>
        
        <el-table-column label="任务名称" :show-overflow-tooltip="true" min-width="15%">
          <template #default="scope">
            <span @click="handleDetail(scope.row)" class="task-name">
              {{ scope.row.name }}
            </span>
          </template>
        </el-table-column>
        
        <el-table-column
          prop="scheduleMode"
          label="模式"
          sortable
          :formatter="stringFormatSchedule"
          min-width="8%"
        ></el-table-column>
        
        <el-table-column label="源端" min-width="10%">
          <template #default="scope">
            <el-popover trigger="hover" placement="top">
              <template #default>
                <p>源端数据源类型：{{ scope.row.sourceType || '--' }}</p>
                <p>源端模式名：{{ scope.row.sourceSchema || '--' }}</p>
              </template>
              <template #reference>
                <div class="name-wrapper">
                  <el-tag size="medium" class="name-tag">
                    {{ scope.row.sourceSchema || '--' }}
                  </el-tag>
                </div>
              </template>
            </el-popover>
          </template>
        </el-table-column>
        
        <el-table-column label="目标端" min-width="10%">
          <template #default="scope">
            <el-popover trigger="hover" placement="top">
              <template #default>
                <p>目标端数据源类型：{{ scope.row.targetType || '--' }}</p>
                <p>目标端模式名：{{ scope.row.targetSchema || '--' }}</p>
              </template>
              <template #reference>
                <div class="name-wrapper">
                  <el-tag size="medium" class="name-tag">
                    {{ scope.row.targetSchema || '--' }}
                  </el-tag>
                </div>
              </template>
            </el-popover>
          </template>
        </el-table-column>
        
        <el-table-column label="运行状态" show-overflow-tooltip sortable min-width="10%">
          <template #default="scope">
            <el-icon class="status-icon" :class="getStatusClass(scope.row.runStatus)">
              <Success v-if="scope.row.runStatus === '执行成功'" />
              <CircleClose v-if="scope.row.runStatus === '执行异常'" />
              <Remove v-if="scope.row.runStatus === '任务取消'" />
              <VideoPlay v-if="scope.row.runStatus === '执行中'" />
              <Loading v-if="scope.row.runStatus === '待执行'" />
            </el-icon>
            <span>{{ scope.row.runStatus || '--' }}</span>
          </template>
        </el-table-column>
        
        <el-table-column
          prop="isPublished"
          label="任务状态"
          sortable
          :formatter="boolFormatPublish"
          :show-overflow-tooltip="true"
          min-width="10%"
        ></el-table-column>
        
        <el-table-column
          prop="scheduleTime"
          label="调度时间"
          :formatter="scheduleTimeFormat"
          min-width="15%"
        ></el-table-column>
        
        <el-table-column label="操作" min-width="35%">
          <template #default="scope">
            <el-tooltip content="跳转到监控调度" placement="top">
              <el-link class="btn-text" type="primary" @click="schedulingLog(scope.row)">
                日志
              </el-link>
            </el-tooltip>
            
            <span class="btn-divider">&nbsp;|&nbsp;</span>
            
            <el-link
              class="btn-text"
              type="primary"
              v-if="!scope.row.isPublished"
              @click="handlePublish(scope.row)"
            >
              启动
            </el-link>
            <el-link
              class="btn-text"
              type="primary"
              v-if="scope.row.isPublished"
              @click="handleRetireTask(scope.row)"
            >
              停止
            </el-link>
            
            <span class="btn-divider" v-if="scope.row.isPublished">&nbsp;|&nbsp;</span>
            <el-link
              class="btn-text"
              type="primary"
              v-if="scope.row.isPublished"
              @click="handleRunTask(scope.row)"
            >
              执行
            </el-link>
            
            <span class="btn-divider" v-if="!scope.row.isPublished">&nbsp;|&nbsp;</span>
            <el-link
              class="btn-text"
              type="primary"
              v-if="!scope.row.isPublished"
              @click="handleUpdate(scope.row)"
            >
              修改
            </el-link>
            
            <span class="btn-divider">&nbsp;|&nbsp;</span>
            <el-link
              class="btn-text"
              type="primary"
              @click="handleDetail(scope.row)"
            >
              详情
            </el-link>
            
            <span class="btn-divider" v-if="!scope.row.isPublished">&nbsp;|&nbsp;</span>
            <el-link
              class="btn-text"
              type="primary"
              v-if="!scope.row.isPublished"
              @click="handleDelete(scope.row)"
            >
              删除
            </el-link>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页区域 -->
      <div class="pagination-area">
        <el-pagination
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          :current-page="currentPage"
          :page-sizes="[5, 10, 20, 40]"
          :page-size="pageSize"
          layout="total, sizes, prev, pager, next, jumper"
          :total="totalCount"
          background
        ></el-pagination>
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from "vue";
import { useRouter } from "vue-router";
import { ElMessage, ElMessageBox } from "element-plus";
// 导入API
import {
  getTaskList,
  batchStartTask,
  batchStopTask,
  exportTask,
  deleteTask,
  publishTask,
  retireTask,
  runTask
} from "@/api/data/pipeline/task";

// 路由实例
const router = useRouter();

// 响应式数据
const loading = ref(true);
const currentPage = ref(1);
const pageSize = ref(10);
const totalCount = ref(0);
const keyword = ref("");
const tableData = ref([]);
const isSelected = ref(true);
const idsSelected = ref([]);

// 加载数据
const loadData = async () => {
  loading.value = true;
  try {
    const res = await getTaskList({
      searchText: keyword.value,
      page: currentPage.value,
      size: pageSize.value
    });
    if (res.code === 200) {
      currentPage.value = res.pagination.page;
      pageSize.value = res.pagination.size;
      totalCount.value = res.pagination.total;
      tableData.value = res.data;
    } else {
      ElMessage.error(`加载任务列表失败: ${res.data.message}`);
    }
  } catch (error) {
    console.error("加载任务列表失败", error);
    ElMessage.error("加载任务列表失败，请重试");
  } finally {
    loading.value = false;
  }
};

// 关键字搜索
const searchByKeyword = () => {
  currentPage.value = 1;
  loadData();
};

// 表格选择事件
const handleSelectionChange = (val) => {
  isSelected.value = val.length === 0;
  // 正确收集选中的ID
  idsSelected.value = val.map(item => item.id);
};

// 批量启动
const batchStart = async () => {
  if (idsSelected.value.length === 0) {
    ElMessage.warning("请选择要启动的任务");
    return;
  }
  try {
    const res = await batchStartTask(idsSelected.value);
    if (res.code === 200) {
      ElMessage.success("任务发布成功!");
      loadData();
    } else {
      ElMessage.error(`任务发布失败: ${res.data.message || '未知错误'}`);
    }
  } catch (error) {
    console.error("批量启动任务失败", error);
    ElMessage.error("批量启动任务失败，请重试");
  }
};

// 批量停止
const batchStop = async () => {
  if (idsSelected.value.length === 0) {
    ElMessage.warning("请选择要停止的任务");
    return;
  }
  try {
    const res = await batchStopTask(idsSelected.value);
    if (res.code === 200) {
      ElMessage.success("下线任务成功!");
      loadData();
    } else {
      ElMessage.error(`下线任务失败: ${res.data.message || '未知错误'}`);
    }
  } catch (error) {
    console.error("批量停止任务失败", error);
    ElMessage.error("批量停止任务失败，请重试");
  }
};

// 批量导出
const batchExport = async () => {
  if (idsSelected.value.length === 0) {
    ElMessage.warning("请选择要导出的任务");
    return;
  }
  try {
    const res = await exportTask(idsSelected.value);
    if (res.status === 200) {
      downloadFile(res);
      ElMessage.success("导出任务成功!");
      loadData();
    } else {
      ElMessage.error(`导出任务失败: ${res.data?.message || '未知错误'}`);
    }
  } catch (error) {
    console.error("批量导出任务失败", error);
    ElMessage.error("批量导出任务失败，请重试");
  }
};

// 文件下载处理
const downloadFile = (resp) => {
  const headers = resp.headers;
  const contentType = headers['content-type'];
  if (!resp.data) {
    console.error('响应异常：', resp);
    ElMessage.error("下载文件失败：响应数据为空");
    return;
  }

  const blob = new Blob([resp.data], { type: contentType });
  const contentDisposition = headers['content-disposition'];
  let fileName = '任务列表导出.xlsx';
  
  if (contentDisposition) {
    try {
      fileName = window.decodeURI(contentDisposition.split('=')[1]);
    } catch (e) {
      console.error("解析文件名失败", e);
    }
  }

  // 非IE下载
  if ('download' in document.createElement('a')) {
    const link = document.createElement('a');
    link.href = window.URL.createObjectURL(blob);
    link.download = fileName;
    link.style.display = 'none';
    document.body.appendChild(link);
    link.click();
    window.URL.revokeObjectURL(link.href);
    document.body.removeChild(link);
  } else {
    // IE10+下载
    window.navigator.msSaveBlob(blob, fileName);
  }
};

// 创建任务
const handleCreate = () => {
  router.push('/data/pipeline/task/create');
};

// 任务详情
const handleDetail = (row) => {
  router.push({ path: '/data/pipeline/task/detail', query: { id: row.id } });
};

// 修改任务
const handleUpdate = (row) => {
  router.push({ path: '/data/pipeline/task/update', query: { id: row.id } });
};

// 删除任务
const handleDelete = async (row) => {
  try {
    await ElMessageBox.confirm(
      `是否确定删除任务【${row.name}】?`,
      "提示",
      {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }
    );
    
    const res = await deleteTask(row.id);
    if (res.data.code === 0) {
      ElMessage.success("删除任务成功!");
      loadData();
    } else {
      ElMessage.error(`删除任务失败: ${res.data.message || '未知错误'}`);
    }
  } catch (error) {
    if (error !== 'cancel') {
      console.error("删除任务失败", error);
      ElMessage.error("删除任务失败，请重试");
    }
  }
};

// 调度日志
const schedulingLog = (row) => {
  if (!row.id) {
    ElMessage.warning("请选择要查看的调度日志");
    return;
  }
  router.push({ path: `/data/pipeline/schedule` , query: { id: row.id }});
};

// 启动单个任务
const handlePublish = async (row) => {
  try {
    const res = await publishTask(row.id);
    if (res.code === 200) {
      ElMessage.success("任务发布成功!");
      loadData();
    } else {
      ElMessage.error(`任务发布失败: ${res.message || '未知错误'}`);
    }
  } catch (error) {
    console.error("启动任务失败", error);
    ElMessage.error("启动任务失败，请重试");
  }
};

// 手动执行任务
const handleRunTask = async (row) => {
  try {
    const res = await runTask(row.id);
    if (res.code === 200) {
      ElMessage.success("手动启动执行任务成功!");
      loadData();
    } else {
      ElMessage.error(`手动启动执行任务失败: ${res.message || '未知错误'}`);
    }
  } catch (error) {
    console.error("执行任务失败", error);
    ElMessage.error("执行任务失败，请重试");
  }
};

// 停止单个任务
const handleRetireTask = async (row) => {
  try {
    const res = await retireTask(row.id);
    if (res.code === 200) {
      ElMessage.success("下线任务成功!");
      loadData();
    } else {
      ElMessage.error(`下线任务失败: ${res.message || '未知错误'}`);
    }
  } catch (error) {
    console.error("停止任务失败", error);
    ElMessage.error("停止任务失败，请重试");
  }
};

// 分页大小改变
const handleSizeChange = (val) => {
  pageSize.value = val;
  loadData();
};

// 当前页改变
const handleCurrentChange = (val) => {
  currentPage.value = val;
  loadData();
};

// 格式化发布状态
const boolFormatPublish = (row) => {
  return row.isPublished ? "启动" : "停止";
};

// 格式化调度时间
const scheduleTimeFormat = (row) => {
  return row.scheduleTime || "--";
};

// 格式化调度模式
const stringFormatSchedule = (row) => {
  return row.scheduleMode === "MANUAL" ? "手动" : "定时";
};

// 获取状态样式类
const getStatusClass = (status) => {
  const classMap = {
    '执行成功': 'color-success',
    '执行异常': 'color-error',
    '任务取消': 'color-cancel',
    '执行中': 'color-running',
    '待执行': 'color-await'
  };
  return classMap[status] || '';
};

// 初始化加载
onMounted(() => {
  loadData();
});
</script>

<style lang="scss" scoped>
.task-list-container {
  width: 100%;
  height: 100%;
  padding: 10px;

  .el-card {
    height: 100%;
    overflow: auto;
  }

  // 操作按钮区域
  .operation-bar {
    margin-bottom: 10px;
    align-items: center;

    .divider {
      color: #e9e9f3;
    }

    .right-add-button-group {
      float: right;
    }
  }

  // 搜索区域
  .search-area {
    // margin-bottom: 10px;

    .search-input {
      width: 100%;
    }
  }

  // 表格样式
  .task-table {
    width: 100%;
    margin-bottom: 15px;

    ::v-deep .el-table--border .el-table__cell {
      border-right: 0px solid transparent !important;
    }

    .status-icon {
      margin-right: 4px;
      vertical-align: middle;
    }

    .color-success {
      color: #6cdbbc !important;
    }

    .color-error {
      color: #ff9c86 !important;
    }

    .color-cancel {
      color: #a0a6b8 !important;
    }

    .color-running {
      color: #6cdbbc !important;
    }

    .color-await {
      color: #a0a6b8 !important;
    }

    .task-name {
      color: #409EFF;
      cursor: pointer;
      transition: all 0.3s;

      &:hover {
        color: red;
        text-decoration: underline;
      }
    }

    .name-wrapper {
      .name-tag {
        overflow: hidden;
        text-overflow: ellipsis;
        white-space: nowrap;
        max-width: 100%;
      }
    }

    .btn-text {
      font-size: 12px;
      color: #6873ce;
    }

    .btn-divider {
      color: #e9e9f3;
    }
  }

  // 分页区域
  .pagination-area {
    text-align: right;
  }
}
</style>