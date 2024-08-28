<template>

  <div>

      <el-table
        ref="singleTableRef"
        :data="tableData"
        highlight-current-row
        style="width: 100%"
        @current-change="handleCurrentChange"
      >
        <el-table-column align="center" type="index" label="序号" width="50" />
        <el-table-column property="name" label="表名" />
        <el-table-column property="comment" label="表注释" />
        <el-table-column align="center" property="dataSize" label="数据量" width="120" />
        <el-table-column property="isSelect" label="是否选择" width="120" />
      </el-table>

      <!-- <div style="margin-top: 20px">
        <el-button @click="setCurrent(tableData[1])">Select second row</el-button>
        <el-button @click="setCurrent()">Clear selection</el-button>
      </div> -->

  </div>

</template>

<script setup>

const currentRow = ref(null)
const singleTableRef = ref(null)

const handleCurrentChange = (val) => {
  currentRow.value = val
}

const tableData = ref([])

/** 获取当前行数据 */
function handleSubmitCurrentRow(){
  if(currentRow.value != null){
    return currentRow.value.name
  }
  return null ;
}

function fillData(rows){
  tableData.value = rows
}

// 暴露给父组件
defineExpose({ fillData , handleSubmitCurrentRow })

</script>

<style scoped lang="scss">
</style>


