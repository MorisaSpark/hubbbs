package com.hubbbs.user.service;

import com.hubbbs.user.dao.MessageDao;
import com.hubbbs.user.pojo.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import util.IdWorker;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 服务层
 *
 * @author Administrator
 */
@Service
@Transactional
public class MessageService {

    @Autowired
    private MessageDao messageDao;

    @Autowired
    private IdWorker idWorker;

    /**
     * 查询全部列表
     *
     * @return
     */
    public List<Message> findAll() {
        return messageDao.findAll();
    }


    /**
     * 条件查询+分页
     *
     * @param whereMap
     * @param page
     * @param size
     * @return
     */
    public Page<Message> findSearch(Map whereMap, int page, int size) {
        Specification<Message> specification = createSpecification(whereMap);
        PageRequest pageRequest = PageRequest.of(page - 1, size);
        return messageDao.findAll(specification, pageRequest);
    }


    /**
     * 条件查询
     *
     * @param whereMap
     * @return
     */
    public List<Message> findSearch(Map whereMap) {
        Specification<Message> specification = createSpecification(whereMap);
        return messageDao.findAll(specification);
    }

    /**
     * 根据ID查询实体
     *
     * @param id
     * @return
     */
    public Message findById(String id) {
        return messageDao.findById(id).get();
    }

    /**
     * 增加
     *
     * @param message
     */
    public void add(Message message) {
        message.setId(idWorker.nextId() + "")
                .setIsBan(0)
                .setSendTime("" + Instant.now().toEpochMilli());
        messageDao.save(message);
    }

    /**
     * 修改
     *
     * @param message
     */
    public void update(Message message) {
        messageDao.save(message);
    }

    /**
     * 删除
     *
     * @param id
     */
    public void deleteById(String id) {
        messageDao.deleteById(id);
    }

    /**
     * 动态条件构建
     *
     * @param searchMap
     * @return
     */
    private Specification<Message> createSpecification(Map searchMap) {

        return new Specification<Message>() {

            @Override
            public Predicate toPredicate(Root<Message> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> predicateList = new ArrayList<Predicate>();
                // 
                if (searchMap.get("id") != null && !"".equals(searchMap.get("id"))) {
                    predicateList.add(cb.like(root.get("id").as(String.class), "%" + (String) searchMap.get("id") + "%"));
                }
                // 
                if (searchMap.get("sendTime") != null && !"".equals(searchMap.get("sendTime"))) {
                    predicateList.add(cb.like(root.get("sendTime").as(String.class), "%" + (String) searchMap.get("sendTime") + "%"));
                }
                // 
                if (searchMap.get("body") != null && !"".equals(searchMap.get("body"))) {
                    predicateList.add(cb.like(root.get("body").as(String.class), "%" + (String) searchMap.get("body") + "%"));
                }
                // 
                if (searchMap.get("senderId") != null && !"".equals(searchMap.get("senderId"))) {
                    predicateList.add(cb.like(root.get("senderId").as(String.class), "%" + (String) searchMap.get("senderId") + "%"));
                }
                // 
                if (searchMap.get("receiverId") != null && !"".equals(searchMap.get("receiverId"))) {
                    predicateList.add(cb.like(root.get("receiverId").as(String.class), "%" + (String) searchMap.get("receiverId") + "%"));
                }

                return cb.and(predicateList.toArray(new Predicate[predicateList.size()]));

            }
        };

    }

    public Page<Map<String, Object>> searchByTypeSelf(String userId, String er, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size, Sort.Direction.DESC, "sendTime");
        if (er.equals("receive")) {
            return messageDao.findByReceiverIdWithUser(userId, pageable);
        } else if (er.equals("send")) {
            return messageDao.findBySenderIdWithUser(userId, pageable);
        } else {
            return null;
        }
    }
}
