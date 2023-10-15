package life.totl.totlback.logs.controllers;

import life.totl.totlback.logs.models.BalesEntity;
import life.totl.totlback.logs.models.LogsEntity;
import life.totl.totlback.logs.models.UserLogsBalesEntity;
import life.totl.totlback.logs.models.dto.BaleEntityDTO;
import life.totl.totlback.logs.models.dto.LogBalesDTO;
import life.totl.totlback.logs.models.dto.LogNamesDTO;
import life.totl.totlback.logs.models.dto.LogsEntityDTO;
import life.totl.totlback.logs.repositories.BalesEntityRepository;
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

    @Autowired
    private LogsBalesController(JWTGenerator jwtGenerator, UserLogsBalesEntityRepository userLogsBalesEntityRepository, UserEntityRepository userEntityRepository, LogsEntityRepository logsEntityRepository, BalesEntityRepository balesEntityRepository) {
        this.jwtGenerator = jwtGenerator;
        this.userLogsBalesEntityRepository = userLogsBalesEntityRepository;
        this.userEntityRepository = userEntityRepository;
        this.logsEntityRepository = logsEntityRepository;
        this.balesEntityRepository = balesEntityRepository;
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
            return ResponseEntity.status(HttpStatus.OK).body(new LogBalesDTO("success", logTarget.get().getAllLogBales()));
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

    @PostMapping(value = "/create-bale")
    public ResponseEntity<?> createNewBale(@RequestBody BaleEntityDTO baleEntityDTO, @RequestHeader("auth-token")String token) {
        if (!jwtGenerator.validateToken(token.substring(7, token.length()))){
            System.out.println("Danger, respond with logout");
        }

        Optional<UserEntity> user = userEntityRepository.findById(baleEntityDTO.getUserId());
        /* Get the record of all logs, bales, and comments created by user */
        Optional<UserLogsBalesEntity> logsBales = userLogsBalesEntityRepository.findById(user.get().getUserLogsBalesEntity().getId());
        Optional<LogsEntity> logsEntity = logsEntityRepository.findByLogName(baleEntityDTO.getParentLog());

        if (logsBales.isPresent() && logsEntity.isPresent()) {
            BalesEntity balesEntity = new BalesEntity(logsEntity.get(), logsBales.get(), baleEntityDTO.getTitle(), baleEntityDTO.getBody());
            balesEntityRepository.save(balesEntity);
            logsBales.get().getBaleEntities().add(balesEntity);
            userLogsBalesEntityRepository.save(logsBales.get());
            return ResponseEntity.status(HttpStatus.OK).body(balesEntity);
        }

        return ResponseEntity.status(HttpStatus.OK).body(baleEntityDTO);

    }

}
