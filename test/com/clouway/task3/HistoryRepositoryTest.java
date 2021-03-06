package com.clouway.task3;

import com.clouway.task3.adapter.ConnectionProvider;
import com.clouway.task3.adapter.PersistentStockHistoryRepository;
import com.clouway.task3.adapter.PersistentStockRepository;
import com.clouway.task3.core.Stock;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * @author Vasil Mitov <v.mitov.clouway@gmail.com>
 */
public class HistoryRepositoryTest {
  private ConnectionProvider connectionProvider = new ConnectionProvider("TASK3");
  private PersistentStockRepository stockRepository = new PersistentStockRepository(connectionProvider);
  private PersistentStockHistoryRepository historyRepository = new PersistentStockHistoryRepository(connectionProvider);

  @Before
  public void fillingUp() {
    deleteTable("STOCK_HISTORY");
    deleteTable("STOCK");
    Stock apple = new Stock("apple", 1.2, 3.5);
    Stock pear = new Stock("pear", 2.2, 4.6);
    Stock orange = new Stock("orange", 3.1, 5.1);
    Stock potato = new Stock("potato", 2.3, 6.5);
    stockRepository.register(apple);
    stockRepository.register(pear);
    stockRepository.register(orange);
    stockRepository.register(potato);
    stockRepository.updateQuantity("apple", 1.1);
    stockRepository.updateQuantity("pear", 1.2);
    stockRepository.updateQuantity("orange", 4.2);
    stockRepository.updateQuantity("potato", 6.7);
  }

  @Test
  public void displayFullHistory() throws Exception {
    List<Stock> actual = historyRepository.getAll();
    List<Stock> expected = new LinkedList<>();
    expected.add(new Stock("apple", 1.2, 3.5));
    expected.add(new Stock("pear", 2.2, 4.6));
    expected.add(new Stock("orange", 3.1, 5.1));
    expected.add(new Stock("potato", 2.3, 6.5));
    assertThat(actual, is(expected));
  }

  @Test
  public void displayInPages() throws Exception {
    List<Stock> actual = historyRepository.getPages(2, 2);
    List<Stock> expected = new LinkedList<>();
    expected.add(new Stock("orange", 3.1, 5.1));
    expected.add(new Stock("potato", 2.3, 6.5));
    assertThat(actual, is(expected));
  }

  @Test
  public void displayLastPageWithoutHavingenoughtStockToFillIt() throws Exception {
    List<Stock> actual = historyRepository.getPages(3, 1);
    List<Stock> expected = new LinkedList<>();
    expected.add(new Stock("potato", 2.3, 6.5));
    assertThat(actual, is(expected));
  }

  private void deleteTable(String name) {
    Connection connection = connectionProvider.get();
    String query = "DELETE FROM " + name;
    try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
      preparedStatement.execute();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }
}
