package personal.netflix.clone.app.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import personal.netflix.clone.app.entities.StreamVideo;

import java.util.List;

@Repository
public interface StreamVideoRepository extends JpaRepository<StreamVideo, Long> {
    List<StreamVideo> findByVideoInfoId(Long id);
}
