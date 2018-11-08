package com.zdzc.api.service;


import com.zdzc.api.entity.GpsSnapshot;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/8/31 0031.
 */
public interface SnapshotService {
    List<GpsSnapshot> selectAll(Map<String, Object> param);

    List<GpsSnapshot> selectByDeviceCodeList(Map<String, Object> param);
}
