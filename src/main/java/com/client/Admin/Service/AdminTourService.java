package com.client.Admin.Service;

import com.kurs.dto.AdminDTOs.*;
import com.kurs.dto.TourDTO;
import com.kurs.dto.TourRequest;
import com.kurs.dto.TourResponse;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;

public class AdminTourService {
    private static final String HOST = "127.0.0.1";
    private static final int PORT = 11000;

    public List<TourDTO> fetchTours() {
        try (Socket socket = new Socket(HOST, PORT);
             ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
             ObjectInputStream in = new ObjectInputStream(socket.getInputStream())) {

            TourRequest req = new TourRequest();
            out.writeObject(req);
            out.flush();

            Object response = in.readObject();
            if (response instanceof TourResponse resp) {
                return resp.getTours();
            } else throw new Exception("Неверный тип ответа от сервера");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public AddTourResponse addTour(AddTourRequest req) {
        try (Socket socket = new Socket(HOST, PORT);
             ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
             ObjectInputStream in = new ObjectInputStream(socket.getInputStream())) {

            out.writeObject(req);
            out.flush();

            Object response = in.readObject();
            if (response instanceof AddTourResponse) {
                return (AddTourResponse) response;
            } else throw new Exception("Неверный тип ответа от сервера");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new AddTourResponse(false, "Добавление тура не удалось");
    }

    public UpdateTourResponse updateTour(UpdateTourRequest req) {
        try (Socket socket = new Socket(HOST, PORT);
             ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
             ObjectInputStream in = new ObjectInputStream(socket.getInputStream())
        ) {
            out.writeObject(req);
            out.flush();

            Object response = in.readObject();
            if (response instanceof UpdateTourResponse) {
                return (UpdateTourResponse) response;
            } else {
                throw new Exception("Неверный тип ответа от сервера");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new UpdateTourResponse(false, "Редактирование тура не удалось", null);
    }

    public DeleteTourResponse deleteTour(int tourId) {
        try (Socket socket = new Socket(HOST, PORT);
             ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
             ObjectInputStream in = new ObjectInputStream(socket.getInputStream())
        ) {
            DeleteTourRequest req = new DeleteTourRequest(tourId);
            out.writeObject(req);
            out.flush();

            Object response = in.readObject();
            if (response instanceof DeleteTourResponse) {
                return (DeleteTourResponse) response;
            } else {
                throw new Exception("Неверный тип ответа от сервера");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new DeleteTourResponse(false, "Удаление тура не удалось");
    }
}
