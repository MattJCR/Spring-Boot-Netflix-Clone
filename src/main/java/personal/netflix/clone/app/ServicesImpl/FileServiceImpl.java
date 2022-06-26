package personal.netflix.clone.app.ServicesImpl;

import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Service;
import personal.netflix.clone.app.services.FileService;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class FileServiceImpl implements FileService {

    private static final Path VIDEO_PATH = Paths.get("./src/main/resources/videos");
    public Boolean saveVideo(Mono<FilePart> filePartMono){
        System.out.println("Path: " + VIDEO_PATH.toString());
        filePartMono
                .doOnNext(fp -> System.out.println("Received File : " + fp.filename()))
                .flatMap(fp -> fp.transferTo(VIDEO_PATH.resolve(fp.filename())))
                .then();
        //TODO Crear validaciones para comprobar si existe previamente.
        return true;
    }
    public Boolean deleteVideo(String name){
        boolean result = false;
        try {
            result = Files.deleteIfExists(Paths.get(VIDEO_PATH + "/" + name));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
}
