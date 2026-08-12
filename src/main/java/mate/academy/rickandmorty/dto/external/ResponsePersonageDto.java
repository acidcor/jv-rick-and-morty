package mate.academy.rickandmorty.dto.external;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ResponsePersonageDto {
    @JsonProperty("id")
    private Long externalId;
    private String name;
    private String status;
    private String gender;
}
