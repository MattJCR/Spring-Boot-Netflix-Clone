package personal.netflix.clone.app.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class VideoInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long duration;
    private String durationFormat;
    private String url;
    private String fileName;
    private String fileExtension;
    private String mimeType;
    private String formatLongName;
    private String formatName;
    private String autor;
    private Long size;
    private Integer bitRate;
    private String encoder;
    private String majorBrand;
    private String minorVersion;
    private String compatibleBrands;

    @OneToMany(mappedBy = "videoInfo", fetch = FetchType.EAGER,
            cascade = CascadeType.ALL)
    private Set<StreamVideo> streamsVideos;

    @OneToMany(mappedBy = "videoInfo", fetch = FetchType.EAGER,
            cascade = CascadeType.ALL)
    private Set<StreamAudio> streamsAudios;

    @ManyToMany(mappedBy = "videoInfos")
    @JsonIgnore
    private Set<User> users = new HashSet<>();
}
