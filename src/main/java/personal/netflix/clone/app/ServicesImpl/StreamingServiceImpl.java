package personal.netflix.clone.app.ServicesImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import personal.netflix.clone.app.services.StreamingService;

@Service
public class StreamingServiceImpl implements StreamingService {
    @Autowired
    private ResourceLoader resourceLoader;

    private static final String FORMAT = "classpath:videos/%s.mp4";

    public Mono<Resource> getVideo(String title){
        return Mono.fromSupplier(()-> resourceLoader.getResource(String.format(FORMAT,title)));
    }
}
