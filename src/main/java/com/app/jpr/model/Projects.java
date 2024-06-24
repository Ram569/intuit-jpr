package com.app.jpr.model;

import jakarta.persistence.*;
import lombok.Data;
import org.antlr.v4.runtime.misc.NotNull;

import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "projects")
@Data
public class Projects implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @NotNull
    private String name;
    @Column(name="scm_url")
    private String scmURL;
    @Column(name="ci_url")
    private String ciURL;
    @Column(name="created_by")
    private String createdBy;
    @Column(name="created_date")
    private Date createDate;
}
