package com.example.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.dao.SpecificationMapper;
import com.example.dao.SpecificationOptionMapper;
import com.example.entity.PageResult;
import com.example.entity.SpecEntity;
import com.example.pojo.Specification;
import com.example.pojo.SpecificationOption;
import com.example.service.SpecificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;

/****
 * @Author:jeff
 * @Description:Specification业务层接口实现类
 * @Date 2021/2/1 14:19
 *****/
@Service
public class SpecificationServiceImpl extends ServiceImpl<SpecificationMapper, Specification> implements SpecificationService {

    @Autowired(required = false)
    private SpecificationMapper specificationMapper;
    @Autowired(required = false)
    private SpecificationOptionMapper specificationOptionMapper;

    /**
     * Specification条件+分页查询
     * @param specification 查询条件
     * @param page 页码
     * @param size 页大小
     * @return 分页结果
     */
    @Override
    public PageResult<Specification> findPage(Specification specification, int page, int size){
         Page<Specification> mypage = new Page<>(page, size);
        QueryWrapper<Specification> queryWrapper = this.createQueryWrapper(specification);
        IPage<Specification> iPage = this.page(mypage, queryWrapper);
        return new PageResult<Specification>(iPage.getTotal(),iPage.getRecords());
    }

    /**
     * Specification分页查询
     * @param page
     * @param size
     * @return
     */
    @Override
    public PageResult<Specification> findPage(int page, int size){
        Page<Specification> mypage = new Page<>(page, size);
        IPage<Specification> iPage = this.page(mypage, new QueryWrapper<Specification>());

        return new PageResult<Specification>(iPage.getTotal(),iPage.getRecords());
    }

    /**
     * Specification条件查询
     * @param specification
     * @return
     */
    @Override
    public List<Specification> findList(Specification specification){
        //构建查询条件
        QueryWrapper<Specification> queryWrapper = this.createQueryWrapper(specification);
        //根据构建的条件查询数据
        return this.list(queryWrapper);
    }


    /**
     * Specification构建查询对象
     * @param specification
     * @return
     */
    public QueryWrapper<Specification> createQueryWrapper(Specification specification){
        QueryWrapper<Specification> queryWrapper = new QueryWrapper<>();
        if(specification!=null){
            // 主键
            if(!StringUtils.isEmpty(specification.getId())){
                 queryWrapper.eq("id",specification.getId());
            }
            // 名称
            if(!StringUtils.isEmpty(specification.getSpecName())){
                 queryWrapper.eq("spec_name",specification.getSpecName());
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
        QueryWrapper<SpecificationOption> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("spec_id",id);
        specificationOptionMapper.delete(queryWrapper);
    }

    /**
     * 修改Specification
     * @param specEntity
     */
    @Override
    public void update(SpecEntity specEntity){
        //1.修改规格名
        this.updateById(specEntity.getSpecification());
        //查到id
        QueryWrapper<SpecificationOption> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("spec_id",specEntity.getSpecification().getId());
        //删除id的关联项
        specificationOptionMapper.delete(queryWrapper);
        //重新添加规格选项
        if (!CollectionUtils.isEmpty(specEntity.getSpecificationOptionList())){
            for (SpecificationOption specificationOption:specEntity.getSpecificationOptionList()){
                specificationOption.setSpecId(specEntity.getSpecification().getId());
                specificationOptionMapper.insert(specificationOption);
            }
        }

    }

    /**
     * 增加Specification
     * @param specEntity
     */
    @Override
    public void add(SpecEntity specEntity){
        //保存规格名称
        this.save(specEntity.getSpecification());

        //得到规格名称的id
        if (specEntity.getSpecificationOptionList()!=null&&specEntity.getSpecificationOptionList().size()>0){
            for (SpecificationOption s:specEntity.getSpecificationOptionList()){
                //3.向规格选项中设置规格ID
                s.setSpecId(specEntity.getSpecification().getId());
                //4.保存规格选项
                specificationOptionMapper.insert(s);
            }
        }
    }

    /**
     * 根据ID查询Specification
     * @param id
     * @return
     */
    @Override
    public SpecEntity findById(Long id) {
        Specification specification = specificationMapper.selectById(id);
        QueryWrapper<SpecificationOption> queryWrapper = new QueryWrapper();
        queryWrapper.eq("spec_id",id);
        List<SpecificationOption> specificationOptionList =  specificationOptionMapper.selectList(queryWrapper);
        SpecEntity specEntity = new SpecEntity();
        specEntity.setSpecification(specification);
        specEntity.setSpecificationOptionList(specificationOptionList);
        return specEntity;
    }

    /**
     * 查询Specification全部数据
     * @return
     */
    @Override
    public List<Specification> findAll() {
        return this.list(new QueryWrapper<Specification>());
    }

    @Override
    public List<Map> selectOptions() {
        return specificationMapper.selectOptions();
    }
}
