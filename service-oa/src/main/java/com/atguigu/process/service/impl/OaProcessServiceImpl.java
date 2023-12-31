package com.atguigu.process.service.impl;


import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.atguigu.auth.service.SysUserService;
import com.atguigu.model.process.Process;
import com.atguigu.model.process.ProcessRecord;
import com.atguigu.model.process.ProcessTemplate;
import com.atguigu.model.system.SysUser;
import com.atguigu.process.mapper.OaProcessMapper;
import com.atguigu.process.service.OaProcessRecordService;
import com.atguigu.process.service.OaProcessService;
import com.atguigu.process.service.OaProcessTemplateService;
import com.atguigu.security.custom.LoginUserInfoHelper;
import com.atguigu.vo.process.ApprovalVo;
import com.atguigu.vo.process.ProcessFormVo;
import com.atguigu.vo.process.ProcessQueryVo;
import com.atguigu.vo.process.ProcessVo;
import com.atguigu.wechat.service.MessageService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.activiti.bpmn.model.*;
import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.history.HistoricTaskInstanceQuery;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipInputStream;

/**
 * <p>
 * 审批类型 服务实现类
 * </p>
 *
 * @author jelly
 * @since 2023-08-05
 */
@Service
public class OaProcessServiceImpl extends ServiceImpl<OaProcessMapper, Process> implements OaProcessService {

    @Autowired
    private OaProcessMapper processMapper;

    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private OaProcessTemplateService processTemplateService;

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private OaProcessRecordService processRecordService;

    @Autowired
    private HistoryService historyService;

    @Autowired
    private MessageService messageService;

    @Override
    public IPage<ProcessVo> selectPage(Page<ProcessVo> pageParam, ProcessQueryVo processQueryVo) {
        IPage<ProcessVo> page = processMapper.selectPage(pageParam, processQueryVo);
        return page;
    }

    @Override
    public void deployByZip(String deployPath) {
        // 定义zip输入流
        InputStream inputStream = this
                .getClass()
                .getClassLoader()
                .getResourceAsStream(deployPath);
        ZipInputStream zipInputStream = new ZipInputStream(inputStream);
        // 流程部署
        Deployment deployment = repositoryService.createDeployment()
                .addZipInputStream(zipInputStream)
                .deploy();

        System.out.println(deployment.getId());
        System.out.println(deployment.getName());
    }

    //启动流程
    @Override
    public void startUp(ProcessFormVo processFormVo) {
//        //1根据当前用户id获取用户信息
//        SysUser sysUser = sysUserService.getById(LoginUserInfoHelper.getUserId());
//
//        //2根据审批模板id把模板信息查询
//        ProcessTemplate processTemplate = processTemplateService.getById(processFormVo.getProcessTemplateId());
//
//        //3保存提交审批信息到业务表，oa_process
//        com.atguigu.model.process.Process process = new com.atguigu.model.process.Process();
//        //processFormVo复制到process对象里面
//        BeanUtils.copyProperties(processFormVo,process);
//        //其他
//        String workNo = System.currentTimeMillis() + "";
//        process.setProcessCode(workNo);
//        process.setUserId(LoginUserInfoHelper.getUserId());
//        process.setFormValues(processFormVo.getFormValues());
//        process.setTitle(sysUser.getName() + "发起" + processTemplate.getName() + "申请");
//        process.setStatus(1);
//
//        baseMapper.insert(process);
//
//        //4 启动流程实例 -RuntimeService
//        //4.1流程定义key
//        String processDefinitionKey = processTemplate.getProcessDefinitionKey();
//        //4.2 业务key processId
//        String businessKey = String.valueOf(process.getId());
//        //4.3流程参数 from表达json数据，转换map集合
//        String formValues = processFormVo.getFormValues();
//        //fromData
//        JSONObject jsonObject = JSON.parseObject(formValues);
//        JSONObject fromData = jsonObject.getJSONObject("formData");
//        //遍历fromData得到内容，封装map集合
//        Map<String,Object> map=new HashMap<>();
//        for (Map.Entry<String,Object> entry:fromData.entrySet()){
//            map.put(entry.getKey(),entry.getValue());
//        }
//        Map<String,Object> variables=new HashMap<>();
//        variables.put("data",map);
//        //4 启动流程实例 -RuntimeService
//        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(processDefinitionKey,
//                businessKey, variables);
//
//        //5查询下一个审批人
//        //审批人可能多个
//        List<Task> taskList=this.getCurrentTaskList(processInstance.getId());
//        List<String> nameList=new ArrayList<>();
//        for (Task task:taskList){
//            String assigneeName = task.getAssignee();
//            SysUser user = sysUserService.getUserByUserName(assigneeName);
//            String name = user.getName();
//            nameList.add(name);
//
//            //6 推送消息
//
//        }
//        process.setProcessInstanceId(processInstance.getId());
//        process.setDescription("等待" + StringUtils.join(nameList.toArray(), ",") + "审批");
//        //7 业务和流程关联 更新oa_process数据
//        baseMapper.updateById(process);
//
//        //记录操作审批信息记录
//        processRecordService.record(process.getId(),1,"发起申请");

        SysUser sysUser = sysUserService.getById(LoginUserInfoHelper.getUserId());

        ProcessTemplate processTemplate = processTemplateService.getById(processFormVo.getProcessTemplateId());
        Process process = new Process();
        BeanUtils.copyProperties(processFormVo, process);
        String workNo = System.currentTimeMillis() + "";
        process.setProcessCode(workNo);
        process.setUserId(LoginUserInfoHelper.getUserId());
        process.setFormValues(processFormVo.getFormValues());
        process.setTitle(sysUser.getName() + "发起" + processTemplate.getName() + "申请");
        process.setStatus(1);
        processMapper.insert(process);

        //绑定业务id
        String businessKey = String.valueOf(process.getId());
        //流程参数
        Map<String, Object> variables = new HashMap<>();
        //将表单数据放入流程实例中
        JSONObject jsonObject = JSON.parseObject(process.getFormValues());
        JSONObject formData = jsonObject.getJSONObject("formData");
        Map<String, Object> map = new HashMap<>();
        //循环转换
        for (Map.Entry<String, Object> entry : formData.entrySet()) {
            map.put(entry.getKey(), entry.getValue());
        }
        variables.put("data", map);
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(processTemplate.getProcessDefinitionKey(), businessKey, variables);
        //业务表关联当前流程实例id
        String processInstanceId = processInstance.getId();
        process.setProcessInstanceId(processInstanceId);

        //计算下一个审批人，可能有多个（并行审批）
        List<Task> taskList = this.getCurrentTaskList(processInstanceId);
        if (!CollectionUtils.isEmpty(taskList)) {
            List<String> assigneeList = new ArrayList<>();
            for(Task task : taskList) {
                SysUser user = sysUserService.getUserByUserName(task.getAssignee());
                assigneeList.add(user.getName());

                //推送消息给下一个审批人，后续完善
                messageService.pushPendingMessage(process.getId(),user.getId(),task.getId());
            }
            process.setDescription("等待" + StringUtils.join(assigneeList.toArray(), ",") + "审批");
        }
        processMapper.updateById(process);

        //记录操作行为
        processRecordService.record(process.getId(), 1, "发起申请");

    }

    //查询处理任务
    @Override
    public IPage<ProcessVo> findPending(Page<Process> processPage) {
        //1 封装查询条件，根据当前登录的用户名称
        TaskQuery query = taskService.createTaskQuery()
                .taskAssignee(LoginUserInfoHelper.getUsername())
                .orderByTaskCreateTime()
                .desc();
        //2 调用方法分页条件查询，返回list集合，代办任务集合
        //listPage方法有两个参数
        //第一个参数：开始位置 第二个位置：每页显示记录数
        int begin = (int) ((processPage.getCurrent() - 1) * processPage.getSize());
        int size=(int)processPage.getSize();
        List<Task> taskList = query.listPage(begin, size);
        long totalCount = query.count();

        //3 封装返回list集合数据 到list<ProcessVo>里面
        //List<Task> -- List<ProcessVo>
        List<ProcessVo> processVoList=new ArrayList<>();
        for (Task task:taskList){
            //从task获取流程实例id
            String processInstanceId = task.getProcessInstanceId();
            //根据流程实例id获取实例对象
            ProcessInstance processInstance = runtimeService.createProcessInstanceQuery()
                    .processInstanceId(processInstanceId)
                    .singleResult();
            //从流程实例对象获取业务key
            String businessKey = processInstance.getBusinessKey();
            if (businessKey==null){
                continue;
            }
            //根据业务key获取ProcessVo对象
            long processId = Long.parseLong(businessKey);
            Process process = baseMapper.selectById(processId);

            //process对象 复制 ProcessVo对象
            ProcessVo processVo = new ProcessVo();
            BeanUtils.copyProperties(process,processVo);
            processVo.setTaskId(task.getId());
            //放到最终list集合processVoList
            processVoList.add(processVo);
        }

        //4 封装返回IPage对象
        IPage<ProcessVo> page=new Page<>(processPage.getCurrent(),
                processPage.getSize(),totalCount);
        page.setRecords(processVoList);
        return page;
    }

    @Override
    public Map<String, Object> show(Long id) {
        //1 根据流程id获取流程信息process
        Process process = baseMapper.selectById(id);

        //2 根据流程id获取流程记录信息
        LambdaQueryWrapper<ProcessRecord> wrapper=new LambdaQueryWrapper<>();
        wrapper.eq(ProcessRecord::getProcessId,id);
        List<ProcessRecord> processRecordList = processRecordService.list(wrapper);

        //3 根据模板id查询模板信息
        ProcessTemplate processTemplate = processTemplateService.getById(process.getProcessTemplateId());


        //4 判断当前用户是否可以审批
        //可以看到信息不一定能审批，不能重复审批
        boolean isApprove=false;
        List<Task> taskList = this.getCurrentTaskList(process.getProcessInstanceId());
        for (Task task:taskList){
            //判断任务审批人是否当前用户
            if (task.getAssignee().equals(LoginUserInfoHelper.getUsername())){
                isApprove=true;
            }
        }
        //5 查询封装到map集合，返回
        Map<String, Object> map = new HashMap<>();
        map.put("process", process);
        map.put("processRecordList", processRecordList);
        map.put("processTemplate", processTemplate);
        map.put("isApprove", isApprove);
        return map;
    }

    @Override
    public void approve(ApprovalVo approvalVo) {
        //1 从approvalVo获取任务id，根据任务id获取流程变量
        String taskId = approvalVo.getTaskId();
        Map<String, Object> variables = taskService.getVariables(taskId);
        for (Map.Entry<String,Object> entry:variables.entrySet()){
            System.out.println(entry.getKey());
            System.out.println(entry.getValue());
        }
        //2 判断审批状态值
        if (approvalVo.getStatus()==1) {
            //2.1 状态值=1 审批通过
            Map<String,Object> variable=new HashMap<>();
            taskService.complete(taskId,variable);
        }else {
            //2.2 状态值=-1 驳回 流程直接结束
            this.endTask(taskId);
        }
        //3 记录审批相关过程信息 oa_process_record
        String description=approvalVo.getStatus().intValue()==1?"已通过":"驳回";
        processRecordService.record(approvalVo.getProcessId(),
                approvalVo.getStatus(),description);
        //4 查询下一个审批人,更新流程表记录process表记录
        Process process = baseMapper.selectById(approvalVo.getProcessId());
        //查询任务
        List<Task> taskList = this.getCurrentTaskList(process.getProcessInstanceId());
        if (!CollectionUtils.isEmpty(taskList)){
            List<String> assignList=new ArrayList<>();
            for (Task task:taskList){
                String assignee=task.getAssignee();
                SysUser sysUser = sysUserService.getUserByUserName(assignee);
                assignList.add(sysUser.getName());

                //TODO 公众号消息推送
            }
            //更新process流程信息
            process.setDescription("等待" + StringUtils.join(assignList.toArray(), ",") + "审批");
            process.setStatus(1);
        }else {
            if(approvalVo.getStatus().intValue() == 1) {
                process.setDescription("审批完成（同意）");
                process.setStatus(2);
            } else {
                process.setDescription("审批完成（拒绝）");
                process.setStatus(-1);
            }
        }
        //推送消息给申请人
        this.updateById(process);
    }

    //已处理
    @Override
    public IPage<ProcessVo> findProcessed(Page<Process> pageParam) {
        //封装查询条件
        HistoricTaskInstanceQuery query = historyService.createHistoricTaskInstanceQuery()
                .taskAssignee(LoginUserInfoHelper.getUsername())
                .finished().orderByTaskCreateTime().desc();

        //调用方法条件分页查询，返回list集合
        int begin= (int) ((pageParam.getCurrent()-1)*pageParam.getSize());
        int size= (int) pageParam.getSize();
        List<HistoricTaskInstance> list = query.listPage(begin, size);
        long totalCount = query.count();


        //遍历返回list集合，封装List<ProcessVo>
        List<ProcessVo> processVoList=new ArrayList<>();
        for (HistoricTaskInstance item:list){
            //流程实例id
            String processInstanceId = item.getProcessInstanceId();
            //根据流程实例id查询获取process信息
            LambdaQueryWrapper<Process> wrapper=new LambdaQueryWrapper<>();
            wrapper.eq(Process::getProcessInstanceId,processInstanceId);
            Process process = baseMapper.selectOne(wrapper);
            //process -- processVo
            ProcessVo processVo=new ProcessVo();
            BeanUtils.copyProperties(process,processVo);
            //放到list
            processVoList.add(processVo);
        }


        //IPage封装分页查询所有数据，返回
        IPage<ProcessVo> pageModel=
                new Page<>(pageParam.getCurrent(),pageParam.getSize(),totalCount);
        pageModel.setRecords(processVoList);
        return pageModel;
    }

    @Override
    public IPage<ProcessVo> findStarted(Page<ProcessVo> pageParam) {
        ProcessQueryVo processQueryVo = new ProcessQueryVo();
        processQueryVo.setUserId(LoginUserInfoHelper.getUserId());
        IPage<ProcessVo> page = processMapper.selectPage(pageParam, processQueryVo);
        for (ProcessVo item : page.getRecords()) {
            item.setTaskId("0");
        }
        return page;    }

    private void endTask(String taskId) {
        //1 根据任务id获取任务对象
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        //2 获取流程定义模型BpmnModel
        BpmnModel bpmnModel = repositoryService.getBpmnModel(task.getProcessDefinitionId());

        //3 获取结束流向节点
        List<EndEvent> endEventList = bpmnModel.getMainProcess().findFlowElementsOfType(EndEvent.class);
        if (CollectionUtils.isEmpty(endEventList)){
            return;
        }
        FlowNode endFlowNode = endEventList.get(0);

        //4 当前流向节点
        FlowNode currentFlowNode = (FlowNode) bpmnModel.getMainProcess().getFlowElement(task.getTaskDefinitionKey());

        //  临时保存当前活动的原始方向
        List originalSequenceFlowList = new ArrayList<>();
        originalSequenceFlowList.addAll(currentFlowNode.getOutgoingFlows());

        //5 清理当前流动方向
        currentFlowNode.getOutgoingFlows().clear();

        //6 创建新流行
        SequenceFlow newSequenceFlow = new SequenceFlow();
        newSequenceFlow.setId("newSequenceFlow");
        newSequenceFlow.setSourceFlowElement(currentFlowNode);
        newSequenceFlow.setTargetFlowElement(endFlowNode);

        //7 当前节点指向新方向
        List newSequenceFlowList = new ArrayList<>();
        newSequenceFlowList.add(newSequenceFlow);
        currentFlowNode.setOutgoingFlows(newSequenceFlowList);

        //8 完成当前任务
        taskService.complete(task.getId());

    }

    private List<Task> getCurrentTaskList(String processInstanceId) {
        List<Task> tasks = taskService.createTaskQuery().processInstanceId(processInstanceId).list();
        return tasks;
    }
}
