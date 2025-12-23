// Copyright tang.  All rights reserved.
// https://gitee.com/inrgihc/dbswitch
//
// Use of this source code is governed by a BSD-style license
//
// Author: tang (inrgihc@126.com)
// Date : 2020/1/2
// Location: beijing , china
/////////////////////////////////////////////////////////////
package com.alinesno.infra.data.pipeline.mapper;

import com.alinesno.infra.data.pipeline.entity.AssignmentJobEntity;
import com.alinesno.infra.data.pipeline.model.ops.OpsTaskJobTrend;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface AssignmentJobMapper extends BaseMapper<AssignmentJobEntity> {

  @Select("<script>"
      + "<if test='_databaseId == \"mysql\" '>"
      + " SELECT  "
      + "   DATE_FORMAT(create_time,'%Y-%m-%d') as of_date ,  "
      + "   count(*) as count_of_job, "
      + "   count(DISTINCT assignment_id) as count_of_task  "
      + "  FROM  "
      + " (  "
      + "   SELECT * FROM DBSWITCH_ASSIGNMENT_JOB "
      + "   WHERE DATE_SUB( CURDATE(), INTERVAL ${days} DAY ) <![CDATA[ <=  ]]> date(create_time) "
      + " ) t  "
      + " GROUP BY of_date "
      + "</if>"
      + "<if test='_databaseId == \"postgresql\" '>"
      + "  SELECT  "
      + "   to_char(create_time, 'YYYY-MM-DD') as of_date ,  "
      + "   count(*) as count_of_job, "
      + "   count(DISTINCT assignment_id) as count_of_task  "
      + "  FROM  "
      + " (  "
      + "   SELECT * FROM DBSWITCH_ASSIGNMENT_JOB "
      + "   WHERE CURRENT_DATE - INTERVAL'${days} day' <![CDATA[ <=  ]]> create_time::date "
      + " ) t  "
      + " GROUP BY of_date "
      + "</if>"
      + "<if test='_databaseId == \"h2\" '>"
      + " SELECT  "
      + "    CAST(create_time as DATE) AS of_date, "
      + "    COUNT(*) AS count_of_job, "
      + "    COUNT(DISTINCT assignment_id) AS count_of_task "
      + " FROM ( "
      + "    SELECT * FROM DBSWITCH_ASSIGNMENT_JOB "
      + "    WHERE CAST(create_time as DATE) <![CDATA[ >=  ]]> ( CURDATE()- ${days} ) "
      + ") t "
      + " GROUP BY of_date "
      + "</if>"
      + "</script>")
  @Results({
      @Result(column = "of_date", property = "dateOfDay"),
      @Result(column = "count_of_job", property = "countOfJob"),
      @Result(column = "count_of_task", property = "countOfTask")
  })
  List<OpsTaskJobTrend> queryTaskJobTrend(@Param("days") Integer days);

}
