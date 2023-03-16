package com.example.dao;
import com.example.pojo.Specification;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/****
 * @Author:jeff
 * @Description:Specification的Dao
 * @Date 2021/2/1 14:19
 *****/
public interface SpecificationMapper extends BaseMapper<Specification> {
    @Select("SELECT `id`,`spec_name` AS `text` FROM tb_specification")
    public List<Map> selectOptions();
}
