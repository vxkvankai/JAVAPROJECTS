package bawp.programming.coursedatabase;

import java.util.List;

public class courseServer {


    public List<Course> getCourseList()
    {
        List<Course> courseList = List.of();

        Course course = new Course();

        for (int i = 0; i < 10; i++)
        {
            course.setCourseName("Course Name " + i);
            course.setCourseAuthor("Author: James " + i);
            courseList.add(course);


        }

        return courseList;

    }

}
