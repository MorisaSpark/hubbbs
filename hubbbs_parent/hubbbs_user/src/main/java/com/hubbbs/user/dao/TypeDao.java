package com.hubbbs.user.dao;

import com.hubbbs.user.pojo.Type;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * 数据访问接口
 *
 * @author Administrator
 */
public interface TypeDao extends JpaRepository<Type, String>, JpaSpecificationExecutor<Type> {

    @Modifying
    @Query(nativeQuery = true, value = "update type set postNum=postNum+(:step) where id=(:typeId)")
    public void updatePostNumById(@Param("typeId") String typeId, @Param("step") String step);
}
