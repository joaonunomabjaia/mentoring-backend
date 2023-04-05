package mz.org.fgh.mentoring.util;

import mz.org.fgh.mentoring.api.RestAPIResponse;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Calendar;
@MappedSuperclass
public abstract class BaseEntity implements RestAPIResponse, Serializable {
    @Id
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )
    @Column(
            name = "ID",
            nullable = false
    )
    private Long id;
    @Column(
            name = "UUID",
            length = 50,
            nullable = false
    )
    private String uuid;
    @Column(
            name = "CREATED_BY",
            length = 50,
            nullable = false
    )
    private String createdBy;
    @Column(
            name = "CREATED_AT",
            nullable = false
    )
    @Temporal(TemporalType.TIMESTAMP)
    private Calendar createdAt;
    @Column(
            name = "UPDATED_BY",
            length = 50
    )
    private String updatedBy;
    @Column(
            name = "UPDATED_AT"
    )
    @Temporal(TemporalType.TIMESTAMP)
    private Calendar updatedAt;
}
