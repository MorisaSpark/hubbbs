package com.hubbbs.user.scheduler;

import com.hubbbs.user.dao.RelCollectionDao;
import com.hubbbs.user.pojo.Post;
import com.hubbbs.user.pojo.RelCollection;
import com.hubbbs.user.service.PostService;
import com.hubbbs.user.utils.StringRedisTemplateUtil;
import com.hubbbs.user.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import util.IdWorker;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: HB
 * Date: 2019/3/28
 *
 * @author HB
 * To change this template use File | Settings | File Templates.
 */
@Component
@Configurable
@EnableScheduling
@SuppressWarnings("unckecked")
@Transactional
public class RedisScheduledTasks {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private StringRedisTemplateUtil redisTemplateUtil;

    @Autowired
    private PostService postService;

    @Autowired
    private RelCollectionDao relCollectionDao;

    @Autowired
    private IdWorker idWorker;

    public void prepareTestData() {
        redisTemplate.opsForValue().set("clickNum::10079006::1000037", "1");
        redisTemplate.opsForValue().set("clickNum::10079006::10002449", "1");
        redisTemplate.opsForValue().set("clickNum::10082920::1000037", "1");
        String now = StringUtils.toEpochMilli();
        redisTemplate.opsForValue().set("RelCollection::10079006::1000037", now);
        redisTemplate.opsForValue().set("RelCollection::10079006::10002449", now);
        redisTemplate.opsForValue().set("RelCollection::10082920::1000037", now);
    }

    @Scheduled(cron = "0 */10 *  * * * ")
    public void persistList() {
        persistClickNumToMysqlDB();
        persistCollectionNumToMysqlDB();
    }

    //      拟采用 map <postid::ip, 1> 1为浏览过   在schedule中定时更新到mysql
    //每10分钟执行一次
//    @Scheduled(cron = "0 */10 *  * * * ")
    public void persistClickNumToMysqlDB() {
        Set<String> keys = redisTemplateUtil.scan(redisTemplate, "clickNum::*");
        System.out.println("persistClickNumToMysqlDB keys====" + keys);
        if (keys != null) { //clickNum::postId::userId
//            redisTemplate.delete(keys);
//            1.将postId 与 新增的点击量存入
//            List<String> ids = new ArrayList<>();
            HashMap<String, Integer> nums = new HashMap<>();
//            2.统计num
            long nowTime = Long.parseLong(StringUtils.toEpochMilli());
            for (String key : keys) {
                //每十分钟一次，每次将十分钟内的数据存入数据库，不删除，自行过期
                if ((nowTime - Long.parseLong(redisTemplate.opsForValue().get(key))) > (10 * 60 * 1000)) {
                    break;
                }
                String[] split = key.split("::");
                String postId = split[1];
                String userId = split[2];
                Integer num = nums.get(postId);
                nums.put(postId, (num == null ? 1 : num + 1));
//                ids.add(postId);
            }
            postService.updateClickNumByPostIdIn(nums);
            //提高代码效率，将直接通过postId使点击量增加step
////            3.用postId查到post的信息
//            List<Post> postDB = postService.findByIdIn(ids);
////            4.将点击量合并
//            for (Post post : postDB) {
//                for (String key : nums.keySet()) {
//                    if (post.getId().equals(key)) {
//                        post.setClickNum(post.getClickNum() + nums.get(key));
//                    }
//                }
//            }
////            5.将数据更新
//            postService.update(postDB);
        }
    }

    //    @Scheduled(cron = "0 */10 *  * * * ")
    public void persistCollectionNumToMysqlDB() {
        Set<String> keys = redisTemplateUtil.scan(redisTemplate, "RelCollection::*");
        System.out.println("persistCollectionNumToMysqlDB keys====" + keys);
        if (keys != null) { //RelCollection::postId::userId
//            1.将postId 与 新增的点击量存入
            List<String> ids = new ArrayList<>();
            List<RelCollection> relCollections = new ArrayList<>();
            HashMap<String, Integer> map = new HashMap<>();
//            2.存储relCollection
            for (String key : keys) {
                String[] split = key.split("::");
                String postId = split[1];
                String userId = split[2];
                String time = redisTemplate.opsForValue().get(key);
                if ("0".equals(time)) {
                    relCollectionDao.deleteByPostIdAndUserId(userId, postId);
                    continue;
                }
                ids.add(postId);

                RelCollection relCollection = new RelCollection()
                        .setId("" + idWorker.nextId())
                        .setTime(time)
                        .setPostId(postId)
                        .setUserId(userId);
                relCollections.add(relCollection);
            }
            relCollectionDao.saveAll(relCollections);
            for (RelCollection relCollection : relCollections) {
                String postId = relCollection.getPostId();
                Integer integer = map.get(postId);
                map.put(postId, integer == null ? 1 : integer + 1);
            }
            List<Post> posts = postService.findByIdIn(ids);
            for (String key : map.keySet()) {
                for (Post post : posts) {
                    if (key.equals(post.getId())) {
                        post.setCollectionNum(post.getCollectionNum() + map.get(key));
                    }
                }
            }
            postService.update(posts);

            redisTemplate.delete(keys);
        }
    }

    private SimpleDateFormat dateFormat() {
        return new SimpleDateFormat("HH:mm:ss");
    }
}