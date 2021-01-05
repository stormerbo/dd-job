package cn.ddlover.job.mapper;

import cn.ddlover.job.entity.Job;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * @author stormer.xia
 * @version 1.0
 * @date 2021/1/5 16:40
 */
@Repository
public interface JobMapper {

  int insert(Job job);

  List<Job> listByExecutorId(@Param("executorId") Long executorId);
}
