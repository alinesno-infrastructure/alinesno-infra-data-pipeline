package com.alinesno.infra.data.pipeline.controller.converter;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import com.alinesno.infra.data.pipeline.common.converter.AbstractConverter;
import com.alinesno.infra.data.pipeline.entity.AssignmentTaskEntity;
import com.alinesno.infra.data.pipeline.model.response.AssignmentsDataResponse;

/**
 * @author Li Zemin
 * @since 2024/4/22 9:44
 */
public class AssignmentsConverter extends
        AbstractConverter<AssignmentTaskEntity, AssignmentsDataResponse> {

	@Override
	public AssignmentsDataResponse convert(AssignmentTaskEntity assignmentTaskEntity) {
		AssignmentsDataResponse response = new AssignmentsDataResponse();
		response.setId(assignmentTaskEntity.getId());
		response.setName(assignmentTaskEntity.getName());
		response.setDescription(assignmentTaskEntity.getDescription());
		response.setScheduleMode(assignmentTaskEntity.getScheduleMode().getName());
		response.setCronExpression(assignmentTaskEntity.getCronExpression());
		response.setIsPublished(assignmentTaskEntity.getPublished());
		response.setCreateTime(DateUtil.format(assignmentTaskEntity.getCreateTime(), DatePattern.NORM_DATETIME_PATTERN));
		response.setUpdateTime(DateUtil.format(assignmentTaskEntity.getUpdateTime(), DatePattern.NORM_DATETIME_PATTERN));
		return response;
	}
}
