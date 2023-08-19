package com.atguigu.process.service;

import com.atguigu.model.process.ProcessType;

import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 审批类型 服务类
 * </p>
 *
 * @author jelly
 * @since 2023-08-05
 */
public interface OaProcessTypeService extends IService<ProcessType> {

    Object findProcessType();
}
