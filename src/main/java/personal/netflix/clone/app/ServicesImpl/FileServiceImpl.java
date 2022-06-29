package personal.netflix.clone.app.ServicesImpl;

import com.xuggle.xuggler.IContainer;
import io.humble.video.*;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;
import personal.netflix.clone.app.entities.StreamAudio;
import personal.netflix.clone.app.entities.StreamVideo;
import personal.netflix.clone.app.entities.VideoInfo;
import personal.netflix.clone.app.repositories.StreamAudioRepository;
import personal.netflix.clone.app.repositories.StreamVideoRepository;
import personal.netflix.clone.app.services.FileService;

import javax.activation.MimetypesFileTypeMap;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class FileServiceImpl implements FileService {

    public static final Path VIDEO_PATH = Paths.get("./src/main/resources/videos");

    @Autowired
    private VideoInfoServiceImpl videoInfoService;

    private void createVideoInfo(VideoInfo videoInfo) {
        File file = new File(VIDEO_PATH + videoInfo.getFileName());
        MimetypesFileTypeMap fileTypeMap = new MimetypesFileTypeMap();
        String mimeType = fileTypeMap.getContentType(file);
        videoInfo.setMimeType(mimeType);
        String[] split = videoInfo.getFileName().split("\\.");
        if (split.length > 1){
            videoInfo.setFileExtension(split[1]);
        }
        try {
            getVideoInfoByDemuxer(videoInfo);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Stream Video: " + videoInfo.getStreamsVideos().size());
        System.out.println("Stream Audio: " + videoInfo.getStreamsAudios().size());
        //getVideoInfoByXuggle(videoInfo);
    }
    //TODO: Fix EXCEPTION_ACCESS_VIOLATION in IContainer class or get metadata by other way
    private void getVideoInfoByXuggle(VideoInfo videoInfo){
        IContainer container = IContainer.make();
        container.open(VIDEO_PATH + "/" + videoInfo.getFileName(), IContainer.Type.READ, null);
        videoInfo.setDuration(TimeUnit.MICROSECONDS.toSeconds(container.getDuration()));
        videoInfo.setSize(container.getFileSize());
        videoInfo.setBitRate(container.getBitRate());
        container.close();
    }

    private void getVideoInfoByDemuxer(VideoInfo videoInfo) throws IOException, InterruptedException {
        Demuxer demuxer = Demuxer.make();
        demuxer.open(VIDEO_PATH + "\\" + videoInfo.getFileName(), null, false, true, null, null);
        DemuxerFormat format = demuxer.getFormat();
        videoInfo.setUrl(demuxer.getURL());
        videoInfo.setSize(demuxer.getFileSize());
        videoInfo.setFormatName(format.getName());
        videoInfo.setFormatLongName(format.getLongName());
        KeyValueBag metadata = demuxer.getMetaData();
        for(String key: metadata.getKeys()){
            mapVideoInfoMetadata(videoInfo,key,metadata.getValue(key));
        }
        videoInfo.setDuration(demuxer.getDuration());
        videoInfo.setDurationFormat(formatTimeStamp(demuxer.getDuration()));
        videoInfo.setBitRate(demuxer.getBitRate()/1000);
        videoInfo.setStreamsVideos(new HashSet<StreamVideo>());
        videoInfo.setStreamsAudios(new HashSet<StreamAudio>());
        int numStreams = demuxer.getNumStreams();
        for (int i = 0; i < numStreams; i++) {
            DemuxerStream stream = null;
            try {
                stream = demuxer.getStream(i);
                if (stream != null){
                    mapStreamDataMetadata(videoInfo,stream);
                }
            } catch (InterruptedException | IOException e) {
                e.printStackTrace();
            }

        }
        demuxer.close();
    }

    private static String formatTimeStamp(long duration) {
        if (duration == Global.NO_PTS) {
            return "00:00:00.00";
        }
        double d = 1.0 * duration / Global.DEFAULT_PTS_PER_SECOND;
        int hours = (int) (d / (60*60));
        int mins = (int) ((d - hours*60*60) / 60);
        int secs = (int) (d - hours*60*60 - mins*60);
        int subsecs = (int)((d - (hours*60*60.0 + mins*60.0 + secs))*100.0);
        return String.format("%1$02d:%2$02d:%3$02d.%4$02d", hours, mins, secs, subsecs);
    }

    private void mapVideoInfoMetadata(VideoInfo videoInfo, String key, Object value){
        switch (key) {
            case "encoder" -> videoInfo.setEncoder((String) value);
            case "major_brand" -> videoInfo.setMajorBrand((String) value);
            case "minor_version" -> videoInfo.setMinorVersion((String) value);
            case "compatible_brands" -> videoInfo.setCompatibleBrands((String) value);
            default -> System.out.println(key + ": " + (String) value);
        }
    }

    private void mapStreamDataMetadata(VideoInfo videoInfo,DemuxerStream stream){
        KeyValueBag metadata = stream.getMetaData();
        String language = metadata.getValue("language");
        Decoder decoder = stream.getDecoder();
        if(decoder.getCodecType().name().equals("MEDIA_VIDEO")){
            StreamVideo streamVideo = new StreamVideo();
            streamVideo.setCodecId(decoder.getCodecID().toString());
            streamVideo.setName(decoder.getCodecID().name());
            streamVideo.setTimeBase(decoder.getTimeBase().toString());
            streamVideo.setWidth(decoder.getWidth());
            streamVideo.setHeight(decoder.getHeight());
            streamVideo.setPixelFormat(decoder.getPixelFormat().name());
            streamVideo.setFrameSize(decoder.getFrameSize());
            streamVideo.setFrameCount(decoder.getFrameCount());
            streamVideo.setLanguage(language);
            videoInfo.getStreamsVideos().add(streamVideo);
        } else if(decoder.getCodecType().name().equals("MEDIA_AUDIO")){
            StreamAudio streamAudio = new StreamAudio();
            streamAudio.setCodecId(decoder.getCodecID().toString());
            streamAudio.setName(decoder.getCodecID().name());
            streamAudio.setTimeBase(decoder.getTimeBase().toString());
            streamAudio.setSampleRate(decoder.getSampleRate());
            streamAudio.setSampleFormat(decoder.getSampleFormat().name());
            streamAudio.setFrameSize(decoder.getFrameSize());
            streamAudio.setFrameCount(decoder.getFrameCount());
            streamAudio.setLanguage(language);
            videoInfo.getStreamsAudios().add(streamAudio);
        }
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
