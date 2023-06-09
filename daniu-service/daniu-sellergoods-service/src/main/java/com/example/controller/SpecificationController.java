package com.example.controller;
import com.example.entity.PageResult;
import com.example.entity.Result;
import com.example.entity.SpecEntity;
import com.example.entity.StatusCode;
import com.example.pojo.Specification;
import com.example.service.SpecificationService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

/****
 * @Author:example
 * @Description:
 * @Date 2021/2/1 14:19
 *****/
@Api(tags = "SpecificationController")
@RestController
@RequestMapping("/specification")
@CrossOrigin
public class SpecificationController {

    @Autowired
    private SpecificationService specificationService;

    /***
     * Specification分页条件搜索实现
     * @param specification
     * @param page
     * @param size
     * @return
     */
    @ApiOperation(value = "Specification条件分页查询",notes = "分页条件查询Specification方法详情",tags = {"SpecificationController"})
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "path", name = "page", value = "当前页", required = true),
            @ApiImplicitParam(paramType = "path", name = "size", value = "每页显示条数", required = true)
    })
    @PostMapping(value = "/search/{page}/{size}" )
    public Result<PageResult<Specification>> findPage(@RequestBody(required = false) @ApiParam(name = "Specification对象",value = "传入JSON数据",required = false) Specification specification, @PathVariable  int page, @PathVariable  int size){
        //调用SpecificationService实现分页条件查询Specification
        PageResult<Specification> pageResult = specificationService.findPage(specification, page, size);
        return new Result(true, StatusCode.OK,"查询成功",pageResult);
    }

    /***
     * Specification分页搜索实现
     * @param page:当前页
     * @param size:每页显示多少条
     * @return
     */
    @ApiOperation(value = "Specification分页查询",notes = "分页查询Specification方法详情",tags = {"SpecificationController"})
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "path", name = "page", value = "当前页", required = true),
            @ApiImplicitParam(paramType = "path", name = "size", value = "每页显示条数", required = true)
    })
    @GetMapping(value = "/search/{page}/{size}" )
    public Result<PageResult<Specification>> findPage(@PathVariable  int page, @PathVariable  int size){
        //调用SpecificationService实现分页查询Specification
        PageResult<Specification> pageResult = specificationService.findPage(page, size);
        return new Result<PageResult<Specification>>(true,StatusCode.OK,"查询成功",pageResult);
    }

    /***
     * 多条件搜索品牌数据
     * @param specification
     * @return
     */
    @ApiOperation(value = "Specification条件查询",notes = "条件查询Specification方法详情",tags = {"SpecificationController"})
    @PostMapping(value = "/search" )
    public Result<List<Specification>> findList(@RequestBody(required = false) @ApiParam(name = "Specification对象",value = "传入JSON数据",required = false) Specification specification){
        //调用SpecificationService实现条件查询Specification
        List<Specification> list = specificationService.findList(specification);
        return new Result<List<Specification>>(true,StatusCode.OK,"查询成功",list);
    }

    /***
     * 根据ID删除品牌数据
     * @param id
     * @return
     */
    @ApiOperation(value = "Specification根据ID删除",notes = "根据ID删除Specification方法详情",tags = {"SpecificationController"})
    @ApiImplicitParam(paramType = "path", name = "id", value = "主键ID", required = true, dataType = "Long")
    @DeleteMapping(value = "/{id}" )
    public Result delete(@PathVariable Long id){
        //调用SpecificationService实现根据主键删除
        specificationService.delete(id);
        return new Result(true,StatusCode.OK,"删除成功");
    }

    /***
     * 修改Specification数据
     * @param specEntity
     * @param id
     * @return
     */
    @ApiOperation(value = "Specification根据ID修改",notes = "根据ID修改Specification方法详情",tags = {"SpecificationController"})
    @ApiImplicitParam(paramType = "path", name = "id", value = "主键ID", required = true, dataType = "Long")
    @PutMapping(value="/{id}")
    public Result update(@RequestBody @ApiParam(name = "Specification对象",value = "传入JSON数据",required = false) SpecEntity specEntity, @PathVariable Long id){
        //设置主键值
        specEntity.getSpecification().setId(id);
        //调用SpecificationService实现修改Specification
        specificationService.update(specEntity);
        return new Result(true,StatusCode.OK,"修改成功");
    }

    /***
     * 新增Specification数据
     * @param specEntity
     * @return
     */
    @ApiOperation(value = "Specification添加",notes = "添加Specification方法详情",tags = {"SpecificationController"})
    @PostMapping
    public Result add(@RequestBody  @ApiParam(name = "SpecEntity复合实体",value = "传入JSON数据",required = true) SpecEntity specEntity){
        //调用SpecificationService实现添加Specification
        specificationService.add(specEntity);
        return new Result(true,StatusCode.OK,"添加成功");
    }

    /***
     * 根据ID查询Specification数据
     * @param id
     * @return
     */
    @ApiOperation(value = "Specification根据ID查询",notes = "根据ID查询Specification方法详情",tags = {"SpecificationController"})
    @ApiImplicitParam(paramType = "path", name = "id", value = "主键ID", required = true, dataType = "Long")
    @GetMapping("/{id}")
    public Result<SpecEntity> findById(@PathVariable Long id){
        //调用SpecificationService实现根据主键查询Specification
        SpecEntity specEntity = specificationService.findById(id);
        return new Result<SpecEntity>(true,StatusCode.OK,"查询成功",specEntity);
    }

    /***
     * 查询Specification全部数据
     * @return
     */
    @ApiOperation(value = "查询所有Specification",notes = "查询所Specification有方法详情",tags = {"SpecificationController"})
    @GetMapping
    public Result<List<Specification>> findAll(){
        //调用SpecificationService实现查询所有Specification
        List<Specification> list = specificationService.findAll();
        return new Result<List<Specification>>(true, StatusCode.OK,"查询成功",list) ;
    }
    @ApiOperation(value = "查询规格下拉列表",notes = "查询规格下拉列表",tags = {"SpecificationController"})
    @GetMapping("/selectOptions")
    public List<Map> selectOptions() {
        return specificationService.selectOptions();
    }
}
