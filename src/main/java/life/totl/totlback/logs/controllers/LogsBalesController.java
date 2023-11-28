package life.totl.totlback.logs.controllers;

import life.totl.totlback.logs.models.BalesEntity;
import life.totl.totlback.logs.models.CommentEntity;
import life.totl.totlback.logs.models.LogsEntity;
import life.totl.totlback.logs.models.UserLogsBalesEntity;
import life.totl.totlback.logs.models.dto.*;
import life.totl.totlback.logs.models.dto.updownvotebutton.UpDownVoteDTO;
import life.totl.totlback.logs.repositories.BalesEntityRepository;
import life.totl.totlback.logs.repositories.CommentEntityRepository;
import life.totl.totlback.logs.repositories.LogsEntityRepository;
import life.totl.totlback.logs.repositories.UserLogsBalesEntityRepository;
import life.totl.totlback.security.utils.jwt.JWTGenerator;
import life.totl.totlback.users.models.UserEntity;
import life.totl.totlback.users.models.response.ResponseMessage;
import life.totl.totlback.users.repository.UserEntityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin
@RequestMapping(value = "/logs")
public class LogsBalesController {

    private final JWTGenerator jwtGenerator;
    private final UserLogsBalesEntityRepository userLogsBalesEntityRepository;
    private final UserEntityRepository userEntityRepository;
    private final LogsEntityRepository logsEntityRepository;
    private final BalesEntityRepository balesEntityRepository;
    private final CommentEntityRepository commentEntityRepository;

    @Autowired
    private LogsBalesController(JWTGenerator jwtGenerator, UserLogsBalesEntityRepository userLogsBalesEntityRepository, UserEntityRepository userEntityRepository, LogsEntityRepository logsEntityRepository, BalesEntityRepository balesEntityRepository, CommentEntityRepository commentEntityRepository) {
        this.jwtGenerator = jwtGenerator;
        this.userLogsBalesEntityRepository = userLogsBalesEntityRepository;
        this.userEntityRepository = userEntityRepository;
        this.logsEntityRepository = logsEntityRepository;
        this.balesEntityRepository = balesEntityRepository;
        this.commentEntityRepository = commentEntityRepository;
    }

    @GetMapping(value = "/all-logs-for-drop-down")
    public ResponseEntity<?> getAllLogs() {
        List<String> logNames = new ArrayList<>();
        for (LogsEntity name : logsEntityRepository.findAll()) {
            logNames.add(name.getLogName());
        }
        return ResponseEntity.status(HttpStatus.OK).body(new LogNamesDTO("success", logNames));
    }

    @GetMapping(value = "/all-bales-in-log/{log}/{baleIndex}")
    public ResponseEntity<?> getAllBalesInLog(@PathVariable("log") String log, @PathVariable("baleIndex") int baleIndex) {

        Optional<LogsEntity> logTarget = logsEntityRepository.findByLogName(log);
        if (logTarget.isPresent()) {

            int indexRange = baleIndex * 10;
            if (indexRange > logTarget.get().getAllLogBales().size()) {
                return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage("invalid", "The number used to find the index is invalid, please enter a valid index"));
            }

            List<BaleDTO> topBales = new ArrayList<>();
            int mostRecent = logTarget.get().getAllLogBales().size() - indexRange;
            int index = mostRecent;

            if (mostRecent > 10) {
                for (int i = 0; i < 10; i++) {
                    BaleDTO send = logTarget.get().getAllLogBales().get(mostRecent - 1).getBaleInformation();
                    topBales.add(send);
                    mostRecent--;
                }
            } else {
                for (int i = 0; i < index; i++) {
                    BaleDTO send = logTarget.get().getAllLogBales().get(mostRecent - 1).getBaleInformation();
                    topBales.add(send);
                    mostRecent--;
                }
            }

            return ResponseEntity.status(HttpStatus.OK).body(new LogBalesDTO("success", logTarget.get().getLogDescription(), logTarget.get().getAllLogBales().size(), topBales));
        }
        ResponseMessage response = new ResponseMessage("Not Found");
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping(value = "/create-log")
    public ResponseEntity<?> createALog(@RequestBody LogsEntityDTO logsEntityDTO, @RequestHeader("auth-token") String token) {

        if (!jwtGenerator.validateToken(token.substring(7, token.length()))) {
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage("bad token", "Danger, respond with logout"));
            /** TODO: Fix by sending user to logout */
        }

        if (logsEntityRepository.existsByLogName(logsEntityDTO.getLogName())) {
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage("taken", "That log name is taken, please select a different name"));
        }

        /* Get the user who made the post */
        Optional<UserEntity> user = userEntityRepository.findById(logsEntityDTO.getUser());

        if (user.isPresent()) {
            if (user.get().getUserLogsBalesEntity().getLogsEntities().size() == 3) {
                return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage("max", "You have created the maximum amount of logs. You're log was not created"));
            }

            /* Get the record of all logs, bales, and comments created by user */
            Optional<UserLogsBalesEntity> logsBales = userLogsBalesEntityRepository.findById(user.get().getUserLogsBalesEntity().getId());
            /* Create new Log and assign ownership to the user who sent the details */
            LogsEntity logsEntity = new LogsEntity(logsBales.get(), logsEntityDTO.getLogName().toLowerCase(), logsEntityDTO.getIntroduction());
            /* save the new log to the repository */
            logsEntityRepository.save(logsEntity);
            /* Add log to users record of logs created by them (This might not be needed) */
            logsBales.get().getLogsEntities().add(logsEntity);
            /* Saving the updated log just in case (might not need this) */
            userLogsBalesEntityRepository.save(logsBales.get());
        }


        return ResponseEntity.status(HttpStatus.OK).body(logsEntityDTO);
    }

    @PostMapping(value = "/downvote-post")
    public ResponseEntity<?> downVoteAPost(@RequestBody UpDownVoteDTO upDownVoteDTO, @RequestHeader("auth-token") String token) {
        if (!jwtGenerator.validateToken(token.substring(7, token.length()))) {
            System.out.println("Danger, respond with logout");
            /** TODO: Fix by sending logout action */
        }

        Optional<BalesEntity> bale = balesEntityRepository.findById(upDownVoteDTO.getBaleId());
        if (bale.isPresent()) {

            if (bale.get().getDownVoteIds().size() == 0 || !bale.get().getDownVoteIds().contains(upDownVoteDTO.getUserId())) {
                bale.get().getDownVoteIds().add(upDownVoteDTO.getUserId());
                balesEntityRepository.save(bale.get());

                if (bale.get().getUpVoteIds().contains(upDownVoteDTO.getUserId())) {
                    bale.get().getUpVoteIds().remove(upDownVoteDTO.getUserId());
                    balesEntityRepository.save(bale.get());
                    return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage("success", "inc-dec"));
                }

                return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage("success", "inc"));

            } else {
                bale.get().getDownVoteIds().remove(upDownVoteDTO.getUserId());
                balesEntityRepository.save(bale.get());
                return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage("success", "dec"));
            }

        }

        return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage("failed", "This bale does not exist."));
    }


    @PostMapping(value = "/upvote-post")
    public ResponseEntity<?> upVoteAPost(@RequestBody UpDownVoteDTO upDownVoteDTO, @RequestHeader("auth-token")String token) {
        if (!jwtGenerator.validateToken(token.substring(7, token.length()))) {
            System.out.println("Danger, respond with logout");
            /** TODO: Fix by sending logout action */
        }

        Optional<BalesEntity> bale = balesEntityRepository.findById(upDownVoteDTO.getBaleId());
        if (bale.isPresent()) {

            if (bale.get().getUpVoteIds().size() == 0 || !bale.get().getUpVoteIds().contains(upDownVoteDTO.getUserId())) {
                bale.get().getUpVoteIds().add(upDownVoteDTO.getUserId());
                balesEntityRepository.save(bale.get());

                if (bale.get().getDownVoteIds().contains(upDownVoteDTO.getUserId())) {
                    bale.get().getDownVoteIds().remove(upDownVoteDTO.getUserId());
                    balesEntityRepository.save(bale.get());
                    return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage("success", "inc-dec"));
                }

                return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage("success", "inc"));

            } else {
                bale.get().getUpVoteIds().remove(upDownVoteDTO.getUserId());
                balesEntityRepository.save(bale.get());
                return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage("success", "dec"));
            }


        }
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage("failed", "This bale does not exist."));
    }

    @PostMapping(value = "/add-log-follow")
    public ResponseEntity<?> addLogToFollow(@RequestBody FollowLogDTO followLogDTO, @RequestHeader("auth-token")String token) {
        if (!jwtGenerator.validateToken(token.substring(7, token.length()))) {
            System.out.println("Danger, respond with logout");
            /** TODO: Fix by sending logout */
        }
        if (!logsEntityRepository.existsByLogName(followLogDTO.getLogName())) {
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage("failed", "That log name does not exist."));
        }
        UserLogsBalesEntity addLog = userEntityRepository.findById(followLogDTO.getUserId()).get().getUserLogsBalesEntity();
        if (addLog.getLogFollow().contains(logsEntityRepository.findByLogName(followLogDTO.getLogName()).get().getId())) {
            addLog.getLogFollow().remove(logsEntityRepository.findByLogName(followLogDTO.getLogName()).get().getId());
            userLogsBalesEntityRepository.save(addLog);
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage("unfollow", "You have unfollowed log/" + followLogDTO.getLogName()));
        }

        addLog.getLogFollow().add(logsEntityRepository.findByLogName(followLogDTO.getLogName()).get().getId());
        userLogsBalesEntityRepository.save(addLog);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage("follow", "You are now following log/" + followLogDTO.getLogName()));
    }

    @GetMapping(value = "/user-logs/{userId}/{logName}")
    public ResponseEntity<?> getUserLogs(@PathVariable("userId") long userId, @PathVariable("logName") String logName) {
        List<String> favLogs = new ArrayList<>();
        String status = "absent";

        UserLogsBalesEntity user = userEntityRepository.findById(userId).get().getUserLogsBalesEntity();
        for (Long ids : user.getLogFollow()) {
            Optional<LogsEntity> log = logsEntityRepository.findById(ids);
            if (log.isPresent()) {
                if (log.get().getLogName().equals(logName)) {
                    status = "present";
                }
                favLogs.add(log.get().getLogName());
            }
        }
        LogNamesDTO logNames = new LogNamesDTO(status, favLogs);
        return ResponseEntity.status(HttpStatus.OK).body(logNames);
    }

    @PostMapping(value = "/create-bale")
    public ResponseEntity<?> createNewBale(@RequestBody CreateBaleEntityDTO createBaleEntityDTO, @RequestHeader("auth-token")String token) {
        if (!jwtGenerator.validateToken(token.substring(7, token.length()))){
            System.out.println("Danger, respond with logout");
            /** TODO: Fix by sending logout */
        }

        Optional<UserEntity> user = userEntityRepository.findById(createBaleEntityDTO.getUserId());
        /* Get the record of all logs, bales, and comments created by user */
        Optional<UserLogsBalesEntity> logsBales = userLogsBalesEntityRepository.findById(user.get().getUserLogsBalesEntity().getId());
        Optional<LogsEntity> logsEntity = logsEntityRepository.findByLogName(createBaleEntityDTO.getParentLog());

        if (logsBales.isPresent() && logsEntity.isPresent()) {

            BalesEntity balesEntity = new BalesEntity(logsEntity.get(), logsBales.get(), createBaleEntityDTO.getTitle(), createBaleEntityDTO.getBody());
            balesEntityRepository.save(balesEntity);
            logsBales.get().getBaleEntities().add(balesEntity);
            userLogsBalesEntityRepository.save(logsBales.get());
            BaleDTO response = balesEntity.getBaleInformation();

            return ResponseEntity.status(HttpStatus.OK).body(response);
        }

        return ResponseEntity.status(HttpStatus.OK).body(createBaleEntityDTO);

    }

    @PostMapping(value = "create-comment")
    public ResponseEntity<?> createNewComment(@RequestBody CommentDTO commentDTO, @RequestHeader("auth-token")String token) {
        if (!jwtGenerator.validateToken(token.substring(7, token.length()))){
            System.out.println("Danger, respond with logout");
            /** TODO: Fix by sending logout */
        }

        Optional<UserEntity> userPresent = userEntityRepository.findById(commentDTO.getUser());
        Optional<BalesEntity> balePresent = balesEntityRepository.findById(commentDTO.getBaleId());

        if (userPresent.isPresent() && balePresent.isPresent()) {
            CommentEntity userComment = new CommentEntity(userPresent.get().getUserLogsBalesEntity(), balePresent.get(), commentDTO.getComment());
            commentEntityRepository.save(userComment);
            balePresent.get().getComments().add(userComment);

            CommentResponseDTO response = new CommentResponseDTO(userComment.getId(), commentDTO.getComment(), balePresent.get().getId(), userPresent.get().getUserName(), userPresent.get().getUserPFP().getImage());

            return ResponseEntity.status(HttpStatus.OK).body(response);
        }

        return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage("Ooops, something went wrong. Refresh the page and try sending your message again."));

    }
    @GetMapping(value = "get-bale-comments/{baleId}")
    public ResponseEntity<?> getBaleComments(@PathVariable("baleId") Long baleId) {
        Optional<BalesEntity> bale = balesEntityRepository.findById(baleId);
        List<CommentResponseDTO> response = new ArrayList<>();

        if (bale.isPresent()) {
            for (CommentEntity comments : commentEntityRepository.findAllByParentBale(bale.get())) {
                CommentResponseDTO addComment = comments.getCommentInformation();
                response.add(addComment);
            }
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage("Ooops, something went wrong. Please try again"));

    }

    @GetMapping(value = "/most-recent-bales/{baleIndex}")
    public ResponseEntity<?> mostRecentBalePosts(@PathVariable("baleIndex") int baleIndex) {
        int indexRange = baleIndex * 10;

        if (indexRange > balesEntityRepository.findAll().size()) {
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage("invalid", "The number used to find the index is invalid, please enter a valid index"));
        }

        List<BaleDTO> topBales = new ArrayList<>();

        int mostRecent = balesEntityRepository.findAll().size() - indexRange;
        int index = mostRecent;
        if (mostRecent > 10) {
            for (int i = 0; i < 10; i++) {
                BaleDTO baleDTO = balesEntityRepository.findAll().get(mostRecent - 1).getBaleInformation();
                topBales.add(baleDTO);
                mostRecent--;
            }
        } else {
            for (int i = 0; i < index; i++) {
                BaleDTO baleDTO = balesEntityRepository.findAll().get(mostRecent - 1).getBaleInformation();
                topBales.add(baleDTO);
                mostRecent--;
            }
        }

        return ResponseEntity.status(HttpStatus.OK).body(new BaleListsIndexDTO(balesEntityRepository.findAll().size(), topBales));
    }

}