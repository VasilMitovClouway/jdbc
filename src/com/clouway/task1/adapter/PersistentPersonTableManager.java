package com.clouway.task1.adapter;

import com.clouway.task1.core.Person;
import com.clouway.task1.core.Provider;
import com.clouway.task1.core.TableManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Vasil Mitov <v.mitov.clouway@gmail.com>
 */
public class PersistentPersonTableManager implements TableManager {
  private Provider<Connection> provider;

  public PersistentPersonTableManager(Provider<Connection> provider) {
    this.provider = provider;
  }

  @Override
  public List<Person> display() {
    Connection connection = provider.get();
    String query = "SELECT * FROM PEOPLE";
    List<Person> result = new LinkedList<>();
    try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
      ResultSet rs = preparedStatement.executeQuery(query);
      while (rs.next()) {
        String name = rs.getString(2);
        String egn = rs.getString(3);
        Integer age = rs.getInt(4);
        String email = rs.getString(5);
        result.add(new Person(name, egn, age, email));
      }
    } catch (SQLException e) {
      e.printStackTrace();
    } finally {
      try {
        connection.close();
      } catch (SQLException e) {
        e.printStackTrace();
      }
    }
    return result;
  }

  @Override
  public void delete() {
    Connection connection = provider.get();
    String query = "DROP TABLE PEOPLE";
    try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
      preparedStatement.execute();
    } catch (SQLException e) {
      e.printStackTrace();
    }
    finally {
      try {
        connection.close();
      } catch (SQLException e) {
        e.printStackTrace();
      }
    }
  }

  @Override
  public void addColumn(String columnName, String type) {
    Connection connection = provider.get();
    String query = "ALTER TABLE PEOPLE ADD (?) (?)";
    try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
      preparedStatement.setString(2, columnName);
      preparedStatement.setString(3, type);
      preparedStatement.execute();
    } catch (SQLException e) {
      e.printStackTrace();
    }
    finally {
      try {
        connection.close();
      } catch (SQLException e) {
        e.printStackTrace();
      }
    }
  }
}
