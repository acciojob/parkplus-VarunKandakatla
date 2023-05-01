package com.driver.services.impl;

import com.driver.model.*;
import com.driver.repository.ParkingLotRepository;
import com.driver.repository.ReservationRepository;
import com.driver.repository.SpotRepository;
import com.driver.repository.UserRepository;
import com.driver.services.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class ReservationServiceImpl implements ReservationService {
    @Autowired
    UserRepository userRepository3;
    @Autowired
    SpotRepository spotRepository3;
    @Autowired
    ReservationRepository reservationRepository3;
    @Autowired
    ParkingLotRepository parkingLotRepository3;

    @Autowired
    PaymentServiceImpl paymentService;
    @Override
    public Reservation reserveSpot(Integer userId, Integer parkingLotId, Integer timeInHours, Integer numberOfWheels) throws Exception {

        User user;
        ParkingLot parkingLot;

        try
        {
            user= userRepository3.findById(userId).get();
            parkingLot=parkingLotRepository3.findById(parkingLotId).get();
        }catch (Exception e)
        {
            throw new Exception("Cannot make reservation");
        }

      List<Spot> spots=parkingLot.getSpotList();

        Collections.sort(spots,(a,b)-> a.getPricePerHour()-b.getPricePerHour());

        Spot spot=null;

        for(Spot spot1 : spots)
        {
            if(spot1.isOccupied()==false && Check(spot1,numberOfWheels))
            {
                spot=spot1;
                break;
            }
        }

        if(spot==null)
        {
            throw new Exception("Cannot make reservation");
        }

        //RESERVATION
        Reservation reservation = new Reservation();
        spot.setOccupied(true);
        spot.getReservationList().add(reservation);

        reservation.setSpot(spot);
        reservation.setUser(user);

        user.getReservationList().add(reservation);

        return reservationRepository3.save(reservation);

    }

    public boolean Check(Spot spot, int numberOfWheels)
    {
        if(spot.getSpotType().equals(SpotType.TWO_WHEELER) && numberOfWheels<=2)
        {
            return true;
        }
        else if(spot.getSpotType().equals(SpotType.FOUR_WHEELER) && numberOfWheels<=4)
        {
            return true;
        }
        else if(spot.getSpotType().equals(SpotType.OTHERS))
        {
            return true;
        }

        return false;
    }
}
