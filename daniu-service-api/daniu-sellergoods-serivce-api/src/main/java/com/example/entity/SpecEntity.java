package com.example.entity;

import com.example.pojo.Specification;
import com.example.pojo.SpecificationOption;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "规格复合实体类",value = "SpecEntity")
public class SpecEntity implements Serializable {
    @ApiModelProperty(value = "规格对象",required = false)
    private Specification specification;
    @ApiModelProperty(value = "规格选项对象",required = false)
    private List<SpecificationOption> specificationOptionList;
}
