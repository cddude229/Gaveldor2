package run;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class RelayThread extends Thread {
	private Socket in;
	private OutputStream os;
	private Server server;
	
	  RelayThread(Socket in,Server server){
		    this.in = in;
		    this.server = server;
	  }
	  
	  public void run(){
		  try{
			  System.out.println("Relay thread up!");
		      InputStream is = in.getInputStream();
		      byte[] buf = new byte[4096];

		      int nRead;
		      while((nRead = is.read(buf, 0, buf.length)) != -1){
		    	  os.write(buf, 0, nRead); os.flush();
		    	  System.out.write(buf, 0, nRead);
		      }
		      if (this.server.firstSocket == this.in) {
		    	  this.server.firstSocket = null; 
		      }
		      if (this.server.secondSocket == this.in) {
		    	  this.server.secondSocket = null; 
		      }
		      in.shutdownOutput();
		      this.server.connectionClosed();
	    }catch(IOException ioe){
		      // do nothing
	    }
	  }
	  
	  public void setOutputStream(Socket out) {
		  try {
			this.os = out.getOutputStream();
		} catch (IOException e) {
			e.printStackTrace();
		}
	  }
}
