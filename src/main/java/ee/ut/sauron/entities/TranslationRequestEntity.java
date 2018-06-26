package ee.ut.sauron.entities;

import javax.persistence.*;
import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;

@Entity
@Table(name = "translation_request", schema = "neurotolge_api", catalog = "")
public class TranslationRequestEntity {
    private int id;
    private byte[] ipv4;
    private String token;
    private Collection<SourceEventEntity> sourceEventsById;
    private Collection<TargetEventEntity> targetEventsById;

    @Id
    @Column(name = "id", nullable = false)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "ipv4", nullable = false)
    public byte[] getIpv4() {
        return ipv4;
    }

    public void setIpv4(byte[] ipv4) {
        this.ipv4 = ipv4;
    }

    @Basic
    @Column(name = "token", nullable = false, length = 16)
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TranslationRequestEntity that = (TranslationRequestEntity) o;
        return id == that.id &&
                Arrays.equals(ipv4, that.ipv4) &&
                Objects.equals(token, that.token);
    }

    @Override
    public int hashCode() {

        int result = Objects.hash(id, token);
        result = 31 * result + Arrays.hashCode(ipv4);
        return result;
    }

    @OneToMany(mappedBy = "translationRequestByRequestHandle")
    public Collection<SourceEventEntity> getSourceEventsById() {
        return sourceEventsById;
    }

    public void setSourceEventsById(Collection<SourceEventEntity> sourceEventsById) {
        this.sourceEventsById = sourceEventsById;
    }

    @OneToMany(mappedBy = "translationRequestByRequestHandle")
    public Collection<TargetEventEntity> getTargetEventsById() {
        return targetEventsById;
    }

    public void setTargetEventsById(Collection<TargetEventEntity> targetEventsById) {
        this.targetEventsById = targetEventsById;
    }
}
