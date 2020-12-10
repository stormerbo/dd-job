package cn.ddlover.job.mapper;

import cn.ddlover.job.entity.ExecutorMachine;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * @author stormer.xia
 * @version 1.0
 * @date 2020/12/10 18:09
 */
@Repository
@Mapper
public interface ExecutorMachineMapper {

  void insert(ExecutorMachine executorMachine);

  List<ExecutorMachine> listByExecutorId(@Param("executorId") Long executorId);
}
