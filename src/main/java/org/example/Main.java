package org.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

public class Main
{
    public static void main(String[] args)
    {
        ArrayList<Student> italianStudents = new ArrayList<>();
        ArrayList<Student> germanStudents = new ArrayList<>();

        Map<String, ArrayList<Student>> countryToStudentsMap = new HashMap<>();
        countryToStudentsMap.put("Italy", italianStudents);
        countryToStudentsMap.put("Germany", germanStudents);

        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/newdb", "developer", "developer");
             Statement statement = connection.createStatement())
        {
            try (ResultSet resultSet = statement.executeQuery("SELECT first_name, last_name, country FROM students;"))
            {
                while (resultSet.next())
                {
                    String country = resultSet.getString("country");
                    if (countryToStudentsMap.containsKey(country))
                    {
                        Student student = new Student(resultSet.getString("first_name"), resultSet.getString("last_name"));
                        countryToStudentsMap.get(country).add(student);
                    }
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        for (Map.Entry<String, ArrayList<Student>> entry : countryToStudentsMap.entrySet())
        {
            System.out.printf("%nStudents from %s:%n", entry.getKey());
            for (Student student : entry.getValue())
            {
                System.out.printf("%s %s%n", student.getName(), student.getSurname());
            }
        }
    }
}