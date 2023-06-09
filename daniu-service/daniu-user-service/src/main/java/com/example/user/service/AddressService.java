package com.example.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.entity.PageResult;
import com.example.user.pojo.Address;

import java.util.List;

/****
 * @Author:jeff
 * @Description:Address业务层接口
 * @Date 2021/2/1 14:19
 *****/

public interface AddressService extends IService<Address> {

    /***
     * Address多条件分页查询
     * @param address
     * @param page
     * @param size
     * @return
     */
    PageResult<Address> findPage(Address address, int page, int size);

    /***
     * Address分页查询
     * @param page
     * @param size
     * @return
     */
    PageResult<Address> findPage(int page, int size);

    /***
     * Address多条件搜索方法
     * @param address
     * @return
     */
    List<Address> findList(Address address);

    /***
     * 删除Address
     * @param id
     */
    void delete(Long id);

    /***
     * 修改Address数据
     * @param address
     */
    void update(Address address);

    /***
     * 新增Address
     * @param address
     */
    void add(Address address);

    /**
     * 根据ID查询Address
     * @param id
     * @return
     */
     Address findById(Long id);

    /***
     * 查询所有Address
     * @return
     */
    List<Address> findAll();
    /**
     * 根据用户查询地址
     * @param userId
     * @return
     */
    public List<Address> findListByUserId(String userId );
}
