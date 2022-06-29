package personal.netflix.clone.app.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import personal.netflix.clone.app.entities.StreamAudio;

import java.util.List;

@Repository
public interface StreamAudioRepository extends JpaRepository<StreamAudio, Long> {
    List<StreamAudio> findByVideoInfoId(Long id);
}
