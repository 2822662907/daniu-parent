package com.example.pay.service.impl;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.request.AlipayTradePrecreateRequest;
import com.alipay.api.request.AlipayTradeQueryRequest;
import com.alipay.api.response.AlipayTradePrecreateResponse;
import com.alipay.api.response.AlipayTradeQueryResponse;
import com.example.pay.service.AliPayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * @author lidi
 * @create 2023-03-16 11:20
 */
@Service
public class AlipayServiceImpl implements AliPayService {
    @Autowired
    private AlipayClient alipayClient;

    /**
     * 生成支付宝支付二维码
     *
     * @param out_trade_no 订单号
     * @param total_fee    金额(分)
     * @return
     */
    @Override
    public Map createNative(String out_trade_no, String total_fee) {
        Map<String,String> map = new HashMap<>();
        //创建预下单请求对象
        AlipayTradePrecreateRequest request = new AlipayTradePrecreateRequest();
        //转换下单金额按照元
        long total = Long.parseLong(total_fee);
        BigDecimal bigDecimal = BigDecimal.valueOf(total);
        BigDecimal cs = BigDecimal.valueOf(100d);
        BigDecimal bigYuan = bigDecimal.divide(cs);
        System.out.println("预下单金额:" + bigYuan.doubleValue());
        request.setBizContent("{" +
                        "    \"out_trade_no\":\"" + out_trade_no + "\"," +
                        "    \"total_amount\":\"" + bigYuan.doubleValue() + "\"," +
                        "    \"subject\":\"测试购买商品001\"," +
                        "    \"store_id\":\"xa_001\"," +
                        "    \"timeout_express\":\"90m\"}"
        );
        //发出预下单业务请求
        try {
            AlipayTradePrecreateResponse response = alipayClient.execute(request);
            //从相应对象读取相应结果
            String code = response.getCode();
            System.out.println("响应码:" + code);
            //全部的响应结果
            String body = response.getBody();
            System.out.println("返回结果:" + body);
            if (code.equals("10000")) {
                map.put("qrcode", response.getQrCode());
                map.put("out_trade_no", response.getOutTradeNo());
                map.put("total_fee", total_fee);
                System.out.println("qrcode:" + response.getQrCode());
                System.out.println("out_trade_no:" + response.getOutTradeNo());
                System.out.println("total_fee:" + total_fee);
            } else {
                System.out.println("预下单接口调用失败:" + body);
            }
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
            return map;
    }
    /**
     * 交易查询接口alipay.trade.query：
     * 获取指定订单编号的，交易状态
     * @throws AlipayApiException
     */
    @Override
    public Map qureypayStatus(String out_trade_no) {
        Map<String,String>  map = new HashMap();
        AlipayTradeQueryRequest request = new AlipayTradeQueryRequest();
        request.setBizContent("{" +
                "    \"out_trade_no\":\""+out_trade_no+"\"," +
                "    \"trade_no\":\"\"}");//设置业务参数
        try {
            AlipayTradeQueryResponse response = alipayClient.execute(request);
            String code = response.getCode();
            System.out.println("返回值1:"+response.getBody());
            if ("10000".equals(code)){
                    map.put("out_trade_no",out_trade_no);
                    map.put("tradestatus",response.getTradeStatus());
                    map.put("tradeNo",response.getTradeNo());
            }
        } catch (AlipayApiException e) {
            throw new RuntimeException(e);
        }

        return map;
    }
}