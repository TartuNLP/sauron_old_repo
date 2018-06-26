package ee.ut.sauron.entities;

import javax.persistence.*;
import java.sql.Date;
import java.util.Objects;

@Entity
@Table(name = "source_event", schema = "neurotolge_api", catalog = "")
public class SourceEventEntity {
    private int id;
    private int requestHandle;
    private Date time;
    private String data;
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
    @Column(name = "data", nullable = false, length = 1024)
    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SourceEventEntity that = (SourceEventEntity) o;
        return id == that.id &&
                requestHandle == that.requestHandle &&
                Objects.equals(time, that.time) &&
                Objects.equals(data, that.data);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, requestHandle, time, data);
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
