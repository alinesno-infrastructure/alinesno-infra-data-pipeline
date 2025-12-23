<template>
  <div>
    <el-row class="acp-dashboard-panel" :gutter="20">

      <el-col :span="18">

        <div class="grid-content">
          <div class="panel-header">
            <div class="header-title"><i class="fa-solid fa-chart-line text-primary text-xl"></i> 每日迁移数据</div>
          </div>
          <div class="panel-body acp-height-auto" style="padding-left: 5px;padding-right: 5px;">
            <TaskMonitorPanel />
          </div>
        </div>

      </el-col>

      <el-col :span="6">
        <div class="grid-content">
          <div class="panel-header">
            <div class="header-title"><i class="fa-solid fa-code text-primary text-2xl"></i> 迁移统计</div>
          </div>
          <div class="panel-body acp-height-auto">
            <ul class="panel-item-text">
              <li style="width: calc(33% - 20px);margin: 10px;padding: 10px;background: #fafafa;border-radius: 15px;" v-for="item in sensitiveWords"
                :key="item.id">
                <div class="item-health-box">
                  <div class="item-health-title">{{ item.title }}</div>
                  <div class="item-health-count">{{ item.count }}</div>
                </div>
              </li>
            </ul>
          </div>
        </div>
      </el-col>

    </el-row>
  </div>
</template>

<script setup>

import TaskMonitorPanel from "./taskMonitorPanel.vue"

import { getStatistics } from "@/api/data/pipeline/overview";

/// 声明定义一下echart
// const echart = echarts;

const sensitiveWords = ref([
  { id: '1', title: '数据源数', key:"connectionStatistics.totalCount", count: 0 },
  { id: '2', title: '任务总数', key: "assignmentTaskStatistics.totalCount" ,  count: 0 },
  { id: '3', title: '任务发布' , key: "assignmentTaskStatistics.publishedCount" , count: 0 },
  { id: '4', title: '作业执行' , key: "assignmentJobStatistics.totalCount" , count: 0 },
  { id: '5', title: '执行中' , key: "assignmentJobStatistics.runningCount" , count: 0 },
  { id: '6', title: '执行异常' , key: "assignmentJobStatistics.failedCount" , count: 0 },
  { id: '7', title: '任务取消' , key: "assignmentJobStatistics.cancelCount" , count: 0 },
  { id: '8', title: '执行成功' , key: "assignmentJobStatistics.successfulCount" , count: 0 },
]);


// 根据key路径获取嵌套对象的值
const getValueByPath = (obj, path) => {
  return path.split('.').reduce((current, key) => {
    return current ? current[key] : undefined;
  }, obj);
};

onMounted(() => {
  getStatistics().then(res => {
    if (res.code === 200 && res.data) {
      // 遍历sensitiveWords数组，根据key更新对应的count值
      sensitiveWords.value.forEach(item => {
        // 从响应数据中获取对应key的值
        const value = getValueByPath(res.data, item.key);
        // 如果找到了对应的值，则更新count
        if (value !== undefined) {
          item.count = value;
        }
      });
    } else {
      console.error('获取统计信息失败:', res.message);
    }
  }).catch(error => {
    console.error('请求统计信息失败:', error);
  });
});

// onUnmounted(() => {
//   echart.dispose;
// });


</script>

<style lang="scss" scoped>
.item-health-title {
  margin-bottom: 5px !important;
}

.item-health-count {
  margin-bottom: 5px;
}


.item-box {
  ul {
    list-style: none;
    padding: 0px;
    margin: 0px;

    .server-desc {
      font-size: 1.6rem;
      color: #3b5998;
    }

    .active {
      background: #3b5998 !important;
      color: #fff !important;

      .server-desc {
        color: #fff !important;
      }
    }

    li.item-box-info {
      padding: 8px;
      float: left;
      width: 100%;
      background: #f9fbfd;
      border-radius: 5px;
      color: #222;
      margin-bottom: 5px;

      .item-status {
        float: left;
      }

      .status-info {
        float: left;
        margin-left: 20px;

        .item-text {
          margin-bottom: 5px;
          font-size: 12px;
          font-weight: 600;
          line-height: 1.67;
        }

        .item-num {
          height: 22px;
          line-height: 22px;
          font-size: 18px;
          font-weight: 600;

          .total-num {
            font-size: 14px;
          }
        }
      }
    }
  }
}

.sidecard-bar {
  padding: 10px;
}

.item-list {
  .item-icon {
    img {
      width: 94px;
      height: 20px;
    }
  }
}

.box-header {
  .title {
    position: relative;
    height: 20px;
    margin-bottom: 20px;
    font-size: 14px;
    font-weight: 600;
    line-height: 1.43;
    zoom: 1;
  }

  .box-body {
    // padding: 20px;
    float: left;
    width: 100%;
    border-radius: 5px;
    background: #f9fbfd;

    ul {
      list-style: none;
      padding: 0px;
      margin: 0px;

      li.item-list {
        float: left;
        width: calc(33% - 10px);
        margin-right: 10px;
        background: #f9fbfd;
        padding: 10px;
        border-radius: 5px;
        margin-bottom: 10px;

        .item-icon {
          float: left;
        }

        .item-label {
          float: right;
        }
      }
    }

    .sidecard-pie {
      float: left;
      margin-left: 30px;
    }

    .sidecard {
      float: left;
      display: flex;
      flex-direction: column;
      justify-content: center;
      margin-left: 45px;
      margin-top: 40px;

      .box-title {
        font-size: 16px;
        font-weight: 600;
        line-height: 1.43;
        margin-bottom: 5px;
      }
    }
  }
}

.item-box {

  display: flex;
  align-items: flex-start;
  padding: 10px;
  border: 1px solid #e8e8e8 ;
  margin-bottom: 10px;
  border-radius: 3px;

  img {
    width: 100px;
  }

  .content{
    padding: 10px 0;
    margin-left:10px;
    display: flex;
    flex-wrap: nowrap;
    flex-direction: column;
    gap: 8px;

    .item-title {
      font-size: 14px;
      font-weight: bold;
    }

    .item-desc {
      font-size: 13px;
    }
  }
}
</style>