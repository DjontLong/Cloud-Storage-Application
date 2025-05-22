package com.djontlong.cloudstorage.mapper;

import com.djontlong.cloudstorage.model.Credential;
import org.apache.ibatis.annotations.*;

import java.util.List;


@Mapper
public interface CredentialMapper {
   @Select("SELECT * FROM CREDENTIALS WHERE credentialid = #{id}")
   Credential findCredential(int id);

   @Select("SELECT * FROM CREDENTIALS")
   List<Credential> getCredentials();

   @Select("SELECT * FROM CREDENTIALS WHERE userId = #{userid}")
   List<Credential> getCredentialsByUser(Integer userId);

   @Insert("INSERT INTO CREDENTIALS(url, username, key, password, userid) VALUES(#{url},  #{username},  #{key}, #{password}, #{userId})")
   @Options(useGeneratedKeys = true, keyProperty = "credentialId")
   Integer insert(Credential credential);

   @Delete("DELETE FROM CREDENTIALS WHERE credentialid = #{id}")
   void delete(int id);

   @Update("UPDATE CREDENTIALS SET url = #{url}, username = #{username}, key = #{key}, password = #{password} " +
            "WHERE credentialid = #{credentialId}")
   void updateCredential(Credential credential);
}

