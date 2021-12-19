package tasks;

import common.Person;
import common.PersonService;
import common.Task;

import java.util.*;
import java.util.stream.Collectors;

/*
Задача 1
Метод на входе принимает List<Integer> id людей, ходит за ними в сервис
(он выдает несортированный Set<Person>, внутренняя работа сервиса неизвестна)
нужно их отсортировать в том же порядке, что и переданные id.
Оценить асимпотику работы
 */
public class Task1 implements Task {

  // !!! Редактируйте этот метод !!!
  private List<Person> findOrderedPersons(List<Integer> personIds) {
    Set<Person> persons = PersonService.findPersons(personIds);

    // создаем хэш-таблицу для наших объектов Person, чтобы находить их за константное время по id
    Map<Integer, Person> personsTable = new HashMap<Integer, Person>();

    // проходим по несортированному Set<Person>  и добавляем каждый объект в хэш-таблицу (каждый за О(1)),
    // в качестве ключа - id (линейное время)
    for (Person p : persons) {
      personsTable.put(p.getId(), p);
    }

    // создаем список, в который будем добавлять объекты Person в нужном нам порядке
    ArrayList<Person> personsOrderedList = new ArrayList<Person>();

    // проходим по исходному списку с id искомых объектов, извлекая каждый объект Person из таблицы (каждый за О(1) )
    // и помещая в список (каждый за О(1)) в том же порядке, что и переданные id. (линейное время)
    for (Integer Id : personIds) {
      personsOrderedList.add(personsTable.get(Id));
    }
    // В целом - время линейное.
    return personsOrderedList;
  }

  @Override
  public boolean check() {
    List<Integer> ids = List.of(1, 2, 3);

    return findOrderedPersons(ids).stream()
        .map(Person::getId)
        .collect(Collectors.toList())
        .equals(ids);
  }

}
