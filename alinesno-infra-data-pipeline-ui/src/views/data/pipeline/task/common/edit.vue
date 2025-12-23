<template>
    <div class="app-container">

  <el-card shadow="never"> 
    <el-steps :active="active" finish-status="success">
      <el-step title="基本信息配置" />
      <el-step title="同步源端配置" />
      <el-step title="目标端配置" />
      <el-step title="映射转换配置" />
      <el-step title="配置确认提交" />
    </el-steps>

    <el-form :model="dataform" status-icon :rules="rules" ref="dataformRef">
      <!-- 基本信息配置 -->
      <div v-show="active === 1" class="common-top">
        <el-form-item label="任务名称" label-width="160px" :required=true prop="name" style="width:65%">
          <el-input v-model="dataform.name" auto-complete="off" placeholder="请输入任务名称" style="width:80%"></el-input>
          <label class="tips-style block">请输入任务名称，只能以字母、数字为开头，包含字母、数字和._-，3-100个字符</label>
        </el-form-item>
        <el-form-item label="描述" label-width="160px" prop="description" style="width:65%">
          <el-input v-model="dataform.description" type="textarea" :rows="3" auto-complete="off" placeholder="请输入任务描述" style="width:80%"></el-input>
        </el-form-item>
        <el-form-item label="集成模式" label-width="160px" :required=true prop="scheduleMode" style="width:80%">
          <el-input v-model="dataform.scheduleMode" v-if="false"></el-input>
          <el-radio-group v-model="dataform.scheduleMode" size="mini">
            <el-radio-button value="MANUAL" label="手动调度"></el-radio-button>
            <el-radio-button value="SYSTEM_SCHEDULED" label="系统调度"></el-radio-button>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="执行周期" label-width="160px" style="width:80%" :required="true" v-if="dataform.scheduleMode === 'SYSTEM_SCHEDULED'">
          <el-select v-model="dataform.cronExpression" filterable allow-create>
            <el-option v-for="(item,index) in cronExprOptionList" :key="index" :label="item.name" :value="item.value"></el-option>
          </el-select>
          <label class="tips-style block">执行周期为CRON表达式，即可以选择以下内置的周期，也可以自行输入或粘贴合法的CRON表达式(最小间隔时间为2分钟)。</label>
        </el-form-item>
      </div>

      <!-- 同步源端配置 -->
      <div v-show="active === 2" class="common-top">
        <el-form-item label="源端数据源" label-width="160px" :required=true prop="sourceConnectionId" style="width:80%">
          <el-select v-model="dataform.sourceConnectionId" @change="selectChangedSourceConnection" placeholder="请选择">
            <el-option v-for="(item,index) in connectionNameList" :key="index" :label="`[${item.id}] ${item.name}`" :value="item.id"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="源端模式名" label-width="160px" :required=true prop="sourceSchema" style="width:80%">
          <el-select v-model="dataform.sourceSchema" filterable @change="selectCreateChangedSourceSchema" placeholder="请选择">
            <el-option v-for="(item,index) in sourceConnectionSchemas" :key="index" :label="item" :value="item"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="源端表类型" label-width="160px" :required=true prop="tableType" style="width:80%">
          <el-select placeholder="请选择表类型" @change="selectCreateChangedTableType" v-model="dataform.tableType">
            <el-option label="物理表" value="TABLE"></el-option>
            <el-option label="视图表" value="VIEW"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="配置方式" label-width="160px" :required=true prop="includeOrExclude" style="width:80%">
          <el-select placeholder="请选择配置方式" v-model="dataform.includeOrExclude">
            <el-option label="包含表" value="INCLUDE"></el-option>
            <el-option label="排除表" value="EXCLUDE"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="表名配置" label-width="160px" :required=false prop="sourceTables" style="width:80%">
          <el-select placeholder="请选择表名" multiple filterable v-model="dataform.sourceTables">
            <el-option v-for="(item,index) in sourceSchemaTables" :key="index" :label="item" :value="item"></el-option>
          </el-select>
          <label class="tips-style block">当为包含表时，选择所要精确包含的表名，如果不选则代表选择所有；当为排除表时，必须选择要精确排除的表名。</label>
        </el-form-item>
        <el-form-item label="增量同步配置" label-width="160px" :required=false style="width:80%">
          &nbsp;&nbsp;
          <el-button type="primary" @click="handleAddInputIncrTable">
            <el-icon class="Plus" />
          </el-button>
          &nbsp;&nbsp;&nbsp;&nbsp;
          <i class="el-icon-question" @click="showDataSyncMessageDialogVisible = true"></i>
          <el-table :data="dataform.incrTableColumns" :header-cell-style="{background:'#eef1f6',color:'#606266'}" size="mini" border>
            <el-table-column label="表名" prop="tableName" min-width="45%"></el-table-column>
            <el-table-column label="增量字段名" prop="columnName" min-width="45%"></el-table-column>
            <el-table-column label="操作" min-width="10%">
              <template #default="scope">
                <el-link icon="el-icon-delete" @click="handleDeleteIncrTable(scope.$index)"></el-link>
              </template>
            </el-table-column>
          </el-table>
          <label class="tips-style block">可点击加号+按钮为需要增量同步的大表配置增量同步的字段来加快数据同步速度</label>
        </el-form-item>
        <el-form-item label="同步前置执行SQL脚本" label-width="160px" prop="sourceBeforeSqlScripts" style="width:80%">
          <el-input v-model="dataform.sourceBeforeSqlScripts" type="textarea" :rows="3" auto-complete="off" style="width: 80%"></el-input>
          <label class="tips-style block">数据同步查询源端数据库前执行的SQL，多个SQL间以英文逗号分隔。</label>
        </el-form-item>
        <el-form-item label="同步后置执行SQL脚本" label-width="160px" prop="sourceAfterSqlScripts" style="width:80%">
          <el-input v-model="dataform.sourceAfterSqlScripts" type="textarea" :rows="3" auto-complete="off" style="width: 80%"></el-input>
          <label class="tips-style block">数据同步查询源端数据库后执行的SQL，多个SQL间以英文逗号分隔。</label>
        </el-form-item>
      </div>

      <!-- 目标端配置 -->
      <div v-show="active === 3" class="common-top">
        <el-form-item label="目的端数据源" label-width="160px" :required=true prop="targetConnectionId" style="width:80%">
          <el-select v-model="dataform.targetConnectionId" @change="selectChangedTargetConnection" placeholder="请选择">
            <el-option v-for="(item,index) in connectionNameList" :key="index" :label="`[${item.id}]${item.name}`" :value="item.id"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="目的端模式名" label-width="160px" :required=true prop="targetSchema" style="width:80%">
          <el-select v-model="dataform.targetSchema" filterable placeholder="请选择">
            <el-option v-for="(item,index) in targetConnectionSchemas" :key="index" :label="item" :value="item"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="自动同步模式" label-width="160px" :required=true prop="autoSyncMode" style="width:80%">
          <span slot="label">
            <span style="color: red"><strong>自动同步模式</strong> </span>
          </span>
          <el-tooltip placement="top">
            <template #content>
              <p>目标端建表并同步数据：首次在目标的自动建表(存在重命表时会删除重建)并执行数据加载同步操作，再次执行时会根据是否有主键进行变化量同步；</p>
              <p>目标端只创建物理表: 每次执行时，只在目标端自动建表，存在重名表时会删除后重建；</p>
              <p>目标端只同步表里数据：每次执行时，目标端需要存在符合映射规则的物理表，最迟需要在执行任务前已经存在目标表；<br />该选项通常适用于两端表结构一致时(或目标端字段包含源端所有的字段且字段数据类型一致)的数据同步场景</p>
            </template>
            <i class="el-icon-question"></i>
          </el-tooltip>
          <el-select v-model="dataform.autoSyncMode">
            <el-option label='目标端建表并同步数据' :value=2></el-option>
            <el-option label='目标端只创建物理表' :value=1></el-option>
            <el-option label='目标端只同步表里数据' :value=0></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="建表字段自增" label-width="160px" :required=true v-if="dataform.autoSyncMode !== 0" prop="targetAutoIncrement" style="width:80%">
          <el-tooltip placement="top">
            <template #content>
              创建表时是否自动支持字段的自增；只有使用自动建表才会生效，不过前提需要两端的数据库表建表时支持指定自增字段，默认为false。
            </template>
            <i class="el-icon-question"></i>
          </el-tooltip>
          <el-select v-model="dataform.targetAutoIncrement">
            <el-option label='是' :value=true></el-option>
            <el-option label='否' :value=false></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="表名转换方法" label-width="160px" :required=true prop="tableNameCase" style="width:80%">
          <el-tooltip placement="top">
            <template #content>
              转换说明：先使用下面的表名映射，然后再使用这里的表名转换方法转换，对支持大小写敏感的数据库类型有效。
            </template>
            <i class="el-icon-question"></i>
          </el-tooltip>
          <el-select v-model="dataform.tableNameCase">
            <el-option v-for="(item,index) in nameConvertList" :key="index" :label="item.name" :value="item.value"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="列名转换方法" label-width="160px" :required=true prop="columnNameCase" style="width:80%">
          <el-tooltip placement="top">
            <template #content>
              转换说明：先使用下面的列名映射，然后再使用这里的转换方法转换，对支持大小写敏感的数据库类型有效。
            </template>
            <i class="el-icon-question"></i>
          </el-tooltip>
          <el-select v-model="dataform.columnNameCase">
            <el-option v-for="(item,index) in nameConvertList" :key="index" :label="item.name" :value="item.value"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="数据批次大小" label-width="160px" :required=true v-if="dataform.autoSyncMode !== 1" prop="batchSize" style="width:80%">
          <el-tooltip placement="top">
            <template #content>
              数据同步时单个批次处理的行记录总数，该值越大越占用内存空间。建议：小字段表设置为10000或20000，大字段表设置为100或500
            </template>
            <i class="el-icon-question"></i>
          </el-tooltip>
          <el-select v-model="dataform.batchSize">
            <el-option v-for="(item,index) in batchSizeList" :key="index" :label="item.toString()" :value="item"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="通道队列大小" label-width="160px" :required=true v-if="dataform.autoSyncMode !== 1" prop="channelSize" style="width:80%">
          <el-tooltip placement="top">
            <template #content>
              数据同步时缓存数据的通道队列大小，该值越大越占用内存空间。当源库读取快目标库写入慢时，缓存在内存中的数据最大占用空间 = 行记录大小 × 数据批次大小 × 通道队列大小 。
            </template>
            <i class="el-icon-question"></i>
          </el-tooltip>
          <el-select v-model="dataform.channelSize">
            <el-option v-for="(item,index) in channelSizeList" :key="index" :label="item.toString()" :value="item"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="同步操作方法" label-width="160px" :required=true v-if="dataform.autoSyncMode !== 1" prop="targetSyncOption" style="width:80%">
          <el-tooltip placement="top">
            <template #content>
              <p>数据同步时包括增删改操作，这里选择配置执行INSERT、UPDATE、DELETE操作类型的方法;</p>
              <p>对首次数据加载无效，只对数据同步有效;</p>
              <p>只对有主键表有效，如果源表为视图表或无主键表则并不生效.</p>
            </template>
            <i class="el-icon-question"></i>
          </el-tooltip>
          <el-select v-model="dataform.targetSyncOption">
            <el-option v-for="(item,index) in targetSyncOptionList" :key="index" :label="item.name" :value="item.value"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="同步前置执行SQL脚本" label-width="160px" v-if="dataform.autoSyncMode !== 1" prop="targetBeforeSqlScripts" style="width:80%">
          <el-input v-model="dataform.targetBeforeSqlScripts" type="textarea" :rows="3" auto-complete="off" style="width: 80%"></el-input>
          <label class="tips-style block">数据同步写入目标端数据库前执行的SQL，多个SQL间以英文逗号分隔。使用场景如：MySQL数据库关闭外键约束 SET FOREIGN_KEY_CHECKS = 0</label>
        </el-form-item>
        <el-form-item label="同步后置执行SQL脚本" label-width="160px" v-if="dataform.autoSyncMode !== 1" prop="targetAfterSqlScripts" style="width:80%">
          <el-input v-model="dataform.targetAfterSqlScripts" type="textarea" :rows="3" auto-complete="off" style="width: 80%"></el-input>
          <label class="tips-style block">数据同步写入目标端数据库后执行的SQL，多个SQL间以英文逗号分隔。使用场景如：MySQL数据库恢复外键约束 SET FOREIGN_KEY_CHECKS = 1</label>
        </el-form-item>
      </div>

      <!-- 映射转换配置 -->
      <div v-show="active === 4" class="common-top">
        <el-alert title="说明信息" type="success">
          <p>(1) 当表名映射规则记录为空时，代表目标表名与源表名的名称相同;</p>
          <p>(2) 当字段名映射规则记录为空时，代表目标表的字段名与源表名的字段名相同</p>
          <p>(3) 在字段名映射规则中，如果目标字段名为空（未填写），则代表剔除该字段（不能是主键）的同步</p>
        </el-alert>
        <el-button type="primary" @click="addTableNameMapperListRow()" round>添加表名映射</el-button>
        <el-button type="warning" @click="previewTableNameMapList()" round>预览表名映射</el-button>
        <el-table :data="dataform.tableNameMapper" size="mini" border height="200" style="width:90%;margin-top: 15px;">
          <template #empty>
            <span>请点击"添加表名映射"按钮添加表名映射关系记录</span>
          </template>
          <el-table-column label="表名匹配的正则名" width="320">
            <template #default="scope">
              <el-input v-model="scope.row.fromPattern" type="string"> </el-input>
            </template>
          </el-table-column>
          <el-table-column label="替换的目标值" width="320">
            <template #default="scope">
              <el-input v-model="scope.row.toValue" type="string"></el-input>
            </template>
          </el-table-column>
          <el-table-column label="操作" width="220">
            <template #default="scope">
              <el-button size="mini" type="danger" @click="deleteTableNameMapperListItem(scope.$index)">删除</el-button>
            </template>
          </el-table-column>
        </el-table>
        <el-button type="primary" @click="addColumnNameMapperListRow()" round>添加字段名映射</el-button>
        <el-button type="warning" @click="previewColumnNameMapList()" round>预览字段名映射</el-button>
        <el-table :data="dataform.columnNameMapper" size="mini" border height="200" style="width:90%;margin-top: 15px;">
          <template #empty>
            <span>请点击"添加字段名映射"按钮添加字段名映射关系记录</span>
          </template>
          <el-table-column label="字段名匹配的正则名" width="320">
            <template #default="scope">
              <el-input v-model="scope.row.fromPattern" type="string"> </el-input>
            </template>
          </el-table-column>
          <el-table-column label="替换的目标值" width="320">
            <template #default="scope">
              <el-input v-model="scope.row.toValue" type="string"></el-input>
            </template>
          </el-table-column>
          <el-table-column label="操作" width="220">
            <template #default="scope">
              <el-button size="mini" type="danger" @click="deleteColumnNameMapperListItem(scope.$index)">删除</el-button>
            </template>
          </el-table-column>
        </el-table>
      </div>

      <!-- 配置确认提交 -->
      <div v-show="active === 5" class="common-top">
        <commonInfo :infoform="dataform"></commonInfo>
      </div>
    </el-form>

    <!-- 步骤按钮 -->
    <el-button round v-if="active > 1" style="margin-top: 12px;margin-left: 20px" @click="pre">
      上一步
    </el-button>
    <el-button round @click="next" v-if="active > 0 && active < 5" style="margin-left: 20px">
      下一步
    </el-button>
    <el-button round @click="handleSave" v-if="active === 5">
      提交
    </el-button>

    <!-- 增量同步字段选择弹窗 -->
    <el-dialog v-if="active === 2" title="选择增量同步表的增量标识字段" v-model="columnNameIncrementDialogVisible" :show-close="false" :before-close="handleClose">
      <el-select @change="queryPreviewColumnNameMapperList" v-model="preiveTableName" placeholder="请选择">
        <el-option v-for="(item,index) in preiveSeeTableNameList" :key="index" :label="item" :value="item"></el-option>
      </el-select>
      <el-table :header-cell-style="{background:'#eef1f6',color:'#606266'}" :data="columnNamesMapperData" @row-click="singleRowClick" highlight-current-row size="mini" border>
        <el-table-column label="#" min-width="10%">
          <template #default="scope">
            {{scope.$index}}
          </template>
        </el-table-column>
        <el-table-column prop="originalName" label="字段名" min-width="30%"></el-table-column>
        <el-table-column prop="typeName" label="字段类型" min-width="30%"></el-table-column>
        <el-table-column prop="canIncrement" label="可标识增量" min-width="20%">
          <template #default="scope">
            <el-tag size="medium">{{ boolValueFormat(scope.row.canIncrement) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="选择" min-width="10%">
          <template #default="scope">
            <el-radio :label="scope.row.originalName" v-model="radio" :disabled="!scope.row.canIncrement" @change="() => singleRowClick(scope.row)"></el-radio>
          </template>
        </el-table-column>
      </el-table>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="handleConfirmSelectIncrTableColumn">确定</el-button>
          <el-button @click="handleCancelSelectIncrTableColumn">取消</el-button>
        </div>
      </template>
    </el-dialog>

    <!-- 数据同步说明弹窗 -->
    <el-dialog v-if="active === 2" title="提示信息" v-model="showDataSyncMessageDialogVisible" :show-close="false" :before-close="handleClose">
      <el-alert title="1、数据同步概念" type="warning" :closable="false" show-icon>
        <ul>
          <li><b>全量同步:</b> 先truncate清空目标表后，然后将源端表数据全部插入目标表的过程</li>
          <li><b>增量同步:</b> 根据增量表指定的增量字段，使用带有WHERE field > value的条件SQL查询源端表数据，然后插入目标表的过程</li>
          <li><b>变化量同步:</b> 在源端表和目标表都有主键且映射一致的条件下，通过两边数据比对计算出差异，然后目标表执行插入/更新/删除数据的过程</li>
        </ul>
      </el-alert>
      <el-alert title="2、dbswitch同步逻辑" type="info" :closable="false" show-icon>
        <ul>
          <li><b>步骤1:</b> 如果是首次同步，则会自动创建目标表，并执行全量数据同步;</li>
          <li><b>步骤2:</b> 非首次同步时，如果表配置了增量同步标识字段，则会执行增量数据同步;</li>
          <li><b>步骤3:</b> 非首次同步时，且没有配置增量同步标识字段，如果两端都有主键且映射一致，则会执行变化量数据同步;</li>
          <li><b>步骤3:</b> 非首次同步时，且没有配置增量同步标识字段，如果两端没有主键或主键不一致，则会执行全量数据同步;</li>
        </ul>
      </el-alert>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="showDataSyncMessageDialogVisible = false">关闭</el-button>
        </div>
      </template>
    </el-dialog>

    <!-- 表名映射预览弹窗 -->
    <el-dialog v-if="active === 4" title="查看表名映射关系" v-model="tableNameMapperDialogVisible" :show-close="false" :before-close="handleClose">
      <el-table :header-cell-style="{background:'#eef1f6',color:'#606266'}" :data="tableNamesMapperData" size="mini" border>
        <el-table-column prop="originalName" label="源端表名" min-width="20%"></el-table-column>
        <el-table-column prop="targetName" label="目标表名" min-width="20%"></el-table-column>
      </el-table>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="tableNameMapperDialogVisible = false">关闭</el-button>
        </div>
      </template>
    </el-dialog>

    <!-- 字段名映射预览弹窗 -->
    <el-dialog v-if="active === 4" title="查看字段映射关系" v-model="columnNameMapperDialogVisible" :show-close="false" :before-close="handleClose">
      <el-select @change="queryPreviewColumnNameMapperList" v-model="preiveTableName" placeholder="请选择">
        <el-option v-for="(item,index) in preiveSeeTableNameList" :key="index" :label="item" :value="item"></el-option>
      </el-select>
      <el-table :header-cell-style="{background:'#eef1f6',color:'#606266'}" :data="columnNamesMapperData" size="mini" border>
        <el-table-column prop="originalName" label="原始字段名" min-width="20%"></el-table-column>
        <el-table-column prop="targetName" label="目标表字段名" min-width="20%"></el-table-column>
      </el-table>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="columnNameMapperDialogVisible = false">关闭</el-button>
        </div>
      </template>
    </el-dialog>

  </el-card>
    </div>
</template>

<script setup>
import { ref, reactive, onMounted, watch } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage, ElAlert, ElMessageBox } from 'element-plus'
import commonInfo from './info'
// 导入API请求函数
import {
  getConnectionList,
  getAssignmentDetail,
  getConnectionSchemas,
  getConnectionTables,
  getConnectionViews,
  previewTableMapper,
  previewColumnMapper,
  createAssignment,
  updateAssignment
} from '@/api/data/pipeline/task'

// 路由实例
const route = useRoute()
const router = useRouter()

// 表单Ref
const dataformRef = ref(null)

// 静态数据列表
const cronExprOptionList = ref([
  { name: "每5分钟执行1次", value: "0 0/5 * * * ? *" },
  { name: "每30分钟执行1次", value: "0 0/30 * * * ? *" },
  { name: "每1小时执行1次", value: "0 0 0/1 * * ? *" },
  { name: "每2小时执行1次", value: "0 0 0/2 * * ? *" },
  { name: "每8小时执行1次", value: "0 0 0/8 * * ? *" },
  { name: "每12小时执行1次", value: "0 0 0/12 * * ? *" },
  { name: "每日0时执行1次", value: "0 0 0 1/1 * ? *" }
])

const nameConvertList = ref([
  { name: "无转换", value: "NONE" },
  { name: "转大写", value: "UPPER" },
  { name: "转小写", value: "LOWER" },
  { name: "下划线转驼峰", value: "CAMEL" },
  { name: "驼峰转下划线", value: "SNAKE" }
])

const batchSizeList = ref([100, 500, 1000, 5000, 10000, 20000])
const channelSizeList = ref([10, 20, 40, 60, 80, 100, 200, 500, 1000])
const targetSyncOptionList = ref([
  { name: "只同步INSERT操作", value: "ONLY_INSERT" },
  { name: "只同步UPDATE操作", value: "ONLY_UPDATE" },
  { name: "只同步INSERT和UPDATE", value: "INSERT_UPDATE" },
  { name: "只同步DELETE操作", value: "ONLY_DELETE" },
  { name: "只同步UPDATE和DELETE", value: "UPDATE_DELETE" },
  { name: "执行所有的同步操作", value: "INSERT_UPDATE_DELETE" }
])

// 响应式数据
const active = ref(1)
const radio = ref('0')
const connectionNameList = ref([])
const sourceConnection = ref({})
const targetConnection = ref({})
const sourceConnectionSchemas = ref([])
const sourceSchemaTables = ref([])
const targetConnectionSchemas = ref([])
const columnNameIncrementDialogVisible = ref(false)
const showDataSyncMessageDialogVisible = ref(false)
const tableNameMapperDialogVisible = ref(false)
const columnNameMapperDialogVisible = ref(false)
const tableNamesMapperData = ref([])
const columnNamesMapperData = ref([])
const preiveSeeTableNameList = ref([])
const preiveTableName = ref("")
const tempIncrTableName = ref("")
const tempIncrColumnName = ref("")

// 表单数据
const dataform = reactive({
  id: 0,
  name: "",
  description: "",
  scheduleMode: "MANUAL",
  scheduleModeName: "手动调度",
  cronExpression: "",
  sourceConnectionId: '请选择',
  sourceTypeName: 'MySQL',
  sourceSchema: "",
  tableType: "TABLE",
  includeOrExclude: "",
  sourceTables: [],
  incrTableColumns: [],
  sourceBeforeSqlScripts: "",
  sourceAfterSqlScripts: "",
  tableNameMapper: [],
  columnNameMapper: [],
  tableNameCase: 'NONE',
  columnNameCase: 'NONE',
  targetConnectionId: '请选择',
  targetTypeName: 'MySQL',
  targetDropTable: true,
  targetOnlyCreate: false,
  targetAutoIncrement: false,
  autoSyncMode: 2,
  targetSchema: "",
  batchSize: 5000,
  channelSize: 100,
  targetSyncOption: 'INSERT_UPDATE_DELETE',
  targetBeforeSqlScripts: '',
  targetAfterSqlScripts: '',
})

// 表单校验规则
const rules = ref({
  name: [
    { required: true, message: "任务名称不能为空", trigger: "blur" }
  ],
  scheduleMode: [
    { required: true, type: 'string', message: "调度方式必须选择", trigger: "change" }
  ],
  sourceConnectionId: [
    { required: true, type: 'integer', message: "必选选择一个来源端", trigger: "change" }
  ],
  sourceSchema: [
    { required: true, type: 'string', message: "必选选择一个Schema名", trigger: "change" }
  ],
  tableType: [
    { required: true, type: 'string', message: "表类型必须选择", trigger: "change" }
  ],
  includeOrExclude: [
    { required: true, type: 'string', message: "配置方式必须选择", trigger: "change" }
  ],
  sourceTables: [
    { required: false, type: 'array', message: "必选选择一个Table名", trigger: "change" }
  ],
  targetConnectionId: [
    { required: true, type: 'integer', message: "必选选择一个目的端", trigger: "change" }
  ],
  targetSchema: [
    { required: true, type: 'string', message: "必选选择一个Schema名", trigger: "change" }
  ],
  batchSize: [
    { required: true, type: 'integer', message: "必选选择一个数据批次大小", trigger: "change" }
  ],
  channelSize: [
    { required: true, type: 'integer', message: "必选选择一个通道队列大小", trigger: "change" }
  ],
  targetSyncOption: [
    { required: true, type: 'string', message: "必选选择一个同步方法来 ", trigger: "change" }
  ]
})

// 方法定义
// 初始化调度模式名称
const initScheduleModeTemp = (val) => {
  if (val === 'SYSTEM_SCHEDULED') return "系统调度"
  if (val === 'MANUAL') return "手动调度"
}

// 调度模式更新
const scheduleModeUpdate = (val) => {

    console.log('调度模式更新:' + val);

  if (val === '系统调度') {
    dataform.scheduleMode = "SYSTEM_SCHEDULED"
  }
  if (val === '手动调度') {
    dataform.scheduleMode = "MANUAL"
  }
}

// 布尔值格式化
const boolValueFormat = (value) => {
  return value ? "是" : "否"
}

// 弹窗关闭处理
const handleClose = () => {
  // 可添加关闭前的逻辑
}

// 下一步
const next = () => {
  if (active.value < 5) active.value++
}

// 上一步
const pre = () => {
  if (active.value > 1) active.value--
}

// 加载连接列表
const loadConnections = async () => {
  try {
    const res = await getConnectionList()
    if (res.code === 200) {
      connectionNameList.value = res.data
    } else {
      connectionNameList.value = []
      if (res.message) {
        ElMessage.error(`加载任务列表失败:${res.message}`)
      }
    }
  } catch (error) {
    ElMessage.error("加载连接列表失败")
    console.error(error)
  }
}

// 加载任务详情
const loadAssignmentDetail = async () => {
  const taskId = route.query.id
  if (taskId && taskId > 0) {
    try {
      const res = await getAssignmentDetail(taskId)
      if (res.code === 200) {
        const detail = res.data
        let varAutoSyncMode = 2
        if (detail.configuration.targetDropTable && detail.configuration.targetOnlyCreate) {
          varAutoSyncMode = 1
        } else if (!detail.configuration.targetDropTable && !detail.configuration.targetOnlyCreate) {
          varAutoSyncMode = 0
        } else {
          varAutoSyncMode = 2
        }

        // 赋值到表单
        Object.assign(dataform, {
          id: detail.id,
          name: detail.name,
          description: detail.description,
          scheduleMode: detail.scheduleMode,
          scheduleModeName: initScheduleModeTemp(detail.scheduleMode),
          cronExpression: detail.cronExpression,
          sourceConnectionId: detail.configuration.sourceConnectionId,
          sourceTypeName: detail.configuration.sourceTypeName,
          sourceConnectionName: detail.configuration.sourceConnectionName,
          sourceSchema: detail.configuration.sourceSchema,
          tableType: detail.configuration.tableType,
          includeOrExclude: detail.configuration.includeOrExclude,
          sourceTables: detail.configuration.sourceTables,
          incrTableColumns: detail.configuration.incrTableColumns,
          sourceBeforeSqlScripts: detail.configuration.sourceBeforeSqlScripts,
          sourceAfterSqlScripts: detail.configuration.sourceAfterSqlScripts,
          tableNameMapper: detail.configuration.tableNameMapper,
          columnNameMapper: detail.configuration.columnNameMapper,
          tableNameCase: detail.configuration.tableNameCase,
          columnNameCase: detail.configuration.columnNameCase,
          targetConnectionId: detail.configuration.targetConnectionId,
          targetTypeName: detail.configuration.targetTypeName,
          targetConnectionName: detail.configuration.targetConnectionName,
          targetDropTable: detail.configuration.targetDropTable,
          targetOnlyCreate: detail.configuration.targetOnlyCreate,
          targetAutoIncrement: detail.configuration.targetAutoIncrement,
          autoSyncMode: varAutoSyncMode,
          targetSchema: detail.configuration.targetSchema,
          batchSize: detail.configuration.batchSize,
          channelSize: detail.configuration.channelSize,
          targetSyncOption: detail.configuration.targetSyncOption,
          targetBeforeSqlScripts: detail.configuration.targetBeforeSqlScripts,
          targetAfterSqlScripts: detail.configuration.targetAfterSqlScripts,
        })

        // 加载关联数据
        selectChangedSourceConnection(dataform.sourceConnectionId)
        selectCreateChangedSourceSchema(dataform.sourceSchema)
        selectChangedTargetConnection(dataform.targetConnectionId)
      } else {
        if (res.message) {
          ElMessage.error(`查询任务失败,${res.message}`)
        }
      }
    } catch (error) {
      ElMessage.error("查询任务详情失败")
      console.error(error)
    }
  }
}

// 源端连接变更
const selectChangedSourceConnection = async (value) => {
  sourceConnection.value = connectionNameList.value.find(item => item.id === value) || {}
  if (sourceConnection.value) {
    dataform.sourceTypeName = sourceConnection.value.typeName
  }

  try {
    const res = await getConnectionSchemas(value)
    if (res.code === 200) {
      sourceConnectionSchemas.value = res.data
    } else {
      ElMessage.error(`查询来源端数据库的Schema失败,${res.message}`)
      sourceConnectionSchemas.value = []
    }
  } catch (error) {
    ElMessage.error("查询来源端Schema失败")
    console.error(error)
  }
}

// 源端Schema变更
const selectCreateChangedSourceSchema = async (value) => {
  sourceSchemaTables.value = []
  try {
    let res
    if (dataform.tableType === 'TABLE') {
      res = await getConnectionTables(dataform.sourceConnectionId, value)
    } else {
      res = await getConnectionViews(dataform.sourceConnectionId, value)
    }
    if (res.code === 200) {
      sourceSchemaTables.value = res.data
    } else {
      ElMessage.error(`查询来源端数据库表列表失败,${res.message}`)
      sourceSchemaTables.value = []
    }
  } catch (error) {
    ElMessage.error("查询来源端表列表失败")
    console.error(error)
  }
}

// 表类型变更
const selectCreateChangedTableType = async (value) => {
  sourceSchemaTables.value = []
  try {
    let res
    if (value === 'TABLE') {
      res = await getConnectionTables(dataform.sourceConnectionId, dataform.sourceSchema)
    } else {
      res = await getConnectionViews(dataform.sourceConnectionId, dataform.sourceSchema)
    }
    if (res.code === 200) {
      sourceSchemaTables.value = res.data
    } else {
      ElMessage.error(`查询来源端数据库表列表失败,${res.message}`)
      sourceSchemaTables.value = []
    }
  } catch (error) {
    ElMessage.error("查询来源端表列表失败")
    console.error(error)
  }
}

// 添加增量同步表
const handleAddInputIncrTable = () => {
  if (!dataform.sourceConnectionId || dataform.sourceConnectionId < 0 || !dataform.sourceSchema) {
    ElMessage.warning("请选择【源端数据源】和【源端模式名】！")
    return
  }

  if (!dataform.includeOrExclude) {
    ElMessage.warning("请选择源端表选择的【配置方式】！")
    return
  }

  if (dataform.includeOrExclude === "INCLUDE") {
    preiveSeeTableNameList.value = dataform.sourceTables.length === 0 
      ? [...sourceSchemaTables.value] 
      : [...dataform.sourceTables]
  } else {
    if (dataform.sourceTables.length === 0) {
      ElMessage.warning("请选择排除表的【表名配置】！")
      return
    }
    // 求差集
    preiveSeeTableNameList.value = JSON.parse(JSON.stringify(sourceSchemaTables.value))
    dataform.sourceTables.forEach(one => {
      preiveSeeTableNameList.value.some((item, index) => {
        if (item === one) {
          preiveSeeTableNameList.value.splice(index, 1)
          return true
        }
      })
    })
  }
  columnNameIncrementDialogVisible.value = true
}

// 删除增量同步表
const handleDeleteIncrTable = (index) => {
  dataform.incrTableColumns.splice(index, 1)
}

// 目标端连接变更
const selectChangedTargetConnection = async (value) => {
  targetConnection.value = connectionNameList.value.find(item => item.id === value) || {}
  if (targetConnection.value) {
    dataform.targetTypeName = targetConnection.value.typeName
  }

  try {
    const res = await getConnectionSchemas(value)
    if (res.code === 200) {
      targetConnectionSchemas.value = res.data
    } else {
      ElMessage.error(`查询目的端数据库的Schema失败,${res.message}`)
      targetConnectionSchemas.value = []
    }
  } catch (error) {
    ElMessage.error("查询目的端Schema失败")
    console.error(error)
  }
}

// 添加表名映射行
const addTableNameMapperListRow = () => {
  dataform.tableNameMapper.push({ "fromPattern": "", "toValue": "" })
}

// 删除表名映射行
const deleteTableNameMapperListItem = (index) => {
  dataform.tableNameMapper.splice(index, 1)
}

// 预览表名映射
const previewTableNameMapList = async () => {
  if (!dataform.sourceConnectionId || dataform.sourceConnectionId < 0 || !dataform.sourceSchema) {
    ElMessage.warning("请选择【源端数据源】和【源端模式名】！")
    return
  }

  if (!dataform.includeOrExclude) {
    ElMessage.warning("请选择源端表选择的【配置方式】！")
    return
  }

  try {
    const res = await previewTableMapper({
      id: dataform.sourceConnectionId,
      schemaName: dataform.sourceSchema,
      isInclude: dataform.includeOrExclude === 'INCLUDE',
      tableNames: dataform.sourceTables,
      nameMapper: dataform.tableNameMapper,
      tableNameCase: dataform.tableNameCase
    })
    if (res.code === 200) {
      tableNamesMapperData.value = res.data
      tableNameMapperDialogVisible.value = true
    } else {
      tableNamesMapperData.value = []
      if (res.message) {
        ElMessage.error(res.message)
      }
    }
  } catch (error) {
    ElMessage.error("预览表名映射失败")
    console.error(error)
  }
}

// 添加字段名映射行
const addColumnNameMapperListRow = () => {
  dataform.columnNameMapper.push({ "fromPattern": "", "toValue": "" })
}

// 删除字段名映射行
const deleteColumnNameMapperListItem = (index) => {
  dataform.columnNameMapper.splice(index, 1)
}

// 预览字段名映射
const previewColumnNameMapList = () => {
  if (!dataform.sourceConnectionId || dataform.sourceConnectionId <= 0 || !dataform.sourceSchema) {
    ElMessage.warning("请选择【源端数据源】和【源端模式名】！")
    return
  }

  if (!dataform.includeOrExclude) {
    ElMessage.warning("请选择源端表选择的【配置方式】！")
    return
  }

  if (dataform.includeOrExclude === "INCLUDE") {
    preiveSeeTableNameList.value = dataform.sourceTables.length === 0 
      ? [...sourceSchemaTables.value] 
      : [...dataform.sourceTables]
  } else {
    if (dataform.sourceTables.length === 0) {
      ElMessage.warning("请选择排除表的【表名配置】！")
      return
    }
    // 求差集
    preiveSeeTableNameList.value = JSON.parse(JSON.stringify(sourceSchemaTables.value))
    dataform.sourceTables.forEach(one => {
      preiveSeeTableNameList.value.some((item, index) => {
        if (item === one) {
          preiveSeeTableNameList.value.splice(index, 1)
          return true
        }
      })
    })
  }
  preiveTableName.value = ""
  columnNamesMapperData.value = []
  columnNameMapperDialogVisible.value = true
}

// 查询预览字段名映射
const queryPreviewColumnNameMapperList = async () => {
  if (!preiveSeeTableNameList.value || preiveSeeTableNameList.value.length === 0) {
    ElMessage.warning("请在源端配置【表名配置】！")
    return
  }

  if (!preiveTableName.value) {
    ElMessage.warning("请选择一个表名！")
    return
  }

  try {
    const res = await previewColumnMapper({
      id: dataform.sourceConnectionId,
      schemaName: dataform.sourceSchema,
      isInclude: dataform.includeOrExclude === 'INCLUDE',
      tableName: preiveTableName.value,
      nameMapper: dataform.columnNameMapper,
      columnNameCase: dataform.columnNameCase
    })
    if (res.code === 200) {
      columnNamesMapperData.value = res.data
    } else {
      if (res.message) {
        ElMessage.error(res.message)
      }
    }
  } catch (error) {
    ElMessage.error("查询字段映射失败")
    console.error(error)
  }
}

// 单行点击选择增量字段
const singleRowClick = (row) => {
  if (row.canIncrement) {
    tempIncrTableName.value = preiveTableName.value
    tempIncrColumnName.value = row.originalName
    radio.value = row.originalName
    console.log(`table=${tempIncrTableName.value};column=${tempIncrColumnName.value}`)
  } else {
    ElMessageBox.alert("非整型或日期时间类型不能被选中", "提示信息", {
      confirmButtonText: "确定",
      type: "warning"
    })
  }
}

// 确认选择增量字段
const handleConfirmSelectIncrTableColumn = () => {
  if (!tempIncrTableName.value || !tempIncrColumnName.value) {
    ElMessageBox.alert("请选择一个标识增量字段来", "错误信息", {
      confirmButtonText: "确定",
      type: "error"
    })
    return
  }
  const exists = dataform.incrTableColumns.some(item => item.tableName === tempIncrTableName.value)
  if (!exists) {
    dataform.incrTableColumns.push({
      tableName: tempIncrTableName.value,
      columnName: tempIncrColumnName.value
    })
    handleCancelSelectIncrTableColumn()
  } else {
    ElMessageBox.alert(`已经存在增量同步表[${tempIncrTableName.value}]的配置了`, "提示信息", {
      confirmButtonText: "确定",
      type: "info"
    })
  }
}

// 取消选择增量字段
const handleCancelSelectIncrTableColumn = () => {
  columnNameIncrementDialogVisible.value = false
  preiveTableName.value = ""
  columnNamesMapperData.value = []
  tempIncrTableName.value = ""
  tempIncrColumnName.value = ""
  radio.value = ""
}

// 保存提交
const handleSave = async () => {
  if (!dataformRef.value) return
  
  // 设置自动同步模式对应的参数
  if (dataform.autoSyncMode === 0) {
    dataform.targetDropTable = false
    dataform.targetOnlyCreate = false
  } else if (dataform.autoSyncMode === 1) {
    dataform.targetDropTable = true
    dataform.targetOnlyCreate = true
  } else {
    dataform.targetDropTable = true
    dataform.targetOnlyCreate = false
  }

  // 表单校验
  const valid = await dataformRef.value.validate()
  if (valid) {
    const submitData = {
      name: dataform.name,
      description: dataform.description,
      scheduleMode: dataform.scheduleMode,
      cronExpression: dataform.cronExpression,
      config: {
        sourceConnectionId: dataform.sourceConnectionId,
        sourceSchema: dataform.sourceSchema,
        tableType: dataform.tableType,
        includeOrExclude: dataform.includeOrExclude,
        sourceTables: dataform.sourceTables,
        incrTableColumns: dataform.incrTableColumns,
        sourceBeforeSqlScripts: dataform.sourceBeforeSqlScripts,
        sourceAfterSqlScripts: dataform.sourceAfterSqlScripts,
        targetConnectionId: dataform.targetConnectionId,
        targetSchema: dataform.targetSchema,
        tableNameMapper: dataform.tableNameMapper,
        columnNameMapper: dataform.columnNameMapper,
        tableNameCase: dataform.tableNameCase,
        columnNameCase: dataform.columnNameCase,
        targetDropTable: dataform.targetDropTable,
        targetOnlyCreate: dataform.targetOnlyCreate,
        targetAutoIncrement: dataform.targetAutoIncrement,
        batchSize: dataform.batchSize,
        channelSize: dataform.channelSize,
        targetSyncOption: dataform.targetSyncOption,
        targetBeforeSqlScripts: dataform.targetBeforeSqlScripts,
        targetAfterSqlScripts: dataform.targetAfterSqlScripts,
      }
    }

    try {
      let res
      if (route.query.id && route.query.id > 0) {
        // 更新任务
        submitData.id = route.query.id
        res = await updateAssignment(submitData)
      } else {
        // 创建任务
        res = await createAssignment(submitData)
      }

      if (res.code === 200) {
        ElMessage.success(route.query.id ? '修改任务成功!' : '添加任务成功!')
        router.push('/data/pipeline/task')
      } else {
        if (res.message) {
          ElMessage.error(res.message)
        }
      }
    } catch (error) {
      ElMessage.error("提交任务失败")
      console.error(error)
    }
  } else {
    ElMessage.warning("请点击【上一步】检查输入")
  }
}

// 生命周期 - 挂载时加载数据
onMounted(() => {
  loadConnections()
  loadAssignmentDetail()
})
</script>

<style scoped>
.el-card {
  width: 100%;
  height: 100%;
  overflow: auto;
}

.tip-content {
  font-size: 12px;
}

.name-mapper-table,
.name-mapper-table table tr th,
.name-mapper-table table tr td {
  border-collapse: collapse;
  border: 1px solid #e0dddd;
  width: 100%;
}

.el-descriptions__body
  .el-descriptions__table
  .el-descriptions-row
  .el-descriptions-item__label {
  min-width: 20px;
  max-width: 60px;
}

.tips-style {
  font-size: 12px;
  color: #a0a6b8;
}

.block {
  padding-top: 6px;
  display: block;
}

.common-top {
  margin-top: 40px;
}
</style>