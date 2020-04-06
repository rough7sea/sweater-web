package com.roughsea.stream;

import com.sun.istack.internal.NotNull;
import com.sun.xml.internal.ws.api.model.wsdl.WSDLOutput;
import lombok.*;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@Data
@AllArgsConstructor
class Event{
    @NotNull
    private UUID id;
    private LocalDateTime timeTag;
    private String description;
}

@Data
@AllArgsConstructor
class Employee{
    private String firstName;
    private String lastName;
    private int ID;
    private int age;
    private POSITION position;
}

enum POSITION{
    CHEF, MANAGER, WORKER
}
@Data
@RequiredArgsConstructor
@ToString(of = {"id", "child"})
class Department{
    private final int id;
    private final int parent;
    private final String name;

    private Set<Department> child = new HashSet<>();
}

public class Streams {
    private List<Employee> emps = Arrays.asList(
            new Employee("A", "a", 1, 43, POSITION.CHEF ),
            new Employee("B", "b", 2, 22, POSITION.WORKER ),
            new Employee("C", "c", 3, 23, POSITION.WORKER ),
            new Employee("D", "d", 4, 24, POSITION.WORKER ),
            new Employee("E", "e", 6, 19, POSITION.WORKER ),
            new Employee("F", "f", 7, 20, POSITION.WORKER ),
            new Employee("G", "g", 11, 44, POSITION.MANAGER ),
            new Employee("H", "h", 12, 34, POSITION.MANAGER ),
            new Employee("I", "i", 13, 33, POSITION.MANAGER ),
            new Employee("J", "j", 14, 30, POSITION.MANAGER )
    );

    private List<Department> deps = Arrays.asList(
            new Department(1, 0, "Head"),
            new Department(2, 1, "West"),
            new Department(3, 1, "East"),
            new Department(4, 2, "Germany"),
            new Department(5, 2, "France"),
            new Department(6, 3, "China"),
            new Department(7, 3, "Japan")
    );

    @Test
    public void creation() throws IOException {
        Stream<String> lines = Files.lines(Paths.get("some.txt"));
        Stream<Path> list = Files.list(Paths.get("/."));
        Stream<Path> walk = Files.walk(Paths.get("/."), 3);

        IntStream intStream = IntStream.of(1, 2, 3, 4);
        DoubleStream doubleStream = DoubleStream.of(1.2, 3.4);
        IntStream range = IntStream.range(10, 100); // 10 .. 99
        IntStream intStream1 = IntStream.rangeClosed(10, 100); // 10 .. 100

        int[] ints = {1, 2, 3, 4};
        IntStream stream = Arrays.stream(ints);

        Stream<String> stringStream = Stream.of("1", "2", "#");
        Stream<? extends Serializable> stream1 = Stream.of(1, "2", "#");

        Stream<String> build = Stream.<String>builder()
                .add("Mike")
                .add("Dave")
                .build();

        Stream<Employee> stream2 = emps.stream();
        Stream<Employee> employeeStream = emps.parallelStream();

        Stream<Event> generate = Stream.generate(() ->
                new Event(UUID.randomUUID(), LocalDateTime.now(), ""));

        Stream<Integer> iterate = Stream.iterate(1950, val -> val + 3);

        Stream<Object> concat = Stream.concat(employeeStream, build);
    }

    @Test
    public void terminate(){
        emps.stream().count();

        emps.stream().forEach(employee -> System.out.println(employee.getAge()));
        emps.forEach(employee -> System.out.println(employee.getAge()));

        emps.stream().forEachOrdered(employee -> System.out.println(employee.getAge()));

        emps.stream().collect(Collectors.toList());
        emps.stream().toArray();

        Map<Integer, String> map = emps.stream().collect(Collectors.toMap(
                Employee::getAge,
                emp -> String.format("%s %s", emp.getFirstName(), emp.getLastName())
        ));

        IntStream intStream = IntStream.of(100, 200, 300, 400);
        intStream.reduce(Integer::sum).getAsInt(); // get int
//        intStream.reduce(Integer::sum).orElse(0); // the same
        System.out.println(deps.stream().reduce(this::reducer));

        IntStream.of(100, 200, 300, 400).average();
        IntStream.of(100, 200, 300, 400).max();
        IntStream.of(100, 200, 300, 400).min();
        IntStream.of(100, 200, 300, 400).sum();
        IntStream.of(100, 200, 300, 400).summaryStatistics();

        emps.stream().max((Comparator.comparingInt(Employee::getAge))); // find max age

        emps.stream().findAny();
        emps.stream().findFirst();

        emps.stream().noneMatch(employee -> employee.getAge() > 60); // true
        emps.stream().allMatch(employee -> employee.getAge() > 18); // true
        emps.stream().anyMatch(employee -> employee.getPosition() == POSITION.CHEF); // true
    }

    @Test
    public void transform(){
        IntStream.of(100, 200, 300, 400).mapToLong(Long::valueOf);
        IntStream.of(100, 200, 300, 400).mapToObj(value ->
                new Event(UUID.randomUUID(),
                        LocalDateTime.of(value, 12, 1, 1, 0),"")
                );

        IntStream.of(100, 200, 300, 400, 100, 200).distinct(); // 100, 200, 300, 400

        Stream<Employee> employeeStream = emps.stream().filter(employee -> employee.getPosition() != POSITION.CHEF);

        emps.stream()
                .skip(3)
                .limit(5);

        emps.stream()
                .sorted(Comparator.comparingInt(Employee::getAge))
                .peek(employee -> employee.setAge(18))
                .map(emp -> String.format("%s %s", emp.getFirstName(), emp.getLastName()));

        IntStream.of(100, 200, 300, 400)
                .flatMap(value -> IntStream.of(value -50, value))
                .forEach(System.out::println);
    }

    @Test
    public void real(){
        Stream<Employee> emp = emps.stream()
                .filter(employee ->
                        employee.getAge() <= 36 && employee.getPosition() != POSITION.WORKER)
                .sorted(Comparator.comparing(Employee::getLastName));
        print(emp);

        Stream<Employee> sorted = emps.stream()
                .filter(employee -> employee.getAge() > 40)
                .sorted(Comparator.comparing(Employee::getAge))
                .limit(4);
        print(sorted);

        IntSummaryStatistics intSummaryStatistics = emps.stream()
                .mapToInt(Employee::getAge)
                .summaryStatistics();
        System.out.println(intSummaryStatistics);
    }


    private void print(Stream<Employee> stream){
        stream
                .map(emp -> String.format(
                        "%4d | %-15s %-10s age %s %s",
                        emp.getID(),
                        emp.getLastName(),
                        emp.getFirstName(),
                        emp.getAge(),
                        emp.getPosition()
                ))
                .forEach(System.out::println);
        System.out.println();
    }

    public Department reducer(Department parent, Department child){

        if (child.getParent() == parent.getId()){
            parent.getChild().add(child);
        }else {
            parent.getChild().forEach(subParent -> reducer(subParent, child));
        }
        return parent;
    }
}