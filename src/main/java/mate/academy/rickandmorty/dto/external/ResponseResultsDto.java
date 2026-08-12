package mate.academy.rickandmorty.dto.external;

import java.util.List;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class ResponseResultsDto {
    private InfoDto info;
    private List<ResponsePersonageDto> results;
}
