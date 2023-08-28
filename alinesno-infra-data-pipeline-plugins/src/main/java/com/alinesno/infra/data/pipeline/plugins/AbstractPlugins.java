package com.alinesno.infra.data.pipeline.plugins;

import com.alinesno.infra.data.pipeline.scheduler.dto.FilterPlugins;

/**
 * 基础插件
 */
public interface AbstractPlugins {

    /**
     * 处理插件每一行数据
     * @param row
     */
    public abstract String handleRow(String row , FilterPlugins plugins) ;

}
