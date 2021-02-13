package com.peri.twitch.rating.Controller;

import com.peri.twitch.rating.Model.Video;
import com.peri.twitch.rating.Service.StreamerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class StreamerController {

    @Autowired
    StreamerService streamerService;

    @GetMapping("/streamer/{username}")
    List<Video> getVideosByStreamerName(@PathVariable String username) {
        return streamerService.getVideosByStreamerName(username);
    }
}
