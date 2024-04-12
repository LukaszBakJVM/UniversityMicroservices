package org.example.course;

import java.util.List;

public record CourseDto(String course, List<Long> subjectId) {
}
