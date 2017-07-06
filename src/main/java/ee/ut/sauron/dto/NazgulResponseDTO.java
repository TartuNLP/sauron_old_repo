package ee.ut.sauron.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class NazgulResponseDTO {

    private List<String> raw_input;

    private List<String> raw_trans;

    private String final_trans;

    private List<String> weights;

}
