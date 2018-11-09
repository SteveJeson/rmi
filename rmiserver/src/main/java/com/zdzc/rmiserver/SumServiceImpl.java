package com.zdzc.rmiserver;

import com.zdzc.api.service.SumService;

public class SumServiceImpl implements SumService {
    @Override
    public int getSum(int a, int b) {
        int sum = a + b;
        System.out.println("处理结果 ==> "+sum);
        return sum;
    }
}
