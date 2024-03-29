package web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import web.Car;
import web.service.CarService;

import java.util.List;


@Controller
public class CarController {

    @Autowired
    private CarService carService;

    @GetMapping("/cars")
    public String getCars(Model model, @RequestParam(value = "count", required = false) Integer count) {
        List<Car> cars = carService.getAllCars();
        if (count != null && count < 5) {
            cars = cars.subList(0, count);
        }
        model.addAttribute("cars", cars);
        return "cars";
    }
}