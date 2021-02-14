package com.peri.twitch.rating.Repository;

import com.peri.twitch.rating.Model.Video;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VideoRepository extends JpaRepository<Video, Long> {
}
