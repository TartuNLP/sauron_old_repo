package ee.ut.sauron.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ResponseDTO {

    private final String src;

    private final String tgt;

    private final String rawTgt;

    private final String alignWeights;

}
