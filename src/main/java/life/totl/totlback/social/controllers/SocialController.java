package life.totl.totlback.social.controllers;

import life.totl.totlback.security.utils.jwt.JWTGenerator;
import life.totl.totlback.social.models.SocialUserHubEntity;
import life.totl.totlback.social.models.TurtleRequestEntity;
import life.totl.totlback.social.models.dtos.FriendRequestDTO;
import life.totl.totlback.social.models.dtos.TurtleRequestStatusDTO;
import life.totl.totlback.social.repository.SocialUserHubRepository;
import life.totl.totlback.social.repository.TurtleRequestRepository;
import life.totl.totlback.users.models.UserEntity;
import life.totl.totlback.users.models.response.ResponseMessage;
import life.totl.totlback.users.repository.UserEntityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "http://localhost:3000", maxAge = 3600, allowCredentials = "true")
@RequestMapping(value = "/social")
public class SocialController {

    private final JWTGenerator jwtGenerator;
    private final UserEntityRepository userEntityRepository;
    private final TurtleRequestRepository turtleRequestRepository;
    private final SocialUserHubRepository socialUserHubRepository;

    @Autowired
    private SocialController(JWTGenerator jwtGenerator, UserEntityRepository userEntityRepository, TurtleRequestRepository turtleRequestRepository, SocialUserHubRepository socialUserHubRepository) {
        this.jwtGenerator = jwtGenerator;
        this.userEntityRepository = userEntityRepository;
        this.turtleRequestRepository = turtleRequestRepository;
        this.socialUserHubRepository = socialUserHubRepository;
    }

    @GetMapping(value = "/addSocial") // make callable with special button
    public ResponseEntity<?> addSocial() {

        List<UserEntity> users = userEntityRepository.findAll();
        HashMap<String, String> status = new HashMap<>();

        for (UserEntity user : users) {
            if (Objects.equals(user.getSocialHub(), null)) {
                user.setSocialHub(new SocialUserHubEntity(user));
                userEntityRepository.save(user);
                status.put(user.getUserName(), "Added");
            } else {
                status.put(user.getUserName(), "Already has");
            }

        }

        return ResponseEntity.status(HttpStatus.OK).body(status);
    }

    @GetMapping(value = "/user-friend-list/{userid}")
    public ResponseEntity<?> usersFriendList(@RequestHeader("auth-token") String token, @PathVariable("id") long userId) {
        try {
            if (!jwtGenerator.validateToken(token.substring(7, token.length()))) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e);
        }

        /** user.getFriendList() one more time
         * getAllTurtleRequest if TurtleRequest.requester/requested.includeUser && requestStatus == 'pending'
         * */

        return ResponseEntity.status(HttpStatus.OK).body("added");
    }

    @GetMapping(value = "/request-status/{userId}/{userName}")
    public ResponseEntity<?> requestStatus(@PathVariable("userId") long userId, @PathVariable("userName") String userName) {

        Optional<UserEntity> requester = userEntityRepository.findById(userId);
        Optional<UserEntity> requested = Optional.ofNullable(userEntityRepository.findByUserName(userName));

        if (requester.isEmpty() || requested.isEmpty()) {
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage("Invalid user", "failed"));
        }

        if (turtleRequestRepository.existsByRequesterAndRequested(requester.get().getSocialHub(), requested.get().getSocialHub())) {

            TurtleRequestEntity request = turtleRequestRepository.findByRequesterAndRequested(requester.get().getSocialHub(), requested.get().getSocialHub());
            return ResponseEntity.status(HttpStatus.OK).body(new TurtleRequestStatusDTO("success", request.getStatus(), request.getRequester().getUser().getUserName(), request.getRequested().getUser().getUserName()));

        } else if (turtleRequestRepository.existsByRequesterAndRequested(requested.get().getSocialHub(), requester.get().getSocialHub())) {

            TurtleRequestEntity request = turtleRequestRepository.findByRequesterAndRequested(requested.get().getSocialHub(), requester.get().getSocialHub());
            return ResponseEntity.status(HttpStatus.OK).body(new TurtleRequestStatusDTO("success", request.getStatus(), request.getRequester().getUser().getUserName(), request.getRequested().getUser().getUserName()));

        } else {

            return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage("success", "empty"));

        }
    }

    @PostMapping(value = "/request-friend")
    public ResponseEntity<?> requestFriend(@RequestHeader("auth-token") String token, @RequestBody FriendRequestDTO requestDTO) {
        try {
            if (!jwtGenerator.validateToken(token.substring(7, token.length()))) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }

        Optional<UserEntity> requester = Optional.ofNullable(userEntityRepository.findByUserName(requestDTO.getRequester()));
        Optional<UserEntity> requested = Optional.ofNullable(userEntityRepository.findByUserName(requestDTO.getRequested()));

        if (requester.isEmpty() || requested.isEmpty()) {
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage("Invalid user", "failed"));
        } else {

            if (turtleRequestRepository.existsByRequesterAndRequested(requester.get().getSocialHub(), requested.get().getSocialHub())) {
                TurtleRequestEntity update = turtleRequestRepository.findByRequesterAndRequested(requester.get().getSocialHub(), requested.get().getSocialHub());
                if (Objects.equals(update.getStatus(), "cancel")) {
                    update.setStatus("pending");
                    update.setRequester(requester.get().getSocialHub());
                    update.setRequested(requested.get().getSocialHub());
                    update.setLastActor(requester.get().getSocialHub());
                    turtleRequestRepository.save(update);
                } else if (Objects.equals(update.getStatus(), "decline")) {
                    // This situation might not ever happen, in which case, this is a block that can be removed.
                    update.setStatus("pending");
                    update.setRequester(requester.get().getSocialHub());
                    update.setRequested(requested.get().getSocialHub());
                    update.setLastActor(requester.get().getSocialHub());
                    turtleRequestRepository.save(update);
                }
                return ResponseEntity.status(HttpStatus.OK).body(update.getFullFriendRequest());
            } else if (turtleRequestRepository.existsByRequesterAndRequested(requested.get().getSocialHub(), requester.get().getSocialHub())) {
                TurtleRequestEntity update = turtleRequestRepository.findByRequesterAndRequested(requested.get().getSocialHub(), requester.get().getSocialHub());
                if (Objects.equals(update.getStatus(), "cancel")) {

                    update.setStatus("pending");
                    update.setRequester(requester.get().getSocialHub());
                    update.setRequested(requested.get().getSocialHub());
                    update.setLastActor(requester.get().getSocialHub());
                    turtleRequestRepository.save(update);
                } else if (Objects.equals(update.getStatus(), "decline")) {
                    update.setStatus("pending");
                    update.setRequester(requester.get().getSocialHub());
                    update.setRequested(requested.get().getSocialHub());
                    update.setLastActor(requester.get().getSocialHub());
                    turtleRequestRepository.save(update);
                }
                return ResponseEntity.status(HttpStatus.OK).body(update.getFullFriendRequest());
            }else {
                turtleRequestRepository.save(new TurtleRequestEntity(requestDTO.getStatus(), requester.get().getSocialHub(), requested.get().getSocialHub(), requester.get().getSocialHub()));
                return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage("added", "success"));
            }

        }
    }

    @PostMapping(value = "/handle-friend-request-action")
    public ResponseEntity<?> friendRequestActionHandler(@RequestHeader("auth-token") String token, @RequestBody FriendRequestDTO requestDTO) {
        try {
            if (!jwtGenerator.validateToken(token.substring(7, token.length()))) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e);
        }

        Optional<UserEntity> requester = Optional.ofNullable(userEntityRepository.findByUserName(requestDTO.getRequester()));
        Optional<UserEntity> requested = Optional.ofNullable(userEntityRepository.findByUserName(requestDTO.getRequested()));

        if (requester.isEmpty() || requested.isEmpty()) {
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage("Invalid user", "failed"));
        } else {

            try {

                /* Should this be a function that lives in TurtleRequest to handle this? Yes
                 *
                 * If requestDTO.status = accept
                 *  TurtleRequest.setStatus("accept")
                 *  user.getSocialHub.getFriendList.add(requester/requested)
                 *  saveUser
                 *
                 * If requestDTO.status = decline
                 *  TurtleRequest.setStatus("decline")
                 *
                 * If requestDTO.status = cancel
                 * TurtleRequest.setStatus("cancel")
                 *
                 * If requestDTO.status = unfriend
                 * TurtleRequest.setStatus("unfriend")
                 * user.getSocialHub.getFriendList.remove(requester/requested)
                 * saveUser
                 *
                 * */

                /** Make the Accept and Delcine handling here */

                if (Objects.equals(requestDTO.getStatus(), "cancel") || Objects.equals(requestDTO.getStatus(), "accept") || Objects.equals(requestDTO.getStatus(), "decline")) {
                    if (turtleRequestRepository.existsByRequesterAndRequested(requester.get().getSocialHub(), requested.get().getSocialHub())) {

                        TurtleRequestEntity request = turtleRequestRepository.findByRequesterAndRequested(requester.get().getSocialHub(), requested.get().getSocialHub());
                        request.setStatus(requestDTO.getStatus());
                        request.setLastActor(requester.get().getSocialHub());
                        turtleRequestRepository.save(request);
                        return ResponseEntity.status(HttpStatus.OK).body(request.getFullFriendRequest());

                    } else if (turtleRequestRepository.existsByRequesterAndRequested(requested.get().getSocialHub(), requester.get().getSocialHub())) {

                        TurtleRequestEntity request = turtleRequestRepository.findByRequesterAndRequested(requested.get().getSocialHub(), requester.get().getSocialHub());
                        request.setStatus(requestDTO.getStatus());
                        request.setLastActor(requester.get().getSocialHub());
                        turtleRequestRepository.save(request);
                        return ResponseEntity.status(HttpStatus.OK).body(request.getFullFriendRequest());
                    } else {

                        return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage("success", "empty"));

                    }

                }


            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
            }

            return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage("success", "exited loop"));

        }


//        return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage("Note"));
    }
}
