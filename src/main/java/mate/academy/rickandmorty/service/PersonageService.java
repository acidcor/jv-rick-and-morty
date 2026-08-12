package mate.academy.rickandmorty.service;

import mate.academy.rickandmorty.dto.internal.PersonageDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public interface PersonageService {
    Page<PersonageDto> findByName(Pageable pageable, String name);

    PersonageDto getRandom();
}
