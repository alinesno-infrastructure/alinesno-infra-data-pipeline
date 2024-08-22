<template>

    <el-form ref="ruleFormRef" label-width="100px">

        <!-- 七牛参数配置 -->
        <div v-for="item in props.pluginSource" :key="item">
            <!-- {{ item.icon }} - {{ item.name }} - {{ item.desc }} -->
            <el-divider content-position="left"><i :class="item.icon"></i> {{ item.desc }} - {{ item.name }} </el-divider>

            <!-- 清理为空字段 -->
            <div v-if="item.name === 'ClearNull'">
                <el-row>
                    <el-col :span="24">
                        <el-form-item label="随机字段">
                            <el-input placeholder="请输入随机字段，以|号分隔" />
                        </el-form-item>
                    </el-col>
                </el-row>
            </div>

            <!-- 大小写转换字段 -->
            <div v-if="item.name === 'ConvertCase'">
                <el-row>
                    <el-col :span="24">
                        <el-form-item label="转换字段">
                            <el-input placeholder="请输入随机字段，以|号分隔" />
                        </el-form-item>
                    </el-col>
                </el-row>
            </div>

            <!-- 字段加密处理 -->
            <div v-if="item.name === 'EncryptData'">
                <el-row>
                    <el-col :span="15">
                        <el-form-item label="需要加密字段">
                            <el-select v-model="value2" clearable multiple collapse-tags placeholder="选择字段" style="width: 240px">
                                <el-option
                                    v-for="item in options"
                                    :key="item.value"
                                    :label="item.label"
                                    :value="item.value"
                                />
                            </el-select>
                        </el-form-item>
                    </el-col>
                    <el-col :span="9">
                        <el-form-item label="加密算法">
                            <el-select placeholder="算法">
                                <el-option
                                    v-for="item in options"
                                    :key="item.value"
                                    :label="item.label"
                                    :value="item.value"
                                />
                            </el-select>
                        </el-form-item>
                    </el-col>
                </el-row>
            </div>

            <!-- 字段解密处理 -->
            <div v-if="item.name === 'DecryptData'">
                <el-row>
                    <el-col :span="15">
                        <el-form-item label="需要解密字段">
                            <el-select v-model="value1" clearable multiple collapse-tags placeholder="选择字段" style="width: 240px">
                                <el-option
                                    v-for="item in options"
                                    :key="item.value"
                                    :label="item.label"
                                    :value="item.value"
                                />
                            </el-select>
                        </el-form-item>
                    </el-col>
                    <el-col :span="24">
                        <el-form-item label="解密密钥">
                            <el-input placeholder="请输入解密密钥" />
                        </el-form-item>
                    </el-col>
                    <el-col :span="9">
                        <el-form-item label="解密算法">
                            <el-select placeholder="算法">
                                <el-option
                                    v-for="item in options"
                                    :key="item.value"
                                    :label="item.label"
                                    :value="item.value"
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
                        <el-form-item label="格式化字段">
                            <el-select v-model="value3" clearable multiple collapse-tags placeholder="选择字段" style="width: 100%">
                                <el-option
                                    v-for="item in options"
                                    :key="item.value"
                                    :label="item.label"
                                    :value="item.value"
                                />
                            </el-select>
                        </el-form-item>
                    </el-col>
                    <el-col :span="24">
                        <el-form-item label="日期格式化">
                            <el-input placeholder="请输入日期格式化，如yyyyMMdd HHmmss" />
                        </el-form-item>
                    </el-col>
                </el-row>
            </div>

            <!-- 将多个字段合并成为一个-->
            <div v-if="item.name === 'MergeFields'">
                <el-row>
                    <el-col :span="24">
                        <el-form-item label="合并字段">
                            <el-select v-model="value3" clearable multiple collapse-tags placeholder="选择字段" style="width: 100%">
                                <el-option
                                    v-for="item in options"
                                    :key="item.value"
                                    :label="item.label"
                                    :value="item.value"
                                />
                            </el-select>
                        </el-form-item>
                    </el-col>
                    <el-col :span="24">
                        <el-form-item label="合并方式">
                            <el-radio-group v-model="radio">
                                <el-radio :key="3" :value="3">求和</el-radio>
                                <el-radio :key="6" :value="6">求积</el-radio>
                                <el-radio :key="9" :value="9">分隔符</el-radio>
                            </el-radio-group>
                        </el-form-item>
                    </el-col>
                </el-row>
            </div>

            <!-- 去除数据字段中的前后空格 -->
            <div v-if="item.name === 'TrimWhitespace'">
                <el-row>
                    <el-col :span="24">
                        <el-form-item label="所有字段">
                            <el-switch v-model="value1" />
                        </el-form-item>
                    </el-col>
                    <el-col :span="24">
                        <el-form-item label="清理字段">
                            <el-select v-model="value3" clearable multiple collapse-tags placeholder="选择字段" style="width: 100%">
                                <el-option
                                    v-for="item in options"
                                    :key="item.value"
                                    :label="item.label"
                                    :value="item.value"
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
                        <el-form-item label="替换字段">
                            <el-select v-model="value3" clearable multiple collapse-tags placeholder="选择字段" style="width: 100%">
                                <el-option
                                    v-for="item in options"
                                    :key="item.value"
                                    :label="item.label"
                                    :value="item.value"
                                />
                            </el-select>
                        </el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item label="原文本">
                            <el-input placeholder="请输入原文本" />
                        </el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item label="替换文本">
                            <el-input placeholder="请输入替换文本" />
                        </el-form-item>
                    </el-col>
                </el-row>
            </div>

            <!-- 用指定值填充缺失的数据 -->
            <div v-if="item.name === 'FillMissing'">
                <el-row>
                    <el-col :span="24">
                        <el-form-item label="全字段">
                            <el-switch v-model="value1" />
                        </el-form-item>
                    </el-col>
                    <el-col :span="24">
                        <el-form-item label="替换字段">
                            <el-select v-model="value3" clearable multiple collapse-tags placeholder="选择字段" style="width: 100%">
                                <el-option
                                    v-for="item in options"
                                    :key="item.value"
                                    :label="item.label"
                                    :value="item.value"
                                />
                            </el-select>
                        </el-form-item>
                    </el-col>
                    <el-col :span="24">
                        <el-form-item label="指定值">
                            <el-input placeholder="请输入指定值" />
                        </el-form-item>
                    </el-col>
                </el-row>
            </div>

            <!-- 将数据字段格式化为指定的输出格式 -->
            <div v-if="item.name === 'FormatData'">
                <el-row>
                    <el-col :span="24">
                        <el-form-item label="替换字段">
                            <el-select v-model="value3" clearable multiple collapse-tags placeholder="选择字段" style="width: 100%">
                                <el-option
                                    v-for="item in options"
                                    :key="item.value"
                                    :label="item.label"
                                    :value="item.value"
                                />
                            </el-select>
                        </el-form-item>
                    </el-col>
                    <el-col :span="24">
                        <el-form-item label="指定格式">
                            <el-input placeholder="请输入指定值" />
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

const props = defineProps({
    pluginSource:{
        type: Array,
    }
})

const value1 = ref([])
const value2 = ref([])
const value3 = ref([])
const radio = ref(3)

const options = ref([
  {
    value: 'Option1',
    label: 'Option1',
  },
  {
    value: 'Option2',
    label: 'Option2',
  },
  {
    value: 'Option3',
    label: 'Option3',
  },
  {
    value: 'Option4',
    label: 'Option4',
  },
  {
    value: 'Option5',
    label: 'Option5',
  },
]);

</script>

<style scoped lang="scss">
</style>


