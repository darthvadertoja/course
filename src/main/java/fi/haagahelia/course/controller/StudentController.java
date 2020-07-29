package fi.haagahelia.course.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import fi.haagahelia.course.model.Course;
import fi.haagahelia.course.model.Student;
import fi.haagahelia.course.repository.CourseRepository;
import fi.haagahelia.course.repository.StudentRepository;

@Controller
public class StudentController {
	@Autowired
    private StudentRepository studentRepository;

	@Autowired
    private CourseRepository courseRepository;
	
	@RequestMapping("/login")
	public String login()
	{

		return "login";
    }	
	
	@RequestMapping("/students")
	public String index(Model model) {
		List<Student> students = (List<Student>) studentRepository.findAll();
		model.addAttribute("students", students);
    	return "students";
    }

    @RequestMapping(value = "add")
    public String addStudent(Model model){
    	model.addAttribute("student", new Student());
        return "addStudent";
    }	
	
    @RequestMapping(value = "save", method = RequestMethod.POST)
    public String save(Student student){
        studentRepository.save(student);
    	return "redirect:/students";
    }
    
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.GET)
    public String editRemoveEmployee(@PathVariable("id") Long studentId, Model model) {
    	studentRepository.deleteById(studentId);
        return "redirect:/students";
    }    
    
    @RequestMapping(value = "addStudentCourse/{id}", method = RequestMethod.GET)
    public String addCourse(@PathVariable("id") Long studentId, Model model){
    	model.addAttribute("courses", courseRepository.findAll());
		model.addAttribute("student", studentRepository.findById(studentId).get());
    	return "addStudentCourse";
    }
    
    //@{/student/{id}/courses(id=${student.id})}
    @RequestMapping(value="/student/{id}/courses", method=RequestMethod.GET)
	public String studentsAddCourse(@PathVariable Long id, @RequestParam Long courseId, Model model) {
		Course course = courseRepository.findById(courseId).get();//tutaj wiemy ze na pewno bedzie kurs
		//i idziemy w ciemno

		//nie jestesmy pewni czy ktos nie usunal studenta z systemu wiec lepiej sie zabezpieczyc

		Optional<Student> mozliweZePobralemStudentaAMozeNicSieNieUdaloPobrac = studentRepository.findById(id);


		//jezeli isPResent zwroci true to znaczy, ze jest tam pelny obiekt
		if (mozliweZePobralemStudentaAMozeNicSieNieUdaloPobrac.isPresent())
		{
			//skoro juz wiemy ze udalo sie pobrac takiego studenta z bazy to  mozemy go oficjalnie wyciagnac
			//  z naszego testu
			Student student = mozliweZePobralemStudentaAMozeNicSieNieUdaloPobrac.get();

			//a skoro juz wiemy, ze istnieje to mozemy go jeszcze raz (zbednie) wyciagnac z bazy
			//Student student = studentRepository.findById(id).get();



			if (student.hasCourse(course))
			{
				///jezeli posiada juz kurs to nic nie rob
				System.out.println("Student o ID : " + id + " posiada juz kurs o id: " + courseId);
			}
			else	//odpowiednik tego elsa to jest if (!student.hasCourse(course))
			{
				student.getCourses().add(course);
			}
			studentRepository.save(student);
		}
		else
		{
			System.out.println("BRAKUJE TEGO UZYTKOWNIKA W BAZIE: " + id);
		}
		return "redirect:/students";
	}    
    
    @RequestMapping(value = "getstudents", method = RequestMethod.GET)
    public @ResponseBody List<Student> getStudents() {
            return (List<Student>) studentRepository.findAll();
    }      
}
