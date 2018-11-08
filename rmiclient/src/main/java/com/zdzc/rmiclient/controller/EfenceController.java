package com.zdzc.rmiclient.controller;


import com.zdzc.api.entity.Efence;
import com.zdzc.api.entity.GpsSnapshot;
import com.zdzc.api.service.EfenceService;
import com.zdzc.api.service.SnapshotService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/8/30 0030.
 */
@RestController
@RequestMapping("/efence")
public class EfenceController {

    private static final Logger logger = LoggerFactory.getLogger(EfenceController.class);

    @Autowired
    private EfenceService efenceService;

    @Autowired
    private ThreadPoolTaskExecutor executor;

    @Autowired
    private SnapshotService snapshotService;

    @RequestMapping("/test")
    public Map<String, Object> test() throws Exception{
        Map<String, Object> param = new HashMap<>();
        param.put("dbName", "gps_main");
        List<String> list = new ArrayList<>();
        list.add("191750000001");
        param.put("deviceList", list);
        List<GpsSnapshot> snapshotList = snapshotService.selectByDeviceCodeList(param);
        return param;
    }

    @RequestMapping("/create")
    public Map<String, Object> createEfence(@RequestBody Efence efence) {
        Map<String, Object> resultMap = new HashMap<>();
        try{
            efenceService.insertEfence(efence);
            resultMap.put("statusCode", 200);
            resultMap.put("success", true);
            resultMap.put("message", "创建电子围栏成功！");
        }catch (Exception e) {
            resultMap.put("statusCode", 500);
            resultMap.put("success", false);
            resultMap.put("message", "创建电子围栏失败！");
        }
        return resultMap;
    }

}
