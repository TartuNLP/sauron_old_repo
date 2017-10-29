package ee.ut.sauron.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class NazgulRequestDTO {

    private String src;

    private Boolean tok;

    private Boolean tc;

    private Boolean align_weights;

    private Boolean quality_estimation;
}
