package launcher;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.george.pubsub.thiroros.endpoint.ThirorosEndpoint;
import com.george.pubsub.util.RemoteAddress;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;

public class Main {

    public static void main(String[] args) throws IOException {
        ThirorosEndpoint thiroros = new ThirorosEndpoint("localhost", 50000);
        ObjectMapper mapper = new ObjectMapper();
        Socket client = new Socket();
        client.connect(new InetSocketAddress("localhost", 50000));
        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(client.getOutputStream());
        BufferedWriter bufferedWriter = new BufferedWriter(outputStreamWriter);
        bufferedWriter.write("GET /join HTTP/1.1\n");
        RemoteAddress remoteAddress = new RemoteAddress("192.168.1.2",15000);
        String str = mapper.writeValueAsString(remoteAddress) + "α";
        bufferedWriter.write("Content-Length: " + str.getBytes().length + "\n\n");
        bufferedWriter.write(str);
        bufferedWriter.flush();

        InputStreamReader inputStreamReader = new InputStreamReader(client.getInputStream());
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        String line = bufferedReader.readLine();
        while (line != null) {
            System.out.println(line);
            line = bufferedReader.readLine();
        }
        client.close();
    }

}
