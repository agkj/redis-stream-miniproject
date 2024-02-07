package com.example.producer.Model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@NoArgsConstructor
@Slf4j
@Data

public class GateModel {

    private int gateNumber;
    private boolean gateStatus;
    private boolean gateHold;

}
