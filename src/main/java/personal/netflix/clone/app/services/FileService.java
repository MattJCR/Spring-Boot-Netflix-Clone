package personal.netflix.clone.app.services;

import org.springframework.http.codec.multipart.FilePart;
import personal.netflix.clone.app.entities.VideoInfo;
import reactor.core.publisher.Mono;


public interface FileService {

    public void saveVideo(VideoInfo videoInfo);
    public Boolean deleteVideoByName(String name);

}
