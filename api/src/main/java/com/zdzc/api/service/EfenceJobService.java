package com.zdzc.api.service;


import com.zdzc.api.entity.EfenceJob;

import java.util.Map;

/**
 * Created by Administrator on 2018/8/31 0031.
 */
public interface EfenceJobService {

    void insertEfenceJob(EfenceJob efenceJob);

    Map<String, Object> selectEfenceInfoByJobCode(String efenceJobCode);

    void startEfenceJob(EfenceJob efenceJob);

    void stopEfenceJob(EfenceJob efenceJob);
}
