package Models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import Database.DatabaseConnection;

public class Member {
    private int id;
    private String name;
    private double fineAmount;

    public Member(String name) {
        this.name = name;
        this.fineAmount = 0.0;
    }

    // Get the name of the member
    public String getName() {
        return name;
    }

    public static void addMemberToDatabase(Member member) throws SQLException {
        String query = "INSERT INTO members (name, fine_amount) VALUES (?, ?)";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, member.name);
            preparedStatement.setDouble(2, member.fineAmount);
            preparedStatement.executeUpdate();
        }
    }

    public static Member getMemberById(int memberId) throws SQLException {
        String query = "SELECT * FROM members WHERE id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, memberId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                Member member = new Member(resultSet.getString("name"));
                member.id = resultSet.getInt("id");
                member.fineAmount = resultSet.getDouble("fine_amount");
                return member;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return "Member ID: " + id + ", Name: " + name + ", Fine: " + fineAmount;
    }
}