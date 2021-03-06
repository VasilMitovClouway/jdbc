package com.clouway.task5.core;

import java.util.List;

/**
 * @author Vasil Mitov <v.mitov.clouway@gmail.com>
 */
public interface ContactRepository {
  void register(Contact contact);

  List<Contact> getAll();
}
