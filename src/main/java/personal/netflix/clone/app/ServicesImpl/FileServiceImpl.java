package personal.netflix.clone.app.ServicesImpl;

import com.xuggle.xuggler.IContainer;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;
import personal.netflix.clone.app.entities.VideoInfo;
import personal.netflix.clone.app.services.FileService;

import javax.activation.MimetypesFileTypeMap;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.TimeUnit;

@Service
public class FileServiceImpl implements FileService {

    public static final Path VIDEO_PATH = Paths.get("./src/main/resources/videos");

    @Autowired
    private VideoInfoServiceImpl videoInfoService;

    private void createVideoInfo(VideoInfo videoInfo){
        File file = new File(VIDEO_PATH + videoInfo.getFileName());
        MimetypesFileTypeMap fileTypeMap = new MimetypesFileTypeMap();
        String mimeType = fileTypeMap.getContentType(file);
        videoInfo.setMimeType(mimeType);
        String[] split = videoInfo.getFileName().split("\\.");
        if (split.length > 1){
            videoInfo.setFileExtension(split[1]);
        }

        //TODO: Fix EXCEPTION_ACCESS_VIOLATION in IContainer class or get metadata by other way
        IContainer container = IContainer.make();
        container.open(VIDEO_PATH + "/" + videoInfo.getFileName(), IContainer.Type.READ, null);
        System.out.println(container.getMetaData().getKeys());
        videoInfo.setDuration(TimeUnit.MICROSECONDS.toSeconds(container.getDuration()));
        videoInfo.setSize(container.getFileSize());
        videoInfo.setBitRate(container.getBitRate());
        container.close();
    }


    public void saveVideo(VideoInfo videoInfo){
        createVideoInfo(videoInfo);
        videoInfoService.saveOrUpdateVideoInfo(videoInfo);
    }
    public Boolean deleteVideoByName(String name){
        boolean result = false;
        try {
            if(videoInfoService.deteleVideoInfoByName(name)){
                result = Files.deleteIfExists(Paths.get(VIDEO_PATH + "/" + name));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
}
