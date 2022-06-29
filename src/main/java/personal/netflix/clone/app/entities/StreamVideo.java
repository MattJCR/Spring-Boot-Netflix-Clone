package personal.netflix.clone.app.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StreamVideo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String codecId;
    private String name;
    private String timeBase;
    private Integer width;
    private Integer height;
    private String pixelFormat;
    private Integer frameSize;
    private Integer frameCount;
    private String language;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "id_videoinfo", nullable = false)
    @JsonIgnore
    private VideoInfo videoInfo;
}
