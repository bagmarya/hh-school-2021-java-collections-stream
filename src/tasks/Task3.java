package tasks;

import common.Person;
import common.Task;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/*
Задача 3
Отсортировать коллекцию сначала по фамилии, по имени (при равной фамилии), и по дате создания (при равных фамилии и имени)
 */
public class Task3 implements Task {

  // !!! Редактируйте этот метод !!!
  private List<Person> sort(Collection<Person> persons) {

//Сначала, в надежде на то что сортировка устойчива, сделала так (и это сработало):
//    List<Person> sortedPersonList = persons.stream()
//            .sorted(Comparator.comparing(Person::getCreatedAt))
//            .sorted(Comparator.comparing(Person::getSecondName))
//            .sorted(Comparator.comparing(Person::getFirstName))
//            .collect(Collectors.toList());

    // Создаем компоратор для наших условий сортировки
    Comparator<Person> compareByName = Comparator
            .comparing(Person::getFirstName)
            .thenComparing(Person::getSecondName)
            .thenComparing(Person::getCreatedAt);

    // Из входного списка создаем стрим и сортируем его с помощью нашего компоратора. Собираем в список
    List<Person> sortedPersonList = persons.stream()
            .sorted(compareByName)
            .collect(Collectors.toList());

    return sortedPersonList;
  }

  @Override
  public boolean check() {
    Instant time = Instant.now();
    List<Person> persons = List.of(
        new Person(1, "Oleg", "Ivanov", time),
        new Person(2, "Vasya", "Petrov", time),
        new Person(3, "Oleg", "Petrov", time.plusSeconds(1)),
        new Person(4, "Oleg", "Ivanov", time.plusSeconds(1))
    );
    List<Person> sortedPersons = List.of(
        new Person(1, "Oleg", "Ivanov", time),
        new Person(4, "Oleg", "Ivanov", time.plusSeconds(1)),
        new Person(3, "Oleg", "Petrov", time.plusSeconds(1)),
        new Person(2, "Vasya", "Petrov", time)
    );
    return sortedPersons.equals(sort(persons));
  }
}
