package ee.ut.sauron.providers;


import lombok.Data;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@Data
@XmlRootElement(name = "translationProviders")
public class GenericProviders {

    private List<GenericProvider> provider;

}
