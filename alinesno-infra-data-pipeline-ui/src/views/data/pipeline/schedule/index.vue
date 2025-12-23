<template>
  <div>
    <el-card>
      <div class="container">
        <el-card class="box-card">
          <template #header>
            <div class="clearfix">
              <span>任务安排列表</span>
              <el-input
                placeholder="请输入关键字搜索"
                v-model="keyword"
                clearable
                @change="changeSearchKeyword"
                style="width: 200px"
              ></el-input>
            </div>
          </template>
          <div class="navsBox">
            <ul>
              <li
                v-for="(item, index) in pageTaskAssignments"
                :key="index"
                @click="handleChooseClick(item.id, index)"
                :class="{ active: index === isActive }"
              >
                [{{ item.id }}]{{ item.name }}
              </li>
            </ul>
            <el-pagination
              small
              layout="sizes, prev, pager, next"
              @current-change="handleLoadPageTaskAssignments"
              :current-page="currentTaskAssignmentPage"
              :page-sizes="[10, 15, 20]"
              @size-change="handleLoadPageTaskAssignmentsSizeChange"
              :page-size="currentTaskAssignmentPageSize"
              :total="pageTaskAssignmentsTotalCount"
            ></el-pagination>
          </div>
        </el-card>

        <div class="contentBox">
          <el-tag v-if="taskId && taskId > 0"
            >当前任务：[{{ taskId }}]{{ taskName }}</el-tag
          >
          <div class="right-refresh-button">
            <el-button
              type="primary"
              plain
              size="mini"
              icon="Refresh"
              @click="handleClickRefresh"
              round
            >
              刷新
            </el-button>
          </div>
          <el-table
            :header-cell-style="{ background: '#eef1f6', color: '#606266' }"
            :data="jobTableData"
          >
            <template #empty>
              <span>记录为空，或者单击左侧任务列表记录来查看作业调度记录</span>
            </template>
            <el-table-column
              prop="jobId"
              label="ID"
              min-width="10%"
            ></el-table-column>
            <el-table-column
              prop="scheduleMode"
              label="调度方式"
              min-width="15%"
            ></el-table-column>
            <el-table-column
              prop="startTime"
              label="开始时间"
              min-width="25%"
            ></el-table-column>
            <el-table-column
              prop="finishTime"
              label="结束时间"
              min-width="25%"
            ></el-table-column>
            <el-table-column
              prop="duration"
              label="持续时长(s)"
              min-width="15%"
            ></el-table-column>
            <el-table-column
              prop="jobStatus"
              label="执行状态"
              min-width="15%"
            ></el-table-column>
            <el-table-column label="日志" min-width="15%">
              <template #default="props">
                <el-button
                  size="mini"
                  type="success"
                  @click="handleShowJobLogs(props.row.jobId)"
                  round
                >
                  <el-icon><View /></el-icon>查看
                </el-button>
              </template>
            </el-table-column>
          </el-table>
          <div class="page" align="right">
            <el-pagination
              @size-change="handleSizeChange"
              @current-change="handleCurrentChange"
              :current-page="currentPage"
              :page-sizes="[5, 10, 20, 40]"
              :page-size="pageSize"
              layout="total, sizes, prev, pager, next, jumper"
              :total="totalCount"
            ></el-pagination>
          </div>
        </div>

        <el-dialog
          title="日志详情"
          v-model="dialogShowLogVisible"
          :show-close="false"
          @close="handleCloseLogDialog"
        >
          <el-alert v-if="status === 0" title="未执行" type="info" center show-icon />
          <el-alert v-if="status === 1" title="执行中" type="success" center show-icon />
          <el-alert v-if="status === 2" title="执行失败" type="error" center show-icon />
          <el-alert v-if="status === 3" title="执行成功" type="success" center show-icon />
          <el-alert v-if="status === 4" title="手动取消" type="warning" center show-icon />
          <el-input
            type="textarea"
            id="log_textarea_id"
            class="log_textarea_style"
            :rows="20"
            :spellcheck="false"
            v-model="logContent"
          ></el-input>
          <template #footer>
            <div class="dialog-footer">
              <el-button
                size="small"
                id="butten_cancel_id"
                type="danger"
                v-if="status === 1"
                @click="handleCancelJob(jobId)"
              >
                终 止
              </el-button>
              <el-button size="small" type="success" @click="handleCloseLogDialog">
                关 闭
              </el-button>
            </div>
          </template>
        </el-dialog>
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, onUnmounted, nextTick } from "vue";
import { useRoute, useRouter } from "vue-router";
import { ElMessage, ElMessageBox } from "element-plus";
// 导入API（已有的直接用，新增的补充）
import {
  getTaskList,
  getAssignmentInfo,
  getJobList,
  cancelJob,
  getJobLogsTail,
  getJobLogsNext,
} from "@/api/data/pipeline/task";

// 路由实例
const route = useRoute();
const router = useRouter();

// 响应式数据（替代原data）
const loading = ref(true);
const currentPage = ref(1);
const pageSize = ref(10);
const totalCount = ref(0);
const currentTaskAssignmentPage = ref(1);
const currentTaskAssignmentPageSize = ref(10);
const keyword = ref(null);
const pageTaskAssignments = ref([]);
const pageTaskAssignmentsTotalCount = ref(0);
const taskId = ref(0);
const taskName = ref("");
const jobTableData = ref([]);
const jobScheduleTime = ref("");
const isActive = ref(-1);
const dialogShowLogVisible = ref(false);
const logContent = ref("");
const jobId = ref(0);
const baseId = ref(0);
const status = ref(0);
let timer = ref(null); // 定时器用ref存储

// 方法定义
// 加载任务列表
const loadPageTaskAssignments = async () => {
  try {
    const res = await getTaskList({
      searchText: keyword.value,
      page: currentTaskAssignmentPage.value,
      size: currentTaskAssignmentPageSize.value,
    });
    if (res.code === 200) {
      pageTaskAssignments.value = res.data;
      pageTaskAssignmentsTotalCount.value = res.pagination.total;
    } else {
      if (res.message) {
        ElMessageBox.alert(`初始化任务安排信息失败:${res.message}`, "错误", {
          type: "error",
        });
      }
    }
  } catch (error) {
    console.error("加载任务列表失败:", error);
    ElMessage.error("初始化任务安排信息失败");
  }
};

// 搜索关键字变更
const changeSearchKeyword = () => {
  currentTaskAssignmentPage.value = 1;
  loadPageTaskAssignments();
};

// 任务列表页码变更
const handleLoadPageTaskAssignments = (currentPageVal) => {
  currentTaskAssignmentPage.value = currentPageVal;
  loadPageTaskAssignments();
};

// 任务列表页大小变更
const handleLoadPageTaskAssignmentsSizeChange = (pageSizeVal) => {
  currentTaskAssignmentPageSize.value = pageSizeVal;
  loadPageTaskAssignments();
};

// 作业列表页大小变更
const handleSizeChange = (pageSizeVal) => {
  loading.value = true;
  pageSize.value = pageSizeVal;
  loadJobsData();
};

// 作业列表页码变更
const handleCurrentChange = (currentPageVal) => {
  loading.value = true;
  currentPage.value = currentPageVal;
  loadJobsData();
};

// 加载任务基本信息
const loadTaskInfo = async () => {
  if (!taskId.value || taskId.value <= 0) return;
  try {
    const res = await getAssignmentInfo(taskId.value);
    if (res.code === 200) {
      taskName.value = res.data.name;
    } else {
      if (res.message) {
        ElMessageBox.alert(`查询任务详情失败:${res.message}`, "错误", {
          type: "error",
        });
      }
    }
  } catch (error) {
    console.error("加载任务信息失败:", error);
    ElMessage.error("查询任务详情失败");
  }
};

// 加载作业数据
const loadJobsData = async () => {
  if (!taskId.value || taskId.value <= 0) return;
  try {
    const res = await getJobList(taskId.value, currentPage.value, pageSize.value);
    if (res.code === 200) {
      currentPage.value = res.pagination.page;
      pageSize.value = res.pagination.size;
      totalCount.value = res.pagination.total;
      jobTableData.value = res.data;
    } else {
      if (res.data.message) {
        ElMessageBox.alert(`查询JOB执行历史纪录失败:${res.message}`, "错误", {
          type: "error",
        });
      }
    }
  } catch (error) {
    console.error("加载作业数据失败:", error);
    ElMessage.error("查询JOB执行历史纪录失败");
  } finally {
    loading.value = false;
  }
};

// 选择左侧任务
const handleChooseClick = (taskIdVal, index) => {
  isActive.value = index;
  taskId.value = taskIdVal;
  loadTaskInfo();
  loadJobsData();
};

// 刷新按钮点击
const handleClickRefresh = () => {
  if (!taskId.value || taskId.value < 0) {
    ElMessage.warning("请先在左侧选择一个任务");
    return;
  }
  loadTaskInfo();
  loadJobsData();
};

// 取消作业执行
const handleCancelJob = async (jobIdVal) => {
  try {
    const res = await cancelJob(jobIdVal);
    if (res.code === 200) {
      // 禁用取消按钮
      const cancelButton = document.getElementById("butten_cancel_id");
      if (cancelButton) {
        cancelButton.innerText = "已取消";
        cancelButton.disabled = true;
      }
      ElMessage.success("停止JOB成功");
      loadJobsData();
    } else {
      if (res.data.message) {
        ElMessageBox.alert(`JOB停止失败:${res.data.message}`, "错误", {
          type: "error",
        });
      }
    }
  } catch (error) {
    console.error("取消作业失败:", error);
    ElMessage.error("JOB停止失败");
  }
};

// 查看作业日志
const handleShowJobLogs = async (jobIdVal) => {
  dialogShowLogVisible.value = true;
  jobId.value = jobIdVal;
  try {
    const res = await getJobLogsTail(jobIdVal, 500);
    if (res.code === 200) {
      const lists = res.data.logs;
      status.value = res.data.status;
      baseId.value = res.data.maxId;
      logContent.value = lists.join("");
      scrollMaxheight();
      // 执行中则定时刷新日志
      if (res.data.status === 1) {
        timer.value = setInterval(() => {
          timerRefreshLogs();
        }, 1000);
      }
    } else {
      if (res.message) {
        ElMessageBox.alert(`加载JOB执行日志失败:${res.message}`, "错误", {
          type: "error",
        });
      }
    }
  } catch (error) {
    console.error("加载作业日志失败:", error);
    ElMessage.error("加载JOB执行日志失败");
  }
};

// 定时刷新日志
const timerRefreshLogs = async () => {
  try {
    const res = await getJobLogsNext(jobId.value, baseId.value);
    if (res.code === 200) {
      const lists = res.data.logs;
      logContent.value += lists.join("");
      baseId.value = res.data.maxId;
      status.value = res.data.status;
      scrollMaxheight();
      // 非执行中则清除定时器
      if (res.data.status !== 1) {
        clearInterval(timer.value);
        timer.value = null;
      }
    }
  } catch (error) {
    console.error("刷新日志失败:", error);
  }
};

// 滚动日志到底部
const scrollMaxheight = () => {
  nextTick(() => {
    setTimeout(() => {
      const textarea = document.getElementById("log_textarea_id");
      if (textarea) {
        textarea.scrollTop = textarea.scrollHeight;
      }
    }, 13);
  });
};

// 关闭日志弹窗
const handleCloseLogDialog = () => {
  dialogShowLogVisible.value = false;
  if (timer.value) {
    clearInterval(timer.value);
    timer.value = null;
  }
};

// 生命周期
// 挂载时执行（替代created）
onMounted(() => {
  loadPageTaskAssignments();
  if (route.query.id) {
    taskId.value = route.query.id;
    loadTaskInfo();
    loadJobsData();
  }
});

// 卸载前清除定时器（替代beforeDestroy）
onUnmounted(() => {
  if (timer.value) {
    clearInterval(timer.value);
    timer.value = null;
  }
});
</script>

<style scoped>
.el-card,
.el-message {
  width: 100%;
  height: 100%;
  overflow: auto;
}

.el-table {
  width: 100%;
  border-collapse: collapse;
}

.demo-table-expand {
  font-size: 0;
}

.demo-table-expand label {
  width: 90px;
  color: #99a9bf;
}

.demo-table-expand .el-form-item {
  margin-right: 0;
  margin-bottom: 0;
  width: 50%;
}

.filter {
  margin: 10px;
}

.container {
  display: flex;
  height: 100%;
}

.container > * {
  float: left; /* 水平排列 */
}

.container .el-card {
  width: 50%;
  height: 100%;
  overflow: auto;
}

.container .el-card__header {
  padding: 8px 10px;
  border-bottom: 1px solid #ebeef5;
  box-sizing: border-box;
}

.container .navsBox ul {
  margin: 0;
  padding-left: 10px;
}

.container .navsBox ul li {
  list-style: none;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap; /* 修正原nowrop拼写错误 */
  cursor: pointer; /*鼠标悬停变小手*/
  padding: 10px 0;
  border-bottom: 1px solid #e0e0e0;
  width: 100%;
}

.container .navsBox .active {
  background: #bcbcbe6e;
  color: rgb(46, 28, 88);
}

.container .contentBox {
  padding: 10px;
  width: calc(100% - 250px);
}

.right-refresh-button {
  float: right;
  margin-right: 2px;
  margin: 10px 2px;
}

/* 日志文本域样式补充 */
.log_textarea_style {
  width: 100%;
  resize: none;
}
</style>