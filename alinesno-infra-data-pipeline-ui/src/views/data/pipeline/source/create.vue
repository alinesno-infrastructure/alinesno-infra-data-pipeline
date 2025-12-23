<template>
  <!-- 传递 props 保持原有逻辑，Vue3 中 props 传递方式兼容 -->
  <common-data-source :is-read-only="false"></common-data-source>
</template>

<script setup lang="ts">
import CommonDataSource from './common.vue'
</script>
