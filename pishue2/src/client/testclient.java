package client;

import java.io.*;
import java.net.Socket;

public class testclient {

    public static void main(String[]args){

        try {
            Socket s = new Socket("127.0.0.1",1996);
            BufferedReader sysin = new BufferedReader(new InputStreamReader(s.getInputStream()));
            PrintWriter sysout = new PrintWriter(new OutputStreamWriter(s.getOutputStream()));
            sysout.print("connect:lol"+"\n");
            sysout.flush();
            String ans = sysin.readLine();
            System.out.println(ans);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
