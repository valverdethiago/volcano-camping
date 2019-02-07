package com.upgrade.volcanocamping.exceptions;

/**
 * Created by valve on 06/02/2019.
 */
public class BookingAlreadyFinishedException extends RuntimeException {

    public BookingAlreadyFinishedException() {
        super("This booking is already finished and can't be cancelled");
    }
}
