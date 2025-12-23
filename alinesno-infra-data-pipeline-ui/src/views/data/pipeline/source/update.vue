<template>
  <commonDataSource :isReadOnly="isReadOnly"></commonDataSource>
</template>

<script setup>
import { ref } from 'vue'
import CommonDataSource from './common'

// 示例：添加响应式数据（如需扩展功能时使用）
const isReadOnly = ref(false)
</script>

<style scoped>
</style>