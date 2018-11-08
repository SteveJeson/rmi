package com.zdzc.rmiclient.dao;

import com.zdzc.api.entity.GpsSnapshot;

import java.util.List;
import java.util.Map;

public interface GpsSnapshotMapper {
    int deleteByPrimaryKey(String deviceCode);

    int insert(GpsSnapshot record);

    GpsSnapshot selectByPrimaryKey(String deviceCode);

    List<GpsSnapshot> selectAll(Map<String, Object> param);

    int updateByPrimaryKey(GpsSnapshot record);

    List<GpsSnapshot> selectByDeviceCodeList(Map<String, Object> param);
}