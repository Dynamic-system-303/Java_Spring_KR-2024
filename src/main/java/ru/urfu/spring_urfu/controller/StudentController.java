package ru.urfu.spring_urfu.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import ru.urfu.spring_urfu.entity.Student;
import ru.urfu.spring_urfu.repository.StudentRepository;

import java.util.Optional;

@Slf4j
@Controller
public class StudentController {

    @Autowired
    private StudentRepository studentRepository;

    @GetMapping("/list")
    public ModelAndView getAllStudents() {
        log.info("/list -> connection");
        ModelAndView mav = new ModelAndView("list-students");
        var students = studentRepository.findAll();
        mav.addObject("students", students);
        return mav;
    }

    @GetMapping("/addStudentForm")
    public ModelAndView addStudentForm() {
        ModelAndView mav = new ModelAndView("add-student-form");
        Student student = new Student();
        mav.addObject("student", student);
        return mav;
    }

    @PostMapping("/saveStudent")
    public String saveStudent(@ModelAttribute Student student) {
        studentRepository.save(student);
        return "redirect:/list";
    }

    @GetMapping("/showUpdateForm")
    public ModelAndView showUpdateForm(@RequestParam Long studentId) {
        ModelAndView mav = new ModelAndView("add-student-form");
        mav.addObject("student", studentRepository.findById(studentId).orElseGet(Student::new));
        return mav;
    }

    @GetMapping("/deleteStudent")
    public String deleteStudent(@RequestParam Long studentId) {
        studentRepository.deleteById(studentId);
        return "redirect:/list";
    }

}
