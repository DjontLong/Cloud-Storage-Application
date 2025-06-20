package com.djontlong.cloudstorage.mapper;

import com.djontlong.cloudstorage.model.File;
import org.apache.ibatis.annotations.*;

import java.util.List;


@Mapper
public interface FileMapper {

	@Select("SELECT * FROM FILES WHERE userId = #{id}")
	public List<File> getFilesByUser(Integer id);

	@Select("SELECT * FROM FILES")
	List<File> getFiles();

	@Select("SELECT filename FROM FILES WHERE userId = #{id} AND filename=#{name}")
	public String findFileByName(String name, Integer id);

	@Select("SELECT * FROM FILES WHERE fileid = #{id}")
	public File findFile(Integer id);

	@Insert("INSERT INTO FILES (filename, contenttype, filesize, userid) "
			+ "VALUES (#{fileName}, #{contentType}, #{fileSize}, #{userId})")
	@Options(useGeneratedKeys = true, keyProperty = "fileId")
	public void insert(File file);

	@Delete("DELETE FROM FILES WHERE fileid = #{id}")
	public void delete(Integer id);

}
