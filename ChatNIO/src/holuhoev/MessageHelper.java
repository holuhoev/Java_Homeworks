package holuhoev;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class MessageHelper
{
    public static ByteBuffer readMessage(SocketChannel channel) throws IOException
    {
        ByteBuffer buf = ByteBuffer.allocate(4);
        //TODO cделать отдельные методы внутри них запихнуть мессадж тайп)
        try
        {
            int nBytes = channel.read(buf);

            if (nBytes == 4)
            {
                buf.flip();
                int size = buf.getInt();

                ByteBuffer message = ByteBuffer.allocate(size + 1);
                channel.read(message);


                return message;
            }
        }catch (Exception ex)
        {
            System.out.println("some error: " + ex.getMessage());
        }

        return null;
    }
    public static ByteBuffer sendingMessage(byte[] messageBytes,MessageType type)
    {
        //Header = size of message + type
        ByteBuffer header = ByteBuffer.allocate(Constant.INT_SIZE);
        header.putInt(messageBytes.length);

        ByteBuffer typeBuf = ByteBuffer.allocate(1);

        if(type == MessageType.TEXT)
            typeBuf.put((byte)0);
        if(type == MessageType.CONNECT)
            typeBuf.put((byte)1);
        if(type == MessageType.DISCONNECT)
            typeBuf.put((byte)2);
        if(type == MessageType.ACCESS)
        {
            typeBuf.put((byte) 3);
            System.out.println("Send user list");
        }

        header.flip();
        typeBuf.flip();

        ByteBuffer byteBuffer = ByteBuffer.allocate(header.capacity() + messageBytes.length + 1);

        byteBuffer.put(header);
        byteBuffer.put(typeBuf);
        byteBuffer.put(messageBytes);

        byteBuffer.flip();

        return byteBuffer;
    }

}
