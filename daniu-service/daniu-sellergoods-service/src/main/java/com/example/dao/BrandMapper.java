package com.example.dao;
import com.example.pojo.Brand;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/****
 * @Author:jeff
 * @Description:Brand的Dao
 * @Date 2021/2/1 14:19
 *****/
public interface BrandMapper extends BaseMapper<Brand> {
   @Select("SELECT `id`,`name` AS `text` FROM tb_brand")
    List<Map> selectOptions();
}
