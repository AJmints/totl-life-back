package life.totl.totlback.logs.controllers;

import life.totl.totlback.logs.models.BalesEntity;
import life.totl.totlback.logs.models.CommentEntity;
import life.totl.totlback.logs.models.LogsEntity;
import life.totl.totlback.logs.models.UserLogsBalesEntity;
import life.totl.totlback.logs.models.dto.*;
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

    @GetMapping(value = "/all-bales-in-log/{log}")
    public ResponseEntity<?> getAllBalesInLog(@PathVariable String log) {

        Optional<LogsEntity> logTarget = logsEntityRepository.findByLogName(log);
        if (logTarget.isPresent()) {

            /** Create a class to hold all necessary information about bales for the front,
             * Create method inside bale entity to prepare that object
             * Create a list, store them, then send them up.
             */
            List<BaleDTO> allLogBales = new ArrayList<>();
            for (BalesEntity obj : logTarget.get().getAllLogBales()) {
                BaleDTO setBale = obj.getBaleInformation();
                allLogBales.add(setBale);
            }

            return ResponseEntity.status(HttpStatus.OK).body(new LogBalesDTO("success",logTarget.get().getLogDescription(), allLogBales));
        }
        ResponseMessage response = new ResponseMessage("Not Found");
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping(value = "/create-log")
    public ResponseEntity<?> createALog(@RequestBody LogsEntityDTO logsEntityDTO, @RequestHeader("auth-token") String token) {

        if (!jwtGenerator.validateToken(token.substring(7, token.length()))){
            System.out.println("Danger, respond with logout");
        }

        /* Get the user who made the post */
        Optional<UserEntity> user = userEntityRepository.findById(logsEntityDTO.getUser());
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


        return ResponseEntity.status(HttpStatus.OK).body(logsEntityDTO);
    }

//    @PostMapping(value = "/like-post")
//    public ResponseEntity<?> likeAPost(@RequestBody) {
//
//        return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage("Yep"));
//    }

    @PostMapping(value = "/create-bale")
    public ResponseEntity<?> createNewBale(@RequestBody CreateBaleEntityDTO createBaleEntityDTO, @RequestHeader("auth-token")String token) {
        if (!jwtGenerator.validateToken(token.substring(7, token.length()))){
            System.out.println("Danger, respond with logout");
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
            return ResponseEntity.status(HttpStatus.OK).body(balesEntity);
        }

        return ResponseEntity.status(HttpStatus.OK).body(createBaleEntityDTO);

    }

    @PostMapping(value = "create-comment")
    public ResponseEntity<?> createNewComment(@RequestBody CommentDTO commentDTO, @RequestHeader("auth-token")String token) {
        if (!jwtGenerator.validateToken(token.substring(7, token.length()))){
            System.out.println("Danger, respond with logout");
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

    // TODO: Make a Get that returns the 10 most recent bales
    @GetMapping(value = "/most-recent-bales")
    public ResponseEntity<?> mostRecentBalePosts() {
        List<BaleDTO> topBales = new ArrayList<>();

        for (BalesEntity bale : balesEntityRepository.findAll()) {
            BaleDTO baleDTO = new BaleDTO(bale.getId(), bale.getParentLog().getLogName(), bale.getTitle(), bale.getBody(), bale.getBaleOwner().getUser().getUserName(), bale.getBaleInformation().getUserPFP());
            topBales.add(baleDTO);
        }
        return ResponseEntity.status(HttpStatus.OK).body(topBales);
    }

}