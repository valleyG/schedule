package com.yss.schedule.mapper;

import com.yss.schedule.entity.TaskEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface ReportTaskDao {
    @Select("select REPORT_CODE as reportId,report_name as reportName" +
            "  from tc_rep_task" +
            " where sysdate between start_time and end_time" +
            "   and product_type_code is null" +
            "   and length(report_code) = 5 and PROCESS_STATUS = #{status} order by report_code asc")
     List<TaskEntity> getTaskListByStatus(@Param("status") String status);


    @Select("select femail from v_ungenerate_task_user_map where freport_id = #{reportId}")
    List<String> getTheEmailForUnGenerateTask(@Param("reportId") String reportId);

    @Select("select femail from v_unaudit_task_user_map where freport_id = #{reportId}")
    List<String> getTheEmailForUnAuditTask(@Param("reportId") String reportId);
}
