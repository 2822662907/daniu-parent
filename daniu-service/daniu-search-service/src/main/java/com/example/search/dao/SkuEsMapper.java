package com.example.search.dao;

import com.example.search.pojo.SkuInfo;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.Map;
@Repository
public interface SkuEsMapper extends ElasticsearchRepository<SkuInfo,Map> {
}
