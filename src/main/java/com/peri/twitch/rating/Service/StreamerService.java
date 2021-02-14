package com.peri.twitch.rating.Service;

import com.fasterxml.jackson.core.JsonEncoding;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.peri.twitch.rating.ApiUtil.ApiUtils;
import com.peri.twitch.rating.Constants.StreamerConstants;
import com.peri.twitch.rating.Constants.VideoConstants;
import com.peri.twitch.rating.Model.Streamer;
import com.peri.twitch.rating.Model.Video;
import com.peri.twitch.rating.Repository.StreamerRepository;
import com.peri.twitch.rating.Repository.VideoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class StreamerService {

    Logger logger = LoggerFactory.getLogger(StreamerService.class);

    @Autowired
    StreamerRepository streamerRepository;

    @Autowired
    VideoRepository videoRepository;

    @Autowired
    private Environment env;

    public Streamer getStreamerbyStreamerName(String name) {
        Optional<Streamer> streamerObj = streamerRepository.findByTwitchName(name);

        if (streamerObj.isPresent()) {
            return streamerObj.get();
        } else {
            return new Streamer();
        }
    }

    public List<Streamer> getAllStreamer() {
        return streamerRepository.findAll();
    }

    public Set<Video> getVideosByStreamerName(String name) {
        Optional<Streamer> streamerObj = streamerRepository.findByTwitchName(name);
        Set<Video> streamerVideos = new HashSet<>();
        if (streamerObj.isPresent()) {
            streamerVideos = streamerObj.get().getStreamerVideo();
        } else {
            streamerVideos = populateVideoStreamer(name);
        }

        return streamerVideos;
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

        Streamer newStreamer = saveStreamerObject(streamerJson);

        String id = streamerJson.get("id").getAsString();
        JsonObject videoJson = ApiUtils.getStreamerVideo(token, id, env);

        JsonElement videoJsonData = videoJson.get("data");
        JsonArray videoJsonArray = videoJsonData.getAsJsonArray();

        Set<Video> videos = fillVideoObjects(videoJsonArray, newStreamer);

        if(!videos.isEmpty()) {
            newStreamer.setStreamerVideo(videos);
            streamerRepository.save(newStreamer);
        }

        logger.info("Videos: " + videoJson);

        logger.info("Token: " + token);
        return videos;
    }

    private Set<Video> fillVideoObjects(JsonArray videoJsonArray, Streamer streamer) {

        JsonObject jsonVideo;
        Set<Video> videos = new HashSet<>();

        Video savedVideo;
        for (int i = 0; i < videoJsonArray.size(); i++) {
            jsonVideo = videoJsonArray.get(i).getAsJsonObject();
            Video video = new Video();

            video.setId(jsonVideo.get(VideoConstants.FIELD_ID).getAsLong());
            video.setTitle(jsonVideo.get(VideoConstants.FIELD_TITLE).getAsString());
            video.setTwitch_id(jsonVideo.get(VideoConstants.FIELD_USER_ID).getAsLong());

            try {
                Date date = new SimpleDateFormat("dd/MM/yyyy").parse(jsonVideo.get(
                        VideoConstants.FIELD_CREATED_AT).getAsString());
                video.setCreate_at(date);
            } catch (ParseException e) {
                logger.error("Error getting date from json video: " + e);
            }

            video.setDuration(jsonVideo.get(VideoConstants.FIELD_DURATION).getAsString());
            video.setVideo_url(jsonVideo.get(VideoConstants.FIELD_URL).getAsString());
            video.setThumbnail_url(jsonVideo.get(VideoConstants.FIELD_THUMBNAIL_URL).getAsString());
            video.setStreamer(streamer);

            savedVideo = videoRepository.save(video);

            videos.add(savedVideo);
        }
        logger.info("Set videos: " + videos);

        return videos;
    }

    private Streamer saveStreamerObject(JsonObject streamer) {
        Streamer newStreamer = new Streamer();

        newStreamer.setId(streamer.get(StreamerConstants.FIELD_ID).getAsLong());
        newStreamer.setTwitchName(streamer.get(StreamerConstants.FIELD_USERNAME).getAsString());
        newStreamer.setImage_url(streamer.get(StreamerConstants.FIELD_IMAGE_URL).getAsString());
        newStreamer.setView_count(streamer.get(StreamerConstants.FIELD_VIDEO_COUNT).getAsLong());

        return streamerRepository.save(newStreamer);
    }
}
