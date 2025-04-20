package com.client.Admin.Service;

import com.client.AppConfig;
import com.kurs.dto.AdminDTOs.*;

import javax.sound.sampled.Port;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;

public class AdminBookingService {
    public List<AdminBookingDTO> fetchAll() {
        try (Socket socket = new Socket(AppConfig.getHost(), AppConfig.getPort());
             ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
             ObjectInputStream in = new ObjectInputStream(socket.getInputStream())) {

            out.writeObject(new AdminBookingRequest());
            AdminBookingResponse response = (AdminBookingResponse) in.readObject();
            return response.isSuccess() ? response.getBookings() : null;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean approve(int bookingId) {
        try (Socket socket = new Socket(AppConfig.getHost(), AppConfig.getPort());
             ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
             ObjectInputStream in = new ObjectInputStream(socket.getInputStream())) {

            out.writeObject(new ApproveBookingRequest(bookingId));
            ApproveBookingResponse resp = (ApproveBookingResponse) in.readObject();
            return resp.isSuccess();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean reject(int bookingId) {
        try (Socket socket = new Socket(AppConfig.getHost(), AppConfig.getPort());
             ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
             ObjectInputStream in = new ObjectInputStream(socket.getInputStream())) {

            out.writeObject(new RejectBookingRequest(bookingId));
            RejectBookingResponse resp = (RejectBookingResponse) in.readObject();
            return resp.isSuccess();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
