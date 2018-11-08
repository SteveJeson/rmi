package com.zdzc.rmiclient.controller;

import com.zdzc.api.entity.EfenceJob;
import com.zdzc.api.service.EfenceJobService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/job")
public class EfenceJobController {
    private static final Logger logger = LoggerFactory.getLogger(EfenceJobController.class);

    @Autowired
    private EfenceJobService service;

    /**
     * 创建任务
     * @param efenceJob
     * @return
     */
    @RequestMapping("/create")
    public Map<String, Object> createJob(@RequestBody EfenceJob efenceJob) {
        Map<String, Object> resultMap = new HashMap<>();
        try{
            service.insertEfenceJob(efenceJob);
            resultMap.put("statusCode", 200);
            resultMap.put("success", true);
            resultMap.put("message", "新建任务成功！");
        }catch (Exception e) {
            resultMap.put("statusCode", 500);
            resultMap.put("success", false);
            resultMap.put("message", "新建任务失败！");
        }
        return resultMap;
    }

    /**
     * 启动任务
     * @param efenceJob
     * @return
     */
    @RequestMapping("/start")
    public Map<String, Object> startJob(@RequestBody EfenceJob efenceJob) {
        Map<String, Object> resultMap = new HashMap<>();
        try{
            service.startEfenceJob(efenceJob);
            resultMap.put("statusCode", 200);
            resultMap.put("success", true);
            resultMap.put("message", "启动任务成功！");
        }catch (Exception e) {
            resultMap.put("statusCode", 500);
            resultMap.put("success", false);
            resultMap.put("message", "启动任务失败！");
        }
        return resultMap;
    }

    /**
     * 停止任务
     * @param efenceJob
     * @return
     */
    @RequestMapping("/stop")
    public Map<String, Object> stopJob(@RequestBody EfenceJob efenceJob) {
        Map<String, Object> resultMap = new HashMap<>();
        try{
            service.stopEfenceJob(efenceJob);
            resultMap.put("statusCode", 200);
            resultMap.put("success", true);
            resultMap.put("message", "停止任务成功！");
        }catch (Exception e) {
            resultMap.put("statusCode", 500);
            resultMap.put("success", false);
            resultMap.put("message", "停止任务失败！");
        }
        return resultMap;
    }


}
