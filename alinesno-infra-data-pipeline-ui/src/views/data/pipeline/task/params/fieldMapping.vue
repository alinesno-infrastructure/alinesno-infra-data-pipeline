<template>
  <div>
    <el-table :data="originalFields" style="width: 100%">
      <el-table-column align="center" type="index" label="序号" width="50" />

      <el-table-column align="left" prop="date" label="原字段">
        <template #default="scope">
            <!-- <el-select value="id" placeholder="选择字段" style="width:100%">
                <el-option
                    v-for="item in originalFields"
                    :key="item.name"
                    :label="item.name"
                    :value="item.name"
                >
                {{ item.name }} - {{ item.description }}
                </el-option>
            </el-select> -->
                {{ scope.row.name }} - {{ scope.row.description }}
        </template>
      </el-table-column>

      <el-table-column align="center" prop="date" label="目标字段">
        <template #default="scope">
            <el-select v-model="targerV" placeholder="选择字段" style="width:100%">
                <el-option
                    v-for="item in originalFields"
                    :key="item.name"
                    :label="item.name"
                    :value="item.name"
                >
                {{ item.name }} - {{ item.description }}
                </el-option>
            </el-select>
        </template>
      </el-table-column>

    </el-table>
  </div>
</template>

<script setup>
import { ref } from 'vue';

const targerV = ref([]);
const originalFields = ref([
  { name: 'id', type: 'int', description: '主键ID' },
  { name: 'name', type: 'varchar', description: '名称' },
  { name: 'age', type: 'int', description: '年龄' },
  { name: 'gender', type: 'varchar', description: '性别' },
  { name: 'birthdate', type: 'date', description: '出生日期' },
  { name: 'address', type: 'varchar', description: '地址' },
  { name: 'email', type: 'varchar', description: '电子邮件' },
  { name: 'phone', type: 'varchar', description: '电话号码' },
  { name: 'maritalStatus', type: 'varchar', description: '婚姻状况' },
  { name: 'height', type: 'decimal', description: '身高' },
  { name: 'weight', type: 'decimal', description: '体重' }
]);

</script>