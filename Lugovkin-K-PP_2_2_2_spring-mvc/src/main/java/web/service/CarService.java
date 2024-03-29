package web.service;

import web.Car;
import java.util.List;

public interface CarService {
    int getCarCount();
    List<Car> getAllCars();
}