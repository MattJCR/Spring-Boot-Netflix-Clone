package personal.netflix.clone.app.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.web.bind.annotation.*;
import personal.netflix.clone.app.services.FileService;
import reactor.core.publisher.Mono;

import java.nio.file.Path;
import java.nio.file.Paths;


@RestController
@RequestMapping("/api/v1/file/")
public class FileController {

    @Autowired
    private FileService fileService;
    private static final Path VIDEO_PATH = Paths.get("./src/main/resources/videos/");
    @PostMapping("/single/upload")
    public Mono<Void> uploadSingleFile(@RequestPart(name = "file") Mono<FilePart> filePartMono){
        System.out.println("Save video...");
        //Long result = fileService.saveVideo(filePartMono);
        //return new ResponseEntity<>("Video uploaded", HttpStatus.ACCEPTED);
        return  filePartMono
                .doOnNext(fp -> System.out.println
                        ("Received File : " + fp.filename()))
                .flatMap(fp -> fp.
                        transferTo(VIDEO_PATH.resolve(fp.filename())))
                .then();
    }
    @DeleteMapping("/delete/{name}")
    public ResponseEntity<Boolean> deleteFile(@PathVariable("name") String name){
        System.out.println("Delete video: " + name);
        Boolean result = fileService.deleteVideo(name);
        if(result){
            return new ResponseEntity<>(result, HttpStatus.OK);
        }
        return new ResponseEntity<>(result, HttpStatus.NOT_FOUND);
    }
}
