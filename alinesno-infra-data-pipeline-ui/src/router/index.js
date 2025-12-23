import { createWebHistory, createRouter } from 'vue-router'
/* Layout */
import Layout from '@/layout/SaaSLayout'
// import Layout from '@/layout'

/**
 * Note: 路由配置项
 *
 * hidden: true                     // 当设置 true 的时候该路由不会再侧边栏出现 如401，login等页面，或者如一些编辑页面/edit/1
 * alwaysShow: true                 // 当你一个路由下面的 children 声明的路由大于1个时，自动会变成嵌套的模式--如组件页面
 *                                  // 只有一个时，会将那个子路由当做根路由显示在侧边栏--如引导页面
 *                                  // 若你想不管路由下面的 children 声明的个数都显示你的根路由
 *                                  // 你可以设置 alwaysShow: true，这样它就会忽略之前定义的规则，一直显示根路由
 * redirect: noRedirect             // 当设置 noRedirect 的时候该路由在面包屑导航中不可被点击
 * name:'router-name'               // 设定路由的名字，一定要填写不然使用<keep-alive>时会出现各种问题
 * query: '{"id": 1, "name": "ry"}' // 访问路由的默认传递参数
 * roles: ['admin', 'common']       // 访问路由的角色权限
 * permissions: ['a:a:a', 'b:b:b']  // 访问路由的菜单权限
 * meta : {
    noCache: true                   // 如果设置为true，则不会被 <keep-alive> 缓存(默认 false)
    title: 'title'                  // 设置该路由在侧边栏和面包屑中展示的名字
    icon: 'svg-name'                // 设置该路由的图标，对应路径src/assets/icons/svg
    breadcrumb: false               // 如果设置为false，则不会在breadcrumb面包屑中显示
    activeMenu: '/system/user'      // 当路由设置了该属性，则会高亮相对应的侧边栏。
  }
 */

// 公共路由
export const constantRoutes = [
  {
    path: '/login',
    component: () => import('@/views/login'),
    hidden: true
  },
  {
    path: '/sso/login',
    component: () => import('@/views/loginSso'),
    hidden: true
  },
  {
    path: "/:pathMatch(.*)*",
    component: () => import('@/views/error/404'),
    hidden: true
  },
  {
    path: '/401',
    component: () => import('@/views/error/401'),
    hidden: true
  },
  {
    path: '',
    component: Layout,
    redirect: '/data/dashboard',
    children: [
      {
        path: '/index',
        component: () => import('@/views/index'),
        name: '/index',
        meta: { title: '首页', icon: 'dashboard', affix: true }
      },
      // 仪表盘（菜单主入口）
      {
        path: '/data/dashboard',
        component: () => import('@/views/index'),
        name: 'DataMigrationDashboard',
        meta: { title: '仪表盘', icon: 'square-poll-vertical', affix: true }
      },
      // 数据源管理（菜单主入口）
      {
        path: '/dashboard/suportTechnique',
        component: () => import('@/views/suportTechnique'),
        name: '/dashboard/suportTechnique',
        meta: { title: '支持管理', icon: 'dashboard', affix: true }
      },
      // 帮助中心（菜单主入口）
      {
        path: '/dashboard/learnPanel',
        component: () => import('@/views/learnPanel'),
        name: '/dashboard/learnPanel',
        meta: { title: '学习手册', icon: 'dashboard', affix: true }
      },
      // 任务管理（菜单路径）
      {
        path: '/data/pipeline/task',
        component: () => import('@/views/data/pipeline/task/index'), // 补充任务管理主页面
        name: 'DataMigrationPipelineTask',
        meta: { title: '任务管理', icon: 'tasks', affix: true }
      },
      {
        path: '/data/pipeline/task/create',
        component: () => import('@/views/data/pipeline/task/create'), // 补充任务管理主页面
        name: 'DataMigrationPipelineTaskCreate',
        meta: { title: '任务管理', icon: 'tasks', affix: true }
      },
      {
        path: '/data/pipeline/task/update',
        component: () => import('@/views/data/pipeline/task/update'), // 补充任务管理主页面
        name: 'DataMigrationPipelineTaskUpdate',
        meta: { title: '任务管理', icon: 'tasks', affix: true }
      },
      {
        path: '/data/pipeline/task/detail',
        component: () => import('@/views/data/pipeline/task/detail'), // 补充任务管理主页面
        name: 'DataMigrationPipelineTaskDetail',
        meta: { title: '任务管理', icon: 'tasks', affix: true }
      },
      // 数据源（菜单路径）
      {
        path: '/data/pipeline/source',
        component: () => import('@/views/data/pipeline/source/index'), // 补充数据源主页面
        name: 'DataMigrationSourceManagement',
        meta: { title: '数据源', icon: 'server', affix: true }
      },
      {
        path: '/data/pipeline/source/select',
        component: () => import('@/views/data/pipeline/source/select'), // 补充数据源主页面
        name: 'DataMigrationSourceManagementSelect',
        meta: { title: '数据源', icon: 'server', affix: true }
      },
      {
        path: '/data/pipeline/source/driver',
        component: () => import('@/views/data/pipeline/source/driver'), // 补充数据源主页面
        name: 'DataMigrationSourceManagementDriver',
        meta: { title: '驱动配置', icon: 'server', affix: true }
      },
      {
        path: '/data/pipeline/source/detail',
        component: () => import('@/views/data/pipeline/source/detail'), // 补充数据源主页面
        name: 'DataMigrationSourceManagementDetail',
        meta: { title: '数据源', icon: 'server', affix: true }
      },
      {
        path: '/data/pipeline/source/update',
        component: () => import('@/views/data/pipeline/source/update'), // 补充数据源主页面
        name: 'DataMigrationSourceManagementUpdate',
        meta: { title: '数据源', icon: 'server', affix: true }
      },
      {
        path: '/data/pipeline/source/create',
        component: () => import('@/views/data/pipeline/source/create'), // 补充数据源主页面
        name: 'DataMigrationSourceManagementCreate',
        meta: { title: '数据源', icon: 'server', affix: true }
      },
      // 数据导航（菜单路径）
      {
        path: '/data/pipeline/navigation',
        component: () => import('@/views/data/pipeline/navigation/index'), // 补充数据导航主页面
        name: 'DataMigrationPipelineNavigation',
        meta: { title: '数据导航', icon: 'network-wired', affix: true }
      },
      // 监控调度（菜单路径）
      {
        path: '/data/pipeline/schedule',
        component: () => import('@/views/data/pipeline/schedule/index'), // 补充监控调度主页面
        name: 'DataMigrationPipelineSchedule',
        meta: { title: '监控调度', icon: 'calendar-days', affix: true }
      },
      // 操作记录（菜单路径）
      {
        path: '/data/pipeline/record',
        component: () => import('@/views/data/pipeline/record/index'), // 补充操作记录主页面
        name: 'DataMigrationPipelineOperationLog',
        meta: { title: '操作记录', icon: 'file-lines', affix: true }
      },
      // 监控记录（底部菜单路径）
      {
        path: '/data/pipeline/monitor',
        component: () => import('@/views/data/pipeline/monitor/index'), // 补充监控记录主页面
        name: 'DataMigrationPipelineMonitor',
        meta: { title: '监控记录', icon: 'magnifying-glass-chart', affix: true }
      },
      // 驱动配置（底部菜单路径）
      {
        path: '/data/pipeline/config',
        component: () => import('@/views/data/pipeline/config/index'), // 补充驱动配置主页面
        name: 'DataMigrationPipelineConfig',
        meta: { title: '驱动配置', icon: 'gears', affix: true }
      },

      // 原有隐藏路由（任务创建相关）
//      {
//        path: '/data/pipeline/task/create',
//        name: 'DataMigrationPipelineTaskCreate',
//        hidden: true,
//        component: () => import('@/views/data/pipeline/task/create'),
//        meta: { title: '创建抽取任务配置', icon: 'tasks', affix: true }
//      },
//      {
//        path: '/data/pipeline/task/createDatasource',
//        name: 'DataMigrationPipelineTaskCreateDatasource',
//        hidden: true,
//        component: () => import('@/views/data/pipeline/task/createDatasource'),
//        meta: { title: '创建抽取任务配置', icon: 'tasks', affix: true }
//      },
//      {
//        path: '/data/pipeline/readerSource/addSource',
//        name: 'DataMigrationPipelineReaderSourceAdd',
//        hidden: true,
//        component: () => import('@/views/data/pipeline/readerSource/addSource'),
//        meta: { title: '创建读取源', icon: 'server', affix: true }
//      },

      // 原有业务模块
      {
        path: '/data/support',
        component: () => import('@/views/suportTechnique'),
        name: 'DataMigrationSupport',
        meta: { title: '支持管理', icon: 'dashboard', affix: true }
      },
      {
        path: '/data/guide',
        component: () => import('@/views/learnPanel'),
        name: 'DataMigrationGuide',
        meta: { title: '学习手册', icon: 'dashboard', affix: true }
      }
    ]
  },
  {
    path: '/user',
    component: Layout,
    hidden: true,
    redirect: 'noredirect',
    children: [
      {
        path: 'profile',
        component: () => import('@/views/system/user/profile/index'),
        name: 'Profile',
        meta: { title: '个人中心', icon: 'user' }
      }
    ]
  }
]

// 动态路由，基于用户权限动态去加载
export const dynamicRoutes = [
]

const router = createRouter({
  history: createWebHistory(),
  routes: constantRoutes,
  scrollBehavior(to, from, savedPosition) {
    if (savedPosition) {
      return savedPosition
    } else {
      return { top: 0 }
    }
  },
});

export default router;