package com.client.Service;

import com.client.AppConfig;
import com.kurs.dto.BookTourRequest;
import com.kurs.dto.BookTourResponse;
import com.kurs.dto.TourRequest;
import com.kurs.dto.TourResponse;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class TourService {
    public TourResponse fetchTours(TourRequest req) {
        try (Socket socket = new Socket(AppConfig.getHost(), AppConfig.getPort());
             ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
             ObjectInputStream in = new ObjectInputStream(socket.getInputStream())) {

            out.writeObject(req);
            out.flush();

            Object response = in.readObject();
            if (response instanceof TourResponse) {
                return (TourResponse) response;
            } else throw new Exception("Неверный тип ответа от сервера");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new TourResponse(false, "Получение туров не удалось", null);
    }

    public BookTourResponse bookTour(BookTourRequest req) {
        try (Socket socket = new Socket(AppConfig.getHost(), AppConfig.getPort());
             ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
             ObjectInputStream in = new ObjectInputStream(socket.getInputStream())) {

            out.writeObject(req);
            out.flush();

            Object response = in.readObject();
            if (response instanceof BookTourResponse) {
                return (BookTourResponse) response;
            } else throw new Exception("Неверный тип ответа от сервера");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new BookTourResponse(false, "Забронировать тур не удалось");
    }
}
