package life.totl.totlback.users.controllers;

import life.totl.totlback.backpack.models.BackPackEntity;
import life.totl.totlback.backpack.repository.BackPackEntityRepository;
import life.totl.totlback.logs.repositories.LogsEntityRepository;
import life.totl.totlback.security.utils.jwt.JWTGenerator;
import life.totl.totlback.users.models.ProfilePictureEntity;
import life.totl.totlback.users.models.UserEntity;
import life.totl.totlback.users.models.dtos.UserContextDTO;
import life.totl.totlback.users.models.response.ResponseMessage;
import life.totl.totlback.users.models.response.UserProfileInfo;
import life.totl.totlback.users.repository.ProfilePictureRepository;
import life.totl.totlback.users.repository.UserEntityRepository;
import life.totl.totlback.users.utils.ImageUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@RestController
@CrossOrigin(origins = "http://localhost:3000", maxAge = 3600, allowCredentials = "true")
@RequestMapping(value = "/profile")
public class UserProfileController {

    private final JWTGenerator jwtGenerator;
    private final ProfilePictureRepository profilePictureRepository;
    private final UserEntityRepository userEntityRepository;
    private final LogsEntityRepository logsEntityRepository;
    private final BackPackEntityRepository backPackEntityRepository;


    @Autowired
    private UserProfileController(JWTGenerator jwtGenerator, ProfilePictureRepository profilePictureRepository, UserEntityRepository userEntityRepository, LogsEntityRepository logsEntityRepository,
                                  BackPackEntityRepository backPackEntityRepository) {
        this.jwtGenerator = jwtGenerator;
        this.profilePictureRepository = profilePictureRepository;
        this.userEntityRepository = userEntityRepository;
        this.logsEntityRepository = logsEntityRepository;
        this.backPackEntityRepository = backPackEntityRepository;
    }

    @PostMapping(value = "/upload-pfp")
    public ResponseEntity<?> uploadPFP(@RequestParam("image") MultipartFile file) throws IOException {

        Optional<ProfilePictureEntity> present = profilePictureRepository.findByUserId(file.getOriginalFilename());
        present.get().setImage(ImageUtility.compressImage(file.getBytes()));
        present.get().setType(file.getContentType());

        profilePictureRepository.save(present.get());

        return ResponseEntity.status(HttpStatus.OK)
                .body(present.get());
    }

    @GetMapping(path = {"user-pfp/{userId}"})
    public ResponseEntity<?> getUserPFP(@PathVariable("userId") String userId) throws IOException {

        /* TODO: this route might be shutting down... pending decision, currently not in use. */

        Optional<UserEntity> user = userEntityRepository.findById(Long.valueOf(userId));
        UserProfileInfo responseMessage;
        responseMessage = user.map(userEntity -> new UserProfileInfo(
                ProfilePictureEntity.builder().image(ImageUtility.decompressImage(userEntity.getUserPFP().getImage())).build(), userEntity.getUserName())).orElseGet(() -> new UserProfileInfo(null, "User does not exist"));

        return ResponseEntity.status(HttpStatus.OK).body(responseMessage);

    }

    @GetMapping(path = {"userInfo/{userId}"})
    public ResponseEntity<?> getUserContextInfo(@PathVariable("userId") String userId) {

        Optional<UserEntity> user;
        try {

            user = userEntityRepository.findById(Long.valueOf(userId));

        } catch (NumberFormatException e) {
            try {

                user = Optional.ofNullable(userEntityRepository.findByUserName(userId));

            } catch (Error err) {
                ResponseMessage response = new ResponseMessage("failed", "User not present");
                return ResponseEntity.status(HttpStatus.OK).body(response);
            }

            if (user.isEmpty()) {
                ResponseMessage response = new ResponseMessage("failed", "User not present");
                return ResponseEntity.status(HttpStatus.OK).body(response);
            }
        }

        List<String> followingLogs = new ArrayList<>();
        // TODO: Change how user saves their logs to their profile, this is temp to send log follow list (said Mar, 18, 2024)
        for (Long id : user.get().getFollowingLogs()) {
            if (logsEntityRepository.findById(id).isPresent()) {
                followingLogs.add(logsEntityRepository.findById(id).get().getLogName());
            }
        }

        UserContextDTO newContext;
        if (Arrays.equals(user.get().getUserPFP().getImage(), new byte[256])){
            newContext = new UserContextDTO(user.get().getUserName(),user.get().getId(),user.get().isAccountVerified(), followingLogs, user.get().getUserMadeLogs());
        } else {
            newContext = new UserContextDTO(user.get().getUserName(), user.get().getId(),user.get().isAccountVerified(), ProfilePictureEntity.builder().image(ImageUtility.decompressImage(user.get().getUserPFP().getImage())).build(), followingLogs, user.get().getUserMadeLogs());
        }


        return ResponseEntity.status(HttpStatus.OK).body(newContext);
    }
}
