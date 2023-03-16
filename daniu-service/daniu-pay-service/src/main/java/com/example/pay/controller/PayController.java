package com.example.pay.controller;

import com.example.entity.Result;
import com.example.entity.StatusCode;
import com.example.pay.service.AliPayService;
import com.example.util.IdWorker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @author lidi
 * @create 2023-03-16 11:56
 */
@RestController
@RequestMapping("/pay")
public class PayController {
    @Autowired(required = false)
    private AliPayService aliPayService;
    @Autowired
    private IdWorker idWorker;

    /**
     * 生成二维码
     *
     * @return
     */
    @GetMapping("/createNative")
    public Map createNative() {
        return aliPayService.createNative(idWorker.nextId() + "", "50000");
    }
    /**
     * 查询支付状态
     * @param out_trade_no
     * @return
     */
    @RequestMapping("/queryPayStatus")
    public Result queryPayStatus(String out_trade_no){
        Result result = null;
        int x=0;

        while (true){
            // 调用查询接口
            Map<String,String> map = null;
            try {
                map = aliPayService.qureypayStatus(out_trade_no);
            } catch (Exception e1) {
                System.out.println("调用查询服务出错");
            }
            if (map==null){
                result = new Result<>(false, StatusCode.ERROR, "支付出错");
                break;
            }
            if(map.get("tradestatus")!=null&&map.get("tradestatus").equals("TRADE_SUCCESS")){//如果成功
                result=new  Result(true,StatusCode.OK,"支付成功");
                break;
            }
            if(map.get("tradestatus")!=null&&map.get("tradestatus").equals("TRADE_CLOSED")){//如果成功
                result=new  Result(true, StatusCode.OK,"未付款交易超时关闭，或支付完成后全额退款");
                break;
            }
            if(map.get("tradestatus")!=null&&map.get("tradestatus").equals("TRADE_FINISHED")){//如果成功
                result=new  Result(true,StatusCode.OK, "交易结束，不可退款");
                break;
            }
            try {
                Thread.sleep(3000);//间隔三秒
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //为了不让循环无休止地运行，我们定义一个循环变量，如果这个变量超过了这个值则退出循环，设置时间为5分钟
            x++;
            if(x>=100){
                result=new  Result(false, StatusCode.ERROR, "二维码超时");
                break;
            }
        }
        return result;
    }
}
