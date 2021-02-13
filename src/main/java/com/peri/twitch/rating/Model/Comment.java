package com.peri.twitch.rating.Model;

import javax.persistence.*;
import java.util.Set;

@Entity
public class Comment {

    @Id
    @GeneratedValue
    private long Id;

    private String content;

    @ManyToMany
    @JoinTable(
            name = "video_comment",
            joinColumns = {@JoinColumn(name = "comment_id")}
    )
    private Set<Video> videoComments;

    @ManyToMany
    @JoinTable(
            name = "user_comment",
            joinColumns = {@JoinColumn(name = "comment_id")}
    )
    private Set<User> userComment;

    public long getId() {
        return Id;
    }

    public void setId(long id) {
        Id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Set<Video> getVideoComments() {
        return videoComments;
    }

    public void setVideoComments(Set<Video> videoComments) {
        this.videoComments = videoComments;
    }

    public Set<User> getUserComment() {
        return userComment;
    }

    public void setUserComment(Set<User> userComment) {
        this.userComment = userComment;
    }
}
