package com.peri.twitch.rating.Model;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Entity
public class Streamer {

    @Id
    @Column
    @GeneratedValue
    private long Id;

    @Column
    private String twitchName;

    @Column
    private byte[] profileImage;

    @OneToMany
    private Set<Video> streamerVideo;

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

    public byte[] getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(byte[] profileImage) {
        this.profileImage = profileImage;
    }

    public Set<Video> getStreamerVideo() {
        return streamerVideo;
    }

    public void setStreamerVideo(Set<Video> streamerVideo) {
        this.streamerVideo = streamerVideo;
    }
}
