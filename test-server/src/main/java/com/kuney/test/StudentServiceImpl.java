package com.kuney.test;

import com.kuney.rpc.api.Student;
import com.kuney.rpc.api.StudentService;

import java.util.ArrayList;
import java.util.List;

/**
 * @author kuneychen
 * @since 2022/7/20 16:50
 */
public class StudentServiceImpl implements StudentService {
    @Override
    public Student getStudent() {
        return new Student(101, "张三", '男');
    }

    @Override
    public List<Student> createList(int size) {
        ArrayList<Student> list = new ArrayList<>();
        for (int i = 1; i <= size; i++) {
            Student student = new Student();
            student.setId(100 + i);
            student.setName("student-name-" + i);
            student.setSex(i % 2 == 0 ? '男' : '女');
            list.add(student);
        }
        return list;
    }
}
