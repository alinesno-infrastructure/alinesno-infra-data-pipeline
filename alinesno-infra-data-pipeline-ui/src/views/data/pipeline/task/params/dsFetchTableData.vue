<template>

  <div>

      <el-table
        ref="singleTableRef"
        :data="tableData"
        highlight-current-row
        style="width: 100%"
        @current-change="handleCurrentChange"
      >
        <el-table-column type="index" label="序号" width="50" />
        <el-table-column property="tableName" label="表名" />
        <el-table-column property="tableComment" label="表注释" />
        <el-table-column property="dataSize" label="数据量" width="120" />
        <el-table-column property="isSelect" label="是否选择" width="120" />
      </el-table>

      <!-- <div style="margin-top: 20px">
        <el-button @click="setCurrent(tableData[1])">Select second row</el-button>
        <el-button @click="setCurrent()">Clear selection</el-button>
      </div> -->

  </div>

</template>

<script setup>

const currentRow = ref()
const singleTableRef = ref(null)

const handleCurrentChange = (val) => {
  currentRow.value = val
}

const tableData = ref([
  {
    "tableName": "users",
    "tableComment": "用户信息表",
    "dataSize": 1000,
    "isSelect": false
  },
  {
    "tableName": "orders",
    "tableComment": "订单信息表",
    "dataSize": 5000,
    "isSelect": false
  },
  {
    "tableName": "products",
    "tableComment": "产品信息表",
    "dataSize": 2000,
    "isSelect": false
  },
  {
    "tableName": "departments",
    "tableComment": "部门信息表",
    "dataSize": 150,
    "isSelect": false
  }
])

/** 获取当前行数据 */
function handleSubmitCurrentRow(){
  return currentRow.value.tableName
}

// 暴露给父组件
defineExpose({ handleSubmitCurrentRow })

</script>

<style scoped lang="scss">
</style>


