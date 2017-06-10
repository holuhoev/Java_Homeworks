package holuhoev.Server;

import holuhoev.Constant;
import holuhoev.MessageHelper;
import holuhoev.MessageType;

import java.io.Console;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.net.*;
import java.nio.*;
import java.nio.channels.*;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class ChatServerNIO implements Runnable
{
    private final int port;
    private ServerSocketChannel ssc;
    private Selector selector;
    private List<String> names = new ArrayList<String>();

    private ChatServerNIO(int port) throws IOException
    {
        this.port = port;
        this.ssc = ServerSocketChannel.open();
        this.ssc.socket().bind(new InetSocketAddress(port));
        this.ssc.configureBlocking(false);
        this.selector = Selector.open();

        this.ssc.register(selector, SelectionKey.OP_ACCEPT);
    }

    @Override
    public void run()
    {
        try
        {
            System.out.println("Server starting on port " + this.port);

            Iterator<SelectionKey> iter;
            SelectionKey key;
            while (this.ssc.isOpen())
            {
                selector.select();
                iter = this.selector.selectedKeys().iterator();
                while (iter.hasNext())
                {
                    key = iter.next();
                    iter.remove();

                    if (!key.isValid())
                        continue;

                    if (key.isAcceptable()) this.handleAccept(key);
                    if (key.isReadable()) this.handleRead(key);
                }
            }
        } catch (IOException e)
        {
            System.out.println("IOException, server of port " + this.port + " terminating. Stack trace:");
            e.printStackTrace();
        }
    }

    private void handleAccept(SelectionKey key) throws IOException
    {
        SocketChannel sc = ((ServerSocketChannel) key.channel()).accept();
        String address = (new StringBuilder(sc.socket().getInetAddress().toString())).append(":").append(sc.socket().getPort()).toString();
        sc.configureBlocking(false);

        sc.register(selector, SelectionKey.OP_READ | SelectionKey.OP_WRITE, address);

        System.out.println("accepted connection from: " + address);
    }

    private void handleRead(SelectionKey key) throws IOException
    {
        SocketChannel ch = (SocketChannel) key.channel();

        ByteBuffer message = MessageHelper.readMessage(ch);

        if (message != null)
        {
            message.flip();
            byte[] msg = new byte[message.remaining()];
            message.get(msg, 0, msg.length);


            MessageType type = (msg[0] == 0 ? MessageType.TEXT : (msg[0] == 1 ? MessageType.CONNECT : (msg[0] == 2 ? MessageType.DISCONNECT : MessageType.ACCESS)));

            if (type == MessageType.ACCESS)
            {
                sendUserList(ch);
                return;
            }
            if (type == MessageType.CONNECT)
            {
                String str = new String(Arrays.copyOfRange(msg, 1, msg.length), StandardCharsets.UTF_8);
                names.add(str);
            }
            if (type == MessageType.DISCONNECT)
            {
                String str = new String(Arrays.copyOfRange(msg, 1, msg.length), StandardCharsets.UTF_8);
                names.remove(str);
            }

            sendAll(msg, type);
        }
    }


    private void sendUserList(SocketChannel socketChannel)
    {
        try
        {
            StringBuilder users = new StringBuilder("");
            for (String user : names)
            {
                users.append(user);
                users.append(" ");
            }


            ByteBuffer buffer = MessageHelper.sendingMessage(users.toString().getBytes("UTF-8"), MessageType.ACCESS);
            socketChannel.write(buffer);

        } catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    private void sendAll(byte[] msg, MessageType type) throws IOException
    {
        ByteBuffer msgBuf = MessageHelper.sendingMessage(Arrays.copyOfRange(msg, 1, msg.length), type);

        for (SelectionKey key : selector.keys())
        {
            if (key.isValid() && key.channel() instanceof SocketChannel)
            {
                SocketChannel sch = (SocketChannel) key.channel();
                // key.interestOps(SelectionKey.OP_READ);
                sch.write(msgBuf);
                msgBuf.rewind();
            }
        }
    }

    public static void main(String[] args) throws IOException
    {
        boolean run = false;
        do
        {
            System.out.println("Введите порт: ");
            try
            {


                Scanner reader = new Scanner(System.in);  // Reading from System.in
                int port = reader.nextInt();

                if(port > 1000 && port < 10000)
                {
                    ChatServerNIO server = new ChatServerNIO(port);
                    (new Thread(server)).start();
                    run = true;
                }
                else
                    System.out.println("Неподходящий порт");

            } catch (Exception ex)
            {

            }
        } while(!run);
    }
}