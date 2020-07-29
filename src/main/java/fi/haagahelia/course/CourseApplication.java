package fi.haagahelia.course;

import fi.haagahelia.course.model.Course;
import fi.haagahelia.course.model.Student;
import fi.haagahelia.course.repository.CourseRepository;
import fi.haagahelia.course.repository.StudentRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.HashSet;
import java.util.Set;

@SpringBootApplication
public class CourseApplication
{

    public static void main(String[] args)
    {
        SpringApplication.run(CourseApplication.class, args);
    }

    @Bean
    public CommandLineRunner demo(StudentRepository repository, CourseRepository crepository) {
        return (args) -> {
            // save students
            Student student1 = new Student("John", "Johnson", "IT", "john@john.com");
            repository.save(new Student("Steve", "Stevens", "IT", "steve.stevent@st.com"));
            repository.save(new Student("Mary", "Robinson", "IT", "mary@robinson.com"));
            repository.save(new Student("Kate", "Keystone", "Nursery","kate@kate.com"));
            repository.save(new Student("Diana", "Doll", "Business","diana@doll.com"));

            Course course1 = new Course("Programming Java");
            Course course2 = new Course("Spring Boot basics");
            crepository.save(new Course("Marketing 1"));
            crepository.save(new Course("Marketing 2"));

            crepository.save(course1);
            crepository.save(course2);

            Set<Course> courses = new HashSet<Course>();
            courses.add(course1);
            courses.add(course2);

            student1.setCourses(courses);
            repository.save(student1);
        };
    }
}
