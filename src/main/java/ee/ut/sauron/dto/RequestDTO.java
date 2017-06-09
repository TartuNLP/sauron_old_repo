package ee.ut.sauron.dto;

import ee.ut.sauron.translation.TranslationDomain;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestDTO {

    private String src;
    private String auth;

    private TranslationDomain domain = TranslationDomain.general;
    private Boolean fast = false;
    private Boolean tok = true;
    private Boolean tc = true;
}
