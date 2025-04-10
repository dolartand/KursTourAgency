package com.client.Service;

import com.client.SessionHolder;
import com.kurs.dto.ProfileRequest;
import com.kurs.dto.ProfileResponse;
import com.kurs.dto.UserProfile;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class ProfileService {
    private static final String HOST = "127.0.0.1";
    private static final int PORT = 11000;

    public ProfileResponse fetchProfile() {
        try (Socket socket = new Socket(HOST, PORT);
             ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
             ObjectInputStream in = new ObjectInputStream(socket.getInputStream())
            ) {
            ProfileRequest req = new ProfileRequest(SessionHolder.getSessionId(), null);
            out.writeObject(req);
            out.flush();

            Object responseObj = in.readObject();
            if (responseObj instanceof ProfileResponse) {
                return (ProfileResponse) responseObj;
            } else throw new IOException("Неверный тип ответа от сервера");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public ProfileResponse updateProfile(UserProfile profile) {
        try (Socket socket = new Socket(HOST, PORT);
             ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
             ObjectInputStream in = new ObjectInputStream(socket.getInputStream())
        ) {
            ProfileRequest req = new ProfileRequest(SessionHolder.getSessionId(), profile);
            out.writeObject(req);
            out.flush();

            Object responseObj = in.readObject();
            if (responseObj instanceof ProfileResponse) {
                return (ProfileResponse) responseObj;
            } else throw new IOException("Неверный тип ответа от сервера");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
