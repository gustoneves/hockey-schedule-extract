package org.personalDev.rides.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import org.personalDev.rides.entities.Passenger;
import org.personalDev.rides.entities.PassengerRide;
import org.personalDev.rides.entities.Ride;
import org.personalDev.rides.service.RideDTO;
import org.personalDev.rides.service.RideQuery;
import org.personalDev.rides.service.RidesSummary;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.Tuple;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class PassengerRideRepository implements PanacheRepository<PassengerRide> {

    public List<RidesSummary> getRidesSummary(RideQuery rideQuery) {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<Tuple> cq = cb.createTupleQuery();
        Root<PassengerRide> passengerRideRoot = cq.from(PassengerRide.class);
        Join<PassengerRide, Passenger> passengerJoin = passengerRideRoot.join("passenger", JoinType.INNER);
        Join<PassengerRide, Ride> rideJoin = passengerRideRoot.join("ride", JoinType.INNER);

        cq.multiselect(
                passengerJoin.get("name").alias("name"),
                cb.sum(passengerRideRoot.get("outgoingCost")).alias("outgoing"),
                cb.sum(passengerRideRoot.get("returningCost")).alias("returning"),
                cb.count(passengerRideRoot).alias("count")
        );

        cq.groupBy(passengerJoin.get("name"));

        List<Predicate> predicates = createFilterPredicateList(rideQuery, cb, passengerJoin, rideJoin);

        cq.where(predicates.toArray(Predicate[]::new));

        TypedQuery<Tuple> query = getEntityManager().createQuery(cq);

        List<Tuple> results = query.getResultList();

        return results.stream().map(tuple -> {
            RidesSummary dto = new RidesSummary();
            dto.setName(tuple.get("name", String.class));
            dto.setOutgoing(tuple.get("outgoing", Double.class));
            dto.setReturning(tuple.get("returning", Double.class));
            dto.setTotal(tuple.get("outgoing", Double.class) + tuple.get("returning", Double.class));
            dto.count = tuple.get("count", Long.class);
            return dto;
        }).toList();
    }

    public List<RideDTO> searchRides(RideQuery rideQuery) {
        CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<RideDTO> query = criteriaBuilder.createQuery(RideDTO.class);
        Root<PassengerRide> root = query.from(PassengerRide.class);
        Join<PassengerRide, Passenger> passengerJoin = root.join("passenger");
        Join<PassengerRide, Ride> rideJoin = root.join("ride");


        query.select(criteriaBuilder.construct(RideDTO.class,
                passengerJoin.get("name"),
                root.get("outgoingCost"),
                root.get("returningCost"),
                rideJoin.get("date")));

        query.where(createFilterPredicateList(rideQuery, criteriaBuilder, passengerJoin, rideJoin).toArray(Predicate[]::new));

        return getEntityManager().createQuery(query).getResultList();
    }

    private static List<Predicate> createFilterPredicateList(RideQuery rideQuery, CriteriaBuilder cb, Join<PassengerRide, Passenger> passengerJoin, Join<PassengerRide, Ride> rideJoin) {
        List<Predicate> predicates = new ArrayList<>();

        if(rideQuery.getStartDate() != null) {
            predicates.add(cb.greaterThanOrEqualTo(rideJoin.get("date"), rideQuery.getStartDate()));
        }

        if(rideQuery.getEndDate() != null) {
            predicates.add(cb.lessThanOrEqualTo(rideJoin.get("date"), rideQuery.getEndDate()));
        }

        if(rideQuery.getName() != null) {
            predicates.add(cb.like(passengerJoin.get("name"), "%"+ rideQuery.getName()+"%"));
        }
        return predicates;
    }
}
