package life.totl.totlback.backpack.controllers;

import life.totl.totlback.backpack.models.BackPackConfigurationEntity;
import life.totl.totlback.backpack.models.BackPackEntity;
import life.totl.totlback.backpack.models.UserSpecificGearEntity;
import life.totl.totlback.backpack.models.dtos.BackPackConfigDTO;
import life.totl.totlback.backpack.models.dtos.GearItemDTO;
import life.totl.totlback.backpack.models.dtos.response.UserGearListResponseDTO;
import life.totl.totlback.backpack.repository.BackPackConfigurationRepository;
import life.totl.totlback.backpack.repository.BackPackEntityRepository;
import life.totl.totlback.backpack.repository.GearItemsEntityRepository;
import life.totl.totlback.backpack.repository.UserSpecificGearEntityRepository;
import life.totl.totlback.logs.repositories.UserLogsBalesEntityRepository;
import life.totl.totlback.security.utils.jwt.JWTGenerator;
import life.totl.totlback.users.models.UserEntity;
import life.totl.totlback.users.models.response.ResponseMessage;
import life.totl.totlback.users.repository.UserEntityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "http://localhost:3000", maxAge = 3600, allowCredentials = "true")
@RequestMapping(value = "/backpack")
public class BackPackController {

    private final JWTGenerator jwtGenerator;
    private final UserEntityRepository userEntityRepository;
    private final BackPackEntityRepository backPackEntityRepository;
    private final BackPackConfigurationRepository backPackConfigurationRepository;
    private final GearItemsEntityRepository gearItemsEntityRepository;
    private final UserSpecificGearEntityRepository userSpecificGearEntityRepository;

    @Autowired
    private BackPackController(JWTGenerator jwtGenerator, UserEntityRepository userEntityRepository, BackPackEntityRepository backPackEntityRepository, BackPackConfigurationRepository backPackConfigurationRepository, GearItemsEntityRepository gearItemsEntityRepository, UserSpecificGearEntityRepository userSpecificGearEntityRepository) {
        this.jwtGenerator = jwtGenerator;
        this.userEntityRepository = userEntityRepository;
        this.backPackEntityRepository = backPackEntityRepository;
        this.backPackConfigurationRepository = backPackConfigurationRepository;
        this.gearItemsEntityRepository = gearItemsEntityRepository;
        this.userSpecificGearEntityRepository = userSpecificGearEntityRepository;
    }

    @GetMapping(value = "/get-users-gear-list/{id}")
    public ResponseEntity<?> getUsersGearList(@PathVariable("id") long userId) {

        Optional<UserEntity> validUser = userEntityRepository.findById(userId);

        if (validUser.isPresent()) {
            if (Objects.equals(validUser.get().getUserBackPack(), null)) {
                BackPackEntity newPack = new BackPackEntity(validUser.get());
                validUser.get().setUserBackPack(newPack);
                userEntityRepository.save(validUser.get());
                return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage("empty", "Pack Empty"));
            }

            if (validUser.get().getUserBackPack().getUserGear().size() == 0) {
                return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage("empty", "Pack Empty"));
            } else {
                return ResponseEntity.status(HttpStatus.OK).body(new UserGearListResponseDTO("success",validUser.get().getUserBackPack().getUserGear()));
            }

        }

        return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage("failed", "pack pack was not made"));

    }

    // New method to Create a pack with the option to Add items or just create an empty pack
    @PostMapping
    public ResponseEntity<?> createNewBackPackConfig (@RequestHeader("auth-token") String token, BackPackConfigDTO packConfigDTO) {
        try {
            if (!jwtGenerator.validateToken(token.substring(7, token.length()))){
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e);
        }
        Optional<UserEntity> user = userEntityRepository.findById(packConfigDTO.getUserID());

        if (!packConfigDTO.getSpecificGearItems().isEmpty()) {
            for (Map<Long, String> gear : packConfigDTO.getSpecificGearItems()) {
            }
        }

        BackPackConfigurationEntity newConfig = new BackPackConfigurationEntity(user.get().getUserBackPack(), packConfigDTO.getConfigType(), packConfigDTO.getPackName());
        newConfig.setHidden(packConfigDTO.isHidden());




        return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage("Testing", "Making sure the method is working."));
    }
}
