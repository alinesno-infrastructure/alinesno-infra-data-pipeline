package com.alinesno.infra.data.pipeline.plugins.handle;

import com.alinesno.infra.data.pipeline.constants.PipeConstants;
import com.alinesno.infra.data.pipeline.plugins.AbstractPlugins;
import com.alinesno.infra.data.pipeline.scheduler.dto.FilterPlugins;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * 清理空字段数据
 */
@Component("null_filed" + PipeConstants.PLUGIN_SUFFIX )
public class NullPlugin implements AbstractPlugins {

    private static final Logger log = LoggerFactory.getLogger(NullPlugin.class) ;

    /**
     * 数据处理
     * @param row
     * @param plugins
     * @return
     */
    @Override
    public String handleRow(String row, FilterPlugins plugins) {

        return row ;
    }
}
