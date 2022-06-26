package personal.netflix.clone.app.ServicesImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import personal.netflix.clone.app.entities.VideoInfo;
import personal.netflix.clone.app.repositories.VideoInfoRepository;
import personal.netflix.clone.app.services.VideoInfoService;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;


@Service
public class VideoInfoServiceImpl implements VideoInfoService {

    @Autowired
    VideoInfoRepository videoInfoRepository;

    @Transactional
    public void saveOrUpdateVideoInfo(VideoInfo videoInfo){
        Optional<VideoInfo> videoInfoOptional = videoInfoRepository.findByFileName(videoInfo.getFileName());
        videoInfoOptional.ifPresent(vi -> videoInfo.setId(vi.getId()));
        System.out.println(videoInfo.toString());
        videoInfoRepository.save(videoInfo);
    }

    public List<VideoInfo> getAllVideoInfo(){
        return videoInfoRepository.findAll();
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
