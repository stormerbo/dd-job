package cn.ddlover.job.mapper;

import cn.ddlover.job.entity.Executor;
import cn.ddlover.job.entity.ListExecutorReq;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * @author stormer.xia
 * @version 1.0
 * @date 2020/12/2 15:32
 */
@Repository
@Mapper
public interface ExecutorMapper {

  int insert(Executor executor);

  Executor selectByExecutorName(@Param("executorName")String executorName);

  List<Executor> listExecutor(ListExecutorReq listExecutorReq);

  Integer countExecutor(ListExecutorReq listExecutorReq);
}
