package com.example.sit708_8_1c_llmchatbox;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import java.util.List;

@Dao
public interface MessageDao {

    @Insert
    void insert(Message message);



    @Query("SELECT * FROM messages_table WHERE username = :targetUser ORDER BY id ASC")
    List<Message> getMessagesByUser(String targetUser);
}