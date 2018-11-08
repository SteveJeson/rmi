package com.zdzc.api.service;


import com.zdzc.api.entity.Efence;

/**
 * Created by Administrator on 2018/8/31 0031.
 */
public interface EfenceService {
    Efence selectEfenceByCode(String code) throws Exception;

    void insertEfence(Efence efence);
}
