<template>
  <div class="app-container driver-config-page">
    <el-card shadow="never">
      <div class="container">
        <el-card shadow="never" class="box-card">
          <template v-slot:header>
            <div class="card-header">
              <span><b>数据库产品类型列表</b></span>
            </div>
          </template>
          <div class="navsBox">
            <el-scrollbar style="height: 600px;">
              <ul>
                <li 
                  v-for="(item, index) in connectionTypes" 
                  :key="index"
                  @click="handleChooseClick(item.type, index)"
                  :class="{ active: index === isActive }"
                >
                  [{{ item.id }}] {{ item.name }}
                </li>
              </ul>
            </el-scrollbar>
          </div>
        </el-card>

        <div class="contentBox">
          <div class="add-btn-wrapper">
            <el-button 
              type="primary" 
              icon="Plus"
              @click="dialogVisible = true"
            >
              添加
            </el-button>
          </div>
          <el-table 
            :header-cell-style="{ background: '#eef1f6', color: '#606266' }"
            :data="versionDrivers"
            stripe
          >
            <template v-slot:empty>
              <span>单击左侧数据库类型来查看对应的驱动版本信息</span>
            </template>
            <el-table-column 
              prop="driverVersion" 
              label="驱动版本号" 
              min-width="30%"
            ></el-table-column>
            <el-table-column 
              prop="driverClass" 
              label="驱动类名" 
              min-width="30%"
            ></el-table-column>
            <el-table-column 
              prop="jarFiles" 
              :formatter="formatJarFileList" 
              label="驱动JAR名称" 
              min-width="40%"
            ></el-table-column>
          </el-table>
        </div>
      </div>
    </el-card>

    <el-dialog 
      title="添加说明"
      v-model="dialogVisible"
      width="40%"
      :before-close="handleClose"
    >
      <div class="dialog-content">
        <p>请按照驱动路径所在的目录${DBSWITCH_HOME}/drivers下，在数据库类型为名称的目录下，以驱动版本号为名称创建目录并放置对应的驱动jar文件，然后重启即可生效。具体可参考https://gitee.com/inrgihc/dbswitch/tree/master/drivers下的目录结构。</p>
        <p>特殊说明：驱动版本目录下的所有JAR必须无任何外部依赖，否则，也需将其依赖JAR一起放置到对应的目录下。</p>
      </div>
      <template v-slot:footer>
        <span class="dialog-footer">
          <el-button @click="dialogVisible = false">取 消</el-button>
          <el-button type="primary" @click="dialogVisible = false">确 定</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted, onUnmounted } from 'vue'
import { ElMessageBox, ElMessage } from 'element-plus'
import { 
  getConnectionTypes, 
  getDriverByType 
} from '@/api/data/pipeline/driverConfigApi' // 导入API方法

// 响应式数据
const dialogVisible = ref(false)
const loading = ref(true)
const connectionTypes = ref([])
const versionDrivers = ref([])
const isActive = ref(-1)

// 加载数据库类型列表
const loadConnectionTypes = async () => {
  try {
    const res = await getConnectionTypes()
    if (res.code === 200) {
      connectionTypes.value = res.data
      if (connectionTypes.value.length > 0) {
        handleChooseClick(connectionTypes.value[0].type, 0)
      }
    } else {
      ElMessage.error(`初始化数据库类型信息失败: ${res.message || '未知错误'}`)
    }
  } catch (error) {
    ElMessage.error(`请求失败: ${error.message}`)
  } finally {
    loading.value = false
  }
}

// 选择数据库类型加载驱动信息
const handleChooseClick = async (type, index) => {
  isActive.value = index
  try {
    const res = await getDriverByType(type)
    if (res.code === 200) {
      versionDrivers.value = res.data
    } else {
      ElMessage.error(`查询驱动版本信息失败: ${res.message || '未知错误'}`)
    }
  } catch (error) {
    ElMessage.error(`请求失败: ${error.message}`)
  }
}

// 关闭对话框确认
const handleClose = (done) => {
  ElMessageBox.confirm('确认关闭？')
    .then(() => {
      done()
    })
    .catch(() => {})
}

// 格式化JAR文件列表
const formatJarFileList = (row) => {
  const jarFiles = row.jarFiles || []
  return jarFiles.join(';\n')
}

// 生命周期
onMounted(() => {
  loadConnectionTypes()
})

onUnmounted(() => {
  // 组件销毁时的清理逻辑
})
</script>

<style lang="scss" scoped>
.driver-config-page {
  width: 100%;
  height: 100%;

  .el-card {
    width: 100%;
    height: 100%;
    overflow: auto;
  }

  .el-table {
    width: 100%;
    border-collapse: collapse;
  }

  .container {
    display: flex;
    height: 100%;

    > * {
      float: left; /* 水平排列 */
    }

    .box-card {
      width: 20%;
      height: 100%;
      overflow: auto;

      .card-header {
        text-align: center;
        padding: 8px 10px;
        border-bottom: 1px solid #ebeef5;
        box-sizing: border-box;
      }

      .navsBox {
        ul {
          margin: 0;
          padding-left: 10px;

          li {
            list-style: none;
            overflow: hidden;
            text-overflow: ellipsis;
            white-space: nowrap; /* 修复原拼写错误 nowrop -> nowrap */
            cursor: pointer;
            padding: 10px 0;
            border-bottom: 1px solid #e0e0e0;
            width: 100%;
            transition: background-color 0.2s;

            &.active {
              background: #bcbcbe6e;
              color: rgb(46, 28, 88);
            }

            &:hover {
              background: #f5f5f5;
            }
          }
        }

        :deep(.el-scrollbar__wrap) {
          overflow-x: hidden;
        }
      }
    }

    .contentBox {
      padding: 10px;
      width: calc(100% - 250px);

      .add-btn-wrapper {
        text-align: right;
        margin: 10px 5px;
        width: 95%;
      }
    }
  }

  .dialog-content {
    p {
      margin: 0 0 10px 0;
      line-height: 1.5;
    }
  }

  .dialog-footer {
    display: flex;
    justify-content: flex-end;
    gap: 8px;
  }
}
</style>