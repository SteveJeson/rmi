package com.zdzc.rmiclient.dao;

import com.zdzc.api.entity.EfenceJob;

import java.util.List;
import java.util.Map;

public interface EfenceJobMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(EfenceJob record);

    EfenceJob selectByPrimaryKey(Integer id);

    List<EfenceJob> selectAll();

    int updateByPrimaryKey(EfenceJob record);

    Map<String, Object> selectEfenceInfoByJobCode(String code);

    void updateByJobCode(EfenceJob efenceJob);
}