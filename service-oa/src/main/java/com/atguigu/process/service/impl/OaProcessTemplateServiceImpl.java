package com.atguigu.process.service.impl;

import com.atguigu.model.process.ProcessTemplate;

import com.atguigu.model.process.ProcessType;
import com.atguigu.process.mapper.OaProcessTemplateMapper;
import com.atguigu.process.service.OaProcessService;
import com.atguigu.process.service.OaProcessTemplateService;
import com.atguigu.process.service.OaProcessTypeService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.Deployment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.InputStream;
import java.util.List;
import java.util.zip.ZipInputStream;

/**
 * <p>
 * 审批模板 服务实现类
 * </p>
 *
 * @author jelly
 * @since 2023-08-05
 */
@Service
public class OaProcessTemplateServiceImpl extends ServiceImpl<OaProcessTemplateMapper, ProcessTemplate> implements OaProcessTemplateService {

    @Autowired
    private OaProcessTypeService processTypeService;

    @Autowired
    private OaProcessService processService;



    @Override
    public IPage<ProcessTemplate> selectPage(Page<ProcessTemplate> pageParam) {
        //调用mapper的方法实现分页查询
        Page<ProcessTemplate> processTemplatePage = baseMapper.selectPage(pageParam, null);

        //第一步分页查询返回分页数据，从分页数据列表获取列表数据
        List<ProcessTemplate> processTemplateList = pageParam.getRecords();

        //遍历list集合，得到每个对象的审批类型id
        for (ProcessTemplate processTemplate:processTemplateList){
            Long processTypeId = processTemplate.getProcessTypeId();
            //根据审批类型id，查询获取对应名称
            LambdaQueryWrapper<ProcessType> wrapper=new LambdaQueryWrapper<>();
            wrapper.eq(ProcessType::getId,processTypeId);
            ProcessType processType = processTypeService.getOne(wrapper);
            if (processType==null){
                continue;
            }
            //完成最终封装processTypeName
            processTemplate.setProcessTypeName(processType.getName());
        }


        return processTemplatePage;
    }

    @Override
    public void publish(Long id) {
        //修改模板发布状态 1 已经发布
        ProcessTemplate processTemplate = baseMapper.selectById(id);
        processTemplate.setStatus(1);
        baseMapper.updateById(processTemplate);

        //TODO 部署流程定义，后续完善
        if (!StringUtils.isEmpty(processTemplate.getProcessDefinitionPath())){
            processService.deployByZip(processTemplate.getProcessDefinitionPath());
        }
    }


}
