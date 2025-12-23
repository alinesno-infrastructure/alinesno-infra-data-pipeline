<template>
  <!-- Vue3 推荐模板中使用 kebab-case 组件名（与导入的驼峰名自动映射） -->
  <common-edit></common-edit>
</template>

<script setup>
// 1. 导入组件（Vue3 推荐导入时使用 PascalCase 命名）
import CommonEdit from './common/edit'

// 2. 【可选】如果需要定义响应式数据/方法，示例如下：
// import { ref, reactive, onMounted } from 'vue'
// 
// // 响应式数据
// const formData = reactive({})
// const isLoading = ref(false)
// 
// // 生命周期钩子
// onMounted(() => {
//   console.log('组件挂载完成')
// })
// 
// // 自定义方法
// const handleSubmit = () => {
//   console.log('提交逻辑')
// }
</script>

<style scoped>
/* 保留原有样式，如需扩展可在此添加 */
</style>