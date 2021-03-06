package com.clouway.task3.adapter;

import com.clouway.task3.core.StockRepository;
import com.clouway.task3.core.Provider;
import com.clouway.task3.core.Stock;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

/**
 * @author Vasil Mitov <v.mitov.clouway@gmail.com>
 */
public class PersistentStockRepository implements StockRepository {
  private Provider<Connection> provider;

  public PersistentStockRepository(Provider<Connection> provider) {
    this.provider = provider;
  }

  @Override
  public void register(Stock stock) {
    Connection connection = provider.get();
    String query = "INSERT INTO STOCK VALUES(?,?,?)";
    try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
      preparedStatement.setString(1, stock.name);
      preparedStatement.setDouble(2, stock.price);
      preparedStatement.setDouble(3, stock.quantity);
      preparedStatement.execute();
    } catch (SQLException e) {
      e.printStackTrace();
    } finally {
      try {
        connection.close();
      } catch (SQLException e) {
        e.printStackTrace();
      }
    }
  }

  @Override
  public Optional<Stock> find(String name) {
    Connection connection = provider.get();
    String query = "SELECT * FROM STOCK WHERE NAME=?";
    try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
      preparedStatement.setString(1, name);
      ResultSet resultSet = preparedStatement.executeQuery();
      while (resultSet.next()) {
        String stockName = resultSet.getString(1);
        Double price = resultSet.getDouble(2);
        Double quantity = resultSet.getDouble(3);
        return Optional.of(new Stock(stockName, price, quantity));
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
    return Optional.empty();
  }

  @Override
  public void updateQuantity(String name, Double newQuantity) {
    Connection connection = provider.get();
    String query = "UPDATE STOCK SET QUANTITY=? WHERE NAME=?";
    try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
      preparedStatement.setDouble(1, newQuantity);
      preparedStatement.setString(2, name);
      preparedStatement.execute();
    } catch (SQLException e) {
      e.printStackTrace();
    } finally {
      try {
        connection.close();
      } catch (SQLException e) {
        e.printStackTrace();
      }
    }
  }
}

