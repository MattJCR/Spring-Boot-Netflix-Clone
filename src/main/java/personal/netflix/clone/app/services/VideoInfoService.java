package personal.netflix.clone.app.services;

import personal.netflix.clone.app.entities.VideoInfo;

import java.util.List;

public interface VideoInfoService {
    public void saveOrUpdateVideoInfo(VideoInfo videoInfo);
    public List<VideoInfo> getAllVideoInfo();
    public VideoInfo getVideoInfoById(Long id);
    public Boolean deteleVideoInfoByName(String name);
}
