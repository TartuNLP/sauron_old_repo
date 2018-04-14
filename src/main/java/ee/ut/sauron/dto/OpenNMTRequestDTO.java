package ee.ut.sauron.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OpenNMTRequestDTO {
    private String src;
    private List<String> feats;
}
