package com.example.accessingdata;

import javax.persistence.*;
import java.math.BigInteger;

@Entity
@Table(name = "lab_data")

public class LabTerms {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Integer id;

    private String lab_name;

    private BigInteger deadline;

    private String group_nums;

    private String subj;

    private Integer max_points;

    private String file_source;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getLab_name() {
        return lab_name;
    }

    public void setLab_name(String lab_name) {
        this.lab_name = lab_name;
    }

    public BigInteger getDeadline() {
        return deadline;
    }

    public void setDeadline(BigInteger deadline) {
        this.deadline = deadline;
    }

    public String getGroup_nums() {
        return group_nums;
    }

    public void setGroup_nums(String group_nums) {
        this.group_nums = group_nums;
    }

    public String getSubj() {
        return subj;
    }

    public void setSubj(String subj) {
        this.subj = subj;
    }

    public Integer getMax_points() {
        return max_points;
    }

    public void setMax_points(Integer max_points) {
        this.max_points = max_points;
    }

    public String getFile_source() {
        return file_source;
    }

    public void setFile_source(String file_source) {
        this.file_source = file_source;
    }

    public String toString() {
        return lab_name + " " + deadline.toString() + " " + max_points.toString();
    }
}