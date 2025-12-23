<template>
    <div>
        <div id="echarts-bar-chart-42" :style="{ width: '100%', height: '280px' }"></div>
    </div>
</template>

<script setup>
import { ref, onMounted, onUnmounted } from 'vue';
import * as echarts from 'echarts';

// 存储echarts实例，用于销毁和resize
let barChart = null;

const drawBar = () => {
  // 初始化图表实例
  barChart = echarts.init(document.getElementById('echarts-bar-chart-42'));
  const lineOption = {
    tooltip: {
      trigger: 'axis'
    },
    grid: {
      height: '75%',
      width: '94%',
      left: '3%',
      right: '3%',
      top: '16%'
    },
    legend: {
      data: ['执行成功', '执行失败']
    },
    calculable: true,
    xAxis: [
      {
        type: 'category',
        boundaryGap: false,
        data: [
          '1', '2', '3', '4', '5', '6', '7',
          '22', '23', '24', '25', '26', '27', '28',
          '1', '2', '3', '4', '5', '6', '7',
          '22', '23', '24', '25', '26', '27', '28',
        ]
      }
    ],
    yAxis: [
      {
        type: 'value',
        axisLabel: {
          formatter: '{value} K'
        }
      }
    ],
    series: [
      {
        name: '正常日志',
        type: 'line',
        data: [
          11, 10, 12, 13, 5, 13, 8,
          11, 9, 15, 8, 12, 8, 8,
          11, 10, 12, 13, 5, 13, 8,
          11, 9, 15, 8, 12, 8, 8,
        ],
        markPoint: {
          data: [
            { type: 'max', name: '最大值' },
            { type: 'min', name: '最小值' }
          ]
        },
        markLine: {
          data: [
            { type: 'average', name: '平均值' }
          ]
        }
      },
      {
        name: '异常日志',
        type: 'line',
        data: [
          8, 2, 7, 5, 9, 10, 7,
          11, 10, 12, 13, 5, 13, 8,
          11, 9, 15, 8, 12, 8, 8,
          8, 2, 7, 5, 9, 10, 7,
        ],
        markPoint: {
          data: [
            { name: '周最低', value: -2, xAxis: 1, yAxis: -1.5 }
          ]
        },
        markLine: {
          data: [
            { type: 'average', name: '平均值' }
          ]
        }
      }
    ]
  };
  
  barChart.setOption(lineOption);

  // 处理窗口resize
  const resizeHandler = () => {
    if (barChart) {
      barChart.resize();
    }
  };
  window.addEventListener('resize', resizeHandler);

  // 组件卸载时移除事件监听并销毁图表
  onUnmounted(() => {
    window.removeEventListener('resize', resizeHandler);
    if (barChart) {
      barChart.dispose();
      barChart = null;
    }
  });
};

// 挂载时绘制图表
onMounted(() => {
  drawBar();
});
</script>