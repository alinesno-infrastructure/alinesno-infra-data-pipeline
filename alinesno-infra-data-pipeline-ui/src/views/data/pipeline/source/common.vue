<template>
  <div class="app-container">

  <el-card class="data-source-card">
    <el-header class="card-header">
      <div class="header-icon">
        <el-image 
          style="width: 60px; height: 60px"
          :src="getIconPath(`${selectedTypeName}`)"
        ></el-image>
      </div>
      <h3 class="h-title">{{ selectedTypeName }}</h3>
    </el-header>

    <el-main class="card-main">
      <el-form 
        ref="dataformRef"
        :rules="rules"
        :model="dataform"
        label-width="120px"
        label-position="right"
        size="medium"
        status-icon
      >
        <div class="form-container">
          <!-- 数据源名称 -->
          <el-form-item 
            prop="name"
            label="数据源名称"
            class="form-item--half"
          >
            <el-input 
              v-model="dataform.name"
              placeholder="请输入数据源名称"
              :readonly="isReadOnly"
              autocomplete="off"
            ></el-input>
            <label 
              v-if="!isReadOnly"
              class="tips-style"
            >
              数据源名称不能包含 &amp;、&lt;、&gt;、&quot;、&apos;、(、) ，长度为1~200字符
            </label>
          </el-form-item>

          <!-- 数据库类型 -->
          <el-form-item 
            :required="true"
            label="数据库类型"
          >
            <span>{{ selectedTypeName }}</span>
          </el-form-item>

          <!-- 驱动版本 -->
          <el-form-item 
            prop="version"
            label="驱动版本"
          >
            <el-select 
              v-model="dataform.version"
              :disabled="isReadOnly"
              placeholder="请选择驱动版本"
            >
              <el-option 
                v-for="(item, index) in connectionDrivers"
                :key="index"
                :label="item.driverVersion"
                :value="item.driverVersion"
              ></el-option>
            </el-select>
          </el-form-item>

          <!-- 连接地址和端口 -->
          <el-form-item 
            v-if="isShowUrlAndPort()"
            prop="address"
            label="连接地址"
          >
            <el-input 
              v-model="dataform.address"
              autocomplete="off"
              @blur="changeUrl"
              style="width:30%"
              :readonly="isReadOnly"
              placeholder="请输入地址"
            ></el-input>
            <span class="port-separator">:</span>
            <el-input 
              v-model="dataform.port"
              autocomplete="off"
              @blur="changeUrl"
              style="width:10%"
              :readonly="isReadOnly"
              placeholder="端口"
            ></el-input>
          </el-form-item>

          <!-- 数据库名 -->
          <el-form-item 
            v-if="isShowDatabaseName()"
            prop="databaseName"
            label="数据库名"
            class="form-item--quarter"
          >
            <el-input 
              v-model="dataform.databaseName"
              autocomplete="off"
              @blur="changeUrl"
              :readonly="isReadOnly"
              placeholder="请输入数据库名"
            ></el-input>
          </el-form-item>

          <!-- 用户名 -->
          <el-form-item 
            label="用户名"
            prop="username"
            class="form-item--quarter"
          >
            <el-input 
              v-model="dataform.username"
              :readonly="isReadOnly"
              autocomplete="off"
            ></el-input>
          </el-form-item>

          <!-- 密码 -->
          <el-form-item 
            label="密码"
            prop="password"
            class="form-item--quarter"
          >
            <el-input 
              type="password"
              v-model="dataform.password"
              :readonly="isReadOnly"
              autocomplete="off"
            ></el-input>
          </el-form-item>

          <!-- JDBC连接串 -->
          <el-form-item 
            label="JDBC连接串"
            label-width="120px"
            prop="url"
            class="form-item--full"
          >
            <el-input 
              type="textarea"
              :rows="6"
              :spellcheck="false"
              placeholder="请输入"
              :readonly="isReadOnly"
              v-model="dataform.url"
              autocomplete="off"
            ></el-input>
            <label 
              v-if="!isReadOnly"
              class="tips-style"
            >
              JDBC连接串（因数据库连接方式，连接参数差异较大所以需要手动拼接好），以便测试连接。
            </label>
          </el-form-item>
        </div>
      </el-form>
    </el-main>

    <el-footer class="card-footer">
      <el-row class="button-group">
        <el-button type="success" @click="startTest">测试</el-button>
        <el-button 
          v-if="!isReadOnly && (!selectedId || selectedId <= 0)"
          type="primary"
          @click="createDataSource"
        >
          创建
        </el-button>
        <el-button 
          v-if="!isReadOnly && selectedId && selectedId > 0"
          type="primary"
          @click="updateDataSource"
        >
          更新
        </el-button>
        <el-button @click="cancel">取消</el-button>
      </el-row>
    </el-footer>
  </el-card>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'

// 引入封装的接口方法
import {
  getDatabaseTypeDetail,
  getConnectionDetail,
  getConnectionDrivers,
  preTestConnection,
  createDataSource as createDataSourceApi,
  updateDataSource as updateDataSourceApi
} from '@/api/data/pipeline/dataSource'

// ========== Props 定义 ==========
const props = defineProps({
  isReadOnly: {
    type: Boolean,
    default: false
  }
})

// ========== 路由相关 ==========
const route = useRoute()
const router = useRouter()

// ========== 响应式数据 ==========
// 表单Ref
const dataformRef = ref(null)

// 基础数据
const selectedType = ref('MYSQL')
const selectedTypeName = ref('MySQL')
const selectedId = ref(0)
const connectionDrivers = ref([])
const databaseTypeDetail = ref({})

// 表单数据
const dataform = reactive({
  id: 0,
  diver: "",
  name: "",
  type: "",
  driver: "",
  version: "",
  address: "",
  port: "",
  databaseName: "",
  username: "",
  password: "",
  sample: "",
  url: "",
  templateUrl: "",
})

// 表单校验规则
const rules = reactive({
  name: [
    {
      required: true,
      message: "数据源名称不能为空",
      trigger: "blur"
    }
  ],
  type: [
    {
      required: true,
      message: "数据库类型必须选择",
      trigger: "change"
    }
  ],
  version: [
    {
      required: true,
      message: "驱动版本必须选择",
      trigger: "change"
    }
  ],
  address: [
    {
      required: true,
      message: "连接地址不能为空",
      trigger: "change"
    }
  ],
  port: [
    {
      required: true,
      message: "连接端口号不能为空",
      trigger: "change"
    }
  ],
  databaseName: [
    {
      required: true,
      message: "数据库名不能为空",
      trigger: "change"
    }
  ],
  url: [
    {
      required: true,
      message: "Jdbc URL必须提供",
      trigger: "blur"
    }
  ],
  username: [
    {
      required: true,
      message: "用户名不能为空",
      trigger: "blur"
    }
  ],
  password: [
    {
      required: true,
      message: "密码不能为空",
      trigger: "blur"
    }
  ]
})

// ========== 方法定义 ==========
/**
 * 加载数据库类型详情
 */
const loadDatabaseTypeDetail = async () => {
  try {
    const res = await getDatabaseTypeDetail(selectedType.value)
    if (res.code === 200) {
      databaseTypeDetail.value = res.data
      if (!selectedId.value || selectedId.value <= 0) {
        selectedTypeName.value = databaseTypeDetail.value.name
        dataform.driver = databaseTypeDetail.value.driver
        dataform.url = databaseTypeDetail.value.sample
      }
      dataform.templateUrl = databaseTypeDetail.value.url.replace(/\[\\\?{params}\]/, "")
    } else {
      ElMessageBox.alert(`加载任务列表失败:${res.message}`, '错误', { type: 'error' })
    }
  } catch (error) {
    ElMessageBox.alert(`加载数据库类型详情失败:${error.message}`, '错误', { type: 'error' })
  }
}

/**
 * 加载数据源连接详情
 */
const loadConnectionDetail = async () => {
  try {
    const res = await getConnectionDetail(selectedId.value)
    if (res.code === 200) {
      const detail = res.data
      selectedType.value = detail.type
      selectedTypeName.value = detail.typeName
      Object.assign(dataform, {
        id: detail.id,
        name: detail.name,
        type: detail.type,
        version: detail.version,
        driver: detail.driver,
        address: detail.address,
        port: detail.port,
        databaseName: detail.databaseName,
        username: detail.username,
        password: detail.password,
        url: detail.url
      })

      loadDatabaseTypeDetail()
      selectChangedDriverVersion()
    } else {
      ElMessage.error(`查询连接详情失败,${res.message}`)
    }
  } catch (error) {
    ElMessage.error(`查询连接详情失败:${error.message}`)
  }
}

/**
 * 获取驱动版本列表
 */
const selectChangedDriverVersion = async () => {
  if (!selectedType.value || props.isReadOnly) return

  try {
    const res = await getConnectionDrivers(selectedType.value)
    if (res.code === 200) {
      connectionDrivers.value = res.data
    } else {
      connectionDrivers.value = []
      ElMessage.error(`查询数据库可用的驱动版本失败,${res.message}`)
    }
  } catch (error) {
    connectionDrivers.value = []
    ElMessage.error(`查询驱动版本失败:${error.message}`)
  }
}

/**
 * 自动拼接URL
 */
const changeUrl = () => {
  const params = dataform.url.split("?")
  let tplUrl = dataform.templateUrl
  let flag = false

  if (dataform.address) {
    tplUrl = tplUrl.replaceAll("{host}", dataform.address)
    flag = true
  }
  if (dataform.port) {
    tplUrl = tplUrl.replaceAll("{port}", dataform.port)
    flag = true
  }
  if (dataform.databaseName) {
    tplUrl = tplUrl.replaceAll("{database}", dataform.databaseName)
    tplUrl = tplUrl.replaceAll("{file}", dataform.databaseName)
    flag = true
  }

  if (flag) {
    dataform.url = params.length > 1 ? `${tplUrl}?${params[1]}` : tplUrl
  } else {
    const sampleUrl = databaseTypeDetail.value.sample || ''
    dataform.url = params.length > 1 ? `${sampleUrl.split("?")[0]}?${params[1]}` : sampleUrl
  }
}

/**
 * 判断是否显示数据库名输入框
 */
const isShowDatabaseName = () => {
  return selectedType.value !== "ELASTICSEARCH"
}

/**
 * 判断是否显示地址和端口输入框
 */
const isShowUrlAndPort = () => {
  return selectedType.value !== "SQLITE3"
}

/**
 * 测试连接
 */
const startTest = async () => {
  try {
    const res = await preTestConnection({
      name: dataform.name,
      type: selectedType.value,
      version: dataform.version,
      driver: dataform.driver,
      url: dataform.url,
      username: dataform.username,
      password: dataform.password
    })
    if (res.code === 200) {
      ElMessage.success('测试连接成功!')
    } else {
      ElMessage.error('测试连接失败!')
    }
  } catch (error) {
    ElMessage.error(`测试连接失败:${error.message}`)
  }
}

/**
 * 创建数据源
 */
const createDataSource = async () => {
  try {
    // 表单校验
    const valid = await dataformRef.value.validate()
    if (!valid) return

    // 构造请求参数
    const params = {
      name: dataform.name,
      type: selectedType.value,
      version: dataform.version,
      driver: dataform.driver,
      address: dataform.address,
      port: dataform.port,
      databaseName: dataform.databaseName,
      characterEncoding: "",
      url: dataform.url,
      username: dataform.username,
      password: dataform.password
    }

    const res = await createDataSourceApi(params)
    if (res.code === 200) {
      ElMessage.success('添加连接信息成功!')
      router.push("/data/pipeline/source")
    } else {
      ElMessage.error(`添加连接信息失败：${res.message}`)
    }
  } catch (error) {
    ElMessage.error(`创建数据源失败：${error.message}`)
  }
}

/**
 * 更新数据源
 */
const updateDataSource = async () => {
  try {
    // 表单校验
    const valid = await dataformRef.value.validate()
    if (!valid) {
      ElMessage.error("请检查输入")
      return
    }

    // 构造请求参数
    const params = {
      id: dataform.id,
      name: dataform.name,
      type: dataform.type,
      version: dataform.version,
      driver: dataform.driver,
      address: dataform.address,
      port: dataform.port,
      databaseName: dataform.databaseName,
      characterEncoding: "",
      url: dataform.url,
      username: dataform.username,
      password: dataform.password
    }

    const res = await updateDataSourceApi(params)
    if (res.code === 200) {
      ElMessage.success('修改连接信息成功!')
      router.push("/data/pipeline/source")
    } else {
      ElMessage.error(`修改连接信息失败：${res.message}`)
    }
  } catch (error) {
    ElMessage.error(`更新数据源失败：${error.message}`)
  }
}


// 动态获取图标路径（避免 require 报错）
function getIconPath(name) {
  try {
    return new URL(`../../../../assets/icons/${name}.png`, import.meta.url).href
  } catch (e) {
    return new URL(`../../../../assets/icons/default.png`, import.meta.url).href
  }
}

/**
 * 取消操作
 */
const cancel = () => {
  router.push("/data/pipeline/source")
}

// ========== 生命周期 ==========
onMounted(() => {
  // 获取路由参数
  selectedType.value = (route.query.type || 'MYSQL').toUpperCase() 
  selectedId.value = route.query.id || 0

  if (selectedId.value && selectedId.value > 0) {
    loadConnectionDetail()
  } else {
    loadDatabaseTypeDetail()
    selectChangedDriverVersion()
  }
})
</script>

<style scoped lang="scss">
// 主卡片样式
.data-source-card {
  border-radius: 4px;
  overflow: visible;

  // 头部样式
  .card-header {
    height: 80px;
    display: flex;
    align-items: center;
    background-color: white;

    .header-icon {
      display: inline-block;
      float: left;
    }

    .h-title {
      font-weight: bolder;
      font-size: 20px;
      margin-left: 80px;
    }
  }

  // 主体样式
  .card-main {
    background-color: white;
    padding: 20px 0;

    .form-container {
      // 表单项宽度控制
      .form-item--half {
        width: 40%;
      }

      .form-item--quarter {
        width: 24%;
      }

      .form-item--full {
        width: 85%;
      }

      // 端口分隔符样式
      .port-separator {
        margin: 0 8px;
      }

      // 提示文字样式
      .tips-style {
        color: red;
        margin-left: 8px;
        display: inline-block;
        margin-top: 4px;
      }
    }
  }

  // 底部样式
  .card-footer {
    background-color: white;

    .button-group {
      text-align: center;

      :deep(.el-button) {
        margin: 0 8px;
      }
    }
  }
}
</style>