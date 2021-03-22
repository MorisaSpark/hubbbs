package com.hubbbs.user.dao;

import com.hubbbs.user.pojo.PostSearch;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Created by IntelliJ IDEA.
 * User: HB
 * Date: 2019/4/3
 *
 * @author HB
 * To change this template use File | Settings | File Templates.
 */
public interface PostSearchDao extends ElasticsearchRepository<PostSearch, String> {
    public Page<PostSearch> findByTitleOrBodyLike(String title, String body, Pageable pageable);

    public void deleteById(String id);

    public void deleteAll();

}
