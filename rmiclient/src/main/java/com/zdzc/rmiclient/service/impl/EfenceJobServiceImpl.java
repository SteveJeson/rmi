package com.zdzc.rmiclient.service.impl;

import com.zdzc.api.entity.EfenceJob;
import com.zdzc.api.service.EfenceJobService;
import com.zdzc.api.service.SnapshotService;
import com.zdzc.rmiclient.dao.EfenceJobMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;

/**
 * Created by Administrator on 2018/8/31 0031.
 */
@Service
public class EfenceJobServiceImpl implements EfenceJobService {

    @Autowired
    EfenceJobMapper mapper;

    @Autowired
    SnapshotService snapshotService;

    @Value("${efence.deviceNum}")
    private int deviceNum;

    @Value("${efence.maxdevices}")
    private int max;

    @Autowired
    private ThreadPoolTaskExecutor executor;

    /**
     * 新增围栏任务
     * @param efenceJob
     */
    @Override
    public void insertEfenceJob(EfenceJob efenceJob) {
        efenceJob.setJobCode(UUID.randomUUID().toString());
        StringBuilder builder = new StringBuilder();
//        String codeBaseStr = "deviceCode";
//        for (int i = 0;i < deviceNum;i++) {
//            builder.append(codeBaseStr+(i+1));
//            if(i < deviceNum-1){
//                builder.append(",");
//            }
//        }
        Long codeBase = 191750000001L;
        for (int i = 0;i < deviceNum;i++) {
            builder.append((codeBase++));
            if(i < deviceNum -1){
                builder.append(",");
            }
        }

        efenceJob.setDeviceCodes(builder.toString());
        mapper.insert(efenceJob);
    }

    /**
     * 按任务编码查询任务信息
     * @param efenceJobCode
     * @return
     */
    @Override
    public Map<String, Object> selectEfenceInfoByJobCode(String efenceJobCode) {
        return mapper.selectEfenceInfoByJobCode(efenceJobCode);
    }

    /**
     * 启动围栏任务
     * @param efenceJob
     */
    @Override
    public void startEfenceJob(EfenceJob efenceJob) {
//        TaskManager.start(executor, efenceJob.getJobCode(), max, mapper, snapshotService, producer);
//        //设置任务状态为 正在运行
//        efenceJob.setStatus(0);
//        mapper.updateByJobCode(efenceJob);
    }

    /**
     * 停止围栏任务
     * @param efenceJob
     */
    @Override
    public void stopEfenceJob(EfenceJob efenceJob) {
//        TaskManager.stop(efenceJob.getJobCode());
//        //设置任务状态为 未运行
//        efenceJob.setStatus(1);
//        mapper.updateByJobCode(efenceJob);
    }
}
