import com.atguigu.ServiceAuthApplication;
import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

/**
 * @author jellyfish
 * @create 2023 -- 08 -- 03 -- 22:33
 * @描述
 * @email:1137648153@qq.com
 */

@SpringBootTest(classes = ServiceAuthApplication.class)
public class activity {

    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private HistoryService historyService;

    //单个流程实例挂起
    @Test
    public void SingleSuspendProcessInstance() {
        String processInstanceId="2cc85daa-3213-11ee-aaa2-00ff0fd27627";
        ProcessInstance processInstance = runtimeService.createProcessInstanceQuery()
                .processInstanceId(processInstanceId)
                .singleResult();
        //获取到当前流程定义是否为暂停状态   suspended方法为true代表为暂停   false就是运行的
        boolean suspended = processInstance.isSuspended();
        if (suspended) {
            runtimeService.activateProcessInstanceById(processInstanceId);
            System.out.println("流程实例:" + processInstanceId + "激活");
        } else {
            runtimeService.suspendProcessInstanceById(processInstanceId);
            System.out.println("流程实例:" + processInstanceId + "挂起");
        }
    }

    @Test
    public void suspendProcessInstance(){
        ProcessDefinition qinjia = repositoryService.createProcessDefinitionQuery()
                .processDefinitionKey("qinjia").singleResult();
        // 获取到当前流程定义是否为暂停状态 suspended方法为true是暂停的，suspended方法为false是运行的
        boolean suspended = qinjia.isSuspended();
        if (suspended){
            // 暂定,那就可以激活
            // 参数1:流程定义的id  参数2:是否激活    参数3:时间点
            repositoryService.activateProcessDefinitionById(qinjia.getId(),true,null);
            System.out.println("流程定义:" + qinjia.getId() + "激活");
        }else {
            repositoryService.suspendProcessDefinitionById(qinjia.getId(), true, null);
            System.out.println("流程定义:" + qinjia.getId() + "挂起");
        }
    }

    //创建流程实例
    @Test
    public void startUpProcessAddBusinessKey(){
        ProcessInstance instance = runtimeService.startProcessInstanceByKey("qinjia", "1001");
        System.out.println(instance.getBusinessKey());
        System.out.println(instance.getId());
    }

    //查询已经处理的任务
    @Test
    public void findCompletedTaskList(){
        List<HistoricTaskInstance> list = historyService.createHistoricTaskInstanceQuery()
                .taskAssignee("zhangsan")
                .finished().list();

        for(HistoricTaskInstance historicTaskInstance:list){
            System.out.println(historicTaskInstance.getProcessInstanceId());
            System.out.println(historicTaskInstance.getId());
            System.out.println(historicTaskInstance.getAssignee());
            System.out.println(historicTaskInstance.getName());
        }
    }

    //处理当前任务
    @Test
    public void completed(){
        String aggin="zhangsan";
        Task task = taskService.createTaskQuery()
                .taskAssignee(aggin).singleResult();
        taskService.complete(task.getId());
    }

    //查询个人的代办事务
    @Test
    public void findTaskList(){
        String aggin="jack";
        List<Task> list = taskService.createTaskQuery()
                .taskAssignee(aggin).list();
        for (Task task:list
             ) {
            System.out.println(task.getProcessInstanceId());
            System.out.println(task.getId());
            System.out.println(task.getAssignee());
            System.out.println(task.getName());
        }
    }

    //启动一个流程的事例
    @Test
    public void startProcess(){
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("qinjia");
        System.out.println(processInstance.getProcessDefinitionId());
        System.out.println(processInstance.getId());
        System.out.println(processInstance.getActivityId());
    }

    //单个文件部署
    @Test
    public void depspspsps(){

        //流程部署
        Deployment deploy = repositoryService.createDeployment().
                addClasspathResource("process/qinjia.bpmn20.xml")
                .addClasspathResource("process/qinjia.png")
                .name("请假申请流程")
                .deploy();
        System.out.println(deploy.getId());
        System.out.println(deploy.getName());
    }

}
