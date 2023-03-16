package com.example.search.service.impl;
import com.alibaba.fastjson.JSON;
import com.example.search.dao.SkuEsMapper;
import com.example.search.service.SkuService;
import com.example.search.feign.SkuFeign;
import com.example.search.pojo.SkuInfo;
import com.example.entity.Result;
import com.example.pojo.Item;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.*;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;

@Service
public class SkuServiceImpl implements SkuService {
    @Autowired
    private SkuFeign skuFeign;
    @Autowired
    private SkuEsMapper skuEsMapper;
    @Autowired
    private ElasticsearchRestTemplate elasticsearchRestTemplate;
    @Override
    public void importSku() {
        Result<List<Item>> result = skuFeign.findByStatus("1");// 查询出状态
        String jsonString = JSON.toJSONString(result.getData());
        List<SkuInfo> skuInfoList = JSON.parseArray(jsonString, SkuInfo.class);
        for (SkuInfo skuInfo : skuInfoList) {
            Map map = JSON.parseObject(skuInfo.getSpec(), Map.class);
            skuInfo.setSpecMap(map);
        }
     skuEsMapper.saveAll(skuInfoList);
    }

    @Override
    public Map search(Map<String, String> searchMap) {
       if (searchMap==null){
       searchMap = new HashMap<>();
       }
       // 1.获取关键字的值
        String keywords = searchMap.get("keywords");
       if (StringUtils.isEmpty(keywords)){
           keywords = "华为";
       }
       // 2.创建构建器
        NativeSearchQueryBuilder nativeSearchQueryBuilder = new NativeSearchQueryBuilder();
            // 设置分组的条件
                // 设置类别分组
        nativeSearchQueryBuilder.addAggregation(AggregationBuilders.terms("skuCategorygroup").field("category.keyword"));
                // 设置商品品牌
        nativeSearchQueryBuilder.addAggregation(AggregationBuilders.terms("skuBrandgrouyp").field("brand.keyword"));
                // 设置商品的规格
        nativeSearchQueryBuilder.addAggregation(AggregationBuilders.terms("skuSpecgroup").field("spec.keyword").size(100));

        //3.设置查询条件
        nativeSearchQueryBuilder.withQuery(QueryBuilders.matchQuery("title",keywords));
        //========================过滤查询 开始=====================================
        //创建多条件组合查询对象
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        //设置品牌查询条件
        if (!StringUtils.isEmpty(searchMap.get("brand"))){
            boolQueryBuilder.filter(QueryBuilders.matchQuery("brand",searchMap.get("brand")));
        }
        //设置分类查询条件
        if (!StringUtils.isEmpty(searchMap.get("category"))){
            boolQueryBuilder.filter(QueryBuilders.matchQuery("category",searchMap.get("category")));
        }
        //关联过滤查询对象到查询器
        nativeSearchQueryBuilder.withFilter(boolQueryBuilder);

            //设置规格过滤
        for (String key : searchMap.keySet()) {//{ brand:"",category:"",spec_网络:"电信4G"}
            if (key.startsWith("spec_")){
                 boolQueryBuilder.filter(QueryBuilders.termQuery("specMap." + key.substring(5) + ".keyword",searchMap.get(key)));
            }
        }
        //价格过滤查询
        String price = searchMap.get("price");
        if (!StringUtils.isEmpty(price)){
            String[] split = price.split("-");
            if (!split[1].equalsIgnoreCase("*")){
                boolQueryBuilder.filter(QueryBuilders.rangeQuery("price").from(split[0],true).to(split[1],true));
            }else {
                boolQueryBuilder.filter(QueryBuilders.rangeQuery(("price")).gte(split[0]));
            }
        }
        Integer pageNum = 1;
//搜索分页
        try {
            pageNum = Integer.parseInt(searchMap.get("pageNum"));
        }catch (NumberFormatException ignored){
        }
        /*Integer pageNum = Integer.parseInt(searchMap.get("pageNum"));//当前页面
        if (pageNum == null){
            pageNum=1;
        }*/
        Integer pageSize = 10; //每页显示记录数
        nativeSearchQueryBuilder.withPageable(PageRequest.of(pageNum-1,pageSize));
        String sortRult = searchMap.get("sortRult");
        String sortField = searchMap.get("sortField");
        if (!StringUtils.isEmpty(sortRult)&&!StringUtils.isEmpty(sortField)){
             nativeSearchQueryBuilder.withSort(SortBuilders.fieldSort(sortField).order(sortRult.equals("DESC")? SortOrder.DESC:SortOrder.ASC));
        }
        // 4、构建查询对像
        NativeSearchQuery searchQuery = nativeSearchQueryBuilder.build();
        //5.执行查询
        SearchHits<SkuInfo> searchHits = elasticsearchRestTemplate.search(searchQuery, SkuInfo.class);
        // 6、查询结果并分页
        SearchPage<SkuInfo> searchPageFor = SearchHitSupport.searchPageFor(searchHits, searchQuery.getPageable());

        // 获取分组信息
                //
        Terms skuCategorygroup = searchHits.getAggregations().get("skuCategorygroup");
        Terms skuBrandgrouyp = searchHits.getAggregations().get("skuBrandgrouyp");
        Terms skuSpecgrouyp = searchHits.getAggregations().get("skuSpecgroup");
        // 获取分类名称集合
        List<String> categoryList = getStringsNameList(skuCategorygroup);
        List<String> BrandList = getStringsNameList(skuBrandgrouyp);
        Map<String,Set<String>> specMap = getStringSetMap(skuSpecgrouyp);
        // 7、遍历结果
        List<SkuInfo> list = new ArrayList<>();
        for (SearchHit<SkuInfo> searchHit : searchHits) {
            SkuInfo content = searchHit.getContent();
            SkuInfo skuInfo = new SkuInfo();
            BeanUtils.copyProperties(content,skuInfo);
            list.add(skuInfo);
        }
        // 8 返回结果
        Map map = new HashMap();
        map.put("rows",list);
        map.put("size",searchPageFor.getSize());
        map.put("TotalPages",searchPageFor.getTotalPages());

        map.put("categoryList",categoryList);
        map.put("BrandList",BrandList);
        map.put("specMap", specMap);
        return map;
    }
    /**
     * 获取规格列表数据
     * @param terms
     * @return
     **/
    private Map<String,Set<String>> getStringSetMap(Terms terms) {
        Map<String,Set<String>> specMap = new HashMap<>();
        Set<String> specList = new HashSet<>();
        if (terms!=null){
            for (Terms.Bucket bucket : terms.getBuckets()) {
                specList.add(bucket.getKeyAsString());
            }
        }
        for (String s : specList) {
            Map<String,String> map = JSON.parseObject(s, Map.class);
            for (Map.Entry<String, String> entry : map.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                Set<String> specValues = specMap.get(key);
                if (specValues == null){
                    specValues = new HashSet<String>();
                }
                //将当前规格加入到集合中
                specValues.add(value);
                specMap.put(key,specValues);
            }
        }
        return specMap;
    }

    /**
     * 获取列表数据
     * @param terms
     * @return
     **/
    private List<String> getStringsNameList(Terms terms) {
        List<String> list = new ArrayList<>();
        if (terms!=null){
            for (Terms.Bucket bucket : terms.getBuckets()) {
                String  keyAsString = bucket.getKeyAsString();
                list.add(keyAsString);
            }
        }
        return list;
    }
}



