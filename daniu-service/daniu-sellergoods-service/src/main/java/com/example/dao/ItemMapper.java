package com.example.dao;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.example.pojo.Item;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Update;

/****
 * @Author:jeff
 * @Description:Item的Dao
 * @Date 2021/2/1 14:19
 *****/
public interface ItemMapper extends BaseMapper<Item> {
    /**
     * 递减库存
     * @param orderItem
     * @return
     */
    @Update("UPDATE tb_item SET num=num-#{num} WHERE id=#{itemId} AND num>=#{num}")
    int decrCount(OrderItem orderItem);
}
