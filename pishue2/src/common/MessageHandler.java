package common;

import javafx.application.Platform;

public class MessageHandler {

    private String cmd ="";
    private String msg ="";
    private String name ="";

    public MessageHandler(String cmd, String msg){
        this.cmd = cmd;
        this.msg = msg;
    }

    public MessageHandler(String cmd, String name, String msg){
        this.cmd = cmd;
        this.name = name;
        this.msg = msg;
    }

    public MessageHandler(String msg){
        String[] msgSplit = msg.split(":");
        this.cmd = msgSplit[0];
        this.msg = msgSplit[1];
        System.out.println(cmd+" "+this.msg);
        if(msgSplit.length>2){
            for(int i = 2; i < msgSplit.length; i++){
                this.msg = this.msg+":"+msgSplit[i];
            }
        }
    }

    public String getCmd() {
        return cmd;
    }

    public String getMsg() {
        return msg;
    }

    public String constructMessage(){
        String message = "";
        if(!name.equals("")){
            message = cmd+":"+name+":"+msg;
        }
        else{
            message = cmd+":"+msg;
        }
        return message;
    }

    public String[] getUsers(){
        String[] users;
        users=msg.split(":");
        return users;
    }

}

