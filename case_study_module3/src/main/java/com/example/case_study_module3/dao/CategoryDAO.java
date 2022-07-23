package com.example.case_study_module3.dao;

import com.example.case_study_module3.model.Category;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CategoryDAO implements ICategoryDAO{
    private String jdbcURL = "jdbc:mysql://localhost:3306/user_manager?useSSL=false";
    private String jdbcUsername = "root";
    private String jdbcPassword = "CHUcu123456";
    protected Connection getConnection() {
        Connection connection = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection(jdbcURL, jdbcUsername, jdbcPassword);
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return connection;
    }
	private static final String INSERT_CATEGORY_SQL = "INSERT INTO category" + "(name) VALUES "+ " (?);";  
    private static final String SELECT_CATEGORY_BY_ID = "select id,namefrom category where id =?";
    private static final String SELECT_ALL_CATEGORY = "select * from category";
    private static final String DELETE_CATEGORY_SQL = "delete from category where id = ?;";
    private static final String UPDATE_CATEGORY_SQL = "update category set name = ? where id = ?;";
    
    public void CategoryDao() {}
    
    public void insertCategory(Category category) throws SQLException {
        System.out.println(INSERT_CATEGORY_SQL);

        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(INSERT_CATEGORY_SQL)) {
            preparedStatement.setString(1, category.getName()); 
            System.out.println(preparedStatement);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            printSQLException(e);
        }
    }

    public Category selectCategory(int id) {
    	Category category = null;

        try (
                Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_CATEGORY_BY_ID);) {
            preparedStatement.setInt(1, id);
            System.out.println(preparedStatement);
         
            ResultSet rs = preparedStatement.executeQuery();
            
            while (rs.next()) {
                String name = rs.getString("name"); 
                 
                category = new Category(id, name  );
            }
        } catch (SQLException e) {
            printSQLException(e);
        }
        return category;
    }

    public List<Category> selectAllCategory() {
        List<Category> category = new ArrayList<>();

        try ( Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL_CATEGORY);) {
            System.out.println(preparedStatement);  
            ResultSet rs = preparedStatement.executeQuery(); 
            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                  
                category.add(new Category(id, name )); 
            }
        } catch (SQLException e) {
            printSQLException(e);
        }
        return category;
    }

    public boolean deleteCategory(int id) throws SQLException {
        boolean rowDeleted;

        try ( Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(DELETE_CATEGORY_SQL);) {
            statement.setInt(1, id);
            rowDeleted = statement.executeUpdate() > 0;
        }
        return rowDeleted;
    }

    public boolean updateCategory(Category category) throws SQLException {
        boolean rowUpdated;

        try ( Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(UPDATE_CATEGORY_SQL);) {
            statement.setInt(1, category.getId());
            statement.setString(2, category.getName());
            rowUpdated = statement.executeUpdate() > 0;
        }
        return rowUpdated;
    }

    private void printSQLException(SQLException ex) {
        for (Throwable e : ex) {
            if (e instanceof SQLException) {
                e.printStackTrace(System.err);
                System.err.println("SQLState: " + ((SQLException) e).getSQLState());
                System.err.println("Error Code: " + ((SQLException) e).getErrorCode());
                System.err.println("Message: " + e.getMessage());
                Throwable t = ex.getCause();
                while (t != null) {
                    System.out.println("Cause: " + t);
                    t = t.getCause();
                }
            }
        }
    }
	
}
