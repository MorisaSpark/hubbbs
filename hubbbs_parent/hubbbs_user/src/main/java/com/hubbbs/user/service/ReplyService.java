package com.hubbbs.user.service;

import com.hubbbs.user.dao.PostDao;
import com.hubbbs.user.dao.ReplyDao;
import com.hubbbs.user.dao.UserDao;
import com.hubbbs.user.pojo.Reply;
import com.hubbbs.user.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import util.IdWorker;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 服务层
 *
 * @author Administrator
 */
@Service
@Transactional
public class ReplyService {

    @Autowired
    private ReplyDao replyDao;
    @Autowired
    private UserDao userDao;
    @Autowired
    private PostDao postDao;
    @Autowired
    private IdWorker idWorker;
    @Autowired
    private MongoTemplate mongoTemplate;

    /**
     * 查询全部列表
     *
     * @return
     */
    public List<Reply> findAll() {
        return replyDao.findAll();
    }

    /*
        OR = "$or"：or条件
        AND = "$and"：and条件
        GT = "$gt"：大于操作
        GTE = "$gte"：大于等于操作
        LT = "$lt"：小于操作
        LTE = "$lte"小于等于操作
        NE = "$ne"：不等于操作
        IN = "$in"：in操作
    */

    /**
     * 条件查询+分页
     *
     * @param whereMap
     * @param page
     * @param size
     * @return
     */
    public Map<String, Object> findSearch(Map whereMap, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        Query query = new Query();
        //条件id =XX
        Criteria criteria = queryCriteria(whereMap);
        query.addCriteria(criteria);
        //mongoTemplate.count计算总数
        long total = mongoTemplate.count(query, Reply.class);
        // mongoTemplate.find 查询结果集
        List<Reply> items = mongoTemplate.find(query.with(pageable), Reply.class);

        HashMap<String, Object> map = new HashMap<>();
        map.put("rows", items);
        map.put("total", total);
        return map;
    }

    public Criteria queryCriteria(Map whereMap) {
        Criteria criteria = Criteria.where("createTime").gte("0");
        if (whereMap.get("id") != null && !"".equals(whereMap.get("id"))) {
            criteria.and("id").is(whereMap.get("id"));
        }
        if (whereMap.get("toId") != null && !"".equals(whereMap.get("toId"))) {
            criteria.and("toId").is(whereMap.get("toId"));
        }
        if (whereMap.get("createTime") != null && !"".equals(whereMap.get("createTime"))) {
            criteria.and("createTime").is(whereMap.get("createTime"));
        }
        if (whereMap.get("body") != null && !"".equals(whereMap.get("body"))) {
            criteria.and("body").is(whereMap.get("body"));
        }
        if (whereMap.get("thumbUpNum") != null && !"".equals(whereMap.get("thumbUpNum"))) {
            criteria.and("thumbUpNum").is(whereMap.get("thumbUpNum"));
        }
        if (whereMap.get("isBan") != null && !"".equals(whereMap.get("isBan"))) {
            criteria.and("isBan").is(whereMap.get("isBan"));
        }
        if (whereMap.get("floorTh") != null && !"".equals(whereMap.get("floorTh"))) {
            criteria.and("floorTh").is(whereMap.get("floorTh"));
        }
        if (whereMap.get("userId") != null && !"".equals(whereMap.get("userId"))) {
            criteria.and("userId").is(whereMap.get("userId"));
        }
        if (whereMap.get("postId") != null && !"".equals(whereMap.get("postId"))) {
            criteria.and("postId").is(whereMap.get("postId"));
        }
        return criteria;
    }


    /**
     * 条件查询
     * @param whereMap
     * @return
     */
//	public List<Reply> findSearch(Map whereMap) {
//		Specification<Reply> specification = createSpecification(whereMap);
//		return replyDao.findAll(specification);
//	}

    /**
     * 根据ID查询实体
     *
     * @param id
     * @return
     */
    public Reply findById(String id) {
        return replyDao.findById(id).get();
    }

    /**
     * 增加
     *
     * @param reply
     */
    public void add(Reply reply) {
        String now = "" + Instant.now().toEpochMilli();
        //获取文章最后回复，目的：获取楼层数
        Reply lastReply = replyDao.findTopByPostIdOrderByFloorThDesc(reply.getPostId());
        reply.setId(idWorker.nextId() + "")
                .setCreateTime(now)
                .setIsBan(0)
                .setThumbUpNum(0)
                .setFloorTh(lastReply != null ? (lastReply.getFloorTh() + 1) : 1);
        postDao.incReplyNumByPostId(reply.getPostId(), 1);
        replyDao.save(reply);
    }

    /**
     * 修改
     *
     * @param reply
     */
    public void update(Reply reply) {
        replyDao.save(reply);
    }

    /**
     * 删除
     *
     * @param id
     */
    public void deleteById(String id) {
        replyDao.deleteById(id);
    }

    /**
     * 动态条件构建
     *
     * @param searchMap
     * @return
     */
    private Specification<Reply> createSpecification(Map searchMap) {

        return new Specification<Reply>() {

            @Override
            public Predicate toPredicate(Root<Reply> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> predicateList = new ArrayList<Predicate>();
                // 
                if (searchMap.get("id") != null && !"".equals(searchMap.get("id"))) {
                    predicateList.add(cb.like(root.get("id").as(String.class), "%" + (String) searchMap.get("id") + "%"));
                }
                // 
                if (searchMap.get("toId") != null && !"".equals(searchMap.get("toId"))) {
                    predicateList.add(cb.like(root.get("toId").as(String.class), "%" + (String) searchMap.get("toId") + "%"));
                }
                // 
                if (searchMap.get("createTime") != null && !"".equals(searchMap.get("createTime"))) {
                    predicateList.add(cb.like(root.get("createTime").as(String.class), "%" + (String) searchMap.get("createTime") + "%"));
                }
                // 
                if (searchMap.get("body") != null && !"".equals(searchMap.get("body"))) {
                    predicateList.add(cb.like(root.get("body").as(String.class), "%" + (String) searchMap.get("body") + "%"));
                }
                // 
                if (searchMap.get("userId") != null && !"".equals(searchMap.get("userId"))) {
                    predicateList.add(cb.like(root.get("userId").as(String.class), "%" + (String) searchMap.get("userId") + "%"));
                }
                // 
                if (searchMap.get("postId") != null && !"".equals(searchMap.get("postId"))) {
                    predicateList.add(cb.like(root.get("postId").as(String.class), "%" + (String) searchMap.get("postId") + "%"));
                }

                return cb.and(predicateList.toArray(new Predicate[predicateList.size()]));

            }
        };

    }

    public Page<Reply> findByPostId(String postId, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page - 1, size);
        return replyDao.findByPostId(postId, pageRequest);
    }

    //三种评论查看方式
//    1.展开式，2.分页式，3.随机式

    //        1.通过postId查到reply，
//        2.用replyId查user
//        3.形成 {
//            total: 11,
//            rows:[
//                {
//                    user: new User(),
//                    reply: new Reply()
//                }
//            ]
//        }
    public Map<String, Object> findByPostId(String postId) {
        return joinReplyList(replyDao.findByPostIdOrderByFloorThAsc(postId), postId);
    }


    public Map<String, Object> searchFloorTh(String postId, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size, Sort.Direction.DESC, "floorTh");
        Page<Reply> replyPage = replyDao.findByPostId(postId, pageable);

        return joinReplyList(replyPage.getContent(), postId);
    }

    //随机评论已关闭
    public Map<String, Object> findRandom(String postId, int size) {
        return findByPostId(postId);
    }

    public Map<String, Object> joinReplyList(List<Reply> replies, String postId) {
        Map<String, Object> mapListMap = new HashMap<>();
        mapListMap.put("total", replyDao.countByPostId(postId));
        mapListMap.put("rows", findUserByReplyIdWithReply(replies));
        return mapListMap;
    }

    private List<Map<String, Object>> findUserByReplyIdWithReply(List<Reply> replies) {
        List<Map<String, Object>> mapList = new ArrayList<>();
//        ArrayList<String> userIds = new ArrayList<>();
//        for (Reply reply : replies) {
//            Map<String, Object> mapObj = new HashMap<>();
////            userIds.add(reply.getUserId());
//
//            User user = userDao.findById(reply.getUserId()).get();
//            mapObj.put("user", user);
//            mapObj.put("reply", reply);
//            mapList.add(mapObj);
//        }
        ArrayList<String> userIds = new ArrayList<>();
        for (Reply reply : replies) {
            userIds.add(reply.getUserId());
        }
        List<User> users = userDao.findByIdIn(userIds);
        for (Reply reply : replies) {
            for (User user : users) {
                if(user.getId().equals(reply.getUserId())){
                    Map<String, Object> mapObj = new HashMap<>();
                    mapObj.put("user", user);
                    mapObj.put("reply", reply);
                    mapList.add(mapObj);
                    break;
                }
            }
        }
        return mapList;
    }

}
