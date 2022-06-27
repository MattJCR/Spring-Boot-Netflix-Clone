package personal.netflix.clone.app.entities;

import lombok.*;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @Fetch(FetchMode.JOIN)
    @JoinTable(
            name = "users_videoinfo",
            joinColumns = { @JoinColumn(name = "id_users") },
            inverseJoinColumns = { @JoinColumn(name = "id_videoinfo") }
    )
    private Set<VideoInfo> videoInfos = new HashSet<>();
}
