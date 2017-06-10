package holuhoev.Client;

import holuhoev.*;
import java.nio.*;
import java.nio.channels.*;
import java.net.*;
import java.io.*;
import java.lang.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import static java.nio.channels.SelectionKey.OP_CONNECT;
import static java.nio.channels.SelectionKey.OP_READ;
import static java.nio.channels.SelectionKey.OP_WRITE;

public class ClientNIO
{
    private ClientGUI gui;
    private SocketChannel channel = null;
    private Selector selector = null;
    private selectorThread st = null;
    private String userlist = "nothing";
    private HashSet<String> names = new HashSet<>();
    private BlockingQueue<ByteBuffer> outputBuffer = new ArrayBlockingQueue<>(2);

    public String clientName = "NoName";

    public ClientNIO(ClientGUI gui)
    {
        this.gui = gui;
    }

    public boolean makeConnection(InetAddress address,int port) throws IOException
    {


        try{
        channel = SocketChannel.open();
        channel.configureBlocking(false);
        selector = Selector.open();
        channel.register(selector, OP_CONNECT);
        channel.connect(new InetSocketAddress(address, port));

        if(channel.finishConnect())
            channel.register(selector,OP_READ|OP_WRITE);

        }catch (Exception ex)
        {
            return false;
        }
        startClientSelector();

        sendAccessMessage();
        return true;

    }

    public void sendDisconnectMessage()
    {
        ByteBuffer data = formMessage(clientName,MessageType.DISCONNECT);
        putToBuffer(data);
    }

    public void sendAccessMessage()
    {
        ByteBuffer data = formMessage(" ", MessageType.ACCESS);
        try
        {
            channel.write(data);
        } catch (IOException e)
        {
            e.printStackTrace();
        }

    }

    public void sendConnectMessage(String username)
    {
        ByteBuffer data = formMessage(username, MessageType.CONNECT);
        putToBuffer(data);
    }

    private void putToBuffer(ByteBuffer data)
    {
        try
        {
            outputBuffer.put(data);
            SelectionKey key = channel.keyFor(selector);
            key.interestOps(OP_WRITE);
            selector.wakeup();
        } catch (InterruptedException e)
        {
            e.printStackTrace();
        }
    }

    public void sendText(String text)
    {
        ByteBuffer data = formMessage(text, MessageType.TEXT);
        putToBuffer(data);
    }

    public String getUserList()
    {
        return userlist;
    }

    private void startConsole()
    {
        //Поток бизнес логики
        Scanner scanner = new Scanner(System.in);
        while (true)
        {
            String line = scanner.nextLine();
            if ("q".equals(line))
            {
                System.exit(0);
            }
            if ("connect".equals(line))
            {
                System.out.println("Введите ник:");
                line = scanner.nextLine();
                ByteBuffer data = formMessage(line, MessageType.CONNECT);
                try
                {
                    outputBuffer.put(data);
                } catch (InterruptedException e)
                {
                    e.printStackTrace();
                }
            }
            try
            {
                ByteBuffer data = formMessage(line, MessageType.TEXT);
                outputBuffer.put(data);
            } catch (InterruptedException e)
            {
                e.printStackTrace();
            }
            SelectionKey key = channel.keyFor(selector);
            key.interestOps(OP_WRITE);
            selector.wakeup();
        }
    }

    private ByteBuffer formMessage(String line, MessageType type)
    {
        byte[] messageBytes = new byte[0];

        //get bytes from text line
        try
        {
            if (type == MessageType.TEXT)
            {
                String result = getCurTime() + " " + clientName + " : " + line;
                messageBytes = result.getBytes("UTF-8");
            }
            if (type == MessageType.CONNECT)
                messageBytes = line.getBytes("UTF-8");
            if(type == MessageType.DISCONNECT)
                messageBytes = line.getBytes("UTF-8");

        } catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();
        }

        ByteBuffer buff = MessageHelper.sendingMessage(messageBytes, type);

        return buff;
    }

    private String getCurTime()
    {
        Calendar rightNow = Calendar.getInstance();
        int hour = rightNow.get(Calendar.HOUR_OF_DAY);
        int min = rightNow.get(Calendar.MINUTE);
        int sec = rightNow.get(Calendar.SECOND);

        return "[" + hour + ":" + (min<10?"0"+min:min) + ":" + (sec<10?"0"+sec:sec) + "]";
    }

    private void startClientSelector()
    {
        st = new selectorThread("Selector thread", channel);
        st.start();
    }


    public class selectorThread extends Thread
    {
        private SocketChannel channel = null;

        selectorThread(String str, SocketChannel client)
        {
            super(str);
            channel = client;
        }

        public void run()
        {
            System.out.println("Selector thread");
            try
            {
                while (channel.isOpen())
                {
                    selector.select();

                    Iterator<SelectionKey> keys = selector.selectedKeys().iterator();
                    SelectionKey key;

                    while (keys.hasNext())
                    {
                        key = keys.next();
                        keys.remove();

                        if (!key.isValid())
                            continue;
                        if (key.isConnectable())
                        {
                            channel.finishConnect();
                        }
                        if (key.isReadable())
                        {
                            readKeyAndPrint();
                        }
                        if (key.isWritable())
                        {
                            ByteBuffer data = outputBuffer.poll();
                            if (data != null)
                            {
                                channel.write(data);
                                key.interestOps(OP_READ);
                            }
                        }
                    }
                }
            } catch (IOException e)
            {
                e.printStackTrace();
            }

        }

        private void readKeyAndPrint() throws IOException
        {
            ByteBuffer message = MessageHelper.readMessage(channel);
            if (message != null)
            {
                message.flip();
                byte[] msg = new byte[message.remaining()];
                message.get(msg, 0, msg.length);

                byte[] arr = Arrays.copyOfRange(msg, 1, msg.length);
                String str = new String(arr, StandardCharsets.UTF_8);

                //in case when receive connect message
                if (msg[0] == 1)
                {

                    System.out.println(getCurTime() + " " + str + " is connected.");
                    gui.addUsername(str);
                }
                if (msg[0] == 0)
                {
                    //text message
                    gui.printTextToListView(str);
                }
                if(msg[0] == 3)
                {
                    System.out.println("Get userlist1: " + str);
                    userlist = str;
                }
                if(msg[0] == 2)
                {
                    try
                    {


                        gui.usernamesList.remove(str);
                    }catch (Exception ex)
                    {

                    }
                }
            }
        }
    }

}