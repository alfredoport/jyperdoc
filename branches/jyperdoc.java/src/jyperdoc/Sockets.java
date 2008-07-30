package jyperdoc;

import java.net.*;
import java.io.*;

public class Sockets {
    
	private int port; 
	private String hostname;
	private InetAddress addr;
	private Socket socket;
	
	/**
	 * 
	 * @param port
	 * @param hostname
	 * @throws Exception
	 */
	public Sockets(int port, String hostname) throws Exception {
		
		this.port = port;
		this.hostname = hostname;
		
        // Create a socket to the host
        this.addr = InetAddress.getByName(this.hostname);
        this.socket = new Socket(addr, port);
	}
	
	/**
	 * 
	 * @param command
	 * @param value
	 * @throws Exception
	 */
    public String post(String command, String value) throws Exception  {

            // Construct data
            String data = command + "=" + value;

            // Send header
            BufferedWriter wr = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF8"));

            wr.write("POST" + this.hostname + ":" + this.port + "HTTP/1.1");
            wr.write("Content-Length: "+ data.length() + "\r\n");
            wr.write("Content-Type: application/plain-text\r\n");
            wr.write("\r\n");
        
            // Send data
            wr.write(data);
            wr.flush();
        
            // Get response
            BufferedReader rd = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String line;
            StringBuffer response = new StringBuffer("");
            while ((line = rd.readLine()) != null) {

            	response.append(line);
            }
            wr.close();
            rd.close();
            
            return response.toString();
    }    
}
