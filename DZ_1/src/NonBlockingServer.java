
import java.io.*;
import java.nio.*;
import java.nio.channels.*;
import java.net.*;
import java.util.*;
import java.nio.charset.*;
import java.lang.*;


public class NonBlockingServer
{
    public Selector sel = null;
    public ServerSocketChannel server = null;
    public SocketChannel socket = null;
    public int port = 4900;
    String result = null;


    public NonBlockingServer()
    {
		System.out.println("Inside default ctor");
    }
    
	public NonBlockingServer(int port)
    {
		System.out.println("Inside the other ctor");
		port = port;
    }

    public void initializeOperations() throws IOException,UnknownHostException
    {
		System.out.println("Inside initialization");
		sel = Selector.open();
		server = ServerSocketChannel.open();
		server.configureBlocking(false);
		InetAddress ia = InetAddress.getLocalHost();
		InetSocketAddress isa = new InetSocketAddress(ia,port);
		server.socket().bind(isa);
    }
    
	public void startServer() throws IOException
    {
		System.out.println("Inside startserver");
        initializeOperations();
		System.out.println("Abt to block on select()");
		SelectionKey acceptKey = server.register(sel, SelectionKey.OP_ACCEPT );	
	
		while (acceptKey.selector().select() > 0 )
		{	
	    
			Set readyKeys = sel.selectedKeys();
			Iterator it = readyKeys.iterator();

			while (it.hasNext()) {
				SelectionKey key = (SelectionKey)it.next();
				it.remove();
                
				if (key.isAcceptable()) {
					//System.out.println("Key is Acceptable");
					ServerSocketChannel ssc = (ServerSocketChannel) key.channel();
					socket = (SocketChannel) ssc.accept();
					socket.configureBlocking(false);
					key = socket.register(sel,SelectionKey.OP_READ|SelectionKey.OP_WRITE);
				}
				if (key.isReadable()) {
					// System.out.println("Key is readable");
					String ret = readMessage(key);
					if (ret.length() > 0) {
						writeMessage(socket,ret);
					}
				}
				if (key.isWritable()) {
					System.out.println("THe key is writable");
					String ret = readMessage(key);
					socket = (SocketChannel)key.channel();
					if (result.length() > 0 ) {
						writeMessage(socket,ret);
					}
				}
			}
		}
    }

    public void writeMessage(SocketChannel socket,String ret)
    {
		System.out.println("Inside the loop");
		System.out.println("Inside the loop");

		if (ret.equals("quit") || ret.equals("shutdown"))
		{
			return;
		}
		File file = new File(ret);
		try
		{
			//3
			int nBytes = 0;

			FileChannel fc = new FileInputStream(file).getChannel();
			ByteBuffer buffer = ByteBuffer.allocate(1024);
			while (fc.read(buffer) >= 0 || buffer.position() > 0)
			{
				buffer.flip();

//                Charset set = Charset.forName("us-ascii");
//                CharsetDecoder dec = set.newDecoder();
//                CharBuffer charBuf = dec.decode(buffer);
//                System.out.println(charBuf.toString());
//                buffer = ByteBuffer.wrap((charBuf.toString()).getBytes());

				nBytes += socket.write(buffer);
				buffer.compact();
			}
			//2
//
//			RandomAccessFile rdm = new RandomAccessFile(file,"r");
//			FileChannel fc = rdm.getChannel();
//
//            long position =0;
//            long count = fc.size();
//            fc.transferTo(position,count,socket);

			//1
//
//            FileChannel fc = new FileInputStream(file).getChannel();
//			ByteBuffer buffer = ByteBuffer.allocate(1024);
//			fc.read(buffer);
//			buffer.flip();
//
//			Charset set = Charset.forName("us-ascii");
//			CharsetDecoder dec = set.newDecoder();
//			CharBuffer charBuf = dec.decode(buffer);
//			System.out.println(charBuf.toString());
//			buffer = ByteBuffer.wrap((charBuf.toString()).getBytes());
//
//			int nBytes = socket.write(buffer);
			System.out.println("nBytes = " + nBytes);
			result = null;
		} catch (Exception e)
		{
			e.printStackTrace();
		}

    }
  
    public String readMessage(SelectionKey key)
    {
		int nBytes = 0;
		socket = (SocketChannel)key.channel();
        ByteBuffer buf = ByteBuffer.allocate(1024);
		try
		{
			while ((nBytes = socket.read(buf)) > 0)
			{
				buf.flip();

				Charset charset = Charset.forName("us-ascii");
				CharsetDecoder decoder = charset.newDecoder();
				CharBuffer charBuffer = decoder.decode(buf);
				result += charBuffer.toString();

				buf.compact();
			}
	    
        }
		catch(IOException e)
		{
			e.printStackTrace();
		}
		return result;
    }

    public static void main(String args[])
    {
		NonBlockingServer nb = new NonBlockingServer();
		try
		{
			nb.startServer();
		}
		catch (IOException e)
		{
			e.printStackTrace();
			System.exit(-1);
		}
		
	}
}



