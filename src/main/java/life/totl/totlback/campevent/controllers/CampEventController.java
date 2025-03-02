package life.totl.totlback.campevent.controllers;

import life.totl.totlback.backpack.models.GearItemsEntity;
import life.totl.totlback.backpack.repository.GearItemsEntityRepository;
import life.totl.totlback.campevent.models.*;
import life.totl.totlback.campevent.models.dtos.CreateNewEventDTO;
import life.totl.totlback.campevent.models.dtos.FoodRecDTO;
import life.totl.totlback.campevent.models.dtos.ItemRecDTO;
import life.totl.totlback.campevent.repository.*;
import life.totl.totlback.security.utils.jwt.JWTGenerator;
import life.totl.totlback.users.models.UserEntity;
import life.totl.totlback.users.models.response.ResponseMessage;
import life.totl.totlback.users.repository.UserEntityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@RestController
@CrossOrigin(origins = "http://localhost:3000", maxAge = 3600, allowCredentials = "true")
@RequestMapping(value = "/campevent")
public class CampEventController {

    private final JWTGenerator jwtGenerator;
    private final ParkDetailEntityRepository parkDetailEntityRepository;
    private final MealBaseEntityRepository mealBaseEntityRepository;
    private final MealRecEntityRepository mealRecEntityRepository;
    private final GearItemsEntityRepository gearItemsEntityRepository;
    private final ItemRecEntityRepository itemRecEntityRepository;
    private final UserEntityRepository userEntityRepository;
    private final CampEventEntityRepository campEventEntityRepository;
    private final CampEventsRelatedToUserEntityRepository campEventsRelatedToUserEntityRepository;

    @Autowired
    private CampEventController(JWTGenerator jwtGenerator, ParkDetailEntityRepository parkDetailEntityRepository, MealBaseEntityRepository mealBaseEntityRepository,
                                MealRecEntityRepository mealRecEntityRepository, GearItemsEntityRepository gearItemsEntity, ItemRecEntityRepository itemRecEntityRepository,
                                UserEntityRepository userEntityRepository, CampEventEntityRepository campEventEntityRepository, CampEventsRelatedToUserEntityRepository campEventsRelatedToUserEntityRepository) {
        this.jwtGenerator = jwtGenerator;
        this.parkDetailEntityRepository = parkDetailEntityRepository;
        this.mealBaseEntityRepository = mealBaseEntityRepository;
        this.mealRecEntityRepository = mealRecEntityRepository;
        this.gearItemsEntityRepository = gearItemsEntity;
        this.itemRecEntityRepository = itemRecEntityRepository;
        this.userEntityRepository = userEntityRepository;
        this.campEventEntityRepository = campEventEntityRepository;
        this.campEventsRelatedToUserEntityRepository = campEventsRelatedToUserEntityRepository;
    }

    @PostMapping(value = "/createEvent")
    public ResponseEntity<?> createUserEvent(@RequestHeader("auth-token") String token,@RequestBody CreateNewEventDTO newEventDTO) {
        try {
            if (!jwtGenerator.validateToken(token.substring(7, token.length()))){
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e);
        }

        for (UserEntity user1 : userEntityRepository.findAll()) {
            Optional<UserEntity> user = Optional.ofNullable(userEntityRepository.findByUserName(user1.getUserName()));
            if (user.get().getCampEventsRelatedToUser() == null) {
                CampEventsRelatedToUserEntity campEvents = new CampEventsRelatedToUserEntity(user.get());
                user.get().setCampEventsRelatedToUser(campEvents);
                userEntityRepository.save(user.get());
            }
        }

        try {

            Optional<CampEventsRelatedToUserEntity> ownerOfEvent = Optional.ofNullable(userEntityRepository.findByUserName(newEventDTO.getUserName()).getCampEventsRelatedToUser());
            ParkDetailEntity parkDetail;
            List<MealRecEntity> eventMealPlan = new ArrayList<>();
            List<ItemRecEntity> gearListRec = new ArrayList<>();
            List<CampEventsRelatedToUserEntity> eventInvites = new ArrayList<>();

            if (ownerOfEvent.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage("Failed", "This user does not exist"));
            }

            if (parkDetailEntityRepository.existsByParkGovID(newEventDTO.getEventDetails().getCampGround().getId())) {
                parkDetail = parkDetailEntityRepository.findByParkGovID(newEventDTO.getEventDetails().getCampGround().getId());
            } else {
                parkDetail = new ParkDetailEntity(newEventDTO.getEventDetails().getCampGround().name
                        ,newEventDTO.getEventDetails().getCampGround().addressString
                        ,newEventDTO.getEventDetails().getCampGround().id
                        ,newEventDTO.getEventDetails().getCampGround().url
                        ,newEventDTO.getEventDetails().getCampGround().latitude
                        ,newEventDTO.getEventDetails().getCampGround().longitude
                        ,newEventDTO.getEventDetails().getCampGround().address.line1
                        ,newEventDTO.getEventDetails().getCampGround().address.line2
                        ,newEventDTO.getEventDetails().getCampGround().address.city
                        ,newEventDTO.getEventDetails().getCampGround().address.stateCode
                        ,newEventDTO.getEventDetails().getCampGround().address.postalCode
                        ,newEventDTO.getEventDetails().getCampGround().amenities.trashRecyclingCollection
                        ,!newEventDTO.getEventDetails().getCampGround().amenities.toilets.isEmpty()
                        ,newEventDTO.getEventDetails().getCampGround().amenities.internetConnectivity
                        ,!newEventDTO.getEventDetails().getCampGround().amenities.showers.isEmpty()
                        ,newEventDTO.getEventDetails().getCampGround().amenities.cellPhoneReception
                        ,newEventDTO.getEventDetails().getCampGround().amenities.laundry
                        ,newEventDTO.getEventDetails().getCampGround().amenities.campStore
                        ,newEventDTO.getEventDetails().getCampGround().amenities.staffOrVolunteerHostOnsite
                        ,newEventDTO.getEventDetails().getCampGround().amenities.iceAvailableForSale
                        ,newEventDTO.getEventDetails().getCampGround().amenities.firewoodForSale);
                parkDetailEntityRepository.save(parkDetail);
            }

            for (FoodRecDTO item : newEventDTO.getMealPlan()) {
                 if (mealBaseEntityRepository.existsByBfastAndLunchAndDinnerAndSnacks(item.getBfast(), item.getLunch(), item.getDinner(), item.getSnacks())) {
                     MealBaseEntity meal = mealBaseEntityRepository.findByBfastAndLunchAndDinnerAndSnacks(item.getBfast(), item.getLunch(), item.getDinner(), item.getSnacks());
                     MealRecEntity newMeal = new MealRecEntity(meal, item.getNotes(), item.getId());
                     mealRecEntityRepository.save(newMeal);
                     eventMealPlan.add(newMeal);
                 } else {
                     MealBaseEntity meal = new MealBaseEntity(item.getSnacks(), item.getDinner(), item.getLunch(), item.getBfast());
                     mealBaseEntityRepository.save(meal);
                     MealRecEntity newMeal = new MealRecEntity(meal, item.getNotes(), item.getId());
                     mealRecEntityRepository.save(newMeal);
                     eventMealPlan.add(newMeal);
                 }
            }

            for (ItemRecDTO item : newEventDTO.getGearRecDTO()) {
                if (gearItemsEntityRepository.existsByCategoryAndTypeAndBrand(item.getCategory(), item.getGearType(), "gearRecItem")) {
                    GearItemsEntity gear = gearItemsEntityRepository.findByCategoryAndTypeAndBrand(item.getCategory(), item.getGearType(), "gearRecItem");
                    ItemRecEntity gearRec = new ItemRecEntity(gear, item.getGroupType(), item.getCount());
                    itemRecEntityRepository.save(gearRec);
                    gearListRec.add(gearRec);
                } else {
                    GearItemsEntity gear = new GearItemsEntity(item.getCategory(), "gearRecItem", item.getGearType());
                    gearItemsEntityRepository.save(gear);
                    ItemRecEntity gearRec = new ItemRecEntity(gear, item.getGroupType(), item.getCount());
                    itemRecEntityRepository.save(gearRec);
                    gearListRec.add(gearRec);
                }
            }

            for (String name : newEventDTO.getFriendListString()) {
                Optional<UserEntity> user = Optional.ofNullable(userEntityRepository.findByUserName(name));
                user.ifPresent(userEntity -> eventInvites.add(userEntity.getCampEventsRelatedToUser()));
            }

            CampEventEntity event = new CampEventEntity(ownerOfEvent.get(),
                                                        new Date(System.currentTimeMillis()),
                                                        newEventDTO.getEventDetails().getEventName(),
                                                        eventInvites,
                                                        eventMealPlan,
                                                        gearListRec,
                                                        newEventDTO.getEventDetails().getCampGround().getAddressString(),
                                                        newEventDTO.getEventDetails().getCampGround().getName(),
                                                        newEventDTO.getEventDetails().getParkState(),
                                                        newEventDTO.getEventDetails().getUserDescription(),
                                                        LocalDateTime.parse(newEventDTO.getEventDetails().getEventEnd(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSX")),
                                                        newEventDTO.getEventDetails().getEndTime(),
                                                        newEventDTO.getEventDetails().getEndDate(),
                                                        LocalDateTime.parse(newEventDTO.getEventDetails().getEventStart(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSX")),
                                                        newEventDTO.getEventDetails().getStartTime(),
                                                        newEventDTO.getEventDetails().getStartDate(),
                                                        newEventDTO.getEventDetails().getIsPrivate(),
                                                        parkDetail
                                                        );
            for (MealRecEntity item : eventMealPlan) {
                item.setEventMealRec(event);
            }
            for (ItemRecEntity item : gearListRec) {
                item.setEventGearRec(event);
            }
            campEventEntityRepository.save(event);

            ownerOfEvent.get().getRelatedCampEvents().add(event);
            ownerOfEvent.get().getUserMadeCampEvents().add(event);
            campEventsRelatedToUserEntityRepository.save(ownerOfEvent.get());

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }

        return ResponseEntity.status(HttpStatus.OK).body("success");
    }

    @GetMapping(value = "/getUserEvent/{user}/{eventId}")
    public ResponseEntity<?> getUserEvent() {

        return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage("getUserEvent"));
    }

    @GetMapping(value = "/getAllRelevantEvents/{user}")
    public ResponseEntity<?> getAllRelevantEvents() {

        return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage("getAllRelevantEvents"));
    }

    @DeleteMapping(value = "/deleteUsersEvent/{user}/{eventId}")
    public ResponseEntity<?> deleteUsersEvent(@RequestHeader("auth-token") String token) {

        return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage("deleteUsersEvent"));
    }

}
