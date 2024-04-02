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
    public CarController(CarService carService) {
        this.carService = carService;
    }

    private final CarService carService;

    @GetMapping("/cars")
    public String getCars(Model model, @RequestParam(value = "count", required = false) Integer count) {
        List<Car> cars = carService.getCars(count);
        model.addAttribute("cars", cars);
        return "cars";
    }
}