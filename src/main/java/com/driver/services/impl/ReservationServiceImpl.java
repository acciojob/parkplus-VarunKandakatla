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
            throw new Exception("reservation cannot be made");
        }

        Spot spot;

        List<Spot> spotsList=parkingLot.getSpotList();

        //Sorting
        Collections.sort(spotsList,(a,b)->a.getPricePerHour()-b.getPricePerHour());

        //Checking whether the spot is available or not
        for(Spot spot1 : spotsList)
        {
            if(spot1.isOccupied()==false && Check(spot1,numberOfWheels)==true)
            {
                //Make reservation
                Reservation reservation = new Reservation();
                //make Payment
                Payment payment;
                int amount=spot1.getPricePerHour()*timeInHours;
                try
                {
                    payment=paymentService.pay(reservation.getId(),amount,"upi");
                }catch (Exception e)
                {
                    throw new Exception(e);
                }

                reservation.setSpot(spot1);
                reservation.setUser(user);
                reservation.setPayment(payment);

                return reservationRepository3.save(reservation);
            }
        }


            throw new Exception("reservation cannot be made");


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
