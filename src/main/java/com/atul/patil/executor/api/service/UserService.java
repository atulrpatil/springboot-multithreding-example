package com.atul.patil.executor.api.service;

import com.atul.patil.executor.api.entiry.User;
import com.atul.patil.executor.api.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
@Slf4j
public class UserService {

    @Autowired
    UserRepository userRepository;


    @Async
    public CompletableFuture<List<User>> saveUsers(MultipartFile file) throws Exception {
        long startTime = System.currentTimeMillis();
        List<User> users = parseCsv(file);

        log.info("Saving list of users of size {} ", users.size(), "" + Thread.currentThread().getName());
        users = userRepository.saveAll(users);
        long endTime = System.currentTimeMillis();
        log.info("Total Time {} ", (endTime - startTime));

        return CompletableFuture.completedFuture(users);
    }

    @Async
    public CompletableFuture<List<User>> findAllUsers(){
        log.info("Get list of user by {} ",Thread.currentThread().getName());
        List<User> users = userRepository.findAll();
        return  CompletableFuture.completedFuture(users);
    }

    private List<User> parseCsv(final MultipartFile file) throws Exception {
        List<User> users = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                User user = new User();
                user.setName(data[0]);
                user.setEmail(data[1]);
                user.setGender(data[2]);
                users.add(user);
            }
            return users;

        } catch (IOException e) {

            log.error("Failed to pars CSV File {} ", e);
            throw new Exception("Failed to pars CSV File {} ", e);
        }
    }
}
