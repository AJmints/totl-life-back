package life.totl.totlback.social.controllers;

import life.totl.totlback.security.utils.jwt.JWTGenerator;
import life.totl.totlback.social.models.SocialUserHubEntity;
import life.totl.totlback.social.models.TurtleRequestEntity;
import life.totl.totlback.social.models.dtos.*;
import life.totl.totlback.social.repository.SocialUserHubRepository;
import life.totl.totlback.social.repository.TurtleRequestRepository;
import life.totl.totlback.users.models.ProfilePictureEntity;
import life.totl.totlback.users.models.UserEntity;
import life.totl.totlback.users.models.dtos.UserContextDTO;
import life.totl.totlback.users.models.response.ResponseMessage;
import life.totl.totlback.users.repository.UserEntityRepository;
import life.totl.totlback.users.utils.ImageUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

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

    @GetMapping(value = "/user-friend-list/{userName}/{callType}")
    public ResponseEntity<?> usersFriendList(@RequestHeader("auth-token") String token, @PathVariable("userName") String userName, @PathVariable("callType") String usersView) {
        try {
            if (!jwtGenerator.validateToken(token.substring(7, token.length()))) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e);
        }

        UserEntity user = userEntityRepository.findByUserName(userName);
        List<FriendListDTO> userFriends = user.getSocialHub().getUsersFriendList();

        if (Objects.equals(usersView, "1")) {

            List<TurtleRequestEntity> turtleRequest = turtleRequestRepository.findAllByRequested(user.getSocialHub());
            List<UserTurtleRequestActionDTO> turtleResponse = new ArrayList<>();

            for (TurtleRequestEntity name : turtleRequest) {
                if (Objects.equals(name.getStatus(), "pending")) {
                UserTurtleRequestActionDTO turtleRequestDetails;
                UserEntity check = userEntityRepository.findByUserName(name.getRequester().getUser().getUserName());
                if (Arrays.equals(check.getUserPFP().getImage(), new byte[256])) {
                    turtleRequestDetails = new UserTurtleRequestActionDTO(name.getRequester().getUser().getUserName(), name.getRequested().getUser().getUserName());
                } else {
                    turtleRequestDetails = new UserTurtleRequestActionDTO(name.getRequester().getUser().getUserName(), name.getRequested().getUser().getUserName(), ProfilePictureEntity.builder().image(ImageUtility.decompressImage(check.getUserPFP().getImage())).build());
                }
                turtleResponse.add(turtleRequestDetails);
                }
            }

            return ResponseEntity.status(HttpStatus.OK).body(new UserRequestsFriendsDTO(userFriends, turtleResponse));

        } else if (Objects.equals(usersView, "2")) {
            return ResponseEntity.status(HttpStatus.OK).body(new UserRequestsFriendsDTO(userFriends));
        }
        /** user.getFriendList() one more time
         * getAllTurtleRequest if TurtleRequest.requester/requested.includeUser && requestStatus == 'pending'
         * */

        return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage("error", "something went wrong"));
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

    // Bring in 2 emtpy spring boot projs, 1 will be producer, 2nd is consumer. Producer will send message over Kafka to Consumer, and comsumer will print message from producer.
    // cannot use controller or api to send message - challenge.
    // can use rest api on producer app to enter the message I want to send

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
                } else if (Objects.equals(update.getStatus(), "unfriend")) {
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
                } else if (Objects.equals(update.getStatus(), "unfriend")) {
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

                if (Objects.equals(requestDTO.getStatus(), "cancel") || Objects.equals(requestDTO.getStatus(), "accept") || Objects.equals(requestDTO.getStatus(), "decline") || Objects.equals(requestDTO.getStatus(), "unfriend")) {
                    if (turtleRequestRepository.existsByRequesterAndRequested(requester.get().getSocialHub(), requested.get().getSocialHub())) {

                        TurtleRequestEntity request = turtleRequestRepository.findByRequesterAndRequested(requester.get().getSocialHub(), requested.get().getSocialHub());

                        if (Objects.equals(requestDTO.getStatus(), "cancel") && Objects.equals(request.getStatus(), "accept") || Objects.equals(requestDTO.getStatus(), "cancel") && Objects.equals(request.getStatus(), "decline")) {
                            return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage("success", "empty"));
                        } else if (Objects.equals(request.getStatus(), "cancel") && Objects.equals(requestDTO.getStatus(), "accept") || Objects.equals(requestDTO.getStatus(), "cancel") && Objects.equals(requestDTO.getStatus(), "decline")) {
                            return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage("success", "empty"));
                        }

                        request.setStatus(requestDTO.getStatus());
                        request.setLastActor(requester.get().getSocialHub());
                        turtleRequestRepository.save(request);

                        if (Objects.equals(requestDTO.getStatus(), "accept")) {
                            requester.get().getSocialHub().getFriendList().add(requested.get());
                            requested.get().getSocialHub().getFriendList().add(requester.get());
                            socialUserHubRepository.save(requester.get().getSocialHub());
                            socialUserHubRepository.save(requested.get().getSocialHub());
                        } else if (Objects.equals(requestDTO.getStatus(), "unfriend")) {
                            requester.get().getSocialHub().getFriendList().remove(requested.get());
                            requested.get().getSocialHub().getFriendList().remove(requester.get());
                            socialUserHubRepository.save(requester.get().getSocialHub());
                            socialUserHubRepository.save(requested.get().getSocialHub());
                        }

                        return ResponseEntity.status(HttpStatus.OK).body(request.getFullFriendRequest());

                    } else if (turtleRequestRepository.existsByRequesterAndRequested(requested.get().getSocialHub(), requester.get().getSocialHub())) {

                        TurtleRequestEntity request = turtleRequestRepository.findByRequesterAndRequested(requested.get().getSocialHub(), requester.get().getSocialHub());

                        if (Objects.equals(requestDTO.getStatus(), "cancel") && Objects.equals(request.getStatus(), "accept")) {
                            return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage("success", "empty"));
                        } else if (Objects.equals(request.getStatus(), "cancel") && Objects.equals(requestDTO.getStatus(), "accept")) {
                            return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage("success", "empty"));
                        }

                        request.setStatus(requestDTO.getStatus());
                        request.setLastActor(requester.get().getSocialHub());
                        turtleRequestRepository.save(request);

                        if (Objects.equals(requestDTO.getStatus(), "accept")) {
                            requester.get().getSocialHub().getFriendList().add(requested.get());
                            requested.get().getSocialHub().getFriendList().add(requester.get());
                            socialUserHubRepository.save(requester.get().getSocialHub());
                            socialUserHubRepository.save(requested.get().getSocialHub());
                        } else if (Objects.equals(requestDTO.getStatus(), "unfriend")) {
                            requester.get().getSocialHub().getFriendList().remove(requested.get());
                            requested.get().getSocialHub().getFriendList().remove(requester.get());
                            socialUserHubRepository.save(requester.get().getSocialHub());
                            socialUserHubRepository.save(requested.get().getSocialHub());
                        }

                        return ResponseEntity.status(HttpStatus.OK).body(request.getFullFriendRequest());
                    } else {

                        System.out.println("Oh no");
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
