package com.client.Service;

import com.kurs.dto.LoginRequest;
import com.kurs.dto.LoginResponse;
import com.kurs.dto.RegistrationRequest;
import com.kurs.dto.RegistrationResponse;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class RegistrationService {
    private static final String HOST = "127.0.0.1";
    private static final int PORT = 11000;

    public RegistrationResponse registration(String name, String login, String password) throws IOException, ClassNotFoundException {
        try (
                Socket socket = new Socket(HOST, PORT);
                ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
                ObjectInputStream in = new ObjectInputStream(socket.getInputStream())
        ) {
            RegistrationRequest registrationRequest = new RegistrationRequest(name, login, password);
            out.writeObject(registrationRequest);
            out.flush();

            Object response = in.readObject();
            if (response instanceof RegistrationResponse) {
                return (RegistrationResponse) response;
            } else throw new IOException("Неверный тип ответа от сервера");
        }
    }
}
