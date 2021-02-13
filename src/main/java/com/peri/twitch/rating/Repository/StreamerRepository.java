package com.peri.twitch.rating.Repository;

import com.peri.twitch.rating.Model.Streamer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StreamerRepository extends JpaRepository<Streamer, Long> {

    Optional<Streamer> findByTwitchName(String username);
}
