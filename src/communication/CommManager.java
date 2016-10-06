package communication;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class CommManager {
	private static String host = "192.168.14.14";
	private int port = 3004;
	private Socket s;
	private BufferedReader input;
	private PrintWriter out;
	public CommManager() {
		try	{
			s = new Socket(host,port);
			input = new BufferedReader(new InputStreamReader(s.getInputStream()));
			out = new PrintWriter(s.getOutputStream(), true);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	public String readRPI() {
		try	{
			String answer = input.readLine();
			return answer;
		} catch (Exception e) {
			return e.getMessage();
		}
	}
	
	public boolean writeRPI(String outData) {
		try	{	         
			System.out.println("outdata:"+outData);
			out.println(outData);
	        return true;
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return false;
		}
	}

	public boolean disconnect() {
		try {
			s.close();
			return true;
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return false;
		}
	}
	
}