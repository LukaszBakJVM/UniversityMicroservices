package org.example.course;

import jakarta.persistence.*;

import java.util.List;

@Entity
public class UniversityCourse {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String course;
    @ElementCollection
  private List<String> subjectId;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public List<String> getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(List<String> subjectId) {
        this.subjectId = subjectId;
    }
}
