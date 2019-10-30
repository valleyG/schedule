package com.yss.schedule.mapper;

import com.yss.schedule.entity.WorkDay;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author yss
 */
@Mapper
public interface WorkDayDao {
    @Select("select biz_date as bizDate,is_holiday as workDayFlag from TC_DF_HOLIDAY where c_data_type = 'CN'")
    List<WorkDay> findList();


    /**
     * 查询给定时间的之前工作日
     * @param currentDate 当前日期
     * @param beforeDays 几天前
     * @return 几天前的工作日
     */
    @Select("SELECT BIZ_DATE" +
            "       FROM (SELECT BIZ_DATE, ROW_NUMBER() OVER(ORDER BY BIZ_DATE DESC) RN\n" +
            "              FROM TC_DF_HOLIDAY C" +
            "             WHERE C.C_DATA_TYPE = 'CN'" +
            "               AND C.IS_HOLIDAY = '1'" +
            "               AND C.BIZ_DATE < #{currentDate}) where rn = #{beforeDays}")
    String getWorKDay(@Param("currentDate") String currentDate, @Param("beforeDays") int beforeDays);

    @Select("select biz_date from TC_DF_HOLIDAY where C_DATA_TYPE = 'CN' and IS_HOLIDAY = '1' and BIZ_DATE = #{currentDate}")
    String isWorkDay(@Param("currentDate") String currentDate);
}
