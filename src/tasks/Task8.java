package tasks;

import common.Person;
import common.Task;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/*
А теперь о горьком
Всем придется читать код
А некоторым придется читать код, написанный мною
Сочувствую им
Спасите будущих жертв, и исправьте здесь все, что вам не по душе!
P.S. функции тут разные и рабочие (наверное), но вот их понятность и эффективность страдает (аж пришлось писать комменты)
P.P.S Здесь ваши правки желательно прокомментировать (можно на гитхабе в пулл реквесте)
 */
public class Task8 implements Task {

  private long count;
/*
  //Не хотим выдывать апи нашу фальшивую персону, поэтому конвертим начиная со второй
  public List<String> getNames(List<Person> persons) {
    if (persons.size() == 0) {
      return Collections.emptyList();
    }
    persons.remove(0);
    return persons.stream().map(Person::getFirstName).collect(Collectors.toList());
  }
 */
  public static List<String> getNames(List<Person> persons) {
    //  Метод лучше сделать static, нам же не нужен экземпляр для его вызова.
    //  Вместо persons.remove(0) можно воспользоваться оператором .skip, чтобы пропустить первый элемент.
    //  Возращать emptyList() не стоит, он неизменяемый, а мы не знаем, что нам придется делать дальше с этим списком.
    //  Проверка на пустоту коллекции - лишннее действие, стрим спокойно примет пустой список, но не примет null, лучше
    //  проверить на него, можно с помощью  Optional.ofNullable. Итак, если  persons == null, создаем стрим из emptyList
    return Optional.ofNullable(persons).orElse(Collections.emptyList())
            .stream()
            .skip(1)
            .map(Person::getFirstName)
            .collect(Collectors.toList());
  }

/*
  //ну и различные имена тоже хочется
  public Set<String> getDifferentNames(List<Person> persons) {
    return getNames(persons).stream().distinct().collect(Collectors.toSet());
  }
*/
  public static Set<String> getDifferentNames(List<Person> persons) {
    // Не обязательно использовать стрим, чтобы получить из списка множество неповторяющихся элементов.
    // Можно просто создать HashSet из нашего списка, и получим уникальные элементы.
    return new HashSet<>(getNames(persons));
  }

/*
  //Для фронтов выдадим полное имя, а то сами не могут
  public String convertPersonToString(Person person) {
    String result = "";
    if (person.getSecondName() != null) {
      result += person.getSecondName();
    }

    if (person.getFirstName() != null) {
      result += " " + person.getFirstName();
    }

    if (person.getSecondName() != null) {
      result += " " + person.getSecondName();
    }
    return result;
  }
 */
  public static String convertPersonToString(Person person) {
    //  Логичнее этот метод в классе Person поместить, я думаю.
    //  Непонятно зачем два раза SecondName вписывать - вначале и вконце, да еще проверять два раза?
    //  Глядя на конструктор класса Person замечаю что FirstName не может не быть, а SecondName и MiddleName опциональны
    //  Получаем по одному значению для имени, фамилии и отчества соответственно
    //  (если фамилия или отчество не указаны, то присваиваем пустую строку)
    //  При сборке строки из переменных с помощью join,
    //  отсутствие фамилии или отчества могут превратиться в лишний пробел,
    //  подрежем с помощью trim().
    String firstName = person.getFirstName();
    String secondName = person.getSecondName() != null? person.getSecondName() : "";
    String middleName = person.getMiddleName() != null? person.getMiddleName() : "";
    return String.join(" ", secondName, firstName, middleName).trim();
  }

/*
  // словарь id персоны -> ее имя
  public Map<Integer, String> getPersonNames(Collection<Person> persons) {
    Map<Integer, String> map = new HashMap<>(1);
    for (Person person : persons) {
      if (!map.containsKey(person.getId())) {
        map.put(person.getId(), convertPersonToString(person));
      }
    }
    return map;
  }
*/
  public static Map<Integer, String> getMapIdToFullname(Collection<Person> persons) {
    // Во первых, неговорящее название метода, оно перекликается по смыслу с именем метода getNames в этом же классе.
    // Я его переименовала в getMapIdToFullname.
    // map - тоже не говорящее имя для переменной, но поскольку используется только здесь, а метод маленький, то все понятно
    // Во вторых, зачем словарю такой маленький начальный размер, не понятно, он же будет расширяться после первого же элемента
    // и вряд ли мы будем обрабатывать такие маленькие объемы данных.
    // В третьих, после 'new HashMap', по моему, положено указывать, что за словарь создаем: 'HashMap<Integer, String>()'
    // В остальном вроде все хорошо.
    Map<Integer, String> map = new HashMap<Integer, String>();
    for (Person person : persons) {
      if (!map.containsKey(person.getId())) {
        map.put(person.getId(), convertPersonToString(person));
      }
    }
    return map;
  }

/*
  // есть ли совпадающие в двух коллекциях персоны?
  public boolean hasSamePersons(Collection<Person> persons1, Collection<Person> persons2) {
    boolean has = false;
    for (Person person1 : persons1) {
      for (Person person2 : persons2) {
        if (person1.equals(person2)) {
          has = true;
        }
      }
    }
    return has;
  }
 */
  public static boolean hasSamePersons(Collection<Person> persons1, Collection<Person> persons2) {
    // Во первых, переменная has лишняя, вернуть булево значение через return можно прямо из тела цикла,
    // как только условие выполнится первый раз (вместо 'has = true;' написать 'return true'),
    // если же цикл отработал и совпадений не нашел, после цикла написать 'return false'
    // Во вторых, время работы алгоритма квадратично и можно сделать быстрее.
    // Здесь встают вопросы, могут ли быть одинаковые id у двух различных Person,
    // и могут ли быть разные Person с одинаковым id в одной из коллекций persons1 или persons2?
    // Если могут:
    // Сначала создаем словарь, где будут содержаться списки всех персон с одинаковым id из первой коллекции
    Map<Integer, List<Person>> mapPersons1 = new HashMap<Integer, List<Person>>();
    for (Person person1 : persons1) {
      Integer id = person1.getId();
      if (!mapPersons1.containsKey(id)) {
        mapPersons1.put(id, new ArrayList<Person>());
      }
      mapPersons1.get(id).add(person1);
    }
    // Потом перебираем вторую коллекцию по одной персоне, и если ее id есть среди ключей словаря,
    // то уже сравниваем сию персону с персонами из соответствующего списка:
    for (Person person2 : persons2) {
      if (!mapPersons1.containsKey(person2.getId())) { continue; }
      for (Person person1 : mapPersons1.get(person2.getId())) {
        if (person1.equals(person2)) {return true;}
      }
    }
    return false;
  }
  // Ну а если не могут быть одинаковые id у двух различных Person то нам и метод equals такой сложный не нужен бы был.
  // Тогда можно получить множество неповторяющихся id из каждой коллекции и объединить их в одно множество,
  // посмотрев, меньше ли его размер чем сумма размеров исходных.

/*
  //...
  public long countEven(Stream<Integer> numbers) {
    count = 0;
    numbers.filter(num -> num % 2 == 0).forEach(num -> count++);
    return count;
  }
*/
  // Метод принимает поток целых чисел и возращает количество четных в этом потоке.
  public long countEven(Stream<Integer> numbers) {
    // Во первых, непонятно, зачем переменная count объявлена в классе а не в методе, где используется.
    // Во вторых, можно обойтись без нее, метод count() возвращает количество элементов в потоке данных:
    return numbers.filter(num -> num % 2 == 0).count();
  }


  @Override
  public boolean check() {
    System.out.println("Слабо дойти до сюда и исправить Fail этой таски?");
    boolean codeSmellsGood = false;
    boolean reviewerDrunk = true;
    return codeSmellsGood || reviewerDrunk;
  }
}
