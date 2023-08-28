package com.alinesno.infra.data.pipeline.plugins;

import com.alinesno.infra.data.pipeline.plugins.utils.SpringContextUtil;
import com.alinesno.infra.data.pipeline.scheduler.dto.FilterPlugins;
import com.alinesno.infra.data.pipeline.scheduler.dto.TaskInfoDto;
import com.alinesno.infra.data.pipeline.transfer.IDataSourcePlugins;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.LineIterator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

@Component
public class PipelinePlugins implements IDataSourcePlugins {

    private static final Logger log = LoggerFactory.getLogger(PipelinePlugins.class) ;

    @Override
    public File transformData(TaskInfoDto taskInfoDto, File sourceFile) throws IOException {

        List<FilterPlugins> pluginsList = taskInfoDto.getPlugins() ;

        File pluginsTransferFile = new File(sourceFile.getAbsolutePath() + ".transfer")  ;

        int count = 0 ;
        LineIterator it = FileUtils.lineIterator(sourceFile , "UTF-8");
        try {
            while (it.hasNext()) {
                String line = it.nextLine();

                // 处理每一条数据，处理之后，重新写入新的文件中
                for(FilterPlugins p : pluginsList){
                    AbstractPlugins pluginObj = (AbstractPlugins) SpringContextUtil.getBean(p.getName());
                    line = pluginObj.handleRow(line , p);
                }

                log.info("-->>>> excel count = {}" , count++);

                // 重新写入新的插件文件
                IOUtils.write(line , new FileOutputStream(pluginsTransferFile ,true), "UTF-8");
            }
        } finally {
            IOUtils.closeQuietly(it);
        }

        return null;
    }


}
