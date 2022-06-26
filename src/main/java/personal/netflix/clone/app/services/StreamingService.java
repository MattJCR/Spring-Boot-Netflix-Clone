package personal.netflix.clone.app.services;

import org.springframework.core.io.Resource;
import reactor.core.publisher.Mono;

public interface StreamingService {
    public Mono<Resource> getVideo(String title);
}
