package com.driver.services.impl;

import com.driver.model.Payment;
import com.driver.model.PaymentMode;
import com.driver.model.Reservation;
import com.driver.repository.PaymentRepository;
import com.driver.repository.ReservationRepository;
import com.driver.services.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Locale;

@Service
public class PaymentServiceImpl implements PaymentService {
    @Autowired
    ReservationRepository reservationRepository2;
    @Autowired
    PaymentRepository paymentRepository2;

    @Override
    public Payment pay(Integer reservationId, int amountSent, String mode) throws Exception {

        Reservation reservation = reservationRepository2.findById(reservationId).get();

        if(reservation.getNumberOfHours()*reservation.getSpot().getPricePerHour() > amountSent)
        {
            throw new Exception("Insufficient Amount");
        }

        //Check payment mode
        mode=mode.toUpperCase();

        if(!PaymentMode.CARD.toString().equals(mode) && !PaymentMode.UPI.toString().equals(mode) && !PaymentMode.CASH.toString().equals(mode) )
        {
            throw new Exception("Payment mode not detected");
        }

        //Make Payment
        Payment payment=new Payment();
        payment.setReservation(reservation);
        payment.setPaymentCompleted(true);
        payment.setPaymentMode(setPayment(mode));
        payment.getReservation().getSpot().setOccupied(true);
        reservation.setPayment(payment);

        return paymentRepository2.save(payment);
    }

    public PaymentMode setPayment(String mode)
    {
        if(PaymentMode.CASH.toString().equals(mode))
        {
            return PaymentMode.CASH;
        }
        else if(PaymentMode.CARD.toString().equals(mode))
        {
            return PaymentMode.CARD;
        }
        else {
            return PaymentMode.UPI;
        }
    }
}
