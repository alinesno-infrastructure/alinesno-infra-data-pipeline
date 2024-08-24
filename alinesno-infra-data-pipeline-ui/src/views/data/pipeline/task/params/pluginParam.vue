<template>

    <el-form ref="ruleFormRef" :model="form" :rules="rules" label-width="120px" label-position="right">

        <!-- 七牛参数配置 -->
        <div v-for="item in props.pluginSource" :key="item">
            <el-divider content-position="left"><i :class="item.icon"></i> {{ item.desc }}</el-divider>

            <!-- 清理为空字段 -->
            <div v-if="item.name === 'ClearNull'">
                <el-row>
                    <el-col :span="24">
                    <el-form-item label="处理为空字段" prop="clearNull.fields">
                        <el-select v-model="form.clearNull.fields" clearable multiple collapse-tags placeholder="选择字段" style="width: 100%">
                            <el-option
                                v-for="option in props.readerFieldMate"
                                :key="option.name"
                                :label="option.name"
                                :value="option.name"
                            />
                        </el-select>
                    </el-form-item>
                    </el-col>
                    <el-col :span="24">
                        <el-form-item label="随机类型" prop="clearNull.randomType">
                            <el-radio-group v-model="form.clearNull.randomType">
                            <el-radio
                                v-for="(type, index) in randomTypes"
                                :key="index"
                                :label="type.value"
                                :value="type.value"
                            >{{ type.label }}</el-radio>
                            </el-radio-group>
                        </el-form-item>
                    </el-col>
                </el-row>
            </div>

            <!-- 大小写转换字段 -->
            <div v-if="item.name === 'ConvertCase'">
                <el-row>
                    <el-col :span="24">
                        <el-form-item label="转换字段" prop="convertCase.fields">
                            <el-select v-model="form.convertCase.fields" clearable multiple collapse-tags placeholder="选择字段" style="width: 100%">
                                <el-option
                                    v-for="option in props.readerFieldMate"
                                    :key="option.name"
                                    :label="option.name"
                                    :value="option.name"
                                />
                            </el-select>
                        </el-form-item>
                    </el-col>
                    <el-col :span="24">
                        <el-form-item label="大小写选择" prop="convertCase.type">
                            <el-radio-group v-model="form.convertCase.type">
                            <el-radio
                                v-for="(type, index) in caseTypes"
                                :key="index"
                                :label="type.label"
                                :value="type.value"
                            >{{ type.label }}</el-radio>
                            </el-radio-group>
                        </el-form-item>
                    </el-col>
                </el-row>
            </div>

            <!-- 字段加密处理 -->
            <div v-if="item.name === 'EncryptData'">
                <el-row>
                <el-col :span="24">
                    <el-form-item label="需要加密字段" prop="encryptData.fields">
                    <el-select v-model="form.encryptData.fields" clearable multiple collapse-tags placeholder="选择字段" style="width: 240px">
                        <el-option
                          v-for="option in props.readerFieldMate"
                          :key="option.name"
                          :label="option.name"
                          :value="option.name"
                        />
                    </el-select>
                    </el-form-item>
                </el-col>
                <el-col :span="24">
                    <el-form-item label="加密算法" prop="encryptData.algorithm">
                    <el-select v-model="form.encryptData.algorithm" placeholder="选择加密算法">
                        <el-option
                        v-for="option in encryptionOptions"
                        :key="option.value"
                        :label="option.label"
                        :value="option.value"
                        />
                    </el-select>
                    </el-form-item>
                </el-col>
                </el-row>
            </div> 

            <!-- 字段解密处理 -->
            <div v-if="item.name === 'DecryptData'">
            <el-row>
                <el-col :span="24">
                <el-form-item label="需要解密字段" prop="decryptData.fields">
                    <el-select v-model="form.decryptData.fields" clearable multiple collapse-tags placeholder="选择字段" style="width: 240px">
                    <el-option
                        v-for="option in props.readerFieldMate"
                          :key="option.name"
                          :label="option.name"
                          :value="option.name"
                    />
                    </el-select>
                </el-form-item>
                </el-col>
                <el-col :span="24">
                <el-form-item label="解密密钥" prop="decryptData.decryptionKey">
                    <el-input v-model="form.decryptData.decryptionKey" placeholder="请输入解密密钥" />
                </el-form-item>
                </el-col>
                <el-col :span="24">
                <el-form-item label="解密算法" prop="decryptData.algorithm">
                    <el-select v-model="form.decryptData.algorithm" placeholder="选择解密算法">
                    <el-option
                        v-for="option in decryptionOptions"
                        :key="option.value"
                        :label="option.label"
                        :value="option.value"
                    />
                    </el-select>
                </el-form-item>
                </el-col>
            </el-row>
            </div>

            <!-- 日期字段格式化 -->
            <div v-if="item.name === 'FormatDate'">
            <el-row>
                <el-col :span="24">
                <el-form-item label="格式化字段" prop="formatDate.fields">
                    <el-select v-model="form.formatDate.fields" clearable multiple collapse-tags placeholder="选择字段" style="width: 100%">
                    <el-option
                        v-for="option in props.readerFieldMate"
                          :key="option.name"
                          :label="option.name"
                          :value="option.name"
                    />
                    </el-select>
                </el-form-item>
                </el-col>
                <el-col :span="24">
                <el-form-item label="日期格式化" prop="formatDate.dateFormat">
                    <el-input v-model="form.formatDate.dateFormat" placeholder="请输入日期格式化，如yyyyMMdd HHmmss" />
                </el-form-item>
                </el-col>
            </el-row>
            </div>

            <!-- 将多个字段合并成为一个 -->
            <div v-if="item.name === 'MergeFields'">
            <el-row>
                <el-col :span="24">
                <el-form-item label="合并字段" prop="mergeFields.fields">
                    <el-select v-model="form.mergeFields.fields" clearable multiple collapse-tags placeholder="选择字段" style="width: 100%">
                    <el-option
                        v-for="option in props.readerFieldMate"
                          :key="option.name"
                          :label="option.name"
                          :value="option.name"
                    />
                    </el-select>
                </el-form-item>
                </el-col>
                <el-col :span="24">
                <el-form-item label="合并方式" prop="mergeFields.mergeMethod">
                    <el-radio-group v-model="form.mergeFields.mergeMethod">
                    <el-radio
                        v-for="(method, index) in mergeMethods"
                        :key="index"
                        :label="method.label"
                        :value="method.value"
                    >{{ method.label }}</el-radio>
                    </el-radio-group>
                </el-form-item>
                </el-col>
                <el-col :span="24">
                <el-form-item label="新字段名称" prop="mergeFields.newFieldName">
                    <el-input v-model="form.mergeFields.newFieldName" placeholder="请输入合并后的新字段名称" />
                </el-form-item>
                </el-col>
            </el-row>
            </div>

            <!-- 去除数据字段中的前后空格 -->
            <div v-if="item.name === 'TrimWhitespace'">
            <el-row>
                <el-col :span="24">
                <el-form-item label="所有字段" prop="trimWhitespace.allFields">
                    <el-switch v-model="form.trimWhitespace.allFields" />
                </el-form-item>
                </el-col>
                <el-col :span="24">
                <el-form-item label="清理字段" prop="trimWhitespace.fields">
                    <el-select v-model="form.trimWhitespace.fields" clearable multiple collapse-tags placeholder="选择字段" style="width: 100%">
                    <el-option
                        v-for="option in props.readerFieldMate"
                          :key="option.name"
                          :label="option.name"
                          :value="option.name"
                    />
                    </el-select>
                </el-form-item>
                </el-col>
            </el-row>
            </div>

            <!-- 替换数据字段中的指定文本 -->
            <div v-if="item.name === 'ReplaceText'">
            <el-row>
                <el-col :span="24">
                <el-form-item label="替换字段" prop="replaceText.fields">
                    <el-select v-model="form.replaceText.fields" clearable multiple collapse-tags placeholder="选择字段" style="width: 100%">
                    <el-option
                        v-for="option in props.readerFieldMate"
                          :key="option.name"
                          :label="option.name"
                          :value="option.name"
                    />
                    </el-select>
                </el-form-item>
                </el-col>
                <el-col :span="24">
                <el-form-item label="原文本" prop="replaceText.originalText">
                    <el-input v-model="form.replaceText.originalText" placeholder="请输入原文本" />
                </el-form-item>
                </el-col>
                <el-col :span="24">
                  <el-form-item label="替换文本" prop="replaceText.replacementText">
                      <el-input v-model="form.replaceText.replacementText" placeholder="请输入替换文本" />
                  </el-form-item>
                </el-col>
            </el-row>
            </div> 

            <!-- 用指定值填充缺失的数据 -->
            <div v-if="item.name === 'FillMissing'">
            <el-row>
                <el-col :span="24">
                  <el-form-item label="全字段" prop="fillMissing.allFields">
                      <el-switch v-model="form.fillMissing.allFields" />
                  </el-form-item>
                </el-col>
                <el-col :span="24">
                <el-form-item label="替换字段" prop="fillMissing.fields">
                    <el-select v-model="form.fillMissing.fields" clearable multiple collapse-tags placeholder="选择字段" style="width: 100%">
                    <el-option
                        v-for="option in props.readerFieldMate"
                          :key="option.name"
                          :label="option.name"
                          :value="option.name"
                    />
                    </el-select>
                </el-form-item>
                </el-col>
                <el-col :span="24">
                <el-form-item label="指定值" prop="fillMissing.fillValue">
                    <el-input v-model="form.fillMissing.fillValue" placeholder="请输入指定值" />
                </el-form-item>
                </el-col>
            </el-row>
            </div>

            <!-- 将数据字段格式化为指定的输出格式 -->
            <div v-if="item.name === 'FormatData'">
                <el-row>
                <el-col :span="24">
                    <el-form-item label="替换字段" prop="formatData.fields">
                    <el-select v-model="form.formatData.fields" clearable multiple collapse-tags placeholder="选择字段" style="width: 100%">
                        <el-option
                        v-for="option in props.readerFieldMate"
                          :key="option.name"
                          :label="option.name"
                          :value="option.name"
                        />
                    </el-select>
                    </el-form-item>
                </el-col>
                <el-col :span="24">
                    <el-form-item label="指定格式" prop="formatData.format">
                    <el-input v-model="form.formatData.format" placeholder="请输入指定值" />
                    </el-form-item>
                </el-col>
                </el-row>
            </div>

        </div>
        <br/>
        <br/>
        <br/>

    </el-form>
</template>

<script setup name="JobReaderParams">
import { reactive, toRefs } from 'vue';

const props = defineProps({
  pluginSource: {
    type: Array,
  },
  readerFieldMate: {
    type: Array,
  },
});

const data = reactive({
  form: {
    clearNull: {
      fields: [],
      randomType: 3
    },
    convertCase: {
      fields: '',
      type: '' ,
    },
    encryptData: {
      fields: [],
      algorithm: ''
    },
    replaceText: {
      fields: [],
      originalText: '',
      replacementText: ''
    },
    trimWhitespace: {
      allFields: false,
      fields: []
    },
    trimWhitespace: {
      allFields: false,
      fields: []
    },
    mergeFields: {
      fields: [],
      mergeMethod: 3
    },
    decryptData: {
      fields: [],
      decryptionKey: '',
      algorithm: ''
    },
    formatDate: {
      fields: [],
      dateFormat: ''
    },
    decryptData: {
      fields: [],
      decryptionKey: '',
      algorithm: ''
    },
    formatDate: {
      fields: [],
      dateFormat: ''
    },
    fillMissing: {
      allFields: false,
      fields: [],
      fillValue: ''
    },
    formatData: {
      fields: [],
      format: ''
    }
  },
  rules: {
    'clearNull.fields': [{ required: true, message: "处理为空字段不能为空", trigger: "change" }],
    'clearNull.randomType': [{ required: true, message: "随机类型不能为空", trigger: "change" }],
    
    'convertCase.fields': [{ required: false, message: "转换字段不能为空", trigger: "blur" }],

    'encryptData.fields': [{ required: true, message: "需要加密字段不能为空", trigger: "change" }],
    'encryptData.algorithm': [{ required: true, message: "加密算法不能为空", trigger: "change" }],

    'replaceText.fields': [{ required: true, message: "替换字段不能为空", trigger: "change" }],
    'replaceText.originalText': [{ required: true, message: "原文本不能为空", trigger: "blur" }],
    'replaceText.replacementText': [{ required: true, message: "替换文本不能为空", trigger: "blur" }],

    'trimWhitespace.allFields': [{ required: false, message: "所有字段选项必须选择", trigger: "change" }],
    'trimWhitespace.fields': [{ required: false, message: "清理字段不能为空", trigger: "change" }],

    'mergeFields.fields': [{ required: true, message: "合并字段不能为空", trigger: "change" }],
    'mergeFields.mergeMethod': [{ required: true, message: "合并方式不能为空", trigger: "change" }],
    'mergeFields.newFieldName': [{ required: true, message: "合并方式新字段名称不能为空", trigger: "change" }],

    'decryptData.fields': [{ required: true, message: "需要解密字段不能为空", trigger: "change" }],
    'decryptData.decryptionKey': [{ required: true, message: "解密密钥不能为空", trigger: "blur" }],
    'decryptData.algorithm': [{ required: true, message: "解密算法不能为空", trigger: "change" }],

    'formatDate.fields': [{ required: true, message: "格式化字段不能为空", trigger: "change" }],
    'formatDate.dateFormat': [{ required: true, message: "日期格式化不能为空", trigger: "blur" }],

    'fillMissing.allFields': [{ required: false, message: "全字段选项必须选择", trigger: "change" }],
    'fillMissing.fields': [{ required: false, message: "替换字段不能为空", trigger: "change" }],
    'fillMissing.fillValue': [{ required: true, message: "指定值不能为空", trigger: "blur" }],

    'formatData.fields': [{ required: true, message: "替换字段不能为空", trigger: "change" }],
    'formatData.format': [{ required: true, message: "指定格式不能为空", trigger: "blur" }]
  }
});

const { form, rules } = toRefs(data);

const mergeMethods = ref([
  { value: 3, label: '求和' },
  { value: 6, label: '求积' },
  { value: 9, label: '分隔符' }
]);

const randomTypes = ref([
  { value: 3, label: '数字' },
  { value: 9, label: '英文' },
  { value: 6, label: '中文' }
]);

const caseTypes = ref([
  { value: 'upper', label: '大写' },
  { value: 'lower', label: '小写' },
]);

const encryptionOptions = ref([
  {value: 'AES',label: 'AES'},
  {value: 'RSA',label: 'RSA'},
  {value: 'DES',label: 'DES'},
]);

const decryptionOptions = ref([
  { value: 'AES', label: 'AES' },
  { value: 'RSA', label: 'RSA' },
  { value: 'DES', label: 'DES' }
]);

// 定义一个方法来收集表单数据
const submitPluginParam = () => {

  const formData = {};
  
  // 遍历form对象的每个属性
  for (const key in form.value) {
    const field = form.value[key];
    
    // 根据字段类型判断是否为空
    if (field !== null && field !== undefined) {
      switch (typeof field) {
        case 'object':
          // 对于数组和对象类型的字段，检查是否为空数组或空对象
          if (Array.isArray(field) && field.length > 0 || !Array.isArray(field) && Object.keys(field).length > 0) {
            formData[key] = field;
          }
          break;
        default:
          // 对于其他类型（如字符串），检查是否为空字符串
          if (field !== '') {
            formData[key] = field;
          }
          break;
      }
    }
  }

  // 可以在这里做进一步的数据处理或者提交表单
  console.log('Collected non-empty form data:', formData);

  return formData;
};

// 主动暴露方法
defineExpose({ submitPluginParam })

</script>

<style scoped lang="scss">
</style>


