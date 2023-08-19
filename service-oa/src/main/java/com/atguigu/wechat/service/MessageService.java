package com.atguigu.wechat.service;

/**
 * @author jellyfish
 * @create 2023 -- 08 -- 10 -- 12:56
 * @描述
 * @email:1137648153@qq.com
 */
public interface MessageService {
    /**
     * 推送待审批人员
     * @param processId
     * @param userId
     * @param taskId
     */
    void pushPendingMessage(Long processId, Long userId, String taskId);

    /**
     * 审批后推送提交审批人员
     * @param processId
     * @param userId
     * @param status
     */
    void pushProcessedMessage(Long processId, Long userId, Integer status);

}
