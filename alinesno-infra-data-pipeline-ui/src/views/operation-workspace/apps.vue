<template>
  <div>
    <el-row class="acp-dashboard-panel" :gutter="20">
      <el-col class="panel-col" :span="18">
        <div class="grid-content">
          <div class="panel-header">
            <div class="header-title"><i class="fa-solid fa-link"></i> 服务基础能力</div>
          </div>
          <div class="panel-body" style="height: auto;position: relative;">
              <div class="direct-box">
                <ul>
                  <li class="box-item" v-for="item in dataIntegrationAdvantages" :key="item">
                    <div class="dire-panel">
                      <div class="panel-title">
                        <i class="dire-panel-icon" :class="item.icon" :alt="item.name" />
                        {{ item.title }}
                      </div>
                      <div class="panel-describe">
                        {{ item.description }}
                      </div>
                    </div>
                  </li>
                </ul>
              </div>
          </div>
        </div>
      </el-col>
      <el-col :span="6">
        <div class="grid-content">
          <div class="panel-header">
            <div class="header-title"><i class="fa-solid fa-link"></i> 应用场景</div>
          </div>
          <div class="panel-body" style="height: auto;position: relative;">
            <div class="acp-app-list">
              <ul>
                <li class="app-items" style="width:100%" v-for="item in dataIntegrationScenarios" :key="item">
                  <div class="app-info">
                    <div class="app-item-title">
                      <i :class="item.icon" :alt="item.name" />
                        {{ item.name }}
                    </div>
                    <div class="app-item desc">{{ item.description }}</div>
                  </div>
                </li>
              </ul>
            </div>
          </div>

        </div>
      </el-col>
    </el-row>

  </div>
</template>

<script setup>

const dataIntegrationAdvantages = [
  { icon: 'fa-solid fa-database', title: '广泛的兼容性', description: '支持多种数据源类型，包括关系型数据库、文件系统等，实现异构数据源间的无缝连接。' },
  { icon: 'fa-solid fa-sync', title: '高效的数据传输', description: '采用高性能的数据读取和写入机制，保证大规模数据迁移时的速度与稳定性。' },
  { icon: 'fa-solid fa-code-branch', title: '灵活的任务调度', description: '支持定时任务和依赖调度，可根据业务需求灵活配置数据同步策略。' },
  { icon: 'fa-solid fa-cog', title: '丰富的数据转换功能', description: '提供强大的数据转换和清洗能力，支持复杂的数据处理逻辑，确保数据质量。' },
  { icon: 'fa-solid fa-book-open', title: '详尽的日志记录', description: '记录详细的执行日志，方便追踪数据同步的过程及结果，便于调试和维护。' },
];

const dataIntegrationScenarios = [
  { icon: 'fa-solid fa-database', name: '跨数据中心数据迁移', description: '支持将数据从一个数据中心迁移到另一个数据中心，例如从本地数据库迁移至云端数据库' },
  { icon: 'fa-solid fa-sync', name: '实时数据同步', description: '实现实时的数据流处理和同步，以便进行即时分析和决策支持。' },
  { icon: 'fa-solid fa-chart-bar', name: '批量数据导入导出', description: '支持大批量数据的导入导出操作，例如定期将历史数据归档至低成本存储。' },
];

</script>