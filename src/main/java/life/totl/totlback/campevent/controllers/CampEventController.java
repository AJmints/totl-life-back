package life.totl.totlback.campevent.controllers;

import life.totl.totlback.backpack.models.BackPackConfigurationEntity;
import life.totl.totlback.backpack.models.BackPackEntity;
import life.totl.totlback.backpack.models.GearItemsEntity;
import life.totl.totlback.backpack.models.dtos.BackPackConfigDTO;
import life.totl.totlback.backpack.repository.GearItemsEntityRepository;
import life.totl.totlback.campevent.models.*;
import life.totl.totlback.campevent.models.dtos.CreateNewEventDTO;
import life.totl.totlback.campevent.models.dtos.FoodRecDTO;
import life.totl.totlback.campevent.models.dtos.ItemRecDTO;
import life.totl.totlback.campevent.repository.MealBaseEntityRepository;
import life.totl.totlback.campevent.repository.ParkDetailEntityRepository;
import life.totl.totlback.security.utils.jwt.JWTGenerator;
import life.totl.totlback.users.models.UserEntity;
import life.totl.totlback.users.models.response.ResponseMessage;
import life.totl.totlback.users.repository.UserEntityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.*;

@RestController
@CrossOrigin(origins = "http://localhost:3000", maxAge = 3600, allowCredentials = "true")
@RequestMapping(value = "/campevent")
public class CampEventController {

    private final JWTGenerator jwtGenerator;
    private final ParkDetailEntityRepository parkDetailEntityRepository;
    private final MealBaseEntityRepository mealBaseEntityRepository;
    private final GearItemsEntityRepository gearItemsEntityRepository;
    private final UserEntityRepository userEntityRepository;

    @Autowired
    private CampEventController(JWTGenerator jwtGenerator, ParkDetailEntityRepository parkDetailEntityRepository, MealBaseEntityRepository mealBaseEntityRepository,
                                GearItemsEntityRepository gearItemsEntity, UserEntityRepository userEntityRepository) {
        this.jwtGenerator = jwtGenerator;
        this.parkDetailEntityRepository = parkDetailEntityRepository;
        this.mealBaseEntityRepository = mealBaseEntityRepository;
        this.gearItemsEntityRepository = gearItemsEntity;
        this.userEntityRepository = userEntityRepository;
    }

    @PostMapping(value = "/createEvent")
    public ResponseEntity<?> createUserEvent(@RequestHeader("auth-token") String token,@RequestBody CreateNewEventDTO newEventDTO) throws ParseException {
        try {
            if (!jwtGenerator.validateToken(token.substring(7, token.length()))){
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e);
        }

        ParkDetailEntity parkDetail;
        List<MealRecEntity> eventMealPlan = new ArrayList<>();
        List<ItemRecEntity> gearListRec = new ArrayList<>();
        List<CampEventsRelatedToUserEntity> eventInvites = new ArrayList<>();

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

        }

        for (FoodRecDTO item : newEventDTO.getMealPlan()) {
             if (mealBaseEntityRepository.existsByBfastAndLunchAndDinnerAndSnacks(item.getBfast(), item.getLunch(), item.getDinner(), item.getSnacks())) {
                 MealBaseEntity meal = mealBaseEntityRepository.findByBfastAndLunchAndDinnerAndSnacks(item.getBfast(), item.getLunch(), item.getDinner(), item.getSnacks());
                 MealRecEntity newMeal = new MealRecEntity(meal, item.getNotes(), item.getId());
                 // need to save newMeal to repository
                 // Need to set event that this mealrec entity is tied too.
                 eventMealPlan.add(newMeal);
             } else {
                 MealBaseEntity meal = new MealBaseEntity(item.getSnacks(), item.getDinner(), item.getLunch(), item.getBfast());
                 MealRecEntity newMeal = new MealRecEntity(meal, item.getNotes(), item.getId());
                 // need to save meal to repository
                 // need to save newMeal to repository
                 // Need to set event that this mealrec entity is tied too.
                 eventMealPlan.add(newMeal);
             }
        }

        for (ItemRecDTO item : newEventDTO.getGearRecDTO()) {
            if (gearItemsEntityRepository.existsByCategoryAndTypeAndBrand(item.getCategory(), item.getGearType(), "gearRecItem")) {
                GearItemsEntity gear = gearItemsEntityRepository.findByCategoryAndTypeAndBrand(item.getCategory(), item.getGearType(), "gearRecItem");
                ItemRecEntity gearRec = new ItemRecEntity(gear, item.getGroupType(), item.getCount());
                // need to save gear to repository
                // Need to set event that this gearRec entity is tied too.
                gearListRec.add(gearRec);
            } else {
                GearItemsEntity gear = new GearItemsEntity(item.getCategory(), "gearRecItem", item.getGearType());
                ItemRecEntity gearRec = new ItemRecEntity(gear, item.getGroupType(), item.getCount());
                // need to save gear to repository
                // need to save gearRec to repository
                // Need to set event that this gearRec entity is tied too.
                gearListRec.add(gearRec);
            }
        }

        for (String name : newEventDTO.getFriendListString()) {
            Optional<UserEntity> user = Optional.ofNullable(userEntityRepository.findByUserName(name));
            if (user.get().getCampEventsRelatedToUser() == null) {
                CampEventsRelatedToUserEntity campEvents = new CampEventsRelatedToUserEntity(user.get());
                user.get().setCampEventsRelatedToUser(campEvents);
                userEntityRepository.save(user.get());

            }
            eventInvites.add(user.get().getCampEventsRelatedToUser());
        }

//        CampEventEntity event = new CampEventEntity(userEntityRepository.findByUserName(newEventDTO.getUserName()).getCampEventsRelatedToUser(),
//                                                    new Date(System.currentTimeMillis()),
//                                                    newEventDTO.getEventDetails().getEventName(),
//                                                    eventInvites,
//                                                    eventMealPlan,
//                                                    gearListRec,
//                                                    parkDetail,
//                                                    newEventDTO.getEventDetails().getCampGround().getAddressString(),
//                                                    newEventDTO.getEventDetails().getCampGround().getName(),
//                                                    newEventDTO.getEventDetails().getParkState(),
//                                                    newEventDTO.getEventDetails().getUserDescription(),
//                                                    DateFormat.getDateInstance().parse(newEventDTO.getEventDetails().getEventEnd()),
//                                                    newEventDTO.getEventDetails().getEndTime(),
//                                                    newEventDTO.getEventDetails().getEndDate(),
//                                                    DateFormat.getDateInstance().parse(newEventDTO.getEventDetails().getEventStart().replaceAll("Z", "")),
//                                                    newEventDTO.getEventDetails().getStartTime(),
//                                                    newEventDTO.getEventDetails().getStartDate(),
//                                                    newEventDTO.getEventDetails().getIsPrivate()
//                                                    );

        try {
//            parkDetailEntityRepository.save(parkDetail);
            System.out.println("Attempt to save to repo");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage("Failed", "Something went wrong saving this event. Please try again later"));
        }

        return ResponseEntity.status(HttpStatus.OK).body("");
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
