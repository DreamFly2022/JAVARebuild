package com.hef.aop;

import com.hef.spring01.Student;

import java.util.List;

/**
 * @Date 2021/5/5
 * @Author lifei
 */
public class Klass02 {

    List<Student> students;

    public List<Student> getStudents() {
        return students;
    }

    public void setStudents(List<Student> students) {
        this.students = students;
    }

    public void dong() {
        System.out.println(this.getStudents());
    }
}
