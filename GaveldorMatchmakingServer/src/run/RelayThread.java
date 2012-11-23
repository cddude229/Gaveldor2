package run;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class RelayThread extends Thread {
	private Socket in,out;
	private Server server;
	
	  RelayThread(Socket in, Socket out, Server server){
		    this.in = in;
		    this.out = out;
		    this.server = server;
	  }
	  
	  public void run(){
		  try{
		      InputStream is = in.getInputStream();
		      OutputStream os = out.getOutputStream();
		      byte[] buf = new byte[4096];

		      int nRead;
		      while((nRead = is.read(buf, 0, buf.length)) != -1){
		    	  os.write(buf, 0, nRead); os.flush();
		    	  System.out.write(buf, 0, nRead);
		      }

		      out.shutdownInput();
		      in.shutdownOutput();
		      this.server.connectionClosed();
	    }catch(IOException ioe){
		      // do nothing
	    }
	  }
}
