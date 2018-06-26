package ee.ut.sauron.entities;

import javax.persistence.*;
import java.sql.Date;
import java.util.Objects;

@Entity
@Table(name = "target_event", schema = "neurotolge_api", catalog = "")
public class TargetEventEntity {
    private int id;
    private int requestHandle;
    private Date time;
    private String tgt;
    private TranslationRequestEntity translationRequestByRequestHandle;

    @Id
    @Column(name = "id", nullable = false)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "request_handle", nullable = false)
    public int getRequestHandle() {
        return requestHandle;
    }

    public void setRequestHandle(int requestHandle) {
        this.requestHandle = requestHandle;
    }

    @Basic
    @Column(name = "time", nullable = false)
    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    @Basic
    @Column(name = "tgt", nullable = false, length = 1024)
    public String getTgt() {
        return tgt;
    }

    public void setTgt(String tgt) {
        this.tgt = tgt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TargetEventEntity that = (TargetEventEntity) o;
        return id == that.id &&
                requestHandle == that.requestHandle &&
                Objects.equals(time, that.time) &&
                Objects.equals(tgt, that.tgt);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, requestHandle, time, tgt);
    }

    @ManyToOne
    @JoinColumn(name = "request_handle", referencedColumnName = "id", nullable = false, insertable = false, updatable = false)
    public TranslationRequestEntity getTranslationRequestByRequestHandle() {
        return translationRequestByRequestHandle;
    }

    public void setTranslationRequestByRequestHandle(TranslationRequestEntity translationRequestByRequestHandle) {
        this.translationRequestByRequestHandle = translationRequestByRequestHandle;
    }
}
