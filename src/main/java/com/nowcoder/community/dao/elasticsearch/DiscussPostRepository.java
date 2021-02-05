package com.nowcoder.community.dao.elasticsearch;

import com.nowcoder.community.entity.DiscussPost;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository // Spring提供的针对数据访问层的注解
public interface DiscussPostRepository extends ElasticsearchRepository<DiscussPost, Integer> {
    // 父接口定义好了对es服务器增删改查的各种方法
}
