package Server;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Scanner;

import Model.Email;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class InputMethods {

    
    public static ArrayList<String> inputLogin() {  //reading logins all
		Scanner scf= null;
		Scanner s= null;
		ArrayList<String> lista_login= new ArrayList<>();
		try {
			scf = new Scanner(new File("logins.txt"));
			String l="";
			while(scf.hasNext()) {
				l=l+ scf.nextLine();
			}
			s= new Scanner(l).useDelimiter("\\s*;\\s*");
			while(s.hasNext()) {
				lista_login.add(s.next());
			}
		}catch (IOException e) {
			System.out.println("PROBLEMA: " + e.getMessage());
		} finally {
			if (scf != null) {
				scf.close();
			}
		}
		return lista_login;
	}

	public static synchronized boolean egsistsLogin(String login, Server server) {
		ArrayList<String> clientList=new ArrayList<String>();
		Scanner scf = null;
		boolean esiste= false;
		try {
			scf = new Scanner(new File("logins.txt"));
			String l="";
			while (scf.hasNext()) {
				clientList.add(l= scf.next());
				if(l.equals(login)) {
					esiste= true;
				}
			}
		}catch (IOException e)
  		{System.out.println("PROBLEMA: " + e.getMessage());
 		 return false;}
		finally {if (scf!=null) //NB: se fallisce la new File lo scanner e' null
 				scf.close();}
		server.updateClients(clientList);
		return esiste;
	}

	//not sure if we need a logs file for every client
	public static void visualizzareLogs(String login) {  
		Scanner scf = null;
		try {
			scf = new Scanner(new File(login+"/logs.txt"));
			String l="";
			while (scf.hasNext()) {
				l= scf.nextLine();
				System.out.println(l);
			}
		}catch (IOException e)
  		{System.out.println("PROBLEMA: " + e.getMessage());
 		 return;}
		finally {if (scf!=null) //NB: se fallisce la new File lo scanner e' null
 				scf.close();}
	}

	@SuppressWarnings("finally")
	public static String getMessage(Integer ID) {
		StringBuffer message = new StringBuffer("");
		Scanner scf = null;
		try {
			scf = new Scanner(new File("mail" + ID + ".txt"));
			while (scf.hasNext()) {
				message.append(scf.nextLine());
			}
		} catch (IOException e) {
			System.out.println("PROBLEMA: " + e.getMessage());
			message.append("No content: We couldn't get the Email.");
		} finally {
			if (scf != null) {
				scf.close();
			}
			return message.toString();
		}

	}

	
	public static int getNewID() {
		Scanner scf= null;
		int i= Integer.MIN_VALUE;
		try {
			scf = new Scanner(new File("ID.txt"));
			i= (int) scf.nextFloat();
			//System.out.println(i);
		}catch (IOException e) {
			System.out.println("PROBLEMA: " + e.getMessage());
		} finally {
			if (scf != null) {
				scf.close();
			}
		}
		return i;

	}

	
	public static Email getEmail(Integer id) {
		Scanner scf = null;
		Scanner s=null;
		Email email= null;
		try {
			scf = new Scanner(new File("Email"+id+".txt"));
			String tmp = "";
			while (scf.hasNext()) {
				tmp= tmp+ scf.next();
			}
			s = new Scanner(tmp).useDelimiter("\\s*#\\s*");
			Integer ID= s.nextInt();
			String topic= s.next();
			LocalDate date= LocalDate.parse(s.next());
			String sender= s.next();
			email = new Email (topic, sender, date, ID);
			while( s.hasNext()) {
				email.aggiungereReceiver(s.next());
			}
		}catch (Exception e)
  		{System.out.println("Eccezione: " + e.getClass()+ " - " + e.getMessage());
  		//return email;
  		 }
		finally {if (scf!=null)
  				scf.close();}
		return email;

	}


	public static ObservableList<Email> getEmails(ArrayList<Integer> list_id) {
		ObservableList<Email> emails= FXCollections.observableArrayList();
		for(Integer i: list_id) {
			emails.add(getEmail(i));
		}
		return emails;
	}

	public static int counter(int line, String file, char separatore) {

			int count= Integer.MIN_VALUE;
			// 1 Open file
			Scanner scf = null;
			try {
				scf = new Scanner(new File(file));
				String tmp = "";
				while (scf.hasNext()) {
					tmp= tmp+ scf.next();
				}
				// 2 place the cursor
				// 3 while char != # : count ++
				int i=0;
				while (i < tmp.length() && line > 0) {
					if(tmp.charAt(i) == separatore) {
						line --;
					}
					i++;
				}
				if(line <= 0) {
					count =i-1;
				}
			}catch (Exception e)
	  		{System.out.println("Eccezione: " + e.getClass()+ " - " + e.getMessage());

	  		 }
			finally {if (scf!=null)
	  				scf.close();}


			// 4 return count-1
			return count;

		}

}

