<template>
  <div>
    <el-table :data="originalFields" style="width: 100%">
      <el-table-column align="center" type="index" label="序号" width="50" />

      <el-table-column align="left" prop="originalField" label="原表字段">
        <template #default="scope">
          <el-select v-model="scope.row.originalField" placeholder="选择字段" style="width:100%">
            <el-option
              v-for="item in originalFields"
              :key="item.name"
              :label="item.name"
              :value="item.name"
            >
              {{ item.name }}
            </el-option>
          </el-select>
        </template>
      </el-table-column>

      <el-table-column align="center" prop="targetField" label="目标表字段">
        <template #default="scope">
          <el-select v-model="scope.row.targetField" placeholder="选择字段" clearable style="width:100%">
            <el-option
              v-for="item in targetFields"
              :key="item.name"
              :label="item.name"
              :value="item.name"
            >
              {{ item.name }} 
            </el-option>
          </el-select>
        </template>
      </el-table-column>
    </el-table>

    <br/>

  </div>
</template>

<script setup>

// const props = defineProps({
//   originalFields: {
//     type: Array,
//   },
//   targetFields: {
//     type: Array,
//   },
// });

// 在Vue 3中，你可以直接使用ref来管理响应式状态
const originalFields = ref([]) ;// ref(props.originalFields);
const targetFields = ref([]) ; // ref(props.targetFields);

// onMounted(() => {
//   console.log(`the component is now mounted.`)
// })

/** 获取映射关系 */
function handleMappingData(o, t){

  console.log("o = " + o + " , t = " + t);

  originalFields.value = o ;
  targetFields.value = t;

  // 设置默认值
  originalFields.value.forEach((field) => {
    field.originalField = field.name;
    field.targetField = field.name; 
  });

  targetFields.value.forEach((field) => {
    field.targetField = field.name; 
  });

  nextTick(() => {
    // 设置默认值
    originalFields.value.forEach((field, index) => {
      field.originalField = field.name;
    });

    targetFields.value.forEach((field, index) => {
      field.targetField = field.name;
    });
  });
}

// 提交映射关系
function submitMapping() {
  const mappings = originalFields.value
    .filter(field => field.targetField !== '') // 过滤掉targetField为空的条目
    .map(field => ({
      mappingPlugin: "copy",
      sourceField: field.originalField,
      targetField: field.targetField
    }));

  return mappings;
}

// 主动暴露方法
defineExpose({ submitMapping , handleMappingData })

</script>