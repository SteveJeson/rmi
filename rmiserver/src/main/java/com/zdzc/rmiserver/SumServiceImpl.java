package com.zdzc.rmiserver;

import com.zdzc.api.service.SumService;

public class SumServiceImpl implements SumService {
    @Override
    public int getSum(int a, int b) {
        return a + b;
    }
}
