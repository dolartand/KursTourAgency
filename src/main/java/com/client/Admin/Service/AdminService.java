package com.client.Admin.Service;

import com.client.AppConfig;
import com.kurs.dto.AdminDTOs.*;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;

public class AdminService {
    public List<UserDTO> getUsers() {
        try (Socket socket = new Socket(AppConfig.getHost(), AppConfig.getPort());
             ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
             ObjectInputStream in = new ObjectInputStream(socket.getInputStream())) {

            UsersRequest req = new UsersRequest();
            out.writeObject(req);
            out.flush();

            Object resp = in.readObject();
            if (resp instanceof UsersResponse) {
                UsersResponse usersResp = (UsersResponse) resp;
                if (usersResp.isSuccess()) {
                    return usersResp.getUsers();
                } else throw new Exception(usersResp.getMessage());
            } else throw new Exception("Неверный тип ответа от сервера");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean deleteUser(int userId) {
        try (Socket socket = new Socket(AppConfig.getHost(), AppConfig.getPort());
             ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
             ObjectInputStream in = new ObjectInputStream(socket.getInputStream())) {

            DeleteUserRequest req = new DeleteUserRequest(userId);
            out.writeObject(req);
            out.flush();

            Object resp = in.readObject();
            if (resp instanceof DeleteUserResponse) {
                DeleteUserResponse deleteUserResp = (DeleteUserResponse) resp;
                return deleteUserResp.isSuccess();
            } else throw new Exception("Неверный тип ответа от сервера");

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean promoteToAdmin(int userId) {
        try (Socket socket = new Socket(AppConfig.getHost(), AppConfig.getPort());
             ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
             ObjectInputStream in = new ObjectInputStream(socket.getInputStream())) {

            PromoteUserRequest req = new PromoteUserRequest(userId);
            out.writeObject(req);
            out.flush();

            Object resp = in.readObject();
            if (resp instanceof PromoteUserResponse) {
                PromoteUserResponse promoteUserResp = (PromoteUserResponse) resp;
                return promoteUserResp.isSuccess();
            } else throw new Exception("Неверный тип ответа от сервера");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
