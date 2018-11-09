package com.zdzc.rmiserver.task;

import com.alibaba.fastjson.JSONArray;
import com.zdzc.api.entity.GpsSnapshot;
import com.zdzc.api.service.SnapshotService;
import com.zdzc.rmiserver.rabbitMQ.Producer;
import com.zdzc.rmiserver.util.GPSConvertion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/8/30 0030.
 */
public class EfenceTask implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(EfenceTask.class);

    private boolean stop = false;
    private Map<String, Boolean> deviceStatus;//存放设备是否在围栏内的状态
    private List<String> deviceCodeList;
    private String coordinates;
    private int efenceType;
    private int alarmType;
    private int period;
    private SnapshotService snapshotService;
    private Producer producer;
    private static final String COMMA = ",";

    public EfenceTask(){}

    public EfenceTask(String coordinates, int efenceType, int alarmType, int period, List<String> deviceCodeList, SnapshotService snapshotService, Producer producer){
        this.coordinates = coordinates;
        this.efenceType = efenceType;
        this.alarmType = alarmType;
        this.period = period;
        this.deviceCodeList = deviceCodeList;
        this.snapshotService = snapshotService;
        this.deviceStatus = new HashMap<>(deviceCodeList.size());
        this.producer = producer;
    }

    @Override
    public void run() {
        try {
            startWork();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void startWork() throws Exception{
        Map<String, Object> param = new HashMap<>();
        param.put("dbName", "gps_main");
        param.put("deviceList", deviceCodeList);
        Map<String, String> latestDeviceLocations = new HashMap<>();
        if (deviceCodeList.size() > 16){
            latestDeviceLocations = new HashMap<>(deviceCodeList.size());
        }
        while (!stop){
            //2.获取设备列表中所有设备的最新经纬度信息
            List<GpsSnapshot> snapshotList = snapshotService.selectByDeviceCodeList(param);
            for (GpsSnapshot snapshot : snapshotList){
                //经纬度由GPS原始坐标系转换成百度坐标系
                double[] coordinate = GPSConvertion.gps84_To_bd09(snapshot.getLon()/ 1000000, snapshot.getLat() / 1000000);
                String lonlat = coordinate[0] + COMMA + coordinate[1];
                latestDeviceLocations.put(snapshot.getDeviceCode(), lonlat);
            }
            //3.遍历设备号列表，处理逻辑
            for (String deviceCode : deviceCodeList){
                //4.根据设备号得到设备最新经纬度信息
                String lonlat = latestDeviceLocations.get(deviceCode);
                if(StringUtils.isEmpty(lonlat)){
                    continue;
                }
                double lon = Double.valueOf(lonlat.split(COMMA)[0]);
                double lat = Double.valueOf(lonlat.split(COMMA)[1]);
                long time = System.currentTimeMillis() / 1000;
                //5.根据设备最新坐标和电子围栏坐标和类型，判断设备此刻在围栏内部还是在围栏外部
                boolean inEfence = false;
                if (efenceType == 1){ //多边形电子围栏
                    inEfence = pointInPolygon(lon, lat, coordinates);
                } else if (efenceType == 2){ //圆形电子围栏
                    inEfence = pointInCircle(lon, lat, coordinates);
                }
                if (inEfence){
                    logger.info("当前在围栏内，设备号：" + deviceCode);
                } else {
                    logger.info("当前在围栏外，设备号：" + deviceCode);
                }
                //6.根据设备号查询 deviceStatus 中是否有该设备的状态信息
                if (deviceStatus.containsKey(deviceCode)){
                    //6.1.1 有，获取已保存的状态信息
                    boolean status = deviceStatus.get(deviceCode);
                    //6.1.2 拿 status 和 5 中的结果相比较是否相同，status = 0 表示在围栏内部，status = 1 表示在围栏外部
                    if (status != inEfence) {
                        //6.1.3 如果比较结果不同
                        //6.1.3.1 把 5 中的结果更新到 deviceStatus 中
                        deviceStatus.put(deviceCode, inEfence);
                        //6.1.3.2 声明报警信息字段，结合 5 中的结果，deviceStatus 的结果以及 type 的值，进行组装
                        StringBuilder alarmInfo = new StringBuilder();
                        if (alarmType == 1 && inEfence && !status){
                            //6.1.3.2.1 当 type == 1 且最近一次在圈内且其上一次在圈外，表示设备进栏，组装进栏报警信息
                            alarmInfo.append(deviceCode).append(COMMA).append(1).append(COMMA).append(time);
                        } else if (alarmType == 2 && !inEfence && status){
                            //6.1.3.2.2 当 type == 2 且最近一次在圈外且其上一次在圈内，表示设备出栏，组装出栏报警信息
                            alarmInfo.append(deviceCode).append(COMMA).append(2).append(COMMA).append(time);
                        } else if (alarmType == 3){
                            //6.1.3.2.3 当 type == 3 时，再结合 5 的结果和 deviceStatus 的结果，组装进栏或出栏报警信息
                            if (inEfence && !status){
                                alarmInfo.append(deviceCode).append(COMMA).append(1).append(COMMA).append(time).append(COMMA).append(lon).append(COMMA).append(lat);
                            } else if (!inEfence && status){
                                alarmInfo.append(deviceCode).append(COMMA).append(2).append(COMMA).append(time).append(COMMA).append(lon).append(COMMA).append(lat);
                            }
                        } else {
                            stop = true;
                            break;
                        }
                        //6.1.3.2.4 通过 producer 把报警信息发送到 mq 上
                        producer.sendMessage(alarmInfo.toString());
                        //6.1.4 如果比较结果相同，不做其他处理
                    }
                }else {
                    //6.2.1 deviceStatus 中没有该设备的信息，直接把 deviceCode 作为 key，5 的结果作为 value，存入 deviceStatus 中
                    deviceStatus.put(deviceCode, inEfence);
                }
            }
            //7. 整个逻辑每个 period 秒后执行一次
            Thread.sleep(period * 1000);
        }
    }

    public void stopWork(){
        stop = true;
    }

    /**
     * 判断坐标点是否在圆形电子围栏内
     * @param lon
     * @param lat
     * @param coordinates
     * @return
     */
    private boolean pointInCircle(double lon, double lat, String coordinates){
        double centerLon = Double.valueOf(JSONArray.parseArray(JSONArray.parseArray(coordinates).get(0).toString()).get(0).toString());
        double centerLat = Double.valueOf(JSONArray.parseArray(JSONArray.parseArray(coordinates).get(0).toString()).get(1).toString());
        double edgeLon = Double.valueOf(JSONArray.parseArray(JSONArray.parseArray(coordinates).get(1).toString()).get(0).toString());
        double edgeLat = Double.valueOf(JSONArray.parseArray(JSONArray.parseArray(coordinates).get(1).toString()).get(1).toString());
        return (Math.pow(lon - centerLon, 2) + Math.pow(lat - centerLat, 2)) <=
                (Math.pow(edgeLon - centerLon, 2) + Math.pow(edgeLat - centerLat, 2));
    }

    /**
     * 判断目标点是否在多边形电子围栏内
     * @param lon
     * @param lat
     * @param coordinates
     * @return
     */
    private boolean pointInPolygon(double lon, double lat, String coordinates) {
        JSONArray jsonArray = JSONArray.parseArray(coordinates);
        int polyCorners = jsonArray.size();
        Double[] polyLon = new Double[polyCorners];
        Double[] polyLat = new Double[polyCorners];
        int i = 0;
        int j = polyCorners - 1;
        boolean oddNodes = false;
        for (int m = 0; m < jsonArray.size(); m++){
            String pointStr = JSONArray.toJSONString(jsonArray.get(m));
            JSONArray pointArray = JSONArray.parseArray(pointStr);
            polyLon[m] = Double.valueOf(pointArray.get(0).toString());
            polyLat[m] = Double.valueOf(pointArray.get(1).toString());
        }
        for (i = 0; i < polyCorners; i++){
            if ((polyLat[i] < lat && polyLat[j] >= lat || polyLat[j] < lat && polyLat[i] >= lat) &&  (polyLon[i]<=lon || polyLon[j]<=lon)){
                if (polyLon[i]+(lat-polyLat[i])/(polyLat[j]-polyLat[i])*(polyLon[j]-polyLon[i])<=lon){
                    oddNodes = !oddNodes;
                }
            }
            j = i;
        }
        return oddNodes;
    }

//    /**
//     * gps 坐标转换
//     * @param lon
//     * @param lat
//     * @return
//     */
//    private double[] gps_to_gcj02(double lon, double lat){
//
//    }
}
