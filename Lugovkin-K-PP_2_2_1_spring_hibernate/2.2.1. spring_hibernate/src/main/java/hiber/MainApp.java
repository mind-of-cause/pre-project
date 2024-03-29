package hiber;

import hiber.config.AppConfig;
import hiber.model.User;
import hiber.model.Car;
import hiber.service.UserService;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.List;

public class MainApp {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext context =
                new AnnotationConfigApplicationContext(AppConfig.class);

        UserService userService = context.getBean(UserService.class);

        Car car1 = new Car("BMW", 5);
        Car car2 = new Car("Mercedes", 7);
        Car car3 = new Car("Toyota", 3);
        Car car4 = new Car("Audi", 8);

        userService.add(new User("User1", "Lastname1", "user1@mail.ru", car1));
        userService.add(new User("User2", "Lastname2", "user2@mail.ru", car2));
        userService.add(new User("User3", "Lastname3", "user3@mail.ru", car3));
        userService.add(new User("User4", "Lastname4", "user4@mail.ru", car4));

        List<User> users = userService.listUsers();
        for (User user : users) {
            System.out.println("Id = " + user.getId());
            System.out.println("First Name = " + user.getFirstName());
            System.out.println("Last Name = " + user.getLastName());
            System.out.println("Email = " + user.getEmail());
            System.out.println("Car Model = " + user.getCar().getModel());
            System.out.println("Car Series = " + user.getCar().getSeries());
            System.out.println();
        }

        User user = userService.getUserByCarModelAndSeries("BMW", 5);
        if (user != null) {
            System.out.println("Пользователь, владеющий BMW серии 5: " + user.getFirstName() + " " + user.getLastName());
        } else {
            System.out.println("Пользователь, владеющий BMW серии 5, не найден.");
        }
        context.close();
    }
}