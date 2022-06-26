package personal.netflix.clone.app.controllers;

import personal.netflix.clone.app.ServicesImpl.StreamingServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import org.springframework.core.io.Resource;

@RestController
@RequestMapping("/api/v1/video/")
public class StreamingController {
    @Autowired
    private StreamingServiceImpl streamingService;
    @GetMapping(value = "/{fileName}",produces = "video/mp4")
    public Mono<Resource> getVideo(@PathVariable("fileName") String fileName, @RequestHeader("Range") String range){
        System.out.println("FileName: " + fileName);
        System.out.println("range in bytes: " + range);
        return streamingService.getVideo(fileName);
    }
}
