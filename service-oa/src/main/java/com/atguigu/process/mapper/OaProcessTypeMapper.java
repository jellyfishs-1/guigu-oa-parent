package com.atguigu.process.mapper;

import com.atguigu.model.process.ProcessType;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 审批类型 Mapper 接口
 * </p>
 *
 * @author jelly
 * @since 2023-08-05
 */
@Mapper
public interface OaProcessTypeMapper extends BaseMapper<ProcessType> {

}
