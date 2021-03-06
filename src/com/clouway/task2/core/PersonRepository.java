package com.clouway.task2.core;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

/**
 * @author Vasil Mitov <v.mitov.clouway@gmail.com>
 */
public interface PersonRepository {
  void register(Person person);

  Optional<Person> find(String egn);

  List<Person> findAllStartingWith(String letter);

  List<Person> findAllStayingAtTheSameTime(String city, Date date);

}
