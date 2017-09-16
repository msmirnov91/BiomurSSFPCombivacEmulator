package ru.iimtech;

import jssc.SerialPort;
import jssc.SerialPortEvent;
import jssc.SerialPortEventListener;
import jssc.SerialPortException;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by ASUS on 12.09.2017.
 */
public class ReceiveDataEventListener implements SerialPortEventListener{
    private String RESPONSE_TEMPLATE;
    private String PRESSURE_REQUEST;

    private SerialPort serialPort;
    private double pressure;

    ReceiveDataEventListener(SerialPort port, double initialPressure){
        RESPONSE_TEMPLATE = "3:mbar:%1.2f\r";
        PRESSURE_REQUEST = "MES 3\r";

        serialPort = port;
        setPressure(initialPressure);
    }

    private String makeResponse(){
        return String.format(RESPONSE_TEMPLATE, pressure);
    }
    public void setPressure(double pressure){
        this.pressure = pressure;
    }

    public void serialEvent(SerialPortEvent serialPortEvent) {
        if(serialPortEvent.isRXCHAR() && serialPortEvent.getEventValue() > 0){
            try {

                String request = "";
                int bytesAvailable = serialPortEvent.getEventValue();
                while (bytesAvailable >=0 ){
                    String currentChar = serialPort.readString(1);
                    request += currentChar;
                    if (currentChar.equals("\r")) {
                        break;
                    }
                }

                if (request.equals(PRESSURE_REQUEST)) {
                    serialPort.writeString(makeResponse());
                }
                else {
                    // for debug
                    writeToFile(request);
                }
            }
            catch (SerialPortException ex) {
                System.out.println(ex);
            }
        }
    }

    private void writeToFile(String what){
        try {
            File file = new File("lastWrongRequest.txt");
            FileWriter fileWriter = new FileWriter(file);
            fileWriter.write(what);
            fileWriter.flush();
            fileWriter.close();
        }
        catch (IOException ex){
            ex.printStackTrace();
        }
    }
}
