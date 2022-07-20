package com.kuney.rpc.api;

import java.util.List;

/**
 * @author kuneychen
 * @since 2022/7/20 16:47
 */
public interface StudentService {

    Student getStudent();

    List<Student> createList(int size);

}
