package com.hubbbs.user.dao;

import com.hubbbs.user.pojo.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Map;
/**
 * 数据访问接口
 *
 * @author Administrator
 */
public interface UserDao extends JpaRepository<User, String>, JpaSpecificationExecutor<User> {

    @Modifying
    @Query(nativeQuery = true, value = "update user set postNum=postNum+(:step) where id=(:userId)")
    public void incPostNumByUserId(@Param("userId") String userId, @Param("step") Integer step);

    @Modifying
    @Query(nativeQuery = true, value = "update user set fansNum=fansNum+(:step) where id=(:userId)")
    public void incFansNumByUserId(@Param("userId") String userId, @Param("step") Integer step);

    @Modifying
    @Query(nativeQuery = true, value = "update user set attenterNum=attenterNum+(:step) where id=(:userId)")
    public void incAttenterNumByUserId(@Param("userId") String userId, @Param("step") Integer step);

    @Modifying
    @Query(nativeQuery = true, value = "update user set postNum=postNum-(:step) where id=(:userId)")
    public void decPostNumByUserId(@Param("userId") String userId, @Param("step") Integer step);

    @Modifying
    @Query(nativeQuery = true, value = "update user set fansNum=fansNum-(:step) where id=(:userId)")
    public void decFansNumByUserId(@Param("userId") String userId, @Param("step") Integer step);

    @Modifying
    @Query(nativeQuery = true, value = "update user set attenterNum=attenterNum-(:step) where id=(:userId)")
    public void decAttenterNumByUserId(@Param("userId") String userId, @Param("step") Integer step);

    @Modifying
    @Query(nativeQuery = true, value = "update user set cookieNum=cookieNum+(:step) where id=(:userId)")
    public void updateCookieNumByUserId(@Param("userId") String userId, @Param("step") String step);

    @Query(nativeQuery = true, value = "select role roles,`user`.nickname, `user`.icon  from account, `user` where account.userId = `user`.id and  `user`.id=(:userId)")
    public Map<String,Object> findInfoById(@Param("userId") String userId);

    public List<User> findByIdIn(List ids);
}
