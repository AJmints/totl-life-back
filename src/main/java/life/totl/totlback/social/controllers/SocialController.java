package life.totl.totlback.social.controllers;

import life.totl.totlback.security.utils.jwt.JWTGenerator;
import life.totl.totlback.social.models.SocialUserHubEntity;
import life.totl.totlback.social.models.TurtleRequestEntity;
import life.totl.totlback.social.models.dtos.FriendRequestDTO;
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

    @Autowired
    private SocialController(JWTGenerator jwtGenerator, UserEntityRepository userEntityRepository, TurtleRequestRepository turtleRequestRepository) {
        this.jwtGenerator = jwtGenerator;
        this.userEntityRepository = userEntityRepository;
        this.turtleRequestRepository = turtleRequestRepository;
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

    @PostMapping(value = "/request-friend")
    public ResponseEntity<?> requestFriend(@RequestHeader("auth-token") String token, @RequestBody FriendRequestDTO requestDTO) {
        try {
            if (!jwtGenerator.validateToken(token.substring(7, token.length()))){
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

            if (turtleRequestRepository.existsByRequesterAndRequested(requester.get().getSocialHub(), requested.get().getSocialHub()) || turtleRequestRepository.existsByRequesterAndRequested(requested.get().getSocialHub(), requester.get().getSocialHub())) {
                return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage( requester.get().getUserName(), "exists-requester"));
            } else {
                turtleRequestRepository.save(new TurtleRequestEntity(requestDTO.getStatus(), requester.get().getSocialHub(), requested.get().getSocialHub(), requester.get().getSocialHub()));
                return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage("added", "success"));
            }

        }
        /** if TurtleRequest contains requester/requested = return request exists (friend request object returned
         *  else new request - save as pending */
    }

    @PostMapping(value = "/handle-friend-request-action")
    public ResponseEntity<?> friendRequestActionHandler(@RequestHeader("auth-token") String token, @RequestBody FriendRequestDTO requestDTO) {
        try {
            if (!jwtGenerator.validateToken(token.substring(7, token.length()))){
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e);
        }

        /** requester/requested is on TurtleRequest requester/requested */

        /** Should this be a function that lives in TurtleRequest to handle this? Yes
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


        return ResponseEntity.status(HttpStatus.OK).body("added");
    }

}
