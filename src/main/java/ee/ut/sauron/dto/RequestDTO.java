package ee.ut.sauron.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestDTO {

    private String langpair;
    private String src;
    private String auth;

    private String domain = "general";
    private Boolean fast = false;
    private Boolean tok = true;
    private Boolean tc = true;
}
