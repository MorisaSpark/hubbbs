package com.hubbbs.user.service;

import com.google.gson.Gson;
import com.hubbbs.user.dao.*;
import com.hubbbs.user.pojo.Post;
import com.hubbbs.user.pojo.PostSearch;
import com.hubbbs.user.pojo.RelTag;
import com.hubbbs.user.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import util.IdWorker;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * 服务层
 *
 * @author Administrator
 */
@Service
@Transactional
public class PostService {

    @Autowired
    private PostDao postDao;

    @Autowired
    private TypeDao typeDao;

    @Autowired
    private UserDao userDao;
    @Autowired
    private AttenterService attenterService;
    @Autowired
    private RelCollectionDao relcollectionDao;
    @Autowired
    private CookieDao cookieDao;

    @Autowired
    private IdWorker idWorker;
    @Autowired
    private Gson gson;

    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private PostSearchDao postSearchDao;

    /**
     * 查询全部列表
     *
     * @return
     */
    public List<Post> findAll() {
        return postDao.findAll();
    }


    /**
     * 条件查询+分页
     *
     * @param whereMap
     * @param page
     * @param size
     * @return
     */
    public Page<Post> findSearch(Map whereMap, int page, int size) {
        Specification<Post> specification = createSpecification(whereMap);
        PageRequest pageRequest = PageRequest.of(page - 1, size, Sort.Direction.DESC, "createTime");
        return postDao.findAll(specification, pageRequest);
    }

    public Page<Map<String, Object>> getListSummary(int page, int size, String typeId, String orderBy) {
        PageRequest pageRequest = PageRequest.of(page - 1, size, Sort.Direction.DESC, "post." + orderBy);
        if ("0".equals(typeId)) {
            return postDao.getListSummaryAll(pageRequest);
        } else {
            return postDao.getListSummary(typeId, pageRequest);
        }
    }


    /**
     * 条件查询
     *
     * @param whereMap
     * @return
     */
    public List<Post> findSearch(Map whereMap) {
        Specification<Post> specification = createSpecification(whereMap);
        return postDao.findAll(specification);
    }

    /**
     * 根据ID查询实体
     *
     * @param id
     * @return
     */
    public Post findById(String id, String ipAddr) {
//		1.先从redis中取
        Post post = (Post) redisTemplate.opsForValue().get("db_post_" + id);
//		2.没有就从数据库中取，并存入缓存（设置过期时间为1day
        if (post == null) {
            post = postDao.findById(id).get();
            redisTemplate.opsForValue().set("db_post_" + id, post, 1, TimeUnit.DAYS);
        }
//      3.redis中存入访问的ip
//      拟采用 map <postid::ip, 1> 1为浏览过   在schedule中定时更新到mysql
        stringRedisTemplate.opsForValue().set("addrIp_" + id + "::" + ipAddr, "1");
        return post;
    }

    public Post findById(String id) {
        return postDao.findById(id).get();
    }

    /**
     * 管理员调用的增加
     *
     * @param post
     */
    public void add(Post post) {
        post.setId(idWorker.nextId() + "");
        postDao.save(post);
    }

    /**
     * @return void
     * @desc 普通用户的发帖
     * @method add
     * @params [post, userId]
     * @author hubdir
     * @date 2019/5/6 21:10
     */
    public void add(Post post, String userId) {
        String second = Instant.now().toEpochMilli() + "";
        post.setId(idWorker.nextId() + "");
        post.setClickNum(0)
                .setCollectionNum(0)
                .setCookieNum(0)
                .setCreateTime(second)
                .setIsBan(1)
                .setIsHot(0)
                .setLastReplyTime(second)
                .setReplyNum(0)
                .setUserId(userId);
        postDao.save(post);
        userDao.incPostNumByUserId(userId, 1);
        typeDao.updatePostNumById(post.getTypeId(), "1");
        PostSearch postSearch = new PostSearch()
                .setId(post.getId())
                .setBody(post.getBody())
                .setIsban(post.getIsBan())
                .setTitle(post.getTitle());
        postSearchDao.save(postSearch);
    }

    /**
     * 修改
     *
     * @param post
     */
    public void update(Post post) {
        String postRedisKey = "post::" + post.getId();
        stringRedisTemplate.delete(postRedisKey);
        postDao.save(post);
        //同步修改elastic中的数据
        PostSearch postSearch = new PostSearch()
                .setId(post.getId())
                .setBody(post.getBody())
                .setIsban(post.getIsBan())
                .setTitle(post.getTitle());
        postSearchDao.save(postSearch);
    }

    public void update(Collection posts) {
        postDao.saveAll(posts);
        for (Object object : posts) {
            Post post = (Post) object;
            String postRedisKey = "post::" + (post).getId();
            stringRedisTemplate.delete(postRedisKey);
            //同步修改elastic中的数据
            PostSearch postSearch = new PostSearch()
                    .setId(post.getId())
                    .setBody(post.getBody())
                    .setIsban(post.getIsBan())
                    .setTitle(post.getTitle());
            postSearchDao.save(postSearch);
        }
    }

    /**
     * 删除
     *
     * @param id
     */
    public void deleteById(String id) {
        String postRedisKey = "post::" + id;
        stringRedisTemplate.delete(postRedisKey);
        postDao.deleteById(id);
        //同步修改elastic中的数据
        postSearchDao.deleteById(id);
    }

    /**
     * 动态条件构建
     *
     * @param searchMap
     * @return
     */
    private Specification<Post> createSpecification(Map searchMap) {

        return new Specification<Post>() {

            @Override
            public Predicate toPredicate(Root<Post> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> predicateList = new ArrayList<Predicate>();
                // 
                if (searchMap.get("id") != null && !"".equals(searchMap.get("id"))) {
                    predicateList.add(cb.like(root.get("id").as(String.class), "%" + (String) searchMap.get("id") + "%"));
                }
                // 
                if (searchMap.get("title") != null && !"".equals(searchMap.get("title"))) {
                    predicateList.add(cb.like(root.get("title").as(String.class), "%" + (String) searchMap.get("title") + "%"));
                }
                // 
                if (searchMap.get("body") != null && !"".equals(searchMap.get("body"))) {
                    predicateList.add(cb.like(root.get("body").as(String.class), "%" + (String) searchMap.get("body") + "%"));
                }
                // 
                if (searchMap.get("createTime") != null && !"".equals(searchMap.get("createTime"))) {
                    predicateList.add(cb.like(root.get("createTime").as(String.class), "%" + (String) searchMap.get("createTime") + "%"));
                }
                // 
                if (searchMap.get("lastReplyTime") != null && !"".equals(searchMap.get("lastReplyTime"))) {
                    predicateList.add(cb.like(root.get("lastReplyTime").as(String.class), "%" + (String) searchMap.get("lastReplyTime") + "%"));
                }
                // 
                if (searchMap.get("replyNum") != null && !"".equals(searchMap.get("replyNum"))) {
                    predicateList.add(cb.like(root.get("replyNum").as(String.class), "%" + (String) searchMap.get("replyNum") + "%"));
                }
                // 
                if (searchMap.get("clickNum") != null && !"".equals(searchMap.get("clickNum"))) {
                    predicateList.add(cb.like(root.get("clickNum").as(String.class), "%" + (String) searchMap.get("clickNum") + "%"));
                }
                //
                if (searchMap.get("cookieNum") != null && !"".equals(searchMap.get("cookieNum"))) {
                    predicateList.add(cb.like(root.get("cookieNum").as(String.class), "%" + (String) searchMap.get("cookieNum") + "%"));
                }
                //
                if (searchMap.get("collectionNum") != null && !"".equals(searchMap.get("collectionNum"))) {
                    predicateList.add(cb.like(root.get("collectionNum").as(String.class), "%" + (String) searchMap.get("collectionNum") + "%"));
                }
                //
                if (searchMap.get("isHot") != null && !"".equals(searchMap.get("isHot"))) {
                    predicateList.add(cb.like(root.get("isHot").as(String.class), "%" + (String) searchMap.get("isHot") + "%"));
                }
                //
                if (searchMap.get("isBan") != null && !"".equals(searchMap.get("isBan"))) {
                    predicateList.add(cb.like(root.get("isBan").as(String.class), "%" + (String) searchMap.get("isBan") + "%"));
                }
                //
                if (searchMap.get("userId") != null && !"".equals(searchMap.get("userId"))) {
                    predicateList.add(cb.like(root.get("userId").as(String.class), "%" + (String) searchMap.get("userId") + "%"));
                }
                //
                if (searchMap.get("typeId") != null && !"".equals(searchMap.get("typeId"))) {
                    predicateList.add(cb.like(root.get("typeId").as(String.class), "%" + (String) searchMap.get("typeId") + "%"));
                }

                return cb.and(predicateList.toArray(new Predicate[predicateList.size()]));

            }
        };

    }

    public List<Post> findByIdIn(List ids) {
        return postDao.findByIdIn(ids);
    }

    public Map findByIdDetailRedis(String postId, String userId) {
        /**
         * @return
         * @desc 1.从缓存或者数据库中取出文章的相关信息；2.每个用户对每个文章的点击量+1，一天计算一次
         * @method findByIdDetailRedis
         * @params [postId, userId]
         * @author hubdir
         * @date 2019/5/6 1:33
         */
        //   	1.先从redis中取
        //		2.没有就从数据库中取，并存入缓存（设置过期时间为1day
        // 查到的结果 作者信息，是否关注，文章信息，是否收藏，是否喂食
        String clickNumRedisKey = "clickNum::" + postId + "::" + userId;
        String isClick = stringRedisTemplate.opsForValue().get(clickNumRedisKey);
        if (isClick == null) {
            stringRedisTemplate.opsForValue().set(clickNumRedisKey, StringUtils.toEpochMilli(), 1, TimeUnit.DAYS);
        }

        String postRedisKey = "post::" + postId;
        String postDetail = stringRedisTemplate.opsForValue().get(postRedisKey);
        Map postDetailMap;
        if (postDetail == null) {
            postDetailMap = postDao.findByIdDetail(postId);

            stringRedisTemplate.opsForValue().set(postRedisKey, gson.toJson(postDetailMap));
        } else {
            postDetailMap = gson.fromJson(postDetail, Map.class);
        }

        Boolean isAtter = attenterService.findIsSelf((String) postDetailMap.get("userId"), userId);
        String relCollectionRedis = stringRedisTemplate.opsForValue().get("RelCollection::" + postId + "::" + userId);
        Boolean isCollection = (relcollectionDao.findByPostIdAndUserId(postId, userId) != null
                || relCollectionRedis != null);
        Boolean isCookie = cookieDao.findByPostIdAndUserId(postId, userId) != null;
        HashMap<String, Object> resultMap = new HashMap<>(postDetailMap);
        resultMap.put("isAtter", isAtter);
        resultMap.put("isCollection", isCollection);
        resultMap.put("isCookie", isCookie);
        return resultMap;
    }


    public Integer findSumClickNumByUserId(String userId) {
        return postDao.findSumClickNumByUserId(userId);
    }

    public Page<Post> searchByFlowers(String userId, String orderBy, Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page - 1, size, Sort.Direction.DESC, orderBy);
        return postDao.findByUserId(userId, pageable);
    }

    public Page<Map<String, Object>> findByPostInTag(List<RelTag> relTagList, Integer page, Integer size) {
        ArrayList<String> list = new ArrayList<>();
        for (RelTag relTag : relTagList) {
            list.add(relTag.getPostId());
        }
        Pageable pageable = PageRequest.of(page - 1, size, Sort.Direction.DESC, "createTime");
        return postDao.findTitleAndIdByIdIn(list, pageable);
    }

    public Page<Map<String, Object>> findByTypeId(String typeId, Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page - 1, size, Sort.Direction.DESC, "createTime");
        return postDao.findTitleAndIdByTypeId(typeId, pageable);
    }

    public Page<Map<String, Object>> findPartByUserId(String userId, Integer page, Integer size, String orderBy) {
        Pageable pageable = PageRequest.of(page - 1, size, Sort.Direction.DESC, orderBy);
        return postDao.findPartByUserId(userId, pageable);
    }

    /**
     * @return java.lang.Boolean
     * @desc 批量修改点击量
     * @method updateClickNumByPostIdIn
     * @params [map]
     * @author hubdir
     * @date 2019/5/6 2:31
     */
    public Boolean updateClickNumByPostIdIn(Map<String, Integer> map) {
        for (String key : map.keySet()) {
            Integer value = map.get(key);
            updateClickNumByPostId(key, value);
        }
        return true;
    }

    /**
     * @return void
     * @desc 单个修改点击量
     * @method updateClickNumByPostId
     * @params [key, value]
     * @author hubdir
     * @date 2019/5/6 2:32
     */
    private void updateClickNumByPostId(String key, Integer value) {
        postDao.updateClickNumByPostId(key, value);
    }
}
