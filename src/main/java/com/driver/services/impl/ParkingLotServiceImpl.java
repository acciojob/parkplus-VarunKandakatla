package com.driver.services.impl;

import com.driver.model.ParkingLot;
import com.driver.model.Spot;
import com.driver.model.SpotType;
import com.driver.repository.ParkingLotRepository;
import com.driver.repository.SpotRepository;
import com.driver.services.ParkingLotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ParkingLotServiceImpl implements ParkingLotService {
    @Autowired
    ParkingLotRepository parkingLotRepository1;
    @Autowired
    SpotRepository spotRepository1;
    @Override
    public ParkingLot addParkingLot(String name, String address) {

        ParkingLot parkingLot=new ParkingLot();

        //Setting
        parkingLot.setName(name);
        parkingLot.setAddress(address);

        //saving
        return parkingLotRepository1.save(parkingLot);
    }

    @Override
    public Spot addSpot(int parkingLotId, Integer numberOfWheels, Integer pricePerHour) {

        ParkingLot parkingLot;
        try {
            parkingLot = parkingLotRepository1.findById(parkingLotId).get();
            //Setting
            Spot spot=new Spot();
            spot.setParkingLot(parkingLot);
            spot.setPricePerHour(pricePerHour);
            spot.setOccupied(false);

            //Setting Spot type
            if(numberOfWheels == 2)
            {
                spot.setSpotType(SpotType.TWO_WHEELER);
            }
            else if(numberOfWheels ==4 )
            {
                spot.setSpotType(SpotType.FOUR_WHEELER);
            }
            else {
                spot.setSpotType(SpotType.OTHERS);
            }

            //Adding Spot to ParkingLot
            parkingLot.getSpotList().add(spot);

            return spotRepository1.save(spot);
        }catch (Exception e){
            return null;
        }


    }

    @Override
    public void deleteSpot(int spotId) {


           Spot spot = spotRepository1.findById(spotId).get();

           ParkingLot parkingLot= spot.getParkingLot();

           //Deleteing
           parkingLot.getSpotList().remove(spot);
           spotRepository1.delete(spot);


    }

    @Override
    public Spot updateSpot(int parkingLotId, int spotId, int pricePerHour) {
        Spot spot1=null;
       try
       {
           ParkingLot parkingLot = parkingLotRepository1.findById(parkingLotId).get();

           for(Spot spot : parkingLot.getSpotList())
           {
               if(spot.getId()==spotId)
               {
                   spot1=spot;
               }
           }
       }catch (Exception e)
       {

       }

        spot1.setPricePerHour(pricePerHour);
        return spotRepository1.save(spot1);
    }

    @Override
    public void deleteParkingLot(int parkingLotId) {
       parkingLotRepository1.deleteById(parkingLotId);
    }
}
