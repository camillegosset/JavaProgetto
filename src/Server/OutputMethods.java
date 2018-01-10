package Server;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Scanner;

import Model.Email;

public class OutputMethods {
    // 2+2=4

	public static boolean writeMessage(String message, Integer ID) {
		PrintWriter p = null;
		try {
			FileWriter f= new FileWriter(ID + ".txt", true);
			p = new PrintWriter(f);
			p.println(message);
			p.flush();
		}
		catch(IOException | RuntimeException e) {
						System.out.println(e.getMessage());
						return false;}
		finally {if (p!=null) //NB: se fallisce la new il PrintWriter e' null!!
			  		p.close();}
		return true;
	}


	public static boolean incrementID() {
		int i= InputMethods.getNewID();
		i++;
		PrintWriter p = null;
		try {
			FileWriter f= new FileWriter("ID.txt");
			p = new PrintWriter(f);
			p.println(i);
			p.flush();
		}
		catch(IOException | RuntimeException e) {
						System.out.println(e.getMessage());
						return false;}
		finally {if (p!=null) //NB: se fallisce la new il PrintWriter e' null!!
			  		p.close();}
		return true;
	}
/*
methods to add:

	-> addEmail(Email email){
	apre il file Emails.txt dove aggiunge la mail (senza il messaggio!!)
	apre il file clientX.txt del mittente
	for(Clients client : Receivers){

	}
	apre il file clientX.txt di tutti i destinatari!!!!

	}
*/

	/*public static void addEmail(Email email) {
		// TODO Auto-generated method stub

	}*/

	public static void removeEmail(Email email, String client) {
		email.setDeleted(LocalDate.now());
		removeEmail(email.getID(), client);
		//Dobbiamo magari fare il metodo nella classe cliente
	}


	public static boolean removeEmail(int index, String client) {
		ArrayList<Integer> emails_received = new ArrayList<Integer>();
		ArrayList<Integer> emails_sent= new ArrayList<Integer>();
		ArrayList<Integer> emails_deleted= new ArrayList<Integer>();
		Scanner scf= null;
		Scanner s= null;
		try {
			scf= new Scanner(new File(client+".txt"));
			String l="";
			while (scf.hasNext()) {
				l= l+ scf.nextLine();
			}
			s = new Scanner(l).useDelimiter("\\s*#\\s*");
			String received= s.next();
			String sent= s.next();
			String deleted= s.next();

			Scanner line= new Scanner(received).useDelimiter("\\s*,\\s*");
			while(line.hasNext()) {
				emails_received.add(line.nextInt());
			}

			line = null;
			line= new Scanner(sent).useDelimiter("\\s*,\\s*");
			while(line.hasNext()) {
				int i= line.nextInt();
				System.out.println(i);
				emails_sent.add(i);
			}

			line= null;
			line= new Scanner(deleted).useDelimiter("\\s*,\\s*");
			while(line.hasNext()) {
				emails_deleted.add(line.nextInt());
			}

			Integer i= (Integer) index;
			boolean b1= emails_received.remove(i);
			boolean b2= emails_received.remove(i);

			if(b1 || b2) {
				emails_deleted.add(i);
			}
		}catch (IOException e)
  		{System.out.println("PROBLEMA: " + e.getMessage());
 		 return false;
 		 }
		finally {if (scf!=null) //NB: se fallisce la new File lo scanner e' null
 				scf.close();
		if(s!=null)
			s.close();
		}
		PrintWriter p= null;
		try {
			FileWriter f= new FileWriter(client+".txt");
			p= new PrintWriter(f);
			String l="";
			for(int i: emails_received) {
				l= l+i+", ";
			}
			l= l+"#";
			p.println(l);
			l="";
			for(int i: emails_sent) {
				l=l+i+ ", ";
			}
			l=l+"#";
			p.println(l);
			l="";
			for(int i: emails_deleted) {
				l=l+i+ ", ";
			}
			l=l+"#";
			p.println(l);
			p.flush();
		}catch (IOException e)
  		{System.out.println("PROBLEMA: " + e.getMessage());
 		 return false;
 		 }
		finally {if (p!=null) //NB: se fallisce la new File lo scanner e' null
 				p.close();}
		return true;
	}


    public static boolean createEmail(Email email) {
		String out= email.toString();
		PrintWriter p = null;
		try {
			FileWriter f= new FileWriter(email.getID()+".txt");
			p = new PrintWriter(f);
			p.println(out);
			p.flush();
		}
		catch(IOException | RuntimeException e) {
						System.out.println(e.getMessage());
						return false;}
		finally {if (p!=null) //NB: se fallisce la new il PrintWriter e' null!!
			  		p.close();}
		return true;
	}

	public static void addEmailClientFile(Email email, String client , int line) {
			int count= InputMethods.counter(line, client+".txt", '#');
			//System.out.println(count);
			//PrintWriter p= null;
			Scanner scanner = null;
			FileWriter writer= null;
			String out= ","+email.getID();
			try {

				File file= new File(client+".txt");
				scanner = new Scanner(file);

				String str="";
				while(scanner.hasNext()) {
					str= str+ scanner.nextLine();
				}
				String newStr= str.substring(0, count)+ out + str.substring(count);
				writer = new FileWriter(file);
				writer.write(newStr);
				writer.close();

			}catch (Exception e)
	  		{System.out.println("Eccezione: " + e.getClass()+ " - " + e.getMessage());
	  		 }
			finally {
			if(scanner!= null) {scanner.close();}
			//if(writer != null) { writer.close();}
			}
		}

		
		public static void addEmail(Email email) {

			PrintWriter p = null;
			String out= email.toString();
			try {
				FileWriter f= new FileWriter("emails.txt", true);
				p= new PrintWriter(f);
				p.println();
				p.println(out);
				p.flush();

			}catch (Exception e)
	  		{System.out.println("Eccezione: " + e.getClass()+ " - " + e.getMessage());
	  		 }
			finally {if (p!=null)
	  				p.close();}

			addEmailClientFile(email, email.getSender(), 2);

			for(String receiver: email.getReceivers()) {
				addEmailClientFile(email, receiver, 1);
				//notify
			}


		}
}
