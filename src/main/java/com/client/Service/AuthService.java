package com.client.Service;

import com.client.AppConfig;
import com.kurs.dto.LoginResponse;
import com.kurs.dto.LoginRequest;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class AuthService {
    public LoginResponse login(String login, String password, String role) throws IOException, ClassNotFoundException {
        try (
                Socket socket = new Socket(AppConfig.getHost(), AppConfig.getPort());
                ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
                ObjectInputStream in = new ObjectInputStream(socket.getInputStream())
        ) {
            LoginRequest loginRequest = new LoginRequest(login, password, role);
            out.writeObject(loginRequest);
            out.flush();

            Object response = in.readObject();
            if (response instanceof LoginResponse) {
                return (LoginResponse) response;
            } else throw new IOException("Неверный тип ответа от сервера");
        }
    }
}
