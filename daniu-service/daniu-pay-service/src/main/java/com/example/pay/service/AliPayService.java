package com.example.pay.service;

import java.util.Map;

/**
 * @author lidi
 * @create 2023-03-16 11:18
 */
public interface AliPayService {
    /**
     * 生成支付宝支付二维码
     * @param out_trade_no 订单号
     * @param total_fee 金额(分)
     * @return
     */
     Map createNative(String out_trade_no,String total_fee);

    /**
     * 查询支付状态
     * @param out_trade_no
     * @return
     */
     Map qureypayStatus(String out_trade_no);
}
