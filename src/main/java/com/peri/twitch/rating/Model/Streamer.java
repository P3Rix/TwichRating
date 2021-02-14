package com.peri.twitch.rating.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Entity
public class Streamer {

    @Id
    @Column
    private long Id;

    @Column
    private String twitchName;

    @JsonIgnore
    @OneToMany
    private Set<Video> streamerVideo;

    private long view_count;

    private String image_url;

    public long getId() {
        return Id;
    }

    public void setId(long id) {
        Id = id;
    }

    public String getTwitchName() {
        return twitchName;
    }

    public void setTwitchName(String twitchName) {
        this.twitchName = twitchName;
    }

    public Set<Video> getStreamerVideo() {
        return streamerVideo;
    }

    public void setStreamerVideo(Set<Video> streamerVideo) {
        this.streamerVideo = streamerVideo;
    }

    public long getView_count() {
        return view_count;
    }

    public void setView_count(long view_count) {
        this.view_count = view_count;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }
}
