<template>
  <div class="task-detail-container">
    <!-- 基础信息卡片 -->
    <el-card class="box-card">
      <el-row class="row-gutter">
        <el-col :span="2">
          <label class="key-text">任务名称</label>
        </el-col>
        <el-col :span="10">
          <label class="value-text">{{ infoform.name || '--' }}</label>
        </el-col>
        <el-col :span="2">
          <label class="key-text">任务类型</label>
        </el-col>
        <el-col :span="10">
          <label class="value-text">普通任务</label>
        </el-col>
      </el-row>
      <el-row class="row-gutter">
        <el-col :span="2">
          <label class="key-text">集成模式</label>
        </el-col>
        <el-col :span="10">
          <label class="value-text">
            <span v-if="infoform.scheduleMode === 'MANUAL'">手动</span>
            <span v-else-if="infoform.scheduleMode === 'SYSTEM_SCHEDULED'">定时</span>
            <span v-else>--</span>
          </label>
        </el-col>
        <el-col :span="2">
          <label class="key-text">调度计划</label>
        </el-col>
        <el-col :span="10">
          <label class="value-text">
            <span v-if="infoform.scheduleMode === 'MANUAL'">--</span>
            <span v-else-if="infoform.scheduleMode === 'SYSTEM_SCHEDULED'">{{ infoform.cronExpression || '--' }}</span>
            <span v-else>--</span>
          </label>
        </el-col>
      </el-row>
      <el-row class="row-gutter">
        <el-col :span="2">
          <label class="key-text">描述</label>
        </el-col>
        <el-col :span="22">
          <label class="value-text">{{ infoform.description || '--' }}</label>
        </el-col>
      </el-row>
    </el-card>

    <!-- 数据源信息区域 -->
    <div class="common-box">
      <div class="datainfo">
        <!-- 源端数据源 -->
        <div class="source">
          <div class="head">
            <div class="head-img">
              <el-image 
                style="width: 60px; height: 60px"
                :src="getSourceIcon(infoform.sourceTypeName)"
                fit="contain"
                fallback-src="https://picsum.photos/60/60">
              </el-image>
            </div>
            <div class="head-text">
              <div class="title">{{ infoform.sourceConnectionName || '--' }}</div>
              <div class="sub-title">源端数据源</div>
            </div>
          </div>
          <div class="body">
            <el-row class="row-gutter">
              <el-col :span="8">
                <label class="key-text">源端schema</label>
              </el-col>
              <el-col :span="16">
                <label class="value-text">{{ infoform.sourceSchema || '--' }}</label>
              </el-col>
            </el-row>
            <el-row class="row-gutter">
              <el-col :span="8">
                <label class="key-text">源端表类型</label>
              </el-col>
              <el-col :span="16">
                <label class="value-text">{{ infoform.tableType || '--' }}</label>
              </el-col>
            </el-row>
            <el-row class="row-gutter">
              <el-col :span="8">
                <label class="key-text">源端表选择方式</label>
              </el-col>
              <el-col :span="16">
                <label class="value-text">
                  {{ infoform.includeOrExclude === 'INCLUDE' ? '包含表' : 
                    infoform.includeOrExclude === 'EXCLUDE' ? '排除表' : '--' }}
                </label>
              </el-col>
            </el-row>
            <el-row class="row-gutter">
              <el-col :span="8">
                <label class="key-text">源端表名列表</label>
              </el-col>
              <el-col :span="16">
                <div class="table-name-list">
                  <ul v-if="infoform.sourceTables && infoform.sourceTables.length">
                    <li v-for="(item, index) in infoform.sourceTables" :key="index">{{ item }}</li>
                  </ul>
                  <div v-else class="empty-tip">无表名数据</div>
                </div>
              </el-col>
            </el-row>
            <el-row class="row-gutter">
              <el-col :span="4">
                <label class="key-text">增量同步配置</label>
              </el-col>
              <el-col :span="20">
                <el-table 
                  :data="infoform.incrTableColumns || []"
                  style="width: 100%"
                  :row-class-name="tableRowClassName"
                  empty-text="无增量同步配置">
                  <el-table-column prop="tableName" label="增量同步表名" />
                  <el-table-column prop="columnName" label="增量标识字段" />
                </el-table>
              </el-col>
            </el-row>
            <el-row class="row-gutter">
              <el-col :span="8">
                <label class="key-text">同步前置执行SQL脚本</label>
              </el-col>
              <el-col :span="16">
                <label class="value-text">
                  {{ infoform.sourceBeforeSqlScripts || '[SQL脚本内容为空]' }}
                </label>
              </el-col>
            </el-row>
            <el-row class="row-gutter">
              <el-col :span="8">
                <label class="key-text">同步后置执行SQL脚本</label>
              </el-col>
              <el-col :span="16">
                <label class="value-text">
                  {{ infoform.sourceAfterSqlScripts || '[SQL脚本内容为空]' }}
                </label>
              </el-col>
            </el-row>
          </div>
        </div>

        <!-- 目标端数据源 -->
        <div class="target">
          <div class="head">
            <div class="head-img">
              <el-image 
                style="width: 60px; height: 60px"
                :src="getSourceIcon(infoform.targetTypeName)"
                fit="contain"
                fallback-src="https://picsum.photos/60/60">
              </el-image>
            </div>
            <div class="head-text">
              <div class="title">{{ infoform.targetConnectionName || '--' }}</div>
              <div class="sub-title">目标端数据源</div>
            </div>
          </div>
          <div class="body">
            <el-row class="row-gutter">
              <el-col :span="8">
                <label class="key-text">目地端schema</label>
              </el-col>
              <el-col :span="16">
                <label class="value-text">{{ infoform.targetSchema || '--' }}</label>
              </el-col>
            </el-row>
            <el-row class="row-gutter">
              <el-col :span="8">
                <label class="key-text">自动同步模式</label>
              </el-col>
              <el-col :span="16">
                <span class="value-text" v-if="infoform.autoSyncMode === 2">目标端建表并同步数据</span>
                <span class="value-text" v-else-if="infoform.autoSyncMode === 1">目标端只创建物理表</span>
                <span class="value-text" v-else-if="infoform.autoSyncMode === 0">目标端只同步表里数据</span>
                <span class="value-text" v-else>--</span>
              </el-col>
            </el-row>
            <el-row class="row-gutter" v-if="infoform.autoSyncMode !== 0">
              <el-col :span="8">
                <label class="key-text">建表字段自增</label>
              </el-col>
              <el-col :span="16">
                <label class="value-text">{{ infoform.targetAutoIncrement || '--' }}</label>
              </el-col>
            </el-row>
            <el-row class="row-gutter" v-if="infoform.autoSyncMode !== 0">
              <el-col :span="8">
                <label class="key-text">表名转换方法</label>
              </el-col>
              <el-col :span="16">
                <label class="value-text">
                  <span v-if="infoform.tableNameCase === 'NONE'">无转换</span>
                  <span v-else-if="infoform.tableNameCase === 'UPPER'">转大写</span>
                  <span v-else-if="infoform.tableNameCase === 'LOWER'">转小写</span>
                  <span v-else-if="infoform.tableNameCase === 'CAMEL'">下划线转驼峰</span>
                  <span v-else-if="infoform.tableNameCase === 'SNAKE'">驼峰转下划线</span>
                  <span v-else>--</span>
                </label>
              </el-col>
            </el-row>
            <el-row class="row-gutter" v-if="infoform.autoSyncMode !== 0">
              <el-col :span="8">
                <label class="key-text">列名转换方法</label>
              </el-col>
              <el-col :span="16">
                <label class="value-text">
                  <span v-if="infoform.columnNameCase === 'NONE'">无转换</span>
                  <span v-else-if="infoform.columnNameCase === 'UPPER'">转大写</span>
                  <span v-else-if="infoform.columnNameCase === 'LOWER'">转小写</span>
                  <span v-else-if="infoform.columnNameCase === 'CAMEL'">下划线转驼峰</span>
                  <span v-else-if="infoform.columnNameCase === 'SNAKE'">驼峰转下划线</span>
                  <span v-else>--</span>
                </label>
              </el-col>
            </el-row>
            <el-row class="row-gutter" v-if="infoform.autoSyncMode !== 1">
              <el-col :span="8">
                <label class="key-text">数据批次大小</label>
              </el-col>
              <el-col :span="16">
                <label class="value-text">{{ infoform.batchSize || '--' }}</label>
              </el-col>
            </el-row>
            <el-row class="row-gutter" v-if="infoform.autoSyncMode !== 1">
              <el-col :span="8">
                <label class="key-text">通道队列大小</label>
              </el-col>
              <el-col :span="16">
                <label class="value-text">{{ infoform.channelSize || '--' }}</label>
              </el-col>
            </el-row>
            <el-row class="row-gutter" v-if="infoform.autoSyncMode !== 1">
              <el-col :span="8">
                <label class="key-text">同步操作方法</label>
              </el-col>
              <el-col :span="16">
                <label class="value-text">{{ infoform.targetSyncOption || '--' }}</label>
              </el-col>
            </el-row>
            <el-row class="row-gutter" v-if="infoform.autoSyncMode !== 1">
              <el-col :span="8">
                <label class="key-text">同步前置执行SQL脚本</label>
              </el-col>
              <el-col :span="16">
                <label class="value-text">
                  {{ infoform.targetBeforeSqlScripts || '[SQL脚本内容为空]' }}
                </label>
              </el-col>
            </el-row>
            <el-row class="row-gutter" v-if="infoform.autoSyncMode !== 1">
              <el-col :span="8">
                <label class="key-text">同步后置执行SQL脚本</label>
              </el-col>
              <el-col :span="16">
                <label class="value-text">
                  {{ infoform.targetAfterSqlScripts || '[SQL脚本内容为空]' }}
                </label>
              </el-col>
            </el-row>
          </div>
        </div>
      </div>

      <!-- 映射规则区域 -->
      <div class="mapper">
        <div class="table-left">
          <div class="mapper-title">表名映射规则</div>
          <el-table 
            :data="infoform.tableNameMapper || []"
            style="width: 100%"
            :row-class-name="tableRowClassName"
            empty-text="无表名映射规则">
            <el-table-column prop="fromPattern" label="表名匹配的正则名" />
            <el-table-column prop="toValue" label="替换的目标值" />
          </el-table>
        </div>
        <div class="table-right">
          <div class="mapper-title">字段名映射规则</div>
          <el-table 
            :data="infoform.columnNameMapper || []"
            style="width: 100%"
            :row-class-name="tableRowClassName"
            empty-text="无字段名映射规则">
            <el-table-column prop="fromPattern" label="表名匹配的正则名" />
            <el-table-column prop="toValue" label="替换的目标值" />
          </el-table>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { defineProps, computed } from 'vue';
// 导入API请求模块（示例）
// import { getTaskDetail } from '@/api/data/pipeline/task.js';

// 定义Props
const props = defineProps({
  infoform: {
    type: Object,
    default: () => ({
      id: 0,
      name: "--",
      description: "--",
      scheduleMode: "MANUAL",
      cronExpression: "",
      sourceConnectionId: 0,
      sourceTypeName: 'MySQL',
      sourceSchema: "",
      runStatus: "",
      tableType: "TABLE",
      includeOrExclude: "",
      sourceTables: [],
      incrTableColumns: [],
      sourceBeforeSqlScripts: "",
      sourceAfterSqlScripts: "",
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
      targetBeforeSqlScripts: '',
      targetAfterSqlScripts: '',
    })
  }
});

// 获取图标（兼容处理）
const getSourceIcon = (typeName) => {
  if (!typeName) return '';
  try {
    return require(`@/assets/icons/${typeName}.png`);
  } catch (error) {
    // 图标不存在时返回默认图
    return 'https://picsum.photos/60/60';
  }
};

// 表格行样式类名
const tableRowClassName = ({ row, rowIndex }) => {
  if (rowIndex === 1) {
    return 'warning-row';
  } else if (rowIndex === 3) {
    return 'success-row';
  }
  return '';
};

// 示例：如果需要从API加载数据（可根据实际需求使用）
// const loadTaskDetail = async (taskId) => {
//   try {
//     const res = await getTaskDetail(taskId);
//     // 这里可以更新infoform（如果是本地状态）
//   } catch (error) {
//     console.error('加载任务详情失败：', error);
//   }
// };
</script>

<style scoped>
.task-detail-container {
  margin-top: 15px;
}

.box-card {
  width: 100%;
}

.row-gutter {
  margin-bottom: 12px;
}

.common-box {
  margin-top: 16px;
  width: 100%;
  display: flex;
  flex-direction: column;
  row-gap: 16px;
  padding-bottom: 24px;
}

.datainfo {
  display: flex;
  justify-content: center;
  align-items: flex-start; /* 修正对齐方式 */
  width: 100%;
  gap: 24px;
}

.source, .target {
  flex: 1;
  border-radius: 4px;
  border: 1px solid #e5e7eb; /* 增加边框提升视觉层次 */
}

.head {
  display: flex;
  height: 72px;
  align-items: center;
  background-color: #f7fbff;
  width: 100%;
  justify-content: flex-start; /* 修正对齐方式 */
  border-bottom: 1px solid #e5e7eb;
}

.head-img {
  width: 60px; /* 修正宽度 */
  padding-left: 24px;
}

.head-text {
  flex-grow: 1;
  padding-left: 16px; /* 调整间距 */
}

.title {
  font-size: 14px;
  color: #0051ff;
  font-weight: bold;
}

.sub-title {
  margin-top: 4px;
  font-size: 12px;
  color: #7d7d7d;
}

.body {
  background-color: white;
  padding: 24px;
  min-height: 328px;
}

/* 键值文本样式统一 */
.key-text {
  font-size: 13px;
  color: #666;
  font-weight: 500;
}

.value-text {
  font-size: 13px;
  color: #333;
}

/* 表名列表样式优化 */
.table-name-list {
  max-height: 120px;
  overflow-y: auto;
}

.table-name-list ul {
  padding: 0;
  margin: 0;
  list-style: none;
}

.table-name-list li {
  padding: 4px 2px;
  border-bottom: 1px solid #e0e0e0;
  font-size: 13px;
}

.empty-tip {
  font-size: 13px;
  color: #999;
  padding: 4px 0;
}

/* 映射规则区域样式 */
.mapper {
  display: flex;
  justify-content: center;
  align-items: flex-start;
  width: 100%;
  gap: 24px;
}

.table-left, .table-right {
  background-color: white;
  width: 100%;
  padding: 24px;
  border: 1px solid #e5e7eb;
  border-radius: 4px;
}

.mapper-title {
  font-size: 14px;
  font-weight: 500;
  margin-bottom: 12px;
  color: #333;
}

/* Element-Plus 表格样式适配 */
:deep(.el-table) {
  --el-table-row-hover-bg-color: #f8f9fa;
}

:deep(.el-table .el-table__cell) {
  padding: 8px 0;
  font-size: 13px;
}

:deep(.warning-row) {
  background: #fffbe6 !important;
}

:deep(.success-row) {
  background: #f0f9ff !important;
}
</style>