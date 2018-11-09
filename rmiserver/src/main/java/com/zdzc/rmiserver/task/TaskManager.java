package com.zdzc.rmiserver.task;

import com.zdzc.api.service.SnapshotService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class TaskManager {
    private static final Logger logger = LoggerFactory.getLogger(TaskManager.class);
    //最小线程数
    public static final int MIN_THREAD_COUNT = 1;

    private static int MAX_DEVICES_PER_THREAD;

    public static ConcurrentHashMap<String, List<EfenceTask>>
            taskMap = new ConcurrentHashMap<>();

    /**
     * 启动任务
     * @param jobCode
     */
//    public static void start(ThreadPoolTaskExecutor executor, String jobCode, int max, EfenceJobMapper jobMapper, SnapshotService snapshotService, Producer producer) {
//        MAX_DEVICES_PER_THREAD = max;
//        Map<String, Object> jobMap = jobMapper.selectEfenceInfoByJobCode(jobCode);
//        //任务名称
//        String jobName = jobMap.get("jobName").toString();
//        //电子围栏坐标
//        String coordinates = jobMap.get("coordinates").toString();
//        //电子围栏类型
//        int efenceType = Integer.parseInt(jobMap.get("efenceType").toString());
//        //报警类型
//        int alarmType = Integer.parseInt(jobMap.get("alarmType").toString());
//        //运行时间间隔
//        int period = Integer.parseInt(jobMap.get("period").toString());
//        //设备号
//        String deviceCodes = jobMap.get("deviceCodes").toString();
//        String[] deviceCodesArr = deviceCodes.split(",");
//        List<String> deviceCodeList = Arrays.asList(deviceCodesArr);
//        //要启动的工作线程数量
//        System.out.println("任务名称：["+jobName+"],任务参数："+"coordinates:"+coordinates+",efenceType:"+efenceType+",alarmType:"+alarmType+",period:"+period);
//        List[] deviceListPerThread = dispatchDeviceCodes(deviceCodeList);
//        System.out.println("实际要启动的工作线程数："+ deviceListPerThread.length);
//        List<EfenceTask> taskList = new ArrayList<>();
//        for (int i = 0;i < deviceListPerThread.length;i++) {
//            //test
//            int deviceSize = deviceListPerThread[i].size();
//            List<String> deviceList = deviceListPerThread[i];
//            logger.info("线程" + (i+1) + " 分配到的设备数为："+ deviceSize + "，设备号分别为：" + deviceList);
//            EfenceTask task = new EfenceTask(coordinates,efenceType,alarmType,period,deviceListPerThread[i],snapshotService,producer);
//            executor.execute(task);
//            taskList.add(task);
//        }
//        //任务和线程关系保存到内存中
//        taskMap.put(jobCode, taskList);
//    }

    /**
     * 停止任务
     * @param jobCode
     */
    public static void stop(String jobCode){
        List<EfenceTask> taskList = taskMap.get(jobCode);
        for(int i = 0;i < taskList.size();i++){
            EfenceTask task = taskList.get(i);
            task.stopWork();
        }
        taskMap.remove(jobCode);
    }

    /**
     * 分发设备
     * @param deviceCodeList
     * @return
     */
    private static List[] dispatchDeviceCodes (List<String> deviceCodeList) {
        //要启动的最少线程数
        int threadCount = deviceCodeList.size()/MAX_DEVICES_PER_THREAD;
        //平均分配后剩余的设备数
        int remainDeviceCount = deviceCodeList.size()%MAX_DEVICES_PER_THREAD;
        //实际要启动的线程数
        int actualThreadCount = threadCount;
        if(threadCount > 0 && remainDeviceCount > 0){
            actualThreadCount = threadCount + 1;
        }else if(threadCount == 0) {
            actualThreadCount = MIN_THREAD_COUNT;
        }

        List<String>[] devicesPerThread = new List[actualThreadCount];
        int deviceIndex = 0;
        int remainIndces = MAX_DEVICES_PER_THREAD;
        for (int i = 0;i < devicesPerThread.length;i++) {
            devicesPerThread[i] = new ArrayList<>();
            if(threadCount > 0) {
                if(remainIndces < MAX_DEVICES_PER_THREAD){
                    for (int j = deviceIndex;j < remainIndces+deviceIndex;j++) {
                        devicesPerThread[i].add(deviceCodeList.get(j));
                    }
                }else {
                    for (int j = deviceIndex;j < MAX_DEVICES_PER_THREAD+deviceIndex;j++) {
                        devicesPerThread[i].add(deviceCodeList.get(j));
                    }
                }
                deviceIndex += MAX_DEVICES_PER_THREAD;
                remainIndces = deviceCodeList.size() - deviceIndex;
            }else {
                for (int j = 0;j < deviceCodeList.size();j++) {
                    devicesPerThread[i].add(deviceCodeList.get(j));
                }
            }
        }

        return devicesPerThread;
    }

}
