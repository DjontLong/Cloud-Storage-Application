package com.djontlong.cloudstorage.mapper;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

import com.djontlong.cloudstorage.model.User;



@Mapper
public interface UserMapper {
    @Select("SELECT * FROM USERS WHERE username = #{id}")
    User getUser(String id);

    @Insert("INSERT INTO USERS (userid, username, salt, password, firstname, lastname)"
            + " VALUES(#{userId}, #{username}, #{salt}, #{password}, #{firstName}, #{lastName})")
    @Options(useGeneratedKeys = true, keyProperty = "userId")
    Integer insert(User user);

    @Delete("DELETE FROM USERS WHERE username = #{id}")
    void delete(String id);
}

