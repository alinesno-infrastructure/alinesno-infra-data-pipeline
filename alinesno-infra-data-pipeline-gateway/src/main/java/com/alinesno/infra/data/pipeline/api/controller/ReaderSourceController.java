package com.alinesno.infra.data.pipeline.api.controller;

import com.alinesno.infra.common.core.constants.SpringInstanceScope;
import com.alinesno.infra.common.facade.pageable.DatatablesPageBean;
import com.alinesno.infra.common.facade.pageable.TableDataInfo;
import com.alinesno.infra.common.facade.response.AjaxResult;
import com.alinesno.infra.common.web.adapter.rest.BaseController;
import com.alinesno.infra.common.web.log.utils.SpringUtils;
import com.alinesno.infra.data.pipeline.api.CheckDbConnectResult;
import com.alinesno.infra.data.pipeline.api.FetchDataDto;
import com.alinesno.infra.data.pipeline.api.dto.DatasourceDto;
import com.alinesno.infra.data.pipeline.api.utils.DbParserUtils;
import com.alinesno.infra.data.pipeline.api.utils.ReaderSourceMapping;
import com.alinesno.infra.data.pipeline.constants.PipeConstants;
import com.alinesno.infra.data.pipeline.datasource.IDataSourceReader;
import com.alinesno.infra.data.pipeline.entity.ReaderSourceEntity;
import com.alinesno.infra.data.pipeline.enums.DbDriverEnums;
import com.alinesno.infra.data.pipeline.scheduler.dto.SourceReader;
import com.alinesno.infra.data.pipeline.service.IReaderSourceService;
import com.alinesno.infra.data.pipeline.transfer.bean.FieldMetaBean;
import com.alinesno.infra.data.pipeline.transfer.bean.ReaderSourceBean;
import com.alinesno.infra.data.pipeline.transfer.bean.TableMetaBean;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.lang.exception.RpcServiceRuntimeException;
import java.util.List;
import java.util.Map;

/**
 * 处理与BusinessLogEntity相关的请求的Controller。
 * 继承自BaseController类并实现IBusinessLogService接口。
 *
 * @author luoxiaodong
 * @version 1.0.0
 */
@Slf4j
@RestController
@Scope(SpringInstanceScope.PROTOTYPE)
@RequestMapping("/api/infra/data/pipeline/readerSource")
public class ReaderSourceController extends BaseController<ReaderSourceEntity, IReaderSourceService> {

    @Autowired
    private IReaderSourceService service;

    /**
     * 获取BusinessLogEntity的DataTables数据。
     *
     * @param request HttpServletRequest对象。
     * @param model Model对象。
     * @param page DatatablesPageBean对象。
     * @return 包含DataTables数据的TableDataInfo对象。
     */
    @PostMapping("/datatables")
    public TableDataInfo datatables(HttpServletRequest request, Model model, DatatablesPageBean page) {
        log.debug("page = {}", ToStringBuilder.reflectionToString(page));
        return this.toPage(model, this.getFeign(), page);
    }

    @PostMapping("/checkDB")
    public AjaxResult checkDBConnect(@Validated @RequestBody DatasourceDto dto ) {

        ReaderSourceEntity dbListEntity = new ReaderSourceEntity() ;
        BeanUtils.copyProperties(dto, dbListEntity) ;

        DbParserUtils.parserJdbcUrl(dbListEntity , dto.getJdbcUrl()) ;

        CheckDbConnectResult result = service.checkDbConnect(dbListEntity);
        if (result.isAccepted()) {
            return AjaxResult.success("操作成功", result);
        } else {
            return AjaxResult.error("数据库检验失败", result);
        }
    }

    @PutMapping("/modifyDb")
    public AjaxResult modifyDb(@Validated @RequestBody DatasourceDto dto ) {

        ReaderSourceEntity dbEntity = new ReaderSourceEntity() ;

        BeanUtils.copyProperties(dto, dbEntity) ;
        DbParserUtils.parserJdbcUrl(dbEntity , dto.getJdbcUrl()) ;

        try {
            return super.update(null, dbEntity) ;
        } catch (Exception e) {
            throw new RpcServiceRuntimeException(e.getMessage()) ;
        }
    }

    /**
     * 获取到所有数据库源
     * @return
     */
    @GetMapping("/allDatasource")
    public AjaxResult allDatasource(){
        return AjaxResult.success(service.list()) ;
    }

    @PostMapping("/saveDb")
    public AjaxResult saveDb(@Validated @RequestBody DatasourceDto dto ) {

        ReaderSourceEntity dbListEntity = new ReaderSourceEntity() ;

        BeanUtils.copyProperties(dto, dbListEntity) ;
        DbParserUtils.parserJdbcUrl(dbListEntity , dto.getJdbcUrl()) ;

        try {
            return super.save(null, dbListEntity) ;
        } catch (Exception e) {
            throw new RpcServiceRuntimeException(e.getMessage()) ;
        }
    }

    /**
     * 获取到数据库源ID
     * @param dto
     * @return
     */
    @PostMapping("/fetchData")
    public AjaxResult fetchData(@Validated @RequestBody FetchDataDto dto) {
        log.debug("dto = {}", dto) ;

        ReaderSourceEntity resourceSource = service.getById(dto.getSourceId()) ;
        String readerType = resourceSource.getReaderType() ;
        IDataSourceReader dataSourceReader = SpringUtils.getBean(readerType.toLowerCase() + PipeConstants.READER_SUFFIX) ;

        SourceReader reader = ReaderSourceMapping.getReaderSource(resourceSource , dto) ;
        List<Map<String, Object>> rows = dataSourceReader.fetchData(reader) ;

        AjaxResult ajax =  AjaxResult.success(rows);
        ajax.put("fieldMeta" , dataSourceReader.analyseSourceField(reader));

        return ajax ;
    }

    /**
     * 获取到数据库源ID的表结构
     * @param dto
     * @return
     */
    @GetMapping("/fetchTableData")
    public AjaxResult fetchTableData(String sourceId) {
        log.debug("sourceId = {}", sourceId) ;

        ReaderSourceEntity resourceSource = service.getById(sourceId) ;
        String readerType = resourceSource.getReaderType() ;
        IDataSourceReader dataSourceReader = SpringUtils.getBean(readerType.toLowerCase() + PipeConstants.READER_SUFFIX) ;

        SourceReader reader = ReaderSourceMapping.getReaderSource(resourceSource , null) ;
        List<TableMetaBean> rows = dataSourceReader.fetchTableData(reader) ;

        return AjaxResult.success(rows);
    }

    @Override
    public IReaderSourceService getFeign() {
        return this.service;
    }
}