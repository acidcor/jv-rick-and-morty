package mate.academy.rickandmorty.mapper;

import mate.academy.rickandmorty.config.MapperConfig;
import mate.academy.rickandmorty.dto.external.ResponsePersonageDto;
import mate.academy.rickandmorty.dto.internal.PersonageDto;
import mate.academy.rickandmorty.model.Personage;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfig.class)
public interface PersonageMapper {
    Personage toModel(ResponsePersonageDto responseCharacterDto);

    PersonageDto toDto(Personage character);
}
