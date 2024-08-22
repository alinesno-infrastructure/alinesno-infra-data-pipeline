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
              :label="item.name + ' - ' + item.description"
              :value="item.name"
            >
              {{ item.name }} - {{ item.description }}
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
              :label="item.name + ' - ' + item.description"
              :value="item.name"
            >
              {{ item.name }} - {{ item.description }}
            </el-option>
          </el-select>
        </template>
      </el-table-column>
    </el-table>

    <br/>

  </div>
</template>

<script setup>
import { ref } from 'vue';

const originalFields = ref([
  { name: 'id', type: 'int', originalField: 'id', targetField: 'id', description: '主键ID' },
  { name: 'name', type: 'varchar', originalField: 'name', targetField: 'name', description: '名称' },
  { name: 'age', type: 'int', originalField: 'age', targetField: 'age', description: '年龄' },
  { name: 'gender', type: 'varchar', originalField: 'gender', targetField: 'gender', description: '性别' },
  { name: 'birthdate', type: 'date', originalField: 'birthdate', targetField: 'birthdate', description: '出生日期' },
  { name: 'address', type: 'varchar', originalField: 'address', targetField: 'address', description: '地址' },
  { name: 'email', type: 'varchar', originalField: 'email', targetField: 'email', description: '电子邮件' },
  { name: 'phone', type: 'varchar', originalField: 'phone', targetField: 'phone', description: '电话号码' },
  { name: 'maritalStatus', type: 'varchar', originalField: 'maritalStatus', targetField: 'maritalStatus', description: '婚姻状况' },
  { name: 'height', type: 'decimal', originalField: 'height', targetField: 'height', description: '身高' },
  { name: 'weight', type: 'decimal', originalField: 'weight', targetField: 'weight', description: '体重' }
]);

const targetFields = ref([
  { name: 'id', type: 'int', targetField: 'id', description: '主键ID' },
  { name: 'name', type: 'varchar', targetField: 'name', description: '名称' },
  { name: 'age', type: 'int', targetField: 'age', description: '年龄' },
  { name: 'gender', type: 'varchar', targetField: 'gender', description: '性别' },
  { name: 'birthdate', type: 'date', targetField: 'birthdate', description: '出生日期' },
  { name: 'address', type: 'varchar', targetField: 'address', description: '地址' },
  { name: 'email', type: 'varchar', targetField: 'email', description: '电子邮件' },
  { name: 'phone', type: 'varchar', targetField: 'phone', description: '电话号码' },
  { name: 'maritalStatus', type: 'varchar', targetField: 'maritalStatus', description: '婚姻状况' },
  { name: 'height', type: 'decimal', targetField: 'height', description: '身高' },
  { name: 'weight', type: 'decimal', targetField: 'weight', description: '体重' }
]);


nextTick(() => {
  // 设置默认值
  originalFields.value.forEach((field, index) => {
    field.originalField = field.name;
  });

  targetFields.value.forEach((field, index) => {
    field.targetField = field.name;
  });
});

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
defineExpose({ submitMapping })

</script>