import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class TestPassWord {
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String rawPassword = "123456"; // Mật khẩu nhập vào
        String encodedPassword = "$2a$10$zJ6Ms94AHmi6r5SZZ6NjDu/8bmfzVrC.mybDbBruCpYz4bC45hoI2"; // Mật khẩu trong database

        boolean isMatch = encoder.matches(rawPassword, encodedPassword);
        System.out.println("Mật khẩu đúng không? " + isMatch);
    }
}
