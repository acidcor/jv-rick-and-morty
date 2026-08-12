package mate.academy.rickandmorty.service.impl;

import java.util.List;
import java.util.Random;
import lombok.RequiredArgsConstructor;
import mate.academy.rickandmorty.dto.internal.PersonageDto;
import mate.academy.rickandmorty.mapper.PersonageMapper;
import mate.academy.rickandmorty.repository.PersonageRepository;
import mate.academy.rickandmorty.service.PersonageService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PersonageServiceImpl implements PersonageService {
    private final PersonageRepository repository;
    private final PersonageMapper characterMapper;

    @Override
    public PersonageDto getRandom() {
        List<PersonageDto> list = repository.findAll().stream()
                .map(characterMapper::toDto)
                .toList();
        Random random = new Random();
        return list.get(random.nextInt(list.size()));
    }

    @Override
    public Page<PersonageDto> findByName(Pageable pageable, String name) {
        List<PersonageDto> list = repository.findByNameContains(name, pageable).stream()
                .map(characterMapper::toDto)
                .toList();
        return new PageImpl<>(list);
    }
}
