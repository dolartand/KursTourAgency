package com.client.Service;

import com.client.AppConfig;
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
    public ProfileResponse fetchProfile() {
        try (Socket socket = new Socket(AppConfig.getHost(), AppConfig.getPort());
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
        try (Socket socket = new Socket(AppConfig.getHost(), AppConfig.getPort());
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
