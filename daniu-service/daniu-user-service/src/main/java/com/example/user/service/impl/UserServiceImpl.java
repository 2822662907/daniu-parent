package com.example.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.entity.PageResult;
import com.example.user.dao.UserMapper;
import com.example.user.pojo.User;
import com.example.user.service.UserService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.concurrent.TimeUnit;

/****
 * @Author:jeff
 * @Description:User业务层接口实现类
 * @Date 2021/2/1 14:19
 *****/
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper,User> implements UserService {
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private RabbitTemplate rabbitTemplate;
    /**
     * User条件+分页查询
     * @param user 查询条件
     * @param page 页码
     * @param size 页大小
     * @return 分页结果
     */
    @Override
    public PageResult<User> findPage(User user, int page, int size){
         Page<User> mypage = new Page<>(page, size);
        QueryWrapper<User> queryWrapper = this.createQueryWrapper(user);
        IPage<User> iPage = this.page(mypage, queryWrapper);
        return new PageResult<User>(iPage.getTotal(),iPage.getRecords());
    }

    /**
     * User分页查询
     * @param page
     * @param size
     * @return
     */
    @Override
    public PageResult<User> findPage(int page, int size){
        Page<User> mypage = new Page<>(page, size);
        IPage<User> iPage = this.page(mypage, new QueryWrapper<User>());

        return new PageResult<User>(iPage.getTotal(),iPage.getRecords());
    }

    /**
     * User条件查询
     * @param user
     * @return
     */
    @Override
    public List<User> findList(User user){
        //构建查询条件
        QueryWrapper<User> queryWrapper = this.createQueryWrapper(user);
        //根据构建的条件查询数据
        return this.list(queryWrapper);
    }


    /**
     * User构建查询对象
     * @param user
     * @return
     */
    public QueryWrapper<User> createQueryWrapper(User user){
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        if(user!=null){
            // 
            if(!StringUtils.isEmpty(user.getId())){
                 queryWrapper.eq("id",user.getId());
            }
            // 用户名
            if(!StringUtils.isEmpty(user.getUsername())){
                queryWrapper.like("username",user.getUsername());
            }
            // 密码，加密存储
            if(!StringUtils.isEmpty(user.getPassword())){
                 queryWrapper.eq("password",user.getPassword());
            }
            // 注册手机号
            if(!StringUtils.isEmpty(user.getPhone())){
                 queryWrapper.eq("phone",user.getPhone());
            }
            // 注册邮箱
            if(!StringUtils.isEmpty(user.getEmail())){
                 queryWrapper.eq("email",user.getEmail());
            }
            // 创建时间
            if(!StringUtils.isEmpty(user.getCreated())){
                 queryWrapper.eq("created",user.getCreated());
            }
            // 
            if(!StringUtils.isEmpty(user.getUpdated())){
                 queryWrapper.eq("updated",user.getUpdated());
            }
            // 会员来源：1:PC，2：H5，3：Android，4：IOS，5：WeChat
            if(!StringUtils.isEmpty(user.getSourceType())){
                 queryWrapper.eq("source_type",user.getSourceType());
            }
            // 昵称
            if(!StringUtils.isEmpty(user.getNickName())){
                 queryWrapper.eq("nick_name",user.getNickName());
            }
            // 真实姓名
            if(!StringUtils.isEmpty(user.getName())){
                queryWrapper.like("name",user.getName());
            }
            // 使用状态（Y正常 N非正常）
            if(!StringUtils.isEmpty(user.getStatus())){
                 queryWrapper.eq("status",user.getStatus());
            }
            // 头像地址
            if(!StringUtils.isEmpty(user.getHeadPic())){
                 queryWrapper.eq("head_pic",user.getHeadPic());
            }
            // QQ号码
            if(!StringUtils.isEmpty(user.getQq())){
                 queryWrapper.eq("qq",user.getQq());
            }
            // 账户余额
            if(!StringUtils.isEmpty(user.getAccountBalance())){
                 queryWrapper.eq("account_balance",user.getAccountBalance());
            }
            // 手机是否验证 （0否  1是）
            if(!StringUtils.isEmpty(user.getIsMobileCheck())){
                 queryWrapper.eq("is_mobile_check",user.getIsMobileCheck());
            }
            // 邮箱是否检测（0否  1是）
            if(!StringUtils.isEmpty(user.getIsEmailCheck())){
                 queryWrapper.eq("is_email_check",user.getIsEmailCheck());
            }
            // 性别，1男，2女
            if(!StringUtils.isEmpty(user.getSex())){
                 queryWrapper.eq("sex",user.getSex());
            }
            // 会员等级
            if(!StringUtils.isEmpty(user.getUserLevel())){
                 queryWrapper.eq("user_level",user.getUserLevel());
            }
            // 积分
            if(!StringUtils.isEmpty(user.getPoints())){
                 queryWrapper.eq("points",user.getPoints());
            }
            // 经验值
            if(!StringUtils.isEmpty(user.getExperienceValue())){
                 queryWrapper.eq("experience_value",user.getExperienceValue());
            }
            // 生日
            if(!StringUtils.isEmpty(user.getBirthday())){
                 queryWrapper.eq("birthday",user.getBirthday());
            }
            // 最后登录时间
            if(!StringUtils.isEmpty(user.getLastLoginTime())){
                 queryWrapper.eq("last_login_time",user.getLastLoginTime());
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
     * 修改User
     * @param user
     */
    @Override
    public void update(User user){
        this.updateById(user);
    }

    /**
     * 增加User
     * @param user
     */
    @Override
    public void add(User user){
        user.setCreated(new Date());//创建时间
        user.setUpdated(new Date());//更新时间
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String password = passwordEncoder.encode(user.getPassword());
        user.setPassword(password);
        this.save(user);
    }

    /**
     * 根据ID查询User
     * @param id
     * @return
     */
    @Override
    public User findById(Long id){
        return  this.getById(id);
    }

    /**
     * 查询User全部数据
     * @return
     */
    @Override
    public List<User> findAll() {
        return this.list(new QueryWrapper<User>());
    }


    @Override
    public void createSmsCode(String phone) {
        // 随机生成6位验证码
        String code = (int) (Math.random() * 100000) + "";
        System.out.println("验证码:"+code);

        // 添加验证码到Redis
        redisTemplate.opsForValue().set(phone,code);

        // 设置redis验证码有效期
        redisTemplate.expire(phone,5, TimeUnit.MINUTES);

        // 发送短信
        Map map = new HashMap();
        map.put("mobile",phone);
        map.put("code",code);
        rabbitTemplate.convertAndSend("daniu.sms.queue",map);

    }

    @Override
    public boolean checkSmsCode(String phone, String code) {

        //得到缓存中存储的验证码
        String sysCode = (String) redisTemplate.opsForValue().get(phone);
        if(sysCode==null){
            return false;
        }
        if(!sysCode.equals(code)){
            return false;
        }
        return true;

    }

    @Override
    public User findByUsername(String username) {

        User user = new User();
        user.setUsername(username);
        List<User> userList = this.findList(user);
        if(userList!=null&&userList.size()>0){
            return userList.get(0);
        }else {
            return null;
        }
    }
}
