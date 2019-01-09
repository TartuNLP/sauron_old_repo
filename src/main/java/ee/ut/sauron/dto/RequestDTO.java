package ee.ut.sauron.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestDTO {

    private String engine;
    private String src;
    private String auth;

    private String conf = "en,fml";
    private Boolean fast = false;
    private Boolean tok = true;
    private Boolean tc = true;
    private String domain = "general";
    private Boolean alignweights = false;
    private Boolean qualityestimation = false;
}
