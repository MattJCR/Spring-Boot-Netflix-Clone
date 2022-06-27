package personal.netflix.clone.app.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.web.bind.annotation.*;
import personal.netflix.clone.app.entities.VideoInfo;
import personal.netflix.clone.app.services.FileService;
import reactor.core.publisher.Mono;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.atomic.AtomicReference;


@RestController
@RequestMapping("/api/v1/file/")
public class FileController {

    @Autowired
    private FileService fileService;
    private static final Path VIDEO_PATH = Paths.get("./src/main/resources/videos/");
    @PostMapping("/single/upload")
    public Mono<Void> uploadSingleFile(
            @RequestPart(name = "file") Mono<FilePart> filePartMono,
            @RequestParam(name = "autor") String autor,
            @RequestParam(name = "title") String title){
        AtomicReference<VideoInfo> videoInfo = new AtomicReference<>(new VideoInfo());
        return filePartMono
                .doOnNext(fp -> {
                    System.out.println("Try to save file: " + fp.filename());
                    videoInfo.set(new VideoInfo());
                    videoInfo.get().setAutor(autor);
                    videoInfo.get().setTitle(title);
                    videoInfo.get().setFileName(fp.filename());
                })
                .flatMap(fp -> fp.transferTo(VIDEO_PATH.resolve(fp.filename())))
                .doFinally(f -> fileService.saveVideo(videoInfo.get()));
    }

    @DeleteMapping("/delete/{name}")
    public ResponseEntity<Boolean> deleteFile(@PathVariable("name") String name){
        System.out.println("try to delete video: " + name);
        Boolean result = fileService.deleteVideoByName(name);
        if(result){
            return new ResponseEntity<>(result, HttpStatus.OK);
        }
        return new ResponseEntity<>(result, HttpStatus.NOT_FOUND);
    }
}
