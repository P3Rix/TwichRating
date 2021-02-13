package com.peri.twitch.rating.Model;

import javax.persistence.*;

@Entity
public class Video {

    @Id
    @Column
    @GeneratedValue
    private long Id;

    @ManyToOne
    private Streamer streamer;

    public long getId() {
        return Id;
    }

    public void setId(long id) {
        Id = id;
    }

    public Streamer getStreamer() {
        return streamer;
    }

    public void setStreamer(Streamer streamer) {
        this.streamer = streamer;
    }
}
