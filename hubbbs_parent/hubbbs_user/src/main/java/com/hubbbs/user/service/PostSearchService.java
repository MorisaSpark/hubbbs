package com.hubbbs.user.service;

import com.hubbbs.user.dao.PostSearchDao;
import com.hubbbs.user.pojo.PostSearch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by IntelliJ IDEA.
 * User: HB
 * Date: 2019/4/3
 *
 * @author HB
 * To change this template use File | Settings | File Templates.
 */
@Service
@Transactional
public class PostSearchService {

    @Autowired
    private PostSearchDao postSearchDao;

    public void add(PostSearch postSearch) {
        postSearchDao.save(postSearch);
    }

    public Page<PostSearch> findByKeyboards(String keyboards, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        return postSearchDao.findByTitleOrBodyLike(keyboards, keyboards, pageable);
    }

    public void deleteAll() {
        postSearchDao.deleteAll();
    }
    public void deleteById(String postId){
        postSearchDao.deleteById(postId);
    }

    public PostSearch findById(String postSearchId) {
        return postSearchDao.findById(postSearchId).get();
    }
}
