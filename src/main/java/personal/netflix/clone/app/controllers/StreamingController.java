package personal.netflix.clone.app.controllers;

import personal.netflix.clone.app.ServicesImpl.StreamingServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import org.springframework.core.io.Resource;

@RestController
public class StreamingController {
    @Autowired
    private StreamingServiceImpl streamingService;
    @GetMapping(value = "/video/{title}",produces = "video/mp4")
    public Mono<Resource> getVideo(@PathVariable("title") String title, @RequestHeader("Range") String range){
        System.out.println("Title: " + title);
        System.out.println("range in bytes: " + range);
        return streamingService.getVideo(title);
    }

    @GetMapping(value = "/holamundo")
    public String getHolaMundo(){
        return "Hola mundo";
    }
}
