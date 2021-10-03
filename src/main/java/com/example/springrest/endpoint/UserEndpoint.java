package com.example.springrest.endpoint;

import com.example.springrest.dto.UserAuthDto;
import com.example.springrest.dto.UserAuthResponseDto;
import com.example.springrest.dto.UserDto;
import com.example.springrest.dto.UserSaveDto;
import com.example.springrest.model.User;
import com.example.springrest.repository.UserRepository;
import com.example.springrest.security.CurrentUser;
import com.example.springrest.util.JwtTokenUtil;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class UserEndpoint {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenUtil jwtTokenUtil;

    private final ModelMapper mapper;


    @PostMapping("/users/auth")
    public ResponseEntity<?> auth(@RequestBody UserAuthDto userAuthDto) {
        Optional<User> byEmail = userRepository.findByEmail(userAuthDto.getEmail());
        if (byEmail.isEmpty()) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .build();
        }

        User user = byEmail.get();
        if (passwordEncoder.matches(userAuthDto.getPassword(), user.getPassword())) {
            return ResponseEntity.ok(UserAuthResponseDto.builder()
                    .token(jwtTokenUtil.generateToken(user.getEmail()))
                    .userDto(mapper.map(user, UserDto.class))
                    .build());
        }
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .build();
    }


    @GetMapping("/users")
    public List<UserDto> users(@AuthenticationPrincipal CurrentUser currentUser) {
        System.out.println("user with " + currentUser.getUser().getEmail() + " email get all users");

        List<User> all = userRepository.findAll();
        List<UserDto> userDtos = new ArrayList<>();
        for (User user : all) {
            UserDto userDto = mapper.map(user, UserDto.class);
            userDtos.add(userDto);
        }
        return userDtos;
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<UserDto> getUser(@PathVariable("id") int id) {
        Optional<User> byId = userRepository.findById(id);
        if (byId.isEmpty()) {
            return ResponseEntity
                    .notFound()
                    .build();
        }
        return ResponseEntity.ok(mapper.map(byId.get(), UserDto.class));
    }

    @PostMapping("/users")
    public UserDto user(@RequestBody UserSaveDto user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return mapper.map(userRepository.save(mapper.map(user, User.class)), UserDto.class);
    }

    @PostMapping("/users/uploadPic/{userId}")
    public ResponseEntity uploadUserPic(@PathVariable("userId") int userId,
                                                @RequestParam("image") MultipartFile multipartFile){
        Optional<User> byId = userRepository.findById(userId);
        if (byId.isEmpty()) {
            return ResponseEntity
                    .notFound()
                    .build();
        }
        //upload Logic ...
        byId.get().setProfilePic("/asdf.jpeg");
        userRepository.save(byId.get());

        return ResponseEntity.noContent().build();
    }

    @PutMapping("/users/{id}")
    public ResponseEntity<UserDto> user(@PathVariable(name = "id") int id, @RequestBody UserSaveDto user) {
        Optional<User> byId = userRepository.findById(id);
        if (byId.isEmpty()) {
            return ResponseEntity
                    .notFound()
                    .build();
        }
        User userFromDb = byId.get();
        userFromDb.setName(user.getName());
        userFromDb.setSurname(user.getSurname());
        userFromDb.setUserType(user.getUserType());

        return ResponseEntity
                .ok()
                .body(mapper.map(userRepository.save(userFromDb), UserDto.class));
    }

    @DeleteMapping("/users/{id}")
    @ApiOperation(notes = "this is for deleteing", value = "delete user by id")
    public ResponseEntity deleteById(@PathVariable("id") int id) {
        Optional<User> byId = userRepository.findById(id);
        if (byId.isEmpty()) {
            return ResponseEntity
                    .notFound()
                    .build();
        }
        userRepository.deleteById(id);
        return ResponseEntity
                .noContent()
                .build();
    }

}
