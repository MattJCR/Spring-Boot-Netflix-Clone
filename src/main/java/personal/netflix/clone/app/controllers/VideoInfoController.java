package personal.netflix.clone.app.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.*;
import personal.netflix.clone.app.entities.VideoInfo;
import personal.netflix.clone.app.services.VideoInfoService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/api/v1/video/info")
public class VideoInfoController {
    @Autowired
    VideoInfoService videoInfoService;
    @GetMapping
    public List<VideoInfo> getAllVideoInfo(){
        return videoInfoService.getAllVideoInfo();
    }
}
