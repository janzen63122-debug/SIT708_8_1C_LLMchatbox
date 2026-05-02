package com.example.sit708_8_1c_llmchatbox;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "messages_table")
public class Message {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String text;
    private boolean isUser;
    private String timestamp;
    private String username;


    public Message(String text, boolean isUser, String timestamp, String username) {
        this.text = text;
        this.isUser = isUser;
        this.timestamp = timestamp;
        this.username = username;
    }


    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getText() { return text; }
    public void setText(String text) { this.text = text; }

    public boolean isUser() { return isUser; }
    public void setUser(boolean user) { isUser = user; }

    public String getTimestamp() { return timestamp; }
    public void setTimestamp(String timestamp) { this.timestamp = timestamp; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
}