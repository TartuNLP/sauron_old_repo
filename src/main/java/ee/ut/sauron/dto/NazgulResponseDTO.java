package ee.ut.sauron.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class NazgulResponseDTO {

    private String raw_input;

    private String raw_trans;

    private String final_trans;

    private String weights;

}
