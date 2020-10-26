package com.wiredbrain.friends.resource;

import com.wiredbrain.friends.model.Friend;
import com.wiredbrain.friends.service.FriendService;
import com.wiredbrain.friends.utils.ErrorMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import sun.security.validator.ValidatorException;

import javax.xml.bind.ValidationException;
import java.util.Optional;

@RestController
public class FriendController {

    @Autowired
    FriendService friendService;

    @PostMapping("/friend")
    Friend create(@Validated @RequestBody Friend friend) {
            return friendService.save(friend);
    }

    @ResponseStatus
    @ExceptionHandler(ValidatorException.class)
    ErrorMessage exceptionHandler(ValidationException e){
        return new ErrorMessage("400", e.getMessage());
    }

    @GetMapping("/friend")
    Iterable<Friend> read(){
        return friendService.findAll();
    }

    @GetMapping("/friend/{id}")
    Optional<Friend> findById(@PathVariable Integer id){
        return friendService.findById(id);
    }

    @GetMapping("/friend/search")
    Iterable<Friend> findbyQuery(
            @RequestParam(value = "first", required = false) String firstName, @RequestParam(value = "last", required = false) String lastName
    ){
        if(firstName != null && lastName != null){
            return friendService.findByFirstNameAndLastName(firstName,lastName);
        }else if(firstName != null){
            return friendService.findByFirstName(firstName);
        }else if(lastName != null){
            return friendService.findByLastName(lastName);
        }else{
            return friendService.findAll();
        }

    };

    @PutMapping("/friend")
    ResponseEntity<Friend> update(@RequestBody Friend friend){
        if(friendService.findById(friend.getId()).isPresent()){
            return new ResponseEntity(friendService.save(friend), HttpStatus.OK);
        }else{
            return new ResponseEntity(friend, HttpStatus.BAD_REQUEST);
        }
    };

    @DeleteMapping("/friend/{id}")
    void delete(@PathVariable Integer id){
        friendService.deleteById(id);
    }

}
