package com.example.order.pojo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.lang.Long;
import java.lang.String;
import java.lang.Integer;
import java.math.BigDecimal;

/****
 * @Author:jeff
 * @Description:OrderItem构建
 * @Date 2021/2/1 14:19
 *****/
@ApiModel(description = "OrderItem",value = "OrderItem")
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName(value="tb_order_item")
public class OrderItem implements Serializable{

	@ApiModelProperty(value = "",required = false)
    @TableId(type = IdType.INPUT)
    @TableField(value = "id")
	private Long id;//

	@ApiModelProperty(value = "商品id",required = false)
    @TableField(value = "item_id")
	private Long itemId;//商品id

	@ApiModelProperty(value = "SPU_ID",required = false)
    @TableField(value = "goods_id")
	private Long goodsId;//SPU_ID

	@ApiModelProperty(value = "订单id",required = false)
    @TableField(value = "order_id")
	private Long orderId;//订单id

	@ApiModelProperty(value = "商品标题",required = false)
    @TableField(value = "title")
	private String title;//商品标题

	@ApiModelProperty(value = "商品单价",required = false)
    @TableField(value = "price")
	private BigDecimal price;//商品单价

	@ApiModelProperty(value = "商品购买数量",required = false)
    @TableField(value = "num")
	private Integer num;//商品购买数量

	@ApiModelProperty(value = "商品总金额",required = false)
    @TableField(value = "total_fee")
	private BigDecimal totalFee;//商品总金额

	@ApiModelProperty(value = "商品图片地址",required = false)
    @TableField(value = "pic_path")
	private String picPath;//商品图片地址

	@ApiModelProperty(value = "商家id",required = false)
    @TableField(value = "seller_id")
	private String sellerId;//商家id



}
