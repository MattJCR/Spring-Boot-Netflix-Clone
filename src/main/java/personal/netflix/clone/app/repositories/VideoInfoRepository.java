package personal.netflix.clone.app.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import personal.netflix.clone.app.entities.VideoInfo;

import java.util.Optional;

@Repository
public interface VideoInfoRepository extends JpaRepository<VideoInfo, Long> {
    Optional<VideoInfo> findByFileName(String fileName);
}
