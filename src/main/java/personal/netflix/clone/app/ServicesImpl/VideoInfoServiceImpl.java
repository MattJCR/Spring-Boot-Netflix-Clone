package personal.netflix.clone.app.ServicesImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import personal.netflix.clone.app.entities.StreamAudio;
import personal.netflix.clone.app.entities.StreamVideo;
import personal.netflix.clone.app.entities.User;
import personal.netflix.clone.app.entities.VideoInfo;
import personal.netflix.clone.app.repositories.StreamAudioRepository;
import personal.netflix.clone.app.repositories.StreamVideoRepository;
import personal.netflix.clone.app.repositories.VideoInfoRepository;
import personal.netflix.clone.app.services.UserService;
import personal.netflix.clone.app.services.VideoInfoService;

import javax.transaction.Transactional;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;


@Service
public class VideoInfoServiceImpl implements VideoInfoService {

    @Autowired
    VideoInfoRepository videoInfoRepository;

    @Autowired
    private StreamVideoRepository streamVideoRepository;

    @Autowired
    private StreamAudioRepository streamAudioRepository;

    @Autowired
    UserService userService;

    @Transactional
    public void saveOrUpdateVideoInfo(VideoInfo videoInfo){
        Set<StreamVideo> streamsVideos = videoInfo.getStreamsVideos();
        Set<StreamAudio> streamsAudios = videoInfo.getStreamsAudios();
        videoInfo.setStreamsVideos(null);
        videoInfo.setStreamsAudios(null);
        Optional<VideoInfo> videoInfoOptional = videoInfoRepository.findByFileName(videoInfo.getFileName());
        videoInfoOptional.ifPresent(vi -> videoInfo.setId(vi.getId()));
        VideoInfo viSaved = videoInfoRepository.save(videoInfo);

        //TODO: Refactorizar en servicio StreamService para borrar los datos anteriores si ya existen
        streamAudioRepository.findByVideoInfoId(viSaved.getId()).forEach(sa -> streamAudioRepository.deleteById(sa.getId()));
        streamVideoRepository.findByVideoInfoId(viSaved.getId()).forEach(sv -> streamVideoRepository.deleteById(sv.getId()));

        User userTest = new User();
        userTest.setId(1L);
        userTest.setName("User Test");
        if(userTest.getVideoInfos() == null){
            userTest.setVideoInfos(new HashSet<VideoInfo>());
        }
        //TODO: Refactorizar en servicio StreamService para guardar los StreamVideo
        if(streamsVideos != null){
            streamsVideos.forEach(sv -> {
                sv.setVideoInfo(viSaved);
                streamVideoRepository.save(sv);
            });
        }
        //TODO: Refactorizar en servicio StreamService para guardar los StreamAudio
        if(streamsAudios != null){
            streamsAudios.forEach(sa -> {
                sa.setVideoInfo(viSaved);
                streamAudioRepository.save(sa);
            });
        }
        userTest.getVideoInfos().add(viSaved);
        userService.updateOrSaveUser(userTest);
    }

    public List<VideoInfo> getAllVideoInfo(){
        List<VideoInfo> result = videoInfoRepository.findAll();
        result.forEach(vi -> System.out.println(vi.getStreamsAudios().size() + " - " + vi.getStreamsVideos().size()));
        return result;
    }

    public VideoInfo getVideoInfoById(Long id){
        return videoInfoRepository.findById(id).orElse(null);
    }

    public Boolean deteleVideoInfoByName(String name){
        Optional<VideoInfo> videoInfoOptional = videoInfoRepository.findByFileName(name);
        videoInfoOptional.ifPresent(vi -> videoInfoRepository.deleteById(vi.getId()));
        return videoInfoOptional.isPresent();
    }
}
