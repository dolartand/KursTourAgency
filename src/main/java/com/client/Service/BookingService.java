package com.client.Service;

import com.client.AppConfig;
import com.kurs.dto.BookingRequest;
import com.kurs.dto.BookingResponse;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class BookingService {
    public BookingResponse fetchBookings(String sessionId) {
        try (Socket socket = new Socket(AppConfig.getHost(), AppConfig.getPort());
             ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
             ObjectInputStream in = new ObjectInputStream(socket.getInputStream())) {

            BookingRequest req = new BookingRequest(sessionId);
            out.writeObject(req);
            out.flush();

            Object resp = in.readObject();
            if (resp instanceof BookingResponse) {
                return (BookingResponse) resp;
            } else throw new Exception("Неверный тип ответа от сервера");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new BookingResponse(false, "Получить бронирования не удалось", null);
    }
}
