package life.totl.totlback.users.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "profile_picture")
public class ProfilePictureEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String userId;
    @OneToOne(mappedBy = "userPFP")
    @JsonIgnore
    private UserEntity user;
    private String type;
    @Column(columnDefinition = "MEDIUMBLOB NOT NULL")
    @Builder.Default
    private byte[] image = new byte[256];

    public ProfilePictureEntity(String userId, UserEntity user) {
        this.userId = userId;
        this.user = user;
        this.image = new byte[256];
    }

}
