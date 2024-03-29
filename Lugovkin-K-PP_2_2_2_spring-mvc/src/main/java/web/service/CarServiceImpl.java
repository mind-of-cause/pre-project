package web.service;

import web.Car;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CarServiceImpl implements CarService{

    private final List<Car> cars;

    public CarServiceImpl() {

        cars = new ArrayList<>();

        cars.add(new Car("Rolls-Royce Ghost", "2360 kg", "250 km/h"));
        cars.add(new Car("Bugatti Veyron Super Sport", "1840 kg", "431 km/h"));
        cars.add(new Car("Lamborghini Aventador", "1625 kg", "350 km/h"));
        cars.add(new Car("Ford Mustang", "1680 kg", "246 km/h"));
        cars.add(new Car("Chevrolet Camaro", "1620 kg", "290 km/h"));
    }

    @Override
    public int getCarCount() {
        return cars.size();
    }

    @Override
    public List<Car> getAllCars() {
        return cars;
    }

    public List<Car> getCars(int count) {
        if (count >= getCarCount() || count <= 0) {
            return getAllCars();
        } else {
            return cars.subList(0, count);
        }
    }
}