package mate.academy.rickandmorty.service.impl;

import java.util.Random;
import lombok.RequiredArgsConstructor;
import mate.academy.rickandmorty.dto.internal.PersonageDto;
import mate.academy.rickandmorty.mapper.PersonageMapper;
import mate.academy.rickandmorty.repository.PersonageRepository;
import mate.academy.rickandmorty.service.PersonageService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PersonageServiceImpl implements PersonageService {
    private final PersonageRepository repository;
    private final PersonageMapper characterMapper;

    @Override
    public PersonageDto getRandom() {
        Random random = new Random();
        long size = repository.count();
        return repository.findById(random.nextLong(size)).stream()
                .map(characterMapper::toDto)
                .findFirst().orElseThrow();
    }

    @Override
    public Page<PersonageDto> findByName(Pageable pageable, String name) {
        return repository.findByNameContains(name, pageable)
                .map(characterMapper::toDto);
    }
}
