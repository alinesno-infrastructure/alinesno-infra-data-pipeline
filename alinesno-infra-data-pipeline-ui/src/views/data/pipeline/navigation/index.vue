<template>
  <div class="metadata-sql-container">
    <el-card>
      <el-tabs v-model="tabActiveTabName" type="border-card">
        <!-- 元数据标签页 -->
        <el-tab-pane label="元数据" name="metadata">
          <div class="flex-between">
            <!-- 左侧数据源树 -->
            <el-aside class="tree-container">
              <div class="select-datasource-container">
                <el-select placeholder="请选择数据源" v-model="dataSourceId" @change="loadTreeData">
                  <el-option v-for="(item, index) in connectionList" :key="index" :label="`[${item.id}] ${item.name}`"
                    :value="item.id"></el-option>
                </el-select>
                <el-button type="primary" :disabled="metadataLoading" icon="el-icon-refresh" @click="loadTreeData">
                  刷新
                </el-button>
              </div>
              <el-scrollbar style="height: 680px;">
                <el-tree ref="metadataTreeRef" empty-text="请选择数据源后查看" :indent="6" :data="treeData" :props="props"
                  :load="loadTreeNode" :expand-on-click-node="true" :highlight-current="true"
                  :render-content="renderContent" @node-click="handleNodeClick" lazy></el-tree>
              </el-scrollbar>
            </el-aside>

            <!-- 右侧元数据详情 -->
            <el-main class="metadata-container">
              当前表：
              <el-tag>
                {{ currentNode.schemaName }} / {{ currentNode.tableName }}
              </el-tag>
              <el-tabs v-model="metadataActiveTabName" type="border-card">
                <!-- 基本信息 -->
                <el-tab-pane label="基本信息" name="first">
                  <el-descriptions :column="2" colon>
                    <el-descriptions-item label="模式名">
                      <el-tag>{{ tableMeta.schemaName }}</el-tag>
                    </el-descriptions-item>
                    <el-descriptions-item label="表名称">
                      <el-tag>{{ tableMeta.tableName }}</el-tag>
                    </el-descriptions-item>
                    <el-descriptions-item label="表类型">
                      <el-tag>{{ tableMeta.type }}</el-tag>
                    </el-descriptions-item>
                    <el-descriptions-item label="表注释">
                      <span class="long-text">{{ tableMeta.remarks }}</span>
                    </el-descriptions-item>
                    <el-descriptions-item label="建表DDL"></el-descriptions-item>
                  </el-descriptions>
                  <!-- 替换 Ace 编辑器为文本域 -->
                  <el-input v-model="tableMeta.createSql" type="textarea" :rows="20" style="width: 100%; resize: none"
                    readonly placeholder="暂无建表DDL信息"></el-input>
                </el-tab-pane>

                <!-- 字段信息 -->
                <el-tab-pane label="字段信息" name="seconde">
                  <el-table :header-cell-style="{ background: '#eef1f6', color: '#606266' }" :data="tableMeta.columns"
                    style="width: 100%">
                    <template #empty>
                      <span>单击点击左侧节点查看对应表的字段信息</span>
                    </template>
                    <el-table-column prop="fieldName" min-width="20%" show-overflow-tooltip
                      label="名称"></el-table-column>
                    <el-table-column prop="typeName" min-width="20%" label="数据类型"></el-table-column>
                    <el-table-column prop="fieldType" min-width="7%" label="类型枚举"></el-table-column>
                    <el-table-column prop="displaySize" min-width="7%" label="长度"></el-table-column>
                    <el-table-column prop="precision" min-width="5%" label="精度"></el-table-column>
                    <el-table-column prop="scale" min-width="5%" label="位数"></el-table-column>
                    <el-table-column prop="isPrimaryKey" min-width="5%" label="主键"></el-table-column>
                    <el-table-column prop="isAutoIncrement" min-width="5%" label="自增"></el-table-column>
                    <el-table-column prop="isNullable" min-width="5%" label="可空"></el-table-column>
                    <el-table-column prop="remarks" min-width="20%" show-overflow-tooltip label="注释"></el-table-column>
                  </el-table>
                </el-tab-pane>

                <!-- 索引信息 -->
                <el-tab-pane label="索引信息" name="third">
                  <el-table :header-cell-style="{ background: '#eef1f6', color: '#606266' }" :data="tableMeta.indexes"
                    style="width: 100%">
                    <template #empty>
                      <span>单击点击左侧节点查看对应表的索引信息</span>
                    </template>
                    <el-table-column prop="indexType" min-width="20%" label="索引类型"></el-table-column>
                    <el-table-column prop="indexName" min-width="20%" label="索引名称"></el-table-column>
                    <el-table-column prop="indexFields" :formatter="formatIndexFields" show-overflow-tooltip
                      min-width="60%" label="索引字段"></el-table-column>
                  </el-table>
                </el-tab-pane>

                <!-- 取样数据 -->
                <el-tab-pane class="table-container-data-table" label="取样数据" name="fourth">
                  <el-table :header-cell-style="{ background: '#eef1f6', color: '#606266' }"
                    :data="sampleData.rows || []">
                    <template #empty>
                      <span>单击点击左侧节点查看对应表的取样数据</span>
                    </template>
                    <el-table-column v-for="(item, index) in sampleData.columns || []" :prop="item" :label="item"
                      :key="index" show-overflow-tooltip></el-table-column>
                  </el-table>
                </el-tab-pane>
              </el-tabs>
            </el-main>
          </div>
        </el-tab-pane>

        <!-- SQL在线标签页 -->
        <el-tab-pane label="SQL在线" name="sqlQuery">
          <el-row :gutter="12" class="padding-row-stype">
            <el-col :span="6">
              <div class="sqlonline-select-suffix">
                <span class="text-label">数据源：</span>
                <el-select placeholder="请选择数据源" v-model="sqlDataSourceId">
                  <template v-for="item in connectionList">
                    <el-option v-if="item.useSql" :key="item.id" :label="`[${item.id}]${item.name}`"
                      :value="item.id"></el-option>
                  </template>
                </el-select>
              </div>
            </el-col>
            <el-col :span="6">
              <div class="sqlonline-select-suffix">
                <span class="text-label">最大记录数：</span>
                <el-select placeholder="选择结果集MaxRow" v-model="rsMaxRowCount">
                  <el-option v-for="(item, index) in maxRowCountList" :key="index" :label="item.name"
                    :value="item.id"></el-option>
                </el-select>
              </div>
            </el-col>
            <el-col :span="6">
              <div class="sqlonline-select-suffix">
                <span class="text-label">编辑器高度：</span>
                <el-input-number v-model="editorHeightNum" :step="10" step-strictly></el-input-number>
              </div>
            </el-col>
            <el-col :span="6">
              <div class="tool">
                <div class="item-button" @click="runAll">
                  <i class="el-icon-video-play"></i><span>执行</span>
                </div>
                <div class="item-button" @click="runSelected">
                  <i class="el-icon-caret-right"></i><span>选中执行</span>
                </div>
                <div class="item-button" @click="formatSql">
                  <i class="el-icon-postcard"></i><span>格式化</span>
                </div>
              </div>
            </el-col>
          </el-row>

          <el-row class="padding-row-stype">
            <div v-loading="sqlResultLoading" :style="{ height: `${editorHeightNum}px`, border: '1px solid #e6e6e6' }">
              <!-- 替换 Ace 编辑器为文本域 -->
              <el-input ref="sqlEditorRef" v-model="sqlContent" type="textarea" :rows="editorHeightNum / 25"
                style="width: 100%; height: 100%; resize: none" placeholder="请输入SQL语句"></el-input>
            </div>
          </el-row>

          <el-row class="padding-row-stype">
            <el-tabs v-model="activeResultTab" tab-position="top" type="border-card">
              <el-tab-pane label="信息" name="0">
                <div v-for="(one, idx) in sqlExecuteResult.summaries" :key="idx" class="sql-summary-item">
                  [SQL]: {{ one.sql }}<br />{{ one.summary }}<br /><br />
                </div>
              </el-tab-pane>
              <el-tab-pane v-for="(one, idx) in sqlExecuteResult.results" :key="idx + 1" :label="`结果${idx + 1}`"
                :name="`${idx + 1}`">
                <el-table :header-cell-style="{
                  background: '#eef1f6',
                  color: '#606266',
                  'font-size': '12px',
                }" style="width: 100%; max-height: 400px; overflow: auto" height="400px" :data="one.rows || []">
                  <template #empty>
                    <span>SQL结果为空</span>
                  </template>
                  <el-table-column v-for="(item, index) in one.columns || []" :prop="item.columnName" :key="index"
                    show-overflow-tooltip>
                    <template #header>
                      {{ item.columnName }}<br />({{ item.columnType }})
                    </template>
                  </el-table-column>
                </el-table>
              </el-tab-pane>
            </el-tabs>
          </el-row>
        </el-tab-pane>
      </el-tabs>
    </el-card>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from "vue";
import { ElMessage, ElAlert } from "element-plus";
// 关键修改：替换 require 为 ESModule 导入
import { format } from "sql-formatter";

// 引入封装的接口
import {
  loadConnectionsApi,
  loadSchemasApi,
  loadTablesListApi,
  getTableMetaApi,
  getTableDataApi,
  executeSqlScriptApi,
} from "@/api/data/pipeline/metadataSqlApi";

// 响应式数据
const metadataLoading = ref(false);
const props = ref({
  label: "label",
  children: "children",
  disabled: false,
  isLeaf: false,
});
// 移除 Ace 编辑器相关配置
const dataSourceId = ref(null);
const connectionList = ref([]);
const treeData = ref([]);
const currentNode = ref({
  tableName: "-",
  schemaName: "-",
});
const tabActiveTabName = ref("metadata");
const metadataActiveTabName = ref("first");
const tableMeta = ref({
  tableName: "-",
  schemaName: "-",
  remarks: "",
  type: "-",
  createSql: "",
  primaryKeys: [],
  columns: [],
  indexes: [],
});
const sampleData = ref({});
const sqlDataSourceId = ref(null);
// 新增 SQL 内容绑定
const sqlContent = ref("");
const rsMaxRowCount = ref(200);
const maxRowCountList = ref([
  { id: 100, name: "100条" },
  { id: 200, name: "200条" },
  { id: 500, name: "500条" },
  { id: 1000, name: "1000条" },
  { id: 2000, name: "2000条" },
  { id: 5000, name: "5000条" },
  { id: 10000, name: "10000条" },
]);
const editorHeightNum = ref(200);
const sqlResultLoading = ref(false);
const sqlExecuteResult = ref({
  summaries: [],
  results: [],
});
const activeResultTab = ref("0");

// 移除 Ace 编辑器 Ref
const metadataTreeRef = ref(null);

// 加载数据源列表
const loadConnections = async () => {
  try {
    const res = await loadConnectionsApi();
    if (res.code === 200) {
      connectionList.value = res.data;
    } else {
      ElMessage.error(`加载数据失败: ${res.message || "未知错误"}`);
      connectionList.value = [];
    }
  } catch (err) {
    ElMessage.error(`加载数据源失败: ${err.message}`);
    connectionList.value = [];
  }
};

// 加载树数据
const loadTreeData = async () => {
  if (!dataSourceId.value || dataSourceId.value <= 0) return;

  treeData.value = [];
  metadataLoading.value = true;

  try {
    const res = await loadSchemasApi(dataSourceId.value);
    metadataLoading.value = false;

    if (res.code === 200) {
      const treeNodes = res.data.map((element) => ({
        dataSourceId: dataSourceId.value,
        label: element,
        parent: dataSourceId.value,
        value: element,
        hasChild: true,
        type: "DATABASE",
      }));
      treeData.value = treeNodes;
    } else {
      ElMessage.error(`加载失败：${res.message}`);
    }
  } catch (err) {
    metadataLoading.value = false;
    ElMessage.error(`加载树数据失败：${err.message}`);
  }
};

// 加载树节点（懒加载）
const loadTreeNode = async (node, resolve) => {
  setTimeout(async () => {
    if (node.level === 1) {
      // 一级节点：数据库 -> 表/视图分类
      const tableView = [
        {
          dataSourceId: dataSourceId.value,
          label: "表",
          parent: dataSourceId.value,
          value: node.label,
          hasChild: true,
          type: "TABLE",
        },
        {
          dataSourceId: dataSourceId.value,
          label: "视图",
          parent: dataSourceId.value,
          value: node.label,
          hasChild: true,
          type: "VIEW",
        },
      ];
      resolve(tableView);
    } else if (node.level === 2) {
      // 二级节点：表/视图分类 -> 具体表名
      await loadTablesList(resolve, dataSourceId.value, node.data.value, node.data.type);
    } else {
      resolve([]);
    }
  }, 500);
};

// 加载表列表
const loadTablesList = async (resolve, dataSourceId, schema, type) => {
  metadataLoading.value = true;

  try {
    const res = await loadTablesListApi(dataSourceId, schema, type);
    metadataLoading.value = false;

    if (res.code === 200) {
      const tableList = res.data.map((element) => ({
        dataSourceId,
        label: element,
        parent: dataSourceId,
        value: schema,
        hasChild: false,
        type,
      }));
      resolve(tableList);
    } else {
      ElAlert(`加载失败：${res.data.message}`, "数据加载失败");
      resolve([]);
    }
  } catch (err) {
    metadataLoading.value = false;
    ElMessage.error(`加载表列表失败：${err.message}`);
    resolve([]);
  }
};

// 自定义树节点渲染
const renderContent = (h, { node, data }) => {
  if (node.level === 1) {
    return h(
      "div",
      { class: "custom-tree-node" },
      [
        h("i", { class: "iconfont icon-shujuku1" }),
        h(
          "el-tooltip",
          {
            class: "item",
            effect: "light",
            placement: "left",
          },
          [
            h("div", { slot: "content" }, node.label),
            // h("span", {}, ` ${data.label}`),
          ]
        ),
      ]
    );
  } else if (node.level === 2) {
    let iconPic = h("img", { style: { width: "16px" }, src: "" });
    if (data.type === "VIEW") {
      iconPic = h("i", { class: "iconfont icon-viewList" });
    }
    return h(
      "div",
      { class: "custom-tree-node" },
      [iconPic, h("span", {}, ` ${data.label}`)]
    );
  } else if (node.level === 3) {
    let iconPic = h("i", { class: "iconfont icon-shitu_biaoge" });
    if (data.type === "VIEW") {
      iconPic = h("img", { style: { width: "16px" }, src: "" });
    }
    return h(
      "div",
      { class: "custom-tree-node" },
      [
        iconPic,
        h(
          "el-tooltip",
          {
            class: "item",
            effect: "light",
            placement: "left",
          },
          [
            h("div", { slot: "content" }, node.label),
            // h("span", {}, ` ${data.label}`),
          ]
        ),
      ]
    );
  } else {
    return h(
      "div",
      { class: "custom-tree-node" },
      [
        h("i", { class: "el-icon-set-up" }),
        h(
          "el-tooltip",
          {
            class: "item",
            effect: "light",
            placement: "left",
          },
          [
            h("div", { slot: "content" }, data.type),
            // h("span", {}, `${data.label}(${data.type})`),
          ]
        ),
      ]
    );
  }
};

// 格式化索引字段
const formatIndexFields = (row) => {
  if (!row.indexFields || row.indexFields.length === 0) return "";
  return row.indexFields
    .map((item) => {
      if (item.ascOrder === null) return item.fieldName;
      return item.ascOrder ? `${item.fieldName} ASC` : `${item.fieldName} DESC`;
    })
    .join(";");
};

// 树节点点击事件
const handleNodeClick = async (data) => {
  const { type, dataSourceId, value: schema, label: table } = data;
  if ((type === "VIEW" || type === "TABLE") && !data.hasChild && dataSourceId && schema && table) {
    tabActiveTabName.value = "metadata";
    metadataActiveTabName.value = "first";
    clearDataSet();
    await getTableMeta(dataSourceId, schema, table);
    await getTableData(dataSourceId, schema, table);
  }
};

// 清空数据集
const clearDataSet = () => {
  tableMeta.value = {
    tableName: "-",
    schemaName: "-",
    remarks: "",
    type: "-",
    createSql: "",
    primaryKeys: [],
    columns: [],
    indexes: [],
  };
  sampleData.value = {};
};

// 获取表元数据
const getTableMeta = async (id, schema, table) => {
  try {
    const res = await getTableMetaApi(id, schema, table);
    if (res.code === 200) {
      tableMeta.value = res.data;
      currentNode.value = { tableName: table, schemaName: schema };
    } else {
      ElMessage.error(`加载表元数据失败：${res.message}`);
    }
  } catch (err) {
    ElMessage.error(`加载表元数据失败：${err.message}`);
  }
};

// 获取表取样数据
const getTableData = async (id, schema, table) => {
  try {
    const res = await getTableDataApi(id, schema, table);
    if (res.code === 200) {
      sampleData.value = res.data;
    } else {
      ElMessage.error(`加载表数据失败：${res.message}`);
    }
  } catch (err) {
    ElMessage.error(`加载表数据失败：${err.message}`);
  }
};

// 格式化SQL（适配文本域）
const formatSql = () => {
  if (sqlResultLoading.value) return;
  if (!sqlContent.value) {
    ElMessage.warning("SQL文本内容为空");
    return;
  }
  try {
    // 关键修改：使用导入的 format 方法
    const formattedSql = format(sqlContent.value);
    sqlContent.value = formattedSql;
  } catch (err) {
    ElMessage.error(`SQL格式化失败：${err.message}`);
  }
};

// 执行全部SQL
const runAll = () => {
  if (!sqlContent.value) {
    ElMessage.warning("SQL文本内容为空");
    return;
  }
  executeSqlScript(sqlContent.value);
};

// 执行选中SQL（文本域暂不支持选中，提示用户）
const runSelected = () => {
  ElMessage.warning("普通文本域暂不支持选中执行，请使用全部执行或后续升级为专业编辑器");
  // 如需兼容，可保留原逻辑或直接调用 runAll
  // runAll();
};

// 执行SQL脚本
const executeSqlScript = async (sqlScript) => {
  if (sqlResultLoading.value) {
    ElMessage.warning("已有一个查询正在进行中");
    return;
  }
  if (!sqlDataSourceId.value || sqlDataSourceId.value < 0) {
    ElMessage.warning("请首先选择一个数据源");
    return;
  }

  sqlResultLoading.value = true;
  sqlExecuteResult.value = { summaries: [], results: [] };

  try {
    const res = await executeSqlScriptApi(sqlDataSourceId.value, sqlScript, 1, rsMaxRowCount.value);
    sqlResultLoading.value = false;
    if (res.code === 200) {
      sqlExecuteResult.value = res.data;
      activeResultTab.value = "0";
    } else {
      ElMessage.error(`SQL执行报错: ${res.message}`);
    }
  } catch (err) {
    sqlResultLoading.value = false;
    ElMessage.error(`SQL执行失败: ${err.message}`);
  }
};

// 生命周期：挂载时加载数据源
onMounted(() => {
  loadConnections();
  loadTreeData();
});
</script>

<style lang="scss" scoped>
.metadata-sql-container {
  width: 100%;
  height: 100%;
  padding: 8px;

  .el-card {
    width: 100%;
    height: 100%;
    min-height: 200px;
  }

  .flex-between {
    display: flex;
    gap: 8px;
  }

  .tree-container {
    min-width: 25%;
    position: relative;
    cursor: default;
    color: #000;
    font-size: 14px;
    background-size: 16px;

    .select-datasource-container {
      display: flex;
      gap: 8px;
      margin-bottom: 8px;
      align-items: center;

      .el-select {
        width: calc(100% - 60px);
      }
    }

    :deep(.el-scrollbar) {
      .el-scrollbar__wrap {
        overflow-x: hidden;
      }

      .el-scrollbar__bar {
        opacity: 1;
      }

      .el-scrollbar__thumb {
        background: #dcdfe6;
      }
    }
  }

  .custom-tree-node {
    font-size: 12px;
    background-size: 16px;
    display: flex;
    align-items: center;
    gap: 4px;
  }

  .metadata-container {
    flex: 1;
    padding: 4px;

    .long-text {
      display: -webkit-box;
      width: 300px;
      white-space: normal !important;
      overflow: hidden;
      text-overflow: ellipsis;
      text-align: left;
      -webkit-line-clamp: 3;
      -webkit-box-orient: vertical;
    }
  }

  .table-container-data-table {
    height: 90%;
    overflow-y: auto;
    overflow-x: hidden;
  }

  :deep(.el-tabs--border-card) {
    .el-tabs__header {
      .el-tabs__item {
        margin-left: 8px;
        border: none;
        border-radius: 8px 8px 0 0;
        background-color: #f3f7fe;
        padding: 4px 20px;
        color: #0065d5;
        line-height: 22px;
        height: 30px;

        &.is-active {
          background-color: #0065d5;
          color: #ffffff;
        }
      }
    }
  }

  .sqlonline-select-suffix {
    display: flex;
    justify-content: flex-start;
    align-items: center;
    gap: 4px;

    .text-label {
      font-size: 11px;
      font-weight: 700;
      white-space: nowrap;
    }
  }

  .tool {
    display: flex;
    justify-content: flex-end;
    gap: 16px;

    .item-button {
      display: inline-flex;
      align-items: center;
      gap: 4px;
      font-size: 20px;
      color: #009966;
      line-height: 26px;
      cursor: pointer;

      span {
        color: #000;
        font-size: 16px;
      }

      &:hover {
        color: #00b377;
      }
    }
  }

  .padding-row-stype {
    padding: 5px;
  }

  .sql-summary-item {
    font-size: 12px;
    line-height: 1.5;
    color: #333;
    white-space: pre-wrap;
  }
}
</style>