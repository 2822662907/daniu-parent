package com.example.order.service.impl;




import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.feign.ItemFeign;
import com.example.order.dao.OrderItemMapper;
import com.example.order.dao.OrderMapper;
import com.example.order.dao.PayLogMapper;
import com.example.order.group.Cart;
import com.example.order.pojo.Order;
import com.example.order.pojo.OrderItem;
import com.example.order.pojo.PayLog;
import com.example.order.service.OrderService;
import com.example.entity.PageResult;
import com.example.util.IdWorker;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/****
 * @Author:jeff
 * @Description:Order业务层接口实现类
 * @Date 2021/2/1 14:19
 *****/
@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper,Order> implements OrderService {
        @Autowired
        private RedisTemplate redisTemplate;
        @Autowired(required = false)
        private OrderItemMapper orderItemMapper;

        @Autowired
        private IdWorker idWorker;
        @Autowired(required = false)
        private ItemFeign itemFeign;
        @Autowired(required = false)
        private PayLogMapper payLogMapper;

    /**
     * Order条件+分页查询
     * @param order 查询条件
     * @param page 页码
     * @param size 页大小
     * @return 分页结果
     */
    @Override
    public PageResult<Order> findPage(Order order, int page, int size){
         Page<Order> mypage = new Page<>(page, size);
        QueryWrapper<Order> queryWrapper = this.createQueryWrapper(order);
        IPage<Order> iPage = this.page(mypage, queryWrapper);
        return new PageResult<Order>(iPage.getTotal(),iPage.getRecords());
    }

    /**
     * Order分页查询
     * @param page
     * @param size
     * @return
     */
    @Override
    public PageResult<Order> findPage(int page, int size){
        Page<Order> mypage = new Page<>(page, size);
        IPage<Order> iPage = this.page(mypage, new QueryWrapper<Order>());

        return new PageResult<Order>(iPage.getTotal(),iPage.getRecords());
    }

    /**
     * Order条件查询
     * @param order
     * @return
     */
    @Override
    public List<Order> findList(Order order){
        //构建查询条件
        QueryWrapper<Order> queryWrapper = this.createQueryWrapper(order);
        //根据构建的条件查询数据
        return this.list(queryWrapper);
    }


    /**
     * Order构建查询对象
     * @param order
     * @return
     */
    public QueryWrapper<Order> createQueryWrapper(Order order){
        QueryWrapper<Order> queryWrapper = new QueryWrapper<>();
        if(order!=null){
            // 订单id
            if(!StringUtils.isEmpty(order.getOrderId())){
                 queryWrapper.eq("order_id",order.getOrderId());
            }
            // 实付金额。精确到2位小数;单位:元。如:200.07，表示:200元7分
            if(!StringUtils.isEmpty(order.getPayment())){
                 queryWrapper.eq("payment",order.getPayment());
            }
            // 支付类型，1、在线支付，2、货到付款
            if(!StringUtils.isEmpty(order.getPaymentType())){
                 queryWrapper.eq("payment_type",order.getPaymentType());
            }
            // 邮费。精确到2位小数;单位:元。如:200.07，表示:200元7分
            if(!StringUtils.isEmpty(order.getPostFee())){
                 queryWrapper.eq("post_fee",order.getPostFee());
            }
            // 状态：1、未付款，2、已付款，3、未发货，4、已发货，5、交易成功，6、交易关闭,7、待评价
            if(!StringUtils.isEmpty(order.getStatus())){
                 queryWrapper.eq("status",order.getStatus());
            }
            // 订单创建时间
            if(!StringUtils.isEmpty(order.getCreateTime())){
                 queryWrapper.eq("create_time",order.getCreateTime());
            }
            // 订单更新时间
            if(!StringUtils.isEmpty(order.getUpdateTime())){
                 queryWrapper.eq("update_time",order.getUpdateTime());
            }
            // 付款时间
            if(!StringUtils.isEmpty(order.getPaymentTime())){
                 queryWrapper.eq("payment_time",order.getPaymentTime());
            }
            // 发货时间
            if(!StringUtils.isEmpty(order.getConsignTime())){
                 queryWrapper.eq("consign_time",order.getConsignTime());
            }
            // 交易完成时间
            if(!StringUtils.isEmpty(order.getEndTime())){
                 queryWrapper.eq("end_time",order.getEndTime());
            }
            // 交易关闭时间
            if(!StringUtils.isEmpty(order.getCloseTime())){
                 queryWrapper.eq("close_time",order.getCloseTime());
            }
            // 物流名称
            if(!StringUtils.isEmpty(order.getShippingName())){
                 queryWrapper.eq("shipping_name",order.getShippingName());
            }
            // 物流单号
            if(!StringUtils.isEmpty(order.getShippingCode())){
                 queryWrapper.eq("shipping_code",order.getShippingCode());
            }
            // 用户id
            if(!StringUtils.isEmpty(order.getUserId())){
                 queryWrapper.eq("user_id",order.getUserId());
            }
            // 买家留言
            if(!StringUtils.isEmpty(order.getBuyerMessage())){
                 queryWrapper.eq("buyer_message",order.getBuyerMessage());
            }
            // 买家昵称
            if(!StringUtils.isEmpty(order.getBuyerNick())){
                 queryWrapper.eq("buyer_nick",order.getBuyerNick());
            }
            // 买家是否已经评价
            if(!StringUtils.isEmpty(order.getBuyerRate())){
                 queryWrapper.eq("buyer_rate",order.getBuyerRate());
            }
            // 收货人地区名称(省，市，县)街道
            if(!StringUtils.isEmpty(order.getReceiverAreaName())){
                 queryWrapper.eq("receiver_area_name",order.getReceiverAreaName());
            }
            // 收货人手机
            if(!StringUtils.isEmpty(order.getReceiverMobile())){
                 queryWrapper.eq("receiver_mobile",order.getReceiverMobile());
            }
            // 收货人邮编
            if(!StringUtils.isEmpty(order.getReceiverZipCode())){
                 queryWrapper.eq("receiver_zip_code",order.getReceiverZipCode());
            }
            // 收货人
            if(!StringUtils.isEmpty(order.getReceiver())){
                 queryWrapper.eq("receiver",order.getReceiver());
            }
            // 过期时间，定期清理
            if(!StringUtils.isEmpty(order.getExpire())){
                 queryWrapper.eq("expire",order.getExpire());
            }
            // 发票类型(普通发票，电子发票，增值税发票)
            if(!StringUtils.isEmpty(order.getInvoiceType())){
                 queryWrapper.eq("invoice_type",order.getInvoiceType());
            }
            // 订单来源：1:app端，2：pc端，3：M端，4：微信端，5：手机qq端
            if(!StringUtils.isEmpty(order.getSourceType())){
                 queryWrapper.eq("source_type",order.getSourceType());
            }
            // 商家ID
            if(!StringUtils.isEmpty(order.getSellerId())){
                 queryWrapper.eq("seller_id",order.getSellerId());
            }
        }
        return queryWrapper;
    }

    /**
     * 删除
     * @param id
     */
    @Override
    public void delete(Long id){
        this.removeById(id);
    }

    /**
     * 修改Order
     * @param order
     */
    @Override
    public void update(Order order){
        this.updateById(order);
    }

    /**
     * 增加Order
     * @param order
     */
    @Override
    public void add(Order order){
        // 得到购物车数据
        List<Cart> cartList = (List<Cart>) redisTemplate.boundHashOps("cartList").get(order.getOrderId());

        List<String> orderIdList = new ArrayList<>();
        double total_money = 0;
        // 遍历购物车，根据信息创建订单对象
        for (Cart cart : cartList) {
            long orderId = idWorker.nextId();
            System.out.println("sellerId:" + cart.getSellerId());
            // 新创建订单对象
            Order tborder = new Order();
            //复制前端传递过来的订单的属性值，到新创建订单对象
            BeanUtils.copyProperties(order, tborder);
            tborder.setOrderId(orderId);
            tborder.setStatus("1");// 未付款
            tborder.setCreateTime(new Date());
            tborder.setUpdateTime(new Date());
            tborder.setSellerId(cart.getSellerId());

            double money = 0;
            for (OrderItem orderItem : cart.getOrderItemList()) {
                orderItem.setId(idWorker.nextId());
                orderItem.setOrderId(orderId);
                orderItem.setSellerId(cart.getSellerId());
                money += orderItem.getTotalFee().doubleValue();// 金额累加
                // 添加订单明细数据到tb_order_item
                orderItemMapper.insert(orderItem);
            }
            orderIdList.add(orderId+"");
            total_money+=money;
            // 设置订单总金额
            tborder.setPayment(new BigDecimal(money));
            // 添加订单信息到数据库
            save(tborder);
        }
        if ("1".equals(order.getPaymentType())) {//线上支付
            PayLog payLog = new PayLog();
            String outTradeNo = idWorker.nextId()+"";//支付订单号
            payLog.setOutTradeNo(outTradeNo);
            payLog.setCreateTime(new Date());//创建时间

            //把元转换成分
            BigDecimal total_money1 = BigDecimal.valueOf(total_money);
            BigDecimal cs = BigDecimal.valueOf(100l);
            BigDecimal multiply = total_money1.multiply(cs);
            payLog.setTotalFee(multiply.toBigInteger().longValue());// 总金额
            payLog.setUserId(order.getUserId());
            payLog.setTradeState("0");//支付状态  0 未支付 1已经支付
            //订单号列表，逗号分隔
            String ids= orderIdList.toString()//[ aa,cc ]
                    .replace("[","")
                    .replace(" ","")
                    .replace("]","");
            payLog.setOrderList(ids);//订单号列表，逗号分隔
            payLog.setPayType(order.getPaymentType());// 支付类型
            payLogMapper.insert(payLog);
            redisTemplate.boundHashOps("paylog").put(order.getOrderId(),payLog);

        }
        //******************减少库存********************************
        //减少库存  调用goods 微服务的 feign 减少库存
        itemFeign.decrCount(order.getUserId());

        //下单成功，清空redis购物车数据
        redisTemplate.boundHashOps("cartList").delete(order.getUserId());

    }

    /**
     * 根据ID查询Order
     * @param id
     * @return
     */
    @Override
    public Order findById(Long id){
        return  this.getById(id);
    }

    /**
     * 查询Order全部数据
     * @return
     */
    @Override
    public List<Order> findAll() {
        return this.list(new QueryWrapper<Order>());
    }


}
