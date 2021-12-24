package tasks;

import common.ApiPersonDto;
import common.Area;
import common.Person;
import common.Task;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/*
Имеются
- коллекция персон Collection<Person>
- словарь Map<Integer, Set<Integer>>, сопоставляющий каждой персоне множество id регионов
- коллекция всех регионов Collection<Area>
На выходе хочется получить множество строк вида "Имя - регион". Если у персон регионов несколько, таких строк так же будет несколько
 */
public class Task6 implements Task {

  private Set<String> getPersonDescriptions(Collection<Person> persons,
                                            Map<Integer, Set<Integer>> personAreaIds,
                                            Collection<Area> areas) {
    // создаем хэш-таблицу из пар (id - name), чтобы получать название региона по id региона
    Map<Integer, String> areaTable = areas.stream().
            collect(Collectors.toMap(Area::getId, Area::getName));


                    //создаем поток из списка персон:
    return persons.stream()
                    // оператору flatMap мы будем отдавать потоки строк вида "Имя - регион",
                    // каждый такой поток создается для отдельного объекта Person:
                    // Сначала получаем id объекта Person,
                    // затем по нему из словаря personAreaIds получаем множество id регионов,
                    // из которого создаем поток id обрабатываемый оператором map для получения строк "Имя - регион"
            .flatMap(p-> personAreaIds.get(p.getId()).stream().
                    map(id -> String.format("%s - %s", p.getFirstName(), areaTable.get(id))))
                    // Собираем HashSet на выходе из потока
            .collect(Collectors.toSet());
  }

  @Override
  public boolean check() {
    List<Person> persons = List.of(
        new Person(1, "Oleg", Instant.now()),
        new Person(2, "Vasya", Instant.now())
    );
    Map<Integer, Set<Integer>> personAreaIds = Map.of(1, Set.of(1, 2), 2, Set.of(2, 3));
    List<Area> areas = List.of(new Area(1, "Moscow"), new Area(2, "Spb"), new Area(3, "Ivanovo"));
    return getPersonDescriptions(persons, personAreaIds, areas)
        .equals(Set.of("Oleg - Moscow", "Oleg - Spb", "Vasya - Spb", "Vasya - Ivanovo"));
  }
}
