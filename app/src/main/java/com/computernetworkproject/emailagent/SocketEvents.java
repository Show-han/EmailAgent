package com.computernetworkproject.emailagent;

import android.util.Base64;
import android.util.Log;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;

import static java.lang.System.exit;

public class SocketEvents {
    private String address;
    private String rcpt_address;
    private String password;
    private String title;
    private String content;
    private int mailType;//1为163邮箱，2为qq邮箱
    private InputStream in; //输入字节流
    private BufferedReader br; //封装了输入字节流，便于对服务器的响应报文做出处理
    private OutputStream out; //输出字节流
    private Socket socket;

    /**
     * 传入邮箱地址、密码/授权码和邮件服务器类型（1为163邮箱，2为qq邮箱），并将邮箱地址和密码/授权码进行Base64转码
     * @param address 邮件地址
     * @param password 密码/授权码
     * @param mailType 服务器类型
     */
    public SocketEvents(String address, String password, int mailType){
            this.mailType = mailType;
            this.address = address;
            this.password = password;
    }
    public SocketEvents(String address, String password, int mailType, String rcpt_address, String title, String content){
            this.mailType = mailType;
            this.address = address;
            this.password = password;
            this.rcpt_address = rcpt_address;
            this.title = title;
            this.content = content;
    }
    /**
     * 用于验证用户名与密码是否能够正确连接
     * @throw IOException
     */

    public boolean checkAccount(){
        String mailUrl; //smtp服务器的url
        int port = 25; //邮件服务器端口号
        if(mailType == 1){
            mailUrl = "smtp.163.com";
        }else{
            mailUrl = "smtp.qq.com";
        }
        try{
            String reply; //暂时存放服务器相应消息，并进行debug处理
            socket = new Socket(mailUrl, port); //获取socket
            in = socket.getInputStream();
            out = socket.getOutputStream();
            br = new BufferedReader(new InputStreamReader(in));
            reply = br.readLine();
            Log.d("FirstMessage", reply); //打印服务器返回消息日志，便于debug
            out.write("HELO email\r\n".getBytes("utf-8")); //发送握手请求
            reply = br.readLine();
            if(mailType == 2){ //QQ的这里会发回来三行！！！很坑人！！！找了半天bug！！！
                reply = br.readLine();
                reply = br.readLine();
            }
            Log.d("SecondMessage", reply);
            if(!reply.startsWith("250")){ //如果没有收到250，那么返回false
                in.close();
                out.close();
                br.close();
                socket.close();
                return false;
            }
            out.write("AUTH LOGIN\r\n".getBytes("utf-8"));
            reply = br.readLine();
            Log.d("ThirdMessage", reply);
            if(!reply.startsWith("334")){
                in.close();
                out.close();
                br.close();
                socket.close();
                return false;
            }
            //将用户名和授权码/密码进行Base64转码，参数Base64.NO_WRAP表明转码结果不换行。
            out.write((new String(Base64.encode(address.getBytes("utf-8"), Base64.NO_WRAP)) + "\r\n").getBytes("utf-8"));
            reply = br.readLine();
            if(!reply.startsWith("334")){
                in.close();
                out.close();
                br.close();
                socket.close();
                return false;
            }
            out.write((new String(Base64.encode(password.getBytes("utf-8"), Base64.NO_WRAP)) + "\r\n").getBytes("utf-8"));
            reply = br.readLine();
            Log.d("FourthMessage", reply);
            if(!reply.startsWith("235")){
                in.close();
                out.close();
                br.close();
                socket.close();
                return false;
            }
            in.close();
            out.close();
            br.close();
            socket.close();
            return true;
        }catch(IOException e){
            e.printStackTrace();
            return false;
        }
    }

    public boolean sendEmail(){
        String mailUrl; //smtp服务器的url
        int port = 25; //邮件服务器端口号
        if(mailType == 1){
            mailUrl = "smtp.163.com";
        }else{
            mailUrl = "smtp.qq.com";
        }
        try{
            String reply; //暂时存放服务器相应消息，并进行debug处理
            socket = new Socket(mailUrl, port); //获取socket
            in = socket.getInputStream();
            out = socket.getOutputStream();
            br = new BufferedReader(new InputStreamReader(in));
            reply = br.readLine();
            out.write("HELO email\r\n".getBytes("utf-8")); //发送握手请求
            reply = br.readLine();
            if(mailType == 2){ //QQ的这里会发回来三行！！！很坑人！！！找了半天bug！！！
                reply = br.readLine();
                reply = br.readLine();
            }
            out.write("AUTH LOGIN\r\n".getBytes("utf-8"));
            reply = br.readLine();
            //将用户名和授权码/密码进行Base64转码，参数Base64.NO_WRAP表明转码结果不换行。
            out.write((new String(Base64.encode(address.getBytes("utf-8"), Base64.NO_WRAP)) + "\r\n").getBytes("utf-8"));
            reply = br.readLine();
            out.write((new String(Base64.encode(password.getBytes("utf-8"), Base64.NO_WRAP)) + "\r\n").getBytes("utf-8"));
            reply = br.readLine();
            Log.d("FourthMessage", reply);
            out.write(("MAIL FROM:<" + address + ">\r\n").getBytes("utf-8"));//邮件发送方
            reply = br.readLine();
            Log.d("FifthMessage", reply);
            if(!reply.startsWith("250")){
                in.close();
                out.close();
                br.close();
                socket.close();
                return false;
            }
            out.write(("RCPT TO:<" + rcpt_address + ">\r\n").getBytes("utf-8"));//邮件接收方
            reply = br.readLine();
            Log.d("SixthMessage", reply);
            if(!reply.startsWith("250")){
                in.close();
                out.close();
                br.close();
                socket.close();
                return false;
            }
            out.write("DATA\r\n".getBytes("utf-8"));//开始写邮件报文
            reply = br.readLine();
            Log.d("SeventhMessage", reply);
            if(!reply.startsWith("354")){
                in.close();
                out.close();
                br.close();
                socket.close();
                return false;
            }
            //下面是邮件报文自身的一部分，而非SMTP握手部分
            out.write(("from:"+ address +"\r\n").getBytes("utf8"));
            out.write(("to:"+rcpt_address+"\r\n").getBytes("utf8"));
            out.write(("subject:"+title+"\r\n").getBytes("utf8"));//邮件主题
            out.write(("Content-Type:text/plain;charset=utf8"+"\r\n\r\n").getBytes("utf8"));//进行编码和字体设置,再加一个空行
            out.write((content+"\r\n").getBytes("utf8"));//邮件正文内容
            out.write((".\r\n").getBytes("utf8"));//单独一行.为结束标志
            reply = br.readLine();
            Log.d("EighthMessage", reply);
            if(!reply.startsWith("250")){
                in.close();
                out.close();
                br.close();
                socket.close();
                return false;
            }
            out.write("QUIT\r\n".getBytes("utf-8"));
            reply = br.readLine();
            Log.d("NinthMessage", reply);
            in.close();
            out.close();
            br.close();
            socket.close();
            return true;
        }catch(IOException e){
            e.printStackTrace();
            return false;
        }
    }

    public boolean receiveLogin() {
        String mailUrl; //pop3服务器的url
        int port = 110; //邮件服务器端口号
        if (mailType == 1) {
            mailUrl = "pop3.163.com";
        } else {
            mailUrl = "pop.qq.com";
        }
        try {
            String reply; //暂时存放服务器相应消息，并进行debug处理
            socket = new Socket(mailUrl, port); //获取socket
            in = socket.getInputStream();
            out = socket.getOutputStream();
            br = new BufferedReader(new InputStreamReader(in));
            reply = br.readLine();
            Log.d("POPMessage1", reply);
            if (!reply.startsWith("+OK")) {
                in.close();
                out.close();
                br.close();
                socket.close();
                return false;
            }
            out.write(("user " + address + "\r\n").getBytes("utf-8"));
            reply = br.readLine();
            Log.d("POPMessage2", reply);
            if (!reply.startsWith("+OK")) {
                in.close();
                out.close();
                br.close();
                socket.close();
                return false;
            }
            out.write(("pass " + password + "\r\n").getBytes("utf-8"));
            reply = br.readLine();
            Log.d("POPMessage3", reply);
            if (!reply.startsWith("+OK")) {
                in.close();
                out.close();
                br.close();
                socket.close();
                return false;
            }
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public int getMailNumber(){
        String mailUrl; //pop3服务器的url
        int port = 110; //邮件服务器端口号
        if(mailType == 1){
            mailUrl = "pop3.163.com";
        }else{
            mailUrl = "pop.qq.com";
        }
        try {
            String reply; //暂时存放服务器相应消息，并进行debug处理
            socket = new Socket(mailUrl, port); //获取socket
            in = socket.getInputStream();
            out = socket.getOutputStream();
            br = new BufferedReader(new InputStreamReader(in));
            reply = br.readLine();
            Log.d("POPMessage1", reply);
            out.write(("user " + address + "\r\n").getBytes("utf-8"));
            reply = br.readLine();
            Log.d("POPMessage2", reply);
            out.write(("pass " + password + "\r\n").getBytes("utf-8"));
            reply = br.readLine();
            Log.d("POPMessage3", reply);
            if(mailType == 1){
                in.close();
                out.close();
                br.close();
                socket.close();
                //对于163邮箱，此时的reply的格式类似：+OK 4 message(s) [10474 byte(s)]，通过parseInt取得第四位上的数字
                return Integer.parseInt(reply.substring(4, reply.indexOf(" message")));
            }else{
                //qq邮箱在输入授权码后只会返回+OK，需要用list来获取一共有多少个邮件
                out.write(("list" + "\r\n").getBytes("utf-8"));
                reply = br.readLine();
                Log.d("POPMessage4", reply);
                if(!reply.startsWith("+OK")){
                    in.close();
                    out.close();
                    br.close();
                    socket.close();
                    return -1; // 如果发生错误就返回-1
                }
                String last_reply = reply; //保存当前行的上一行，来读取一共有多少个邮件
                while(!reply.equals(".")){
                    last_reply = reply;
                    reply = br.readLine();
                }
                in.close();
                out.close();
                br.close();
                socket.close();
                //此时的last_reply类似于这个样子：53 2384
                return Integer.parseInt(last_reply.substring(0, last_reply.indexOf(" ")));
            }
        }catch(IOException e){
            e.printStackTrace();
            return -1; //如果发生错误就返回-1
        }
    }

    public String getMail(int num) {
        try {
            String mailUrl; //pop3服务器的url
            int port = 110; //邮件服务器端口号
            if (mailType == 1) {
                mailUrl = "pop3.163.com";
            } else {
                mailUrl = "pop.qq.com";
            }
            String reply; //暂时存放服务器相应消息，并进行debug处理
            socket = new Socket(mailUrl, port); //获取socket
            in = socket.getInputStream();
            out = socket.getOutputStream();
            br = new BufferedReader(new InputStreamReader(in));
            reply = br.readLine();
            Log.d("POPMessage1", reply);
            out.write(("user " + address + "\r\n").getBytes("utf-8"));
            reply = br.readLine();
            Log.d("POPMessage2", reply);
            out.write(("pass " + password + "\r\n").getBytes("utf-8"));
            reply = br.readLine();
            Log.d("POPMessage3", reply);
            out.write(("retr " + num + "\r\n").getBytes("utf-8"));
            reply = br.readLine();
            Log.d("POPMessage4", reply);
            if(!reply.startsWith("+OK")){
                in.close();
                out.close();
                br.close();
                socket.close();
                return null; // 如果发生错误就返回null
            }
            String mailContent = "";
            while(!reply.equals(".")){ //这里刚开始写成了 reply!="."  debug了老半天才发现。。。我吐了
                mailContent = mailContent + "\n" + reply;
                reply = br.readLine();
            }
            in.close();
            out.close();
            br.close();
            socket.close();
            return mailContent;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
