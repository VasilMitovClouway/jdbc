package com.clouway.task5.core;

import java.util.List;

/**
 * @author Vasil Mitov <v.mitov.clouway@gmail.com>
 */
public interface UserRepository {
  void register(User user);

  List<User> getAll();
}
