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

import java.util.List;

/**
 * @author jellyfish
 * @create 2023 -- 08 -- 04 -- 12:50
 * @描述
 * @email:1137648153@qq.com
 */

@SpringBootTest(classes = ServiceAuthApplication.class)
public class activityGateway {
    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private HistoryService historyService;

    /**
     * 1.部署流程定义
     */
    @Test
    public void deployProcess04() {
        // 流程部署
        Deployment deploy = repositoryService.createDeployment()
                .addClasspathResource("process/qjia003.bpmn20.xml")
                .name("请假申请流程003")
                .deploy();
        System.out.println(deploy.getId());
        System.out.println(deploy.getName());

//        Map<String, Object> map = new HashMap<>();
        //设置请假天数
//        map.put("day", "2");
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("qjia003");
        System.out.println(processInstance.getId());
    }

    /**
     * 2.
     */
    // 查询个人的代办任务--Tim
    @Test
    public void findTaskList02(){
//        String assign = "wang5";
        String assign = "xiaoli";
        List<Task> list = taskService.createTaskQuery()
                .taskAssignee(assign).list();
        for (Task task : list) {
            System.out.println("task.getProcessInstanceId() = " + task.getProcessInstanceId()); // 06eca124-bb42-11ed-9bbc-005056c00001
            System.out.println("任务id：" + task.getId()); // 06f071b8-bb42-11ed-9bbc-005056c00001
            System.out.println("任务负责人：" + task.getAssignee()); // Tim
            System.out.println("任务名称：" + task.getName()); // 经理审批
        }
    }



    /**
     * 3.办理个人任务
     */
    @Test
    public void completGroupTask() {
        Task task = taskService.createTaskQuery()
                .taskAssignee("gouwa")  //要查询的负责人
                .singleResult();//返回一条
        taskService.complete(task.getId());
    }

    //归还组任务
    @Test
    public void assigneeToGroupTask() {
        String taskId = "d96c3f28-825e-11ed-95b4-7c57581a7819";
        // 任务负责人
        String userId = "zhangsan01";
        // 校验userId是否是taskId的负责人，如果是负责人才可以归还组任务
        Task task = taskService
                .createTaskQuery()
                .taskId(taskId)
                .taskAssignee(userId)
                .singleResult();
        if (task != null) {
            // 如果设置为null，归还组任务,该 任务没有负责人
            taskService.setAssignee(taskId, null);
        }
    }

}
