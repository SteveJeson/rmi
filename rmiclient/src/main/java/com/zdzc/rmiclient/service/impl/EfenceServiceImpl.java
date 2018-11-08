package com.zdzc.rmiclient.service.impl;

import com.zdzc.api.entity.Efence;
import com.zdzc.api.service.EfenceService;
import com.zdzc.rmiclient.dao.EfenceMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * Created by Administrator on 2018/8/31 0031.
 */
@Service
public class EfenceServiceImpl implements EfenceService {

    @Autowired
    private EfenceMapper efenceMapper;

    @Override
    public Efence selectEfenceByCode(String code) throws Exception {
        return efenceMapper.selectByCode(code);
    }

    @Override
    public void insertEfence(Efence efence) {
        efence.setRunStatus(1);
        efence.setCode(UUID.randomUUID().toString());
        efenceMapper.insert(efence);
    }
}
