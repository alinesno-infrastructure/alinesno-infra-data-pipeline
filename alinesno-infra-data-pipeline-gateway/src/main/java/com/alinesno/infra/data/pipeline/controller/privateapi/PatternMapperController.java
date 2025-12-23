package com.alinesno.infra.data.pipeline.controller.privateapi;

import com.alinesno.infra.data.pipeline.common.annotation.TokenCheck;
import com.alinesno.infra.data.pipeline.common.response.Result;
import com.alinesno.infra.data.pipeline.config.SwaggerConfig;
import com.alinesno.infra.data.pipeline.model.request.PreviewColumnNameMapperRequest;
import com.alinesno.infra.data.pipeline.model.request.PreviewTableNameMapperRequest;
import com.alinesno.infra.data.pipeline.model.response.PreviewNameMapperResponse;
import com.alinesno.infra.data.pipeline.domain.PatternMapperService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@Api(tags = {"映射关系预览"})
@RestController
@RequestMapping(value = SwaggerConfig.API_V1 + "/mapper")
public class PatternMapperController {

  @Resource
  private PatternMapperService patternMapperService;

  @TokenCheck
  @ApiOperation(value = "表名映射预览")
  @PostMapping(value = "/preview/table", produces = MediaType.APPLICATION_JSON_VALUE)
  public Result<List<PreviewNameMapperResponse>> previewTableNamesMapper(
      @RequestBody PreviewTableNameMapperRequest request) {
    return patternMapperService.previewTableNamesMapper(request);
  }

  @TokenCheck
  @ApiOperation(value = "字段名映射预览")
  @PostMapping(value = "/preview/column", produces = MediaType.APPLICATION_JSON_VALUE)
  public Result<List<PreviewNameMapperResponse>> previewColumnNamesMapper(
      @RequestBody PreviewColumnNameMapperRequest request) {
    return patternMapperService.previewColumnNamesMapper(request);
  }
}
