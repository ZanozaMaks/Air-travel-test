package com.gridnine.testing;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        List<Flight> flights = FlightBuilder.createFlights();
        System.out.println("Список всех перелётов:");
        flights.forEach(System.out::println);

        System.out.println("\nПерелёты после фильтрации:");
        FlightFilterImpl filter = new FlightFilterImpl();

        // Создаем экземпляры правил через объект filter
        FlightFilterImpl.DepartureBeforeNowRule departureBeforeNowRule = filter.new DepartureBeforeNowRule(); // исключает рейсы, вылет которых уже прошёл
        FlightFilterImpl.ArrivalBeforeDepartureRule arrivalBeforeDepartureRule = filter.new ArrivalBeforeDepartureRule(); // исключает полёт, где время прилёта раньше времени вылета
        FlightFilterImpl.ExcessiveGroundTimeRule excessiveGroundTimeRule = filter.new ExcessiveGroundTimeRule(2); // исключает рейсы, если общее время ожидания на земле превышает два часа

        // Вылет до текущего момента времени
        List<Flight> filteredFlights = filter.filterFlights(flights, departureBeforeNowRule);
        System.out.println("\nСписок перелётов, которые ждут вылета:");
        filteredFlights.forEach(System.out::println);

        // Сегменты с датой прилёта раньше даты вылета
        filteredFlights = filter.filterFlights(flights, arrivalBeforeDepartureRule);
        System.out.println("\nСписок перелётов с датой до прибытия:");
        filteredFlights.forEach(System.out::println);

        // Перелеты, где общее время, проведённое на земле, превышает два часа
        filteredFlights = filter.filterFlights(flights, excessiveGroundTimeRule);
        System.out.println("\nСписок перелётов с ожиданием на земле более 2-ух часов:");
        filteredFlights.forEach(System.out::println);
    }
}
