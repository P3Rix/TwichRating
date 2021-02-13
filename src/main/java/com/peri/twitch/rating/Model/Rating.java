package com.peri.twitch.rating.Model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import java.util.Set;

@Entity
public class Rating {

    @Id
    @GeneratedValue
    private long Id;

    private long likes;

    private long dislikes;

    @ManyToMany
    private Set<User> userRating;

    @ManyToMany
    private Set<Video> videoRatings;

    public long getId() {
        return Id;
    }

    public void setId(long id) {
        Id = id;
    }

    public long getLikes() {
        return likes;
    }

    public void setLikes(long likes) {
        this.likes = likes;
    }

    public long getDislikes() {
        return dislikes;
    }

    public void setDislikes(long dislikes) {
        this.dislikes = dislikes;
    }

    public Set<User> getUserRating() {
        return userRating;
    }

    public void setUserRating(Set<User> userRating) {
        this.userRating = userRating;
    }
}
