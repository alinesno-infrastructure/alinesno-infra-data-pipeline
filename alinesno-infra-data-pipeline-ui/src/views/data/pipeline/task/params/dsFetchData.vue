<template>
  <div style="margin-top:10px">

    <!-- <el-button @click="fetchData">Fetch Data</el-button> -->

    <el-table :data="tableData" style="width: 100%">
      <el-table-column
        v-for="(column, index) in columns"
        :key="index"
        :prop="column.key"
        :label="column.label"
        :width="column.width || ''"
        :show-overflow-tooltip="true"
      ></el-table-column>
    </el-table>
  </div>
</template>

<script setup>
import { ElMessage } from 'element-plus';

// 模拟数据
const tableData = ref([]);
const columns = ref([]);

// 模拟从后端获取数据的方法
function fetchData(mockData) {

  if(mockData.length == 0){
    return ;
  }
  
  // 获取列名
  const columnNames = Object.keys(mockData[0]);
  const columnLabels = columnNames.map(name => name);

  // 设置列宽
  const columnWidths = columnNames.map(name => (name === 'address' ? 250 : 180));

  // 构建列配置
  columns.value = columnNames.map((name, index) => ({
    key: name,
    label: columnLabels[index],
    width: columnWidths[index]
  }));

  tableData.value = mockData;
  ElMessage.success('Data fetched successfully!');
}

// 主动暴露方法
defineExpose({ fetchData })

</script>
