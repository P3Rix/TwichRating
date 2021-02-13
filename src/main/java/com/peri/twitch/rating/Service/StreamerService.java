package com.peri.twitch.rating.Service;

import com.fasterxml.jackson.core.JsonEncoding;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.peri.twitch.rating.ApiUtil.ApiUtils;
import com.peri.twitch.rating.Model.Streamer;
import com.peri.twitch.rating.Model.Video;
import com.peri.twitch.rating.Repository.StreamerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class StreamerService {

    Logger logger = LoggerFactory.getLogger(StreamerService.class);

    @Autowired
    StreamerRepository streamerRepository;

    @Autowired
    private Environment env;

    public List<Video> getVideosByStreamerName(String name) {
        Optional<Streamer> streamerObj = streamerRepository.findByTwitchName(name);
        Set<Video> streamerVideos = new HashSet<>();
        if (streamerObj.isPresent()) {
            streamerVideos = streamerObj.get().getStreamerVideo();
        } else {
            streamerVideos = populateVideoStreamer(name);
        }

        return new ArrayList<>(streamerVideos);
    }

    private Set<Video> populateVideoStreamer(String name) {
        String token = ApiUtils.generateToken(env);
        JsonObject streamerJson = ApiUtils.getStreamer(token, name, env);
        logger.info("JSON: " + streamerJson);
        JsonElement streamer = streamerJson.get("data");
        JsonArray array = streamer.getAsJsonArray();

        logger.debug("Array: " + array);
        streamerJson = array.get(0).getAsJsonObject();

        logger.debug("Id: " + streamerJson.get("id"));

        String id = streamerJson.get("id").getAsString();
        JsonObject getVideoJson = ApiUtils.getStreamerVideo(token, id, env);

        logger.info("Videos: " + getVideoJson);

        logger.info("Token: " + token);
        return null;
    }
}
