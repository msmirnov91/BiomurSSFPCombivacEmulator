package ru.iimtech;

import jssc.SerialPort;
import jssc.SerialPortException;

import java.util.Scanner;

/**
 * Created by ASUS on 07.09.2017.
 */
public class CombivacEmulator {
    private SerialPort serialPort;
    private ReceiveDataEventListener receiveListener;


    CombivacEmulator(String portName, double initialPressure){
        serialPort = new SerialPort(portName);
        receiveListener = new ReceiveDataEventListener(serialPort, initialPressure);
    }

    private void establishCommunication() throws Exception {
        try {
            serialPort.openPort();
            serialPort.setParams(SerialPort.BAUDRATE_9600,
                    SerialPort.DATABITS_8,
                    SerialPort.STOPBITS_1,
                    SerialPort.PARITY_NONE,
                    false,
                    false);

            serialPort.addEventListener(receiveListener, SerialPort.MASK_RXCHAR);
        }
        catch (SerialPortException ex) {
            System.out.println(ex);
        }
    }

    public void emulate() throws Exception{
        establishCommunication();
        double pressure = 1;
        Scanner pressureReader = new Scanner(System.in);
        while (pressure > 0){
            System.out.println("Enter new pressure value. Enter negative value to quit");
            pressure = pressureReader.nextDouble();
            receiveListener.setPressure(pressure);
        }

        serialPort.closePort();

    }

}
