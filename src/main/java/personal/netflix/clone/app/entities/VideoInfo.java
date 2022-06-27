package personal.netflix.clone.app.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
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
    private String title;
    private String fileName;
    private String mediaType;
    private String autor;
    private Long size;
    private Integer bitRate;
    @ManyToMany(mappedBy = "videoInfos")
    @JsonIgnore
    private Set<User> users = new HashSet<>();
}
