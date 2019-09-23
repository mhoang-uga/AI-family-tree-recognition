import java.io.*;
import java.math.*;
import java.text.*;
import java.util.*;

public class Traceroute{

public static void main(String[] args){
	if (args.length == 1){	//the tcpdump text file
		String filename = args[0];
		try{//open file
			FileInputStream fis = new FileInputStream(filename);
			DataInputStream dis = new DataInputStream(fis);
			BufferedReader br = new BufferedReader(new InputStreamReader(dis));
			
			//list of Tracert hops
			ArrayList<Traceroute> hops = new ArrayList<Traceroute>();
			
			boolean newLine = true;
			String str = "";
			String newStr = "";
			int count = 0; //line count
  
			//reading the file
			while ((str = br.readLine()) != null){
				newLine = true;
				String resetString = newStr + str;
				str = resetString;
				//deal with finding where the new line starts. New lines are determined by whether or not the first character in a line is an integer
				while (newLine){
					if ((newStr = br.readLine()) != null){
						String str2 = "";
						str2 += newStr.charAt(0);
					try{
						Integer.parseInt(str2);
						newLine = false;
					}
					catch (Exception e){str += newStr;}
				}
					else{
						break;
					}
				}//while inside
				
				//Dealing with ICMP part first
				//find ICMP and get the time2
				int icmpIn= str.indexOf("ICMP"); 
				if (icmpIn != -1){//dealing with any string that has ICMP first 
					// find time2
					String time2 = "";
					for (int i = 0; str.charAt(i) != ' '; i++){
						time2 += str.charAt(i);
					}//for
					
					//find 2nd id
					int idIn1 = str.indexOf("id");
					str = str.substring(idIn1 + 3);					
					int idIn2 = str.indexOf("id");
					String idStr = "";
					for (int n = idIn2 + 3; str.charAt(n) != ','; n++){
						idStr += str.charAt(n);
					}

					//if id != 0, then we can find the IP
					if (!idStr.equals("0")){
						int id = 0;
						double doubTime = 0;
						
						id = Integer.parseInt(idStr); // make id int
						doubTime = Double.parseDouble(time2); // make time2 double

						//find IP
						int ipIn = str.indexOf("length");
						str = str.substring(ipIn);
						ipIn = str.indexOf(")") + 2;
						str = str.substring(ipIn);
						ipIn =0;
						
						//values 48-57 are numbers from 0 to 9
						//if the value of the character is not a number (if its value is not in between 48 to 57), then ipIn++ to find the right index
						for (int i = 0; !(Character.valueOf(str.charAt(i)) >= 48 && Character.valueOf(str.charAt(i)) <= 57); i++){
							ipIn++;
						}
						
						//get the str from the ip index to the end so we can get the IP
						str = str.substring(ipIn);
						String ipStr = "";
						for (int i = 0; (Character.valueOf(str.charAt(i)) >= 48 && Character.valueOf(str.charAt(i)) <= 57) || Character.valueOf(str.charAt(i)) == 46; i++){
							ipStr += str.charAt(i);
						}
						//if second id == first id, add it to hops' list
						for (int i = 0; i < hops.size(); i++){
							int idComp = hops.get(i).getId();

							if (i != count && idComp == id){
								hops.get(i).setTime2(doubTime);
								hops.get(i).setIP(ipStr);
							}
						}
					}
				}
				else{//all of the part that are NOT ICPM
					
					//find first id
					int idIn2 = str.indexOf("id");
					String idStr = "";
					for (int i = idIn2 + 3; str.charAt(i) != ','; i++){
						idStr += str.charAt(i);
					}

					//if id == 0, nothing to find 
					if (!idStr.equals("0")){
						//find time1
						String time1 = "";
						for (int i = 0; str.charAt(i) != ' '; i++){
							time1 += str.charAt(i);
						}

						//find the TTL
						int ttlIn = str.indexOf("ttl");
						String ttlStr = "";
						for (int i = ttlIn + 4; str.charAt(i) != ','; i++){
							ttlStr += str.charAt(i);
						}

						int ttl, id;
						double doubtime1 = 0;
						
						//parse ttl and id from string to int
						ttl = Integer.parseInt(ttlStr);
						id = Integer.parseInt(idStr);
						doubtime1 = Double.parseDouble(time1);
						//if a valid hop has been found, add it to the list
						if (ttl > 0 && id > 0 && time1.length() > 0){
							Traceroute hop = new Traceroute(ttl, id, "", doubtime1);
							hops.add(hop);
						}//if a hop is found
					}//if id is not 0
				}//else for nonICMP part
				count++;
			}//while reading files

			//find max TTL number
			int maxTTL=0;
			for (int h = 0; h < hops.size(); h++){
				int temp = hops.get(h).getTtl();
				if (temp > maxTTL){
					maxTTL = temp;
				}
			}

			ArrayList<Traceroute> hop2 = new ArrayList<Traceroute>();

			//find the rounded time difference, add new hop with new time to a new hop list
			for (int t = 0; t < maxTTL; t++){
				for (int h = 0; h < hops.size(); h++){
					int ttl = hops.get(h).getTtl();

					if (ttl == t){
						double time1 = hops.get(h).getTime1();
						double time2 = hops.get(h).getTime2();
						String roundedTime = null;

						if (time2 != 0){
							//calculating the time to send package
							double time = (time2 - time1) * 1000;
							
							BigDecimal bd = new BigDecimal(Double.toString(time));
							bd = bd.setScale(3, BigDecimal.ROUND_HALF_UP);
							//formatting it to 00.000
							DecimalFormat myFormatter = new DecimalFormat("0.000");
							roundedTime = myFormatter.format(Double.parseDouble(bd.toString()));

							Traceroute hop = new Traceroute(hops.get(h).getTtl(), hops.get(h).getIP(), roundedTime);
							hop2.add(hop);

						}
					}
				}
			}

			//print as required
			ArrayList<Traceroute> hop3 = new ArrayList<Traceroute>(); //a temp list of hops by the same ttl
			ArrayList<String> printlist = new ArrayList<String>(); //a list of what to print. probably don't need this.

			for (int t = 0; t < maxTTL; t++){
				for (int h = 0; h < hop2.size(); h++){
					if (t == hop2.get(h).getTtl()){
						hop3.add(hop2.get(h));
					}
				}

				boolean toPrint = true; //check if TTL needs to be printed
				//checking if 
				while (!hop3.isEmpty()){
					if (toPrint){
						printlist.add("TTL " + hop3.get(0).getTtl());
					}
  
					printlist.add(hop3.get(0).getIP());
					printlist.add(hop3.get(0).getTime() + " ms");

					String ip = hop3.get(0).getIP();
					hop3.remove(0);
					
					//compare ips with the same ttl and add to string
					for (int i = 0; i < hop3.size(); i++){
						if (ip.equals(hop3.get(i).getIP())){
							printlist.add(hop3.get(i).getTime() + " ms");
							hop3.remove(i);
							i--;
						}
					}

					toPrint = false;
				}//while hop3 is not empty
			}
			//This is where the printing starts
			for (int i = 0; i < printlist.size(); i++){
				System.out.println(printlist.get(i));
			}

			dis.close();
		}//if the file could be found;
		catch (Exception e){
			System.out.println("Your file name is wrong.");
		}
	}
	else{//if the filename is wrong 
		System.out.println("Your file name is wrong.");
		System.exit(1);
	}
}//end of main class

private int ttl;
private int id;
private double time1, time2;
private String time;
private String IP;


//a hop (traceroute)
public Traceroute(int ttl, int id, String IP, double time1){
	this.ttl = ttl;
	this.id = id;
	this.IP = IP;
	this.time1 = time1;
}

/*
* A hop with information needed pertaining to printing
*/
public Traceroute(int ttl, String IP, String time){
	this.ttl = ttl;
	this.IP = IP;
	this.time = time;
}

//get and set for Traceroute
public String getTime(){return time;}
public double getTime1(){return time1;}
public double getTime2(){return time2;}
public int getTtl(){return ttl;}
public String getIP(){return IP;}
public void setTime1(double time1){this.time1 = time1;}
public void setTime2(double time2){this.time2 = time2;}
public void setIP(String IP){this.IP = IP;}
public int getId(){return id;}

}