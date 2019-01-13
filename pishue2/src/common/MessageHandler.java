package common;
/**
 * @author 5127797, Ramli, Benedictus William
 * @author 5130292, Fadilah, Verdy Aprian
 * 
 */
public class MessageHandler {

    private String cmd ="";
    private String msg ="";

    /**
     * ist eine Steurungsmethode
     * @param msg
     */
    public MessageHandler(String msg){
        String[] msgSplit = msg.split(":");
        this.cmd = msgSplit[0];
        this.msg = msgSplit[1];
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

    public String[] getUsers(){
        String[] users;
        users=msg.split(":");
        return users;
    }

}

