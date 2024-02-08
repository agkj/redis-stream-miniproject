package com.example.producer.Model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@NoArgsConstructor
@Slf4j
@Data

public class GateModel {

    private int gateNumber; //gets gate number
    private String gateGroup; // gate group -> need a key
    private int timestamp; // stream entry time
    private boolean gateStatus; //gets gate current status, pass or fail


    //TODO: Implement checkbox method, when checked, disable autodeletion for checked streams
    private boolean gateHold;

}
