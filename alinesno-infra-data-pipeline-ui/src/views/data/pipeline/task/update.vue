<template>
  <commonEdit></commonEdit>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import commonEdit from './common/edit'

// // 示例：添加响应式数据
// const isLoading = ref(false)

// // 示例：添加方法
// const handleInit = () => {
//   isLoading.value = true
//   // 可添加初始化逻辑
//   setTimeout(() => {
//     isLoading.value = false
//   }, 1000)
// }

// // 示例：添加生命周期
// onMounted(() => {
//   handleInit()
// })
</script>

<style scoped>
</style>