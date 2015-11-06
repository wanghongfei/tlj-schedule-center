package com.taolijie.schedule.dao.mapper.schedule;

import com.taolijie.schedule.model.TaskModel;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface TaskModelMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(TaskModel record);

    int insertSelective(TaskModel record);

    TaskModel selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(TaskModel record);

    int updateByPrimaryKey(TaskModel record);

    List<TaskModel> findBy(TaskModel example);

    List<TaskModel> selectByInterval(@Param("start") Date start, @Param("end") Date end);
}