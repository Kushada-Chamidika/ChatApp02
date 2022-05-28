package com.javaistitute.mychatapplication.Model;

public class FriendsList {
    String friend_id;
    String friend_email;
    String friend_username;
    String last_msg_timestamp;

    public FriendsList() {
    }

    public FriendsList(String friend_id, String friend_email, String friend_username) {
        this.friend_id = friend_id;
        this.friend_email = friend_email;
        this.friend_username = friend_username;
    }

    public String getFriend_id() {
        return friend_id;
    }

    public void setFriend_id(String friend_id) {
        this.friend_id = friend_id;
    }

    public String getFriend_email() {
        return friend_email;
    }

    public void setFriend_email(String friend_email) {
        this.friend_email = friend_email;
    }

    public String getFriend_username() {
        return friend_username;
    }

    public void setFriend_username(String friend_username) {
        this.friend_username = friend_username;
    }

    public String getLast_msg_timestamp() {
        return last_msg_timestamp;
    }

    public void setLast_msg_timestamp(String last_msg_timestamp) {
        this.last_msg_timestamp = last_msg_timestamp;
    }
}
