package com.gridnine.testing;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class FlightFilterImpl implements FlightFilter {
    @Override
    public List<Flight> filterFlights(List<Flight> flights, FilterRule rule) {
        return flights.stream()
                .filter(rule::apply)
                .collect(Collectors.toList());
    }
    interface FilterRule {
        boolean apply(Flight flight);
    }

    class DepartureBeforeNowRule implements FilterRule {
        @Override
        public boolean apply(Flight flight) {
            return flight.getSegments().stream()
                    .noneMatch(segment -> segment.getDepartureDate().isBefore(LocalDateTime.now()));
        }
    }

    class ArrivalBeforeDepartureRule implements FilterRule {
        @Override
        public boolean apply(Flight flight) {
            return flight.getSegments().stream()
                    .noneMatch(segment -> segment.getArrivalDate().isBefore(segment.getDepartureDate()));
        }
    }

    class ExcessiveGroundTimeRule implements FilterRule {
        private final int maxGroundTimeHours;

        public ExcessiveGroundTimeRule(int maxGroundTimeHours) {
            this.maxGroundTimeHours = maxGroundTimeHours;
        }

        @Override
        public boolean apply(Flight flight) {
            List<Segment> segments = flight.getSegments();
            int totalGroundTimeHours = 0;
            for (int i = 0; i < segments.size() - 1; i++) {
                LocalDateTime arrival = segments.get(i).getArrivalDate();
                LocalDateTime nextDeparture = segments.get(i + 1).getDepartureDate();
                totalGroundTimeHours += java.time.Duration.between(arrival, nextDeparture).toHours();
            }
            return totalGroundTimeHours <= maxGroundTimeHours;
        }
    }
}
