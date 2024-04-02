package life.totl.totlback.backpack.controllers;


import life.totl.totlback.backpack.models.GearItemsEntity;
import life.totl.totlback.backpack.models.UserSpecificGearEntity;
import life.totl.totlback.backpack.models.dtos.GearItemDTO;
import life.totl.totlback.backpack.repository.BackPackConfigurationRepository;
import life.totl.totlback.backpack.repository.BackPackEntityRepository;
import life.totl.totlback.backpack.repository.GearItemsEntityRepository;
import life.totl.totlback.backpack.repository.UserSpecificGearEntityRepository;
import life.totl.totlback.security.utils.jwt.JWTGenerator;
import life.totl.totlback.users.models.UserEntity;
import life.totl.totlback.users.repository.UserEntityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@CrossOrigin(origins = "http://localhost:3000", maxAge = 3600, allowCredentials = "true")
@RequestMapping(value = "/gear")
public class GearController {

    private final JWTGenerator jwtGenerator;
    private final UserEntityRepository userEntityRepository;
    private final BackPackEntityRepository backPackEntityRepository;
    private final BackPackConfigurationRepository backPackConfigurationRepository;
    private final GearItemsEntityRepository gearItemsEntityRepository;
    private final UserSpecificGearEntityRepository userSpecificGearEntityRepository;

    @Autowired
    private GearController(JWTGenerator jwtGenerator, UserEntityRepository userEntityRepository, BackPackEntityRepository backPackEntityRepository, BackPackConfigurationRepository backPackConfigurationRepository, GearItemsEntityRepository gearItemsEntityRepository, UserSpecificGearEntityRepository userSpecificGearEntityRepository) {
        this.jwtGenerator = jwtGenerator;
        this.userEntityRepository = userEntityRepository;
        this.backPackEntityRepository = backPackEntityRepository;
        this.backPackConfigurationRepository = backPackConfigurationRepository;
        this.gearItemsEntityRepository = gearItemsEntityRepository;
        this.userSpecificGearEntityRepository = userSpecificGearEntityRepository;
    }

    @PostMapping(value = "/create-backpack-item")
    public ResponseEntity<?> createBackPackItem(@RequestHeader("auth-token") String token, @RequestBody GearItemDTO gearItemDTO) {
        try {
            if (!jwtGenerator.validateToken(token.substring(7, token.length()))) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e);
        }

        Optional<UserEntity> user = userEntityRepository.findById(gearItemDTO.getUserId());

        if (user.isPresent()) {
            GearItemsEntity newBackPack = new GearItemsEntity(gearItemDTO.getCategory(), gearItemDTO.getBrand(), gearItemDTO.getType());
            UserSpecificGearEntity userGear = new UserSpecificGearEntity(newBackPack, user.get().getUserBackPack(), "", false, 1, "", false, false, 10.00);

            return ResponseEntity.status(HttpStatus.OK).body(userGear);
        }
        return ResponseEntity.status(HttpStatus.OK).body(gearItemDTO);

    }

}
