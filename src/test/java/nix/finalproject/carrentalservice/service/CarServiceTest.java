package nix.finalproject.carrentalservice.service;

import nix.finalproject.carrentalservice.dto.CarDTO;
import nix.finalproject.carrentalservice.entity.*;
import nix.finalproject.carrentalservice.exceptions.ExceptionMessages;
import nix.finalproject.carrentalservice.exceptions.ServiceException;
import nix.finalproject.carrentalservice.repository.*;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class CarServiceTest {

    @Mock
    private CarRepository carRepository;
    @Mock
    private BrandRepository brandRepository;
    @Mock
    private EngineTypeRepository engineTypeRepository;
    @Mock
    private CarTypeRepository carTypeRepository;
    @Mock
    private CarStatusRepository carStatusRepository;

    private final CarService carService;

    public CarServiceTest() {
        MockitoAnnotations.initMocks(this);
        this.carService = new CarService(carRepository, brandRepository, engineTypeRepository, carTypeRepository, carStatusRepository);
    }

    @Test
    public void methodShouldReturnCorrectCarModel() {
        Long id1 = 1L;
        Long id2 = 2L;

        Brand brand1 = new Brand(id1, "Renault");
        Brand brand2 = new Brand(id2, "Peugeot");

        EngineType engineType1 = new EngineType(id1, "Diesel", "1.5");
        EngineType engineType2 = new EngineType(id2, "Gasoline", "1.4");

        CarType carType1 = new CarType(id1, "Hatchback");
        CarType carType2 = new CarType(id2, "Sedan");

        CarStatus carStatus1 = new CarStatus(id1, "Available");
        CarStatus carStatus2 = new CarStatus(id2, "Broken");

        Car car1 = new Car(id1, brand1, engineType1, carType1, "Kangoo", "Manual", 2006, carStatus1, "20 euro");
        Car car2 = new Car(id2, brand2, engineType2, carType2, "206", "Manual", 2006, carStatus2, "25 euro");

        given(carRepository.findById(id1)).willReturn(Optional.of(car1));
        given(carRepository.findById(id2)).willReturn(Optional.of(car2));

        CarDTO carDTO1 = carService.findCarDTOById(id1);
        CarDTO carDTO2 = carService.findCarDTOById(id2);

        assertEquals("Kangoo", carDTO1.getModel());
        assertEquals("206", carDTO2.getModel());

        verify(carRepository).findById(id1);
        verify(carRepository).findById(id2);
    }

    @Test()
    public void methodShouldThrowException() {
        Long absentId = 20L;
        Long presentId = 1L;

        Brand brand = new Brand(presentId, "Audi");
        EngineType engineType = new EngineType(presentId, "Diesel", "1.5");
        CarType carType = new CarType(presentId, "Hatchback");
        CarStatus carStatus = new CarStatus(presentId, "Available");

        Car car = new Car(brand, engineType, carType, "A1", "Automatic", 2017, carStatus, "40 euro");

        given(carRepository.findById(absentId)).willThrow(ServiceException.entityNotFound(ExceptionMessages.CAN_NOT_FIND_CAR));
        given(carRepository.findById(presentId)).willReturn(Optional.of(car));

        assertThrows(ResponseStatusException.class, () -> carService.findCarDTOById(absentId));

        verify(carRepository).findById(absentId);
    }

    @Test
    public void methodShouldReturnAllCars() {
        List<Car> carList = new ArrayList<>();

        Long id1 = 1L;
        Long id2 = 2L;
        Long id3 = 3L;

        Brand brand1 = new Brand(id1, "Renault");
        Brand brand2 = new Brand(id2, "Peugeot");
        Brand brand3 = new Brand(id3, "Audi");

        EngineType engineType1 = new EngineType(id1, "Diesel", "1.5");
        EngineType engineType2 = new EngineType(id2, "Gasoline", "1.4");
        EngineType engineType3 = new EngineType(id3, "Diesel", "2.0");

        CarType carType1 = new CarType(id1, "Hatchback");
        CarType carType2 = new CarType(id2, "Sedan");
        CarType carType3 = new CarType(id3, "SUV");

        CarStatus carStatus1 = new CarStatus(id1, "Available");
        CarStatus carStatus2 = new CarStatus(id2, "Broken");
        CarStatus carStatus3 = new CarStatus(id3, "Unavailable");

        carList.add(new Car(id1, brand1, engineType1, carType1, "Kangoo", "Manual", 2006, carStatus1, "20 euro"));
        carList.add(new Car(id2, brand2, engineType2, carType2, "206", "Manual", 2006, carStatus2, "25 euro"));
        carList.add(new Car(id3, brand3, engineType3, carType3, "A1", "Automatic", 2017, carStatus3, "40 euro"));

        given(carRepository.findAll()).willReturn(carList);

        List<CarDTO> carDTOList = carService.findAllCarDTO();

        assertEquals(carList.get(0).getModel(), carDTOList.get(0).getModel());
        assertEquals(carList.get(1).getModel(), carDTOList.get(1).getModel());
        assertEquals(carList.get(2).getModel(), carDTOList.get(2).getModel());
    }

    @Test
    public void methodShouldFindAllCarsByBrand() {
        List<Car> carList = new ArrayList<>();

        Long id1 = 1L;
        Long id2 = 2L;
        Long id3 = 3L;

        Brand brand1 = new Brand(id1, "Renault");
        Brand brand2 = new Brand(id2, "Audi");

        EngineType engineType1 = new EngineType(id1, "Diesel", "1.5");
        EngineType engineType2 = new EngineType(id2, "Gasoline", "1.4");
        EngineType engineType3 = new EngineType(id3, "Diesel", "2.0");

        CarType carType1 = new CarType(id1, "Hatchback");
        CarType carType2 = new CarType(id2, "Sedan");
        CarType carType3 = new CarType(id3, "SUV");

        CarStatus carStatus1 = new CarStatus(id1, "Available");
        CarStatus carStatus2 = new CarStatus(id2, "Broken");
        CarStatus carStatus3 = new CarStatus(id3, "Unavailable");

        carList.add(new Car(id1, brand1, engineType1, carType1, "Kangoo", "Manual", 2006, carStatus1, "20 euro"));
        carList.add(new Car(id2, brand1, engineType2, carType2, "Koleos", "Automatic", 2013, carStatus2, "40 euro"));
        carList.add(new Car(id3, brand2, engineType3, carType3, "A1", "Automatic", 2017, carStatus3, "40 euro"));

        List<Car> carList1;
        given(carRepository.findAllByBrand(brand1.getBrandName())).willReturn(carList1 = carList.stream().filter(c -> c.getBrand().getBrandName().equals("Renault")).collect(Collectors.toList()));

        List<CarDTO> carDTOList = carService.findAllCarDTOByBrandName(brand1.getBrandName());

        assertEquals(carList1.get(0).getModel(), carDTOList.get(0).getModel());
        assertEquals(carList1.get(1).getModel(), carDTOList.get(1).getModel());
    }

    @Test
    public void methodShouldFindAllCarsByEngine() {
        List<Car> carList = new ArrayList<>();

        Long id1 = 1L;
        Long id2 = 2L;
        Long id3 = 3L;

        Brand brand1 = new Brand(id1, "Renault");
        Brand brand2 = new Brand(id2, "Audi");

        EngineType engineType1 = new EngineType(id1, "Diesel", "1.5");
        EngineType engineType2 = new EngineType(id2, "Gasoline", "2.0");

        CarType carType1 = new CarType(id1, "Hatchback");
        CarType carType2 = new CarType(id2, "Sedan");
        CarType carType3 = new CarType(id3, "SUV");

        CarStatus carStatus1 = new CarStatus(id1, "Available");
        CarStatus carStatus2 = new CarStatus(id2, "Unavailable");

        carList.add(new Car(id1, brand1, engineType1, carType1, "Kangoo", "Manual", 2006, carStatus1, "20 euro"));
        carList.add(new Car(id2, brand1, engineType2, carType2, "Koleos", "Automatic", 2013, carStatus2, "40 euro"));
        carList.add(new Car(id3, brand2, engineType1, carType3, "A1", "Automatic", 2017, carStatus1, "40 euro"));

        List<Car> carList1;
        given(carRepository.findAllByEngine(engineType1.getType())).willReturn(carList1 = carList.stream().filter(c -> c.getEngineType().getType().equals("Diesel")).collect(Collectors.toList()));

        List<CarDTO> carDTOList = carService.findAllCarDTOByEngine(engineType1.getType());

        assertEquals(carList1.get(0).getEngineType().getType(), carDTOList.get(0).getEngineType());
        assertEquals(carList1.get(1).getEngineType().getType(), carDTOList.get(1).getEngineType());
    }

    @Test
    public void methodShouldDeleteCar() {
        Long presentId = 1L;

        Brand brand = new Brand(presentId, "Audi");
        EngineType engineType = new EngineType(presentId, "Diesel", "1.5");
        CarType carType = new CarType(presentId, "Hatchback");
        CarStatus carStatus = new CarStatus(presentId, "Available");

        Car car = new Car(brand, engineType, carType, "A1", "Automatic", 2017, carStatus, "40 euro");

        when(carRepository.findById(presentId)).thenReturn(Optional.of(car));

        carService.deleteById(presentId);

        verify(carRepository).deleteById(presentId);
    }

}
