import com.atguigu.ServiceAuthApplication;
import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author jellyfish
 * @create 2023 -- 08 -- 04 -- 12:50
 * @描述
 * @email:1137648153@qq.com
 */

@SpringBootTest(classes = ServiceAuthApplication.class)
public class activity1 {
    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private HistoryService historyService;

    @Test
    public void deployProcess03() {
        // 流程部署
        Deployment deploy = repositoryService.createDeployment()
                .addClasspathResource("process/jiaban02.bpmn20.xml")
                .name("加班申请流程")
                .deploy();
        System.out.println(deploy.getId());
        System.out.println(deploy.getName());
    }

    /**
     * 启动流程实例
     */
    @Test
    public void startUpProcess03() {
        //创建流程实例,我们需要知道流程定义的key
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("jiaban02");
        //输出实例的相关信息
        System.out.println("流程定义id：" + processInstance.getProcessDefinitionId());
        System.out.println("流程实例id：" + processInstance.getId());
    }


    @Test
    public void deployProcess02() {
        // 流程部署
        Deployment deploy = repositoryService.createDeployment()
                .addClasspathResource("process/jiaban01.bpmn20.xml")
                .name("加班申请流程")
                .deploy();
        System.out.println(deploy.getId());
        System.out.println(deploy.getName());
    }

    /**
     * 启动流程实例
     */
    @Test
    public void startUpProcess02() {
        //创建流程实例,我们需要知道流程定义的key
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("jiaban01");
        //输出实例的相关信息
        System.out.println("流程定义id：" + processInstance.getProcessDefinitionId());
        System.out.println("流程实例id：" + processInstance.getId());
    }


    /**
     *
     */

    //部署流程定义
    @Test
    public void deployProcess(){
        //流程部署
        Deployment deploy = repositoryService.createDeployment().
                addClasspathResource("process/jiaban.bpmn20.xml")
                .name("请假申请流程")
                .deploy();
        System.out.println(deploy.getId());
        System.out.println(deploy.getName());
    }

    //启动流程实例
    @Test
    public void startProcess(){
        Map<String,Object> map=new HashMap<>();
        map.put("assignee1","lucy");
        map.put("assignee2","mary");
        ProcessInstance processInstance =
                runtimeService.startProcessInstanceByKey("jiaban",map);
        System.out.println(processInstance.getProcessDefinitionId());
        System.out.println(processInstance.getId());
        System.out.println(processInstance.getActivityId());
    }
    // 查询个人的代办任务--Tim
    @Test
    public void findTaskList02(){
        String assign = "lucy";
        List<Task> list = taskService.createTaskQuery()
                .taskAssignee(assign).list();
        for (Task task : list) {
            System.out.println("task.getProcessInstanceId() = " + task.getProcessInstanceId()); // 06eca124-bb42-11ed-9bbc-005056c00001
            System.out.println("任务id：" + task.getId()); // 06f071b8-bb42-11ed-9bbc-005056c00001
            System.out.println("任务负责人：" + task.getAssignee()); // Tim
            System.out.println("任务名称：" + task.getName()); // 经理审批
        }
    }

}
