package com.zdzc.rmiclient.controller;

import com.zdzc.api.service.SumService;
import com.zdzc.rmiclient.util.SpringContextUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/demo")
public class MainController {

    @RequestMapping(value="/sum", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> getSum(HttpServletRequest request){
        System.out.println("=============收到http请求=============");
        SumService service = (SumService) SpringContextUtil.getBean(SumService.class);
        int a =  Integer.parseInt(request.getParameter("a"));
        int b = Integer.parseInt(request.getParameter("b"));
        int sum = service.getSum(a, b);
        Map<String, Object> map = new HashMap<>();
        map.put("sum", sum);
        map.put("statusCode", 200);
        return map;
    }
}
