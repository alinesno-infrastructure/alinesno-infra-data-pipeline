<template>
  <div class="siderbar">
    <el-menu default-active="1" class="el-menu-vertical" :collapse="isCollapse" @open="handleOpen" @close="handleClose">

          <el-menu-item
              :index="item.id"
              @click="openServiceList(item.link)"
              v-for="item in menuItems"
              :key="item.id"
              class="aip-menu-item">
            <i :class="item.icon"></i>
            <span>
              {{ item.desc }}
            </span>
          </el-menu-item>

    </el-menu>

    <el-menu style="" class="el-menu-vertical acp-suggest" :collapse="isCollapse" @open="handleOpen" @close="handleClose">
          <el-menu-item
            :index="item.id"
            v-for="item in footerMenuItems"
            :key="item.id"
            @click="openServiceList(item.link)"
            class="aip-menu-item">
            <i :class="item.icon"></i>
            <span>
              {{ item.desc }}
            </span>
          </el-menu-item>
    </el-menu>

  </div>
</template>

<script setup>

const dialogVisible = ref(false)
const router = useRouter();

// 菜单列表
const footerMenuItems = ref([
  { id: '21', icon: 'fa-solid fa-chart-pie', link: '/data/pipeline/monitor', desc: '监控记录' },
  { id: '22', icon: 'fa-solid fa-gears', link: '/data/pipeline/source/driver', desc: '驱动配置' },
])

const menuItems = ref([
  { id:'3', icon:'fa-solid fa-desktop', link:'/index', desc:'仪表盘' },
  { id:'8', icon:'fa-solid fa-tasks', link:'/data/pipeline/task', desc:'任务管理' },
  { id:'5', icon:'fa-solid fa-server', link:'/data/pipeline/source', desc:'数据源' },
  { id:'9', icon:'fa-solid fa-calendar-days', link:'/data/pipeline/schedule', desc:'监控调度' },
  { id:'7', icon:'fa-solid fa-file-lines', link:'/data/pipeline/record', desc:'操作记录' },
  { id:'4', icon:'fa-solid fa-network-wired', link:'/data/pipeline/navigation', desc:'数据导航' },
]);

// 打开服务市场
function openServiceList(_path) {
  router.push({ path: _path });
}

</script>

<style lang="scss" scoped>
.el-menu-vertical:not(.el-menu--collapse) {
  width: 70px;
}

.acp-suggest {
  bottom: 20px;
  position: absolute;
}

.siderbar {
  float: left;
  height: 100%;
  width: 70px;
  border-right: 1px solid #e6e6e6;
  padding-top: 40px;
  overflow: hidden;
  background-color: #fff;
  position: fixed;
}

.aip-menu-item {

  display: flex;
  flex-direction: column;
  gap: 5px;
  line-height: 1.2rem;
  padding-top: 0px;
  margin: 5px;
  border-radius: 10px;
  justify-content: center;

  span{
    font-size:12px;
    color: #888;
  }
}
</style>