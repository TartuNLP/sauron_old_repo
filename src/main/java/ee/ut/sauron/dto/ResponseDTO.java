package ee.ut.sauron.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class ResponseDTO {

    private final List<String> src;

    private final String tgt;

    private final List<String> rawTgt;

    private final List<String> alignweights;

    private final String qualityestimation;

}
