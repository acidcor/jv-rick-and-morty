package mate.academy.rickandmorty.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import mate.academy.rickandmorty.dto.internal.PersonageDto;
import mate.academy.rickandmorty.service.PersonageService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/personages")
public class PersonagesController {
    private final PersonageService personageService;

    @Operation(summary = "Return a random character from Rick and Morty")
    @GetMapping
    public PersonageDto getRandom() {
        return personageService.getRandom();
    }

    @Operation(summary = "Return a page of all characters whose name contains the search string")
    @GetMapping("search/{name}")
    public Page<PersonageDto> findByName(Pageable pageable, @PathVariable String name) {
        return personageService.findByName(pageable, name);
    }
}
