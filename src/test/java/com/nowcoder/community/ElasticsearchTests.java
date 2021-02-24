package com.nowcoder.community;

import com.nowcoder.community.dao.DiscussPostMapper;
import com.nowcoder.community.dao.elasticsearch.DiscussPostRepository;
import com.nowcoder.community.entity.DiscussPost;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.*;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.test.context.ContextConfiguration;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@ContextConfiguration(classes = CommunityApplication.class)
public class ElasticsearchTests {

    @Autowired
    private DiscussPostMapper discussMapper;

    @Autowired
    private DiscussPostRepository discussRepository;

    @Autowired
    private ElasticsearchRestTemplate elasticsearchRestTemplate;

    @Test
    public void testInsert(){
        discussRepository.save(discussMapper.selectDiscussPostById(241));
        discussRepository.save(discussMapper.selectDiscussPostById(242));
        discussRepository.save(discussMapper.selectDiscussPostById(243));
    }

    @Test
    public void testInsertList(){
        discussRepository.saveAll(discussMapper.selectDiscussPosts(101,0,100, 0));
        discussRepository.saveAll(discussMapper.selectDiscussPosts(102,0,100, 0));
        discussRepository.saveAll(discussMapper.selectDiscussPosts(103,0,100, 0));
        discussRepository.saveAll(discussMapper.selectDiscussPosts(111,0,100, 0));
        discussRepository.saveAll(discussMapper.selectDiscussPosts(112,0,100, 0));
        discussRepository.saveAll(discussMapper.selectDiscussPosts(131,0,100, 0));
        discussRepository.saveAll(discussMapper.selectDiscussPosts(132,0,100, 0));
        discussRepository.saveAll(discussMapper.selectDiscussPosts(133,0,100, 0));
        discussRepository.saveAll(discussMapper.selectDiscussPosts(134,0,100, 0));
    }

    @Test
    public void testUpdate(){
        DiscussPost post = discussMapper.selectDiscussPostById(231);
        post.setContent("我是新人，使劲灌水。");
        discussRepository.save(post);
    }

    @Test
    public void testDelete(){
        discussRepository.deleteById(231);
        // discussRepository.deleteAll();
    }

    @Test
    public void testSearchByRepository(){
        // 构造搜索条件
        NativeSearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(QueryBuilders.multiMatchQuery("互联网寒冬", "title", "content"))
                .withSort(SortBuilders.fieldSort("type").order(SortOrder.DESC))
                .withSort(SortBuilders.fieldSort("score").order(SortOrder.DESC))
                .withSort(SortBuilders.fieldSort("createTime").order(SortOrder.DESC))
                .withPageable(PageRequest.of(0,10))
                .withHighlightFields(
                        new HighlightBuilder.Field("title").preTags("<em>").postTags("</em>"),
                        new HighlightBuilder.Field("content").preTags("<em>").postTags("</em>")
                ).build();

        // search方法的底层调用elasticTemplate.queryForPage(), 获取到了高亮显示的值，但没有处理
        Page<DiscussPost> page = discussRepository.search(searchQuery); // 集合：当前这一页的实体类
        System.out.println(page.getTotalElements());
        System.out.println(page.getTotalPages());
        System.out.println(page.getNumber());
        System.out.println(page.getSize());
        for(DiscussPost post : page){
            System.out.println(post);
        }
    }

    @Test
    public void testSearchByTemplate(){
        NativeSearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(QueryBuilders.multiMatchQuery("互联网寒冬", "title", "content"))
                .withSort(SortBuilders.fieldSort("type").order(SortOrder.DESC))
                .withSort(SortBuilders.fieldSort("score").order(SortOrder.DESC))
                .withSort(SortBuilders.fieldSort("createTime").order(SortOrder.DESC))
                .withPageable(PageRequest.of(0, 10))
                .withHighlightFields(
                        new HighlightBuilder.Field("title").preTags("<em>").postTags("</em>"),
                        new HighlightBuilder.Field("content").preTags("<em>").postTags("</em>")
                ).build();
        //elasticsearchTemplate.queryForPage()
        //elasticsearchRestTemplate.queryForPage(searchQuery, DiscussPost.class, new SearchRe)

        // SearchHits搜索命中数据
        SearchHits<DiscussPost> hits = elasticsearchRestTemplate.search(searchQuery, DiscussPost.class);
        // Wrap the returned SearchPage<T> object in a SearchPage<T>
        SearchPage<DiscussPost> searchPage = SearchHitSupport.searchPageFor(hits, searchQuery.getPageable());
        System.out.println(searchPage.getTotalElements());
        System.out.println(searchPage.getTotalPages());
        System.out.println(searchPage.getNumber());
        System.out.println(searchPage.getSize());
        for (SearchHit<DiscussPost> post : searchPage) {
            System.out.println(post);
        }









        // List<DiscussPost> list = new ArrayList<>();
        // for(SearchHit<DiscussPost> hit : hits){ // hit是JSON格式的数据
        //     DiscussPost post = new DiscussPost();
        //     hit.
        // }















        // Wrap the returned SearchPage<T> object in a SearchPage<T>
        // SearchPage<DiscussPost> searchPage = SearchHitSupport.searchPageFor(search, searchQuery.getPageable());
        //
        // System.out.println(searchPage.getTotalElements());
        // System.out.println(searchPage.getTotalPages());
        // System.out.println(searchPage.getNumber());
        // System.out.println(searchPage.getSize());
        // for(SearchHit<DiscussPost> post : searchPage){
        //     System.out.println(post);
        // }

        // // 得到查询结果返回的内容
        // List<SearchHit<DiscussPost>> searchHits = search.getSearchHits();
        // // 设置一个需要返回的实体类集合
        // List<DiscussPost> discussPosts = new ArrayList<>();
        // // 遍历返回的内容进行处理
        // for(SearchHit<DiscussPost> searchHit : searchHits){
        //     // 高亮的内容
        //     Map<String, List<String>> highLightFields = searchHit.getHighlightFields();
        //     // 将高亮的内容填充到content中
        //     searchHit.getContent().setTitle(highLightFields.get("title") == null ? searchHit.getContent().getTitle() : highLightFields.get("title").get(0));
        //     searchHit.getContent().setTitle(highLightFields.get("content") == null ? searchHit.getContent().getContent() : highLightFields.get("content").get(0));
        //     // 放到实体类中
        //     discussPosts.add(searchHit.getContent());
        // }
        // System.out.println(discussPosts.size());
        // for(DiscussPost discussPost : discussPosts){
        //     System.out.println(discussPost);
        // }
    }

}
