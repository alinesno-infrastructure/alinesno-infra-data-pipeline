<template>
  <div class="app-container">
  <el-card>
      <h3 class="h-title">请选择数据源类型</h3>
      <el-row>
        <el-col
          :span="4"
          v-for="(item, index) in databaseTypes"
          :key="index"
          :offset="1"
        >
          <div style="margin-top: 20px; cursor: pointer">
            <el-card
              class="card-item"
              :body-style="{ padding: '0px' }"
              shadow="hover"
              @click.native="selectDataSourceType(item, index)"
              :class="selectedIndex === index ? 'active' : ''"
            >
              <div style="display: inline-block; float: left">
                <img
                  :src="getIconPath(item.name)"
                  class="image"
                  alt=""
                />
              </div>
              <div style="display: inline-block; float: left; padding: 10px 0 0 10px">
                <span>{{ item.name }}</span><br />
              </div>
            </el-card>
          </div>
        </el-col>
      </el-row>
      <el-row>
        <el-button type="primary" class="next" @click="next">下一步</el-button>
        <el-button class="cancel" @click="cancel">取消</el-button>
      </el-row>
  </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { getCurrentInstance } from 'vue'
import { getDatabaseTypes } from '@/api/data/pipeline/dataSource'

const { proxy } = getCurrentInstance()
const router = proxy.$router

// 响应式数据
const databaseTypes = ref([])
const selectedIndex = ref(-1)
const selectedDataSource = ref(null)

// 加载数据库类型
function loadDatabaseTypes() {
  getDatabaseTypes().then(response => {
    console.log(response.code)
    if (response?.code === 200) {
      databaseTypes.value = response.data.map((item, idx) => ({
        id: idx + 1,
        name: item.name,
        type: item.name // 若后端返回的是字符串数组，type 即为 name
      }))
    } else {
      proxy.$modal.msgError('加载数据库类型失败：' + (response?.message || ''))
    }
  }).catch(() => {
    proxy.$modal.msgError('网络请求异常')
  })
}

// 选择类型
function selectDataSourceType(item, index) {
  selectedIndex.value = index
  selectedDataSource.value = item
}

// 下一步
function next() {
  if (selectedIndex.value < 0 || !selectedDataSource.value) {
    proxy.$message.error('请选择数据库类型！')
    return
  }
  router.push({
    path: '/data/pipeline/source/create',
    query: { type: selectedDataSource.value.type }
  })
}

// 取消
function cancel() {
  router.push('/connection/list')
}

// 动态获取图标路径（避免 require 报错）
function getIconPath(name) {
  try {
    return new URL(`../../../../assets/icons/${name}.png`, import.meta.url).href
  } catch (e) {
    return new URL(`../../../../assets/icons/default.png`, import.meta.url).href
  }
}

// 初始化
onMounted(() => {
  loadDatabaseTypes()
})
</script>

<style scoped>
.el-card {
  border-radius: 4px;
  overflow: visible;
}

.card-item {
  border-radius: 10px;
  overflow: hidden;
}

.el-header,
.el-main,
.el-footer {
  background-color: white;
}

.h-title {
  font-weight: bolder;
  font-size: 20px;
}

.image {
  display: inline-block;
  width: 60px;
  height: 60px;
  padding: 2px 0 0 2px;
}

.active {
  background-color: #ffffff !important;
  border: 1px solid #409eff;
}

.cancel {
  float: right;
  padding: 6px 14px;
  border: 1px solid #dcdcdd;
  cursor: pointer;
  background-color: white;
}

.next {
  float: right;
  margin-left: 20px;
  padding: 6px 14px;
  border: none;
  color: white;
  background-color: #409eff;
  cursor: pointer;
}
</style>