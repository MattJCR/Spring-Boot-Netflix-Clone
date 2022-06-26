package personal.netflix.clone.app.services;

import org.springframework.http.codec.multipart.FilePart;
import reactor.core.publisher.Mono;


public interface FileService {
    public Boolean saveVideo(Mono<FilePart> filePartMono);
    public Boolean deleteVideo(String name);
}
