package com.atguigu.process.service;

import com.atguigu.model.process.ProcessTemplate;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 审批模板 服务类
 * </p>
 *
 * @author jelly
 * @since 2023-08-05
 */
public interface OaProcessTemplateService extends IService<ProcessTemplate> {

    //审批管理列表
    IPage<ProcessTemplate> selectPage(Page<ProcessTemplate> pageParam);

    void publish(Long id);


}
