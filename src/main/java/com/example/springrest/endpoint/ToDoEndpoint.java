package com.example.springrest.endpoint;

import com.example.springrest.dto.ToDoDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/todo")
public class ToDoEndpoint {

    private static final String BASE_URL = "https://jsonplaceholder.typicode.com/";

    @GetMapping("/{id}")
    public ToDoDto singleToDo(@PathVariable("id") int id) {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<ToDoDto> todoRestEntity = restTemplate.getForEntity(BASE_URL + "todos/" + id, ToDoDto.class);
        return todoRestEntity.getBody();
    }

    @GetMapping("/")
    public List<ToDoDto> getAll() {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<ToDoDto[]> todoRestEntity = restTemplate.getForEntity(BASE_URL + "todos/", ToDoDto[].class);

        return new ArrayList<>(Arrays.asList(todoRestEntity.getBody()));
    }

}
