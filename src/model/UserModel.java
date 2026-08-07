package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import controller.DatabaseConnection;

public class UserModel {
    
    // Login စစ်ဆေးမည့် Method
    public boolean authenticate(String username, String password, String role) {
        // ယာယီ စစ်ဆေးချက် (Database မရှိသေးရင် ဒီအတိုင်း စမ်းနိုင်ပါတယ်)
        if ("admin".equals(username) && "123".equals(password) && "Admin".equals(role)) {
            return true;
        } else if ("user".equals(username) && "123".equals(password) && "User".equals(role)) {
            return true;
        } else if ("artist".equals(username) && "123".equals(password) && "Artist".equals(role)) {
            return true;
        }
        return false;
    }

    // Sign Up ပြုလုပ်သည့်အခါ Database ထဲသို့ User အသစ်အချက်အလက်များ ထည့်သွင်းမည့် Method
    public boolean registerUser(String username, String email, String password, String dob, String role) {
        // သင့်ရဲ့ Database schema တွင်ပါရှိသော ဇယားကွက်အတိုင်း INSERT query ရေးသားခြင်း
        String query = "INSERT INTO users (username, email, password_hash, date_of_birth, role) VALUES (?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection(); // သင့်ပရောဂျက်ရှိ Database Connection ပုံစံဖြင့် ချိတ်ဆက်ပါ
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setString(1, username);
            pstmt.setString(2, email);
            pstmt.setString(3, password); // လုံခြုံရေးအတွက် နောက်ပိုင်းတွင် Hashing (ဥပမာ- BCrypt) ပြောင်းလဲအသုံးပြုနိုင်ပါသည်
            pstmt.setString(4, dob);
            pstmt.setString(5, role);
            
            int rowsInserted = pstmt.executeUpdate();
            return rowsInserted > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            // Username သို့မဟုတ် Email ထပ်နေပါက (Duplicate entry) SQLException တက်မည်ဖြစ်ပြီး false ပြန်မည်
            return false;
        }
    }
}