package life.totl.totlback.users.controllers;

import life.totl.totlback.security.utils.jwt.JWTGenerator;
import life.totl.totlback.users.models.ProfilePictureEntity;
import life.totl.totlback.users.models.UserEntity;
import life.totl.totlback.users.models.response.ResponseMessage;
import life.totl.totlback.users.repository.ProfilePictureRepository;
import life.totl.totlback.users.repository.UserEntityRepository;
import life.totl.totlback.users.utils.ImageUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "http://localhost:3000", maxAge = 3600, allowCredentials = "true")
@RequestMapping(value = "/profile")
public class UserProfileController {

    private final JWTGenerator jwtGenerator;
    private final ProfilePictureRepository profilePictureRepository;
    private final UserEntityRepository userEntityRepository;

    @Autowired
    private UserProfileController(JWTGenerator jwtGenerator, ProfilePictureRepository profilePictureRepository, UserEntityRepository userEntityRepository) {
        this.jwtGenerator = jwtGenerator;
        this.profilePictureRepository = profilePictureRepository;
        this.userEntityRepository = userEntityRepository;
    }

    @PostMapping(value = "/upload-pfp")
    public ResponseEntity<?> uploadPFP(@RequestParam("image") MultipartFile file) throws IOException {

        if (profilePictureRepository.findByUserId(file.getOriginalFilename()).isPresent()) {
            Optional<ProfilePictureEntity> remove = profilePictureRepository.findByUserId(file.getOriginalFilename());
            remove.ifPresent(profilePictureEntity -> profilePictureRepository.deleteById(profilePictureEntity.getId()));
        }

        profilePictureRepository.save(ProfilePictureEntity.builder()
                .userId(file.getOriginalFilename())
                .type(file.getContentType())
                .image(ImageUtility.compressImage(file.getBytes())).build());

        ProfilePictureEntity pic = ProfilePictureEntity.builder().id(1).userId("0").type("image/jpg").image(file.getBytes()).build();

        return ResponseEntity.status(HttpStatus.OK)
                .body(pic);
    }

    @GetMapping(path = {"user-pfp/{userId}"})
    public ResponseEntity<?> getUserPFP(@PathVariable("userId") String userId) throws IOException {
        Optional<UserEntity> user = userEntityRepository.findById(Long.valueOf(userId));
        Optional<ProfilePictureEntity> dbImage = profilePictureRepository.findByUserId(userId);
        if (user.isEmpty() || dbImage.isEmpty()) {
            ResponseMessage responseMessage = new ResponseMessage(null);
            return ResponseEntity.status(HttpStatus.OK).body(responseMessage);
        }
        return ResponseEntity.status(HttpStatus.OK).body(ProfilePictureEntity.builder().id(dbImage.get().getId()).userId(user.get().getUserName()).type(dbImage.get().getType()).image(ImageUtility.decompressImage(dbImage.get().getImage())).build());
    }
}
