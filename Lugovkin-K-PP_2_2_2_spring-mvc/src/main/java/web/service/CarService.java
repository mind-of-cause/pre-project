package web.service;

import web.Car;
import java.util.List;

public interface CarService {

    List<Car> getCars(Integer count);
}