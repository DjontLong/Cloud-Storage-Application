package com.djontlong.cloudstorage.mapper;

import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import org.apache.ibatis.annotations.*;

import java.util.List;


	@Mapper
	public interface  NoteMapper {
	   @Select("SELECT * FROM NOTES WHERE noteid = #{id}")
	   Note findNote(int id);
	   
	   @Select("SELECT * FROM NOTES")
	   List<Note> getNotes();
	   
	   @Select("SELECT * FROM NOTES WHERE userId = #{userid}")
	   List<Note> getNotesByUser(Integer userId);

	   @Insert("INSERT INTO NOTES(notetitle, notedescription, userid) VALUES(#{noteTitle},  #{noteDescription}, #{userId})")
	   @Options(useGeneratedKeys = true, keyProperty = "noteId")
	   Integer insert(Note note);

	   @Delete("DELETE FROM NOTES WHERE noteid = #{id}")
	   void delete(int id);
	   
	   @Update("UPDATE NOTES SET noteTitle = #{noteTitle}, notedescription = #{noteDescription} " +
	            "WHERE noteId = #{noteId}")
	   void updateNote(Note note);
	}

