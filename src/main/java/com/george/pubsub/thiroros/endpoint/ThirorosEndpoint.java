package com.george.pubsub.thiroros.endpoint;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.george.pubsub.thiroros.service.Thiroros;
import com.george.pubsub.thiroros.service.impl.ThirorosImpl;
import com.george.pubsub.thiroros.util.DistributedNode;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThirorosEndpoint {

    private String ip;
    private int port;
    private ObjectMapper mapper;
    private ServerSocket server;
    private ExecutorService executor;
    private Thiroros thiroros = new ThirorosImpl();

    public ThirorosEndpoint() {}

    public ThirorosEndpoint(String ip, int port) throws IOException {
        this.ip = ip;
        this.port = port;
        mapper = new ObjectMapper();
        server = new ServerSocket(port, 50, InetAddress.getByName(ip));
        System.out.println("thiroros started... listening on ip=" + server.getInetAddress() + " port=" + server.getLocalPort());
        executor = Executors.newSingleThreadExecutor();

        executor.submit(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try (Socket client = server.accept()) {
                        InputStreamReader inputStreamReader = new InputStreamReader(client.getInputStream());
                        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                        String line = bufferedReader.readLine();
                        String type = line.split(" ")[1].substring(1);
                        int len = 0;
                        line = bufferedReader.readLine();
                        while (!line.isEmpty()) {
                            if (line.startsWith("Content-Length")) {
                                String[] tokens = line.split(":");
                                len = Integer.parseInt(tokens[1].trim());
                            }
                            line = bufferedReader.readLine();
                        }
                        if (len > 0) {
                            char[] buf = new char[len];
                            bufferedReader.read(buf, 0, len);
                            line = String.copyValueOf(buf);
                        }
                        if (type.equals("leave")) {
                            DistributedNode distributedNode = mapper.readValue(line, DistributedNode.class);
                            client.getOutputStream().write(("HTTP/1.1 200 OK\n\n" + thiroros.leave(distributedNode)).getBytes("UTF-8"));
                        } else if (type.equals("join")) {
                            DistributedNode distributedNode = mapper.readValue(line, DistributedNode.class);
                            client.getOutputStream().write(("HTTP/1.1 200 OK\n\n" + mapper.writeValueAsString(thiroros.join(distributedNode))).getBytes("UTF-8"));
                        } else if (type.equals("update")) {
                            client.getOutputStream().write(("HTTP/1.1 200 OK\n\n" + mapper.writeValueAsString(thiroros.update())).getBytes("UTF-8"));
                        } else {
                            client.getOutputStream().write("HTTP/1.1 404 Not Found\n\n".getBytes("UTF-8"));
                        }
                    } catch (IOException e) {
                        System.out.println(e.getMessage());
                    }
                }
            }
        });
    }

    public void shutdown() throws IOException {
        server.close();
        executor.shutdown();
    }

}
