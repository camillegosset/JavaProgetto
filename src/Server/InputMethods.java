package Server;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Scanner;

import Model.Email;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class InputMethods {

	@SuppressWarnings("resource")
	public static ArrayList<String> inputLogin() { // reading logins all
		Scanner scf = null;
		Scanner s = null;
		ArrayList<String> lista_login = new ArrayList<>();
		try {
			scf = new Scanner(new File("logins.txt"));
			String l = "";//
			while (scf.hasNext()) {
				l = l + scf.nextLine();
			}
			s = new Scanner(l).useDelimiter("\\s*;\\s*");
			while (s.hasNext()) {
				lista_login.add(s.next());
			}
		} catch (IOException e) {
			System.out.println("PROBLEMA: " + e.getMessage());
		} finally {
			if (scf != null) {
				scf.close();
			}
		}
		return lista_login;
	}

	public static synchronized boolean egsistsLogin(String login, Server server) {
		ArrayList<String> clientList = new ArrayList<String>();
		Scanner scf = null;
		boolean esiste = false;
		try {
			scf = new Scanner(new File("logins.txt")).useDelimiter("\\s*,\\s*");
			String l = "";
			while (scf.hasNext()) {
				clientList.add(l = scf.next());
				if (l.equals(login)) {
					esiste = true;
				}
			}
		} catch (IOException e) {
			System.out.println("PROBLEMA: " + e.getMessage());
			return false;
		} finally {
			if (scf != null) // NB: se fallisce la new File lo scanner e' null
				scf.close();
		}
		server.updateClients(clientList);
		return esiste;
	}

	// not sure if we need a logs file for every client
	public static void visualizzareLogs(String login) {
		Scanner scf = null;
		try {
			scf = new Scanner(new File(login + "/logs.txt"));
			String l = "";
			while (scf.hasNext()) {
				l = scf.nextLine();
				System.out.println(l);
			}
		} catch (IOException e) {
			System.out.println("PROBLEMA: " + e.getMessage());
			return;
		} finally {
			if (scf != null) // NB: se fallisce la new File lo scanner e' null
				scf.close();
		}
	}

	@SuppressWarnings("finally")
	public static String getMessage(Integer ID) {
		StringBuffer message = new StringBuffer("");
		Scanner scf = null;
		try {
			scf = new Scanner(new File(ID + ".txt"));
			while (scf.hasNext()) {
				message.append(scf.nextLine());
			}
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("PROBLEMA: " + e.getMessage());
			message.append("No content: We couldn't get the Email.");
		} finally {
			if (scf != null) {
				scf.close();
			}
			return message.toString();
		}

	}

	public static int getID() {
		Scanner scf = null;
		int i = Integer.MIN_VALUE;
		try {
			scf = new Scanner(new File("ID.txt"));
			i = (int) scf.nextFloat();
		} catch (IOException e) {
			System.out.println("PROBLEMA: " + e.getMessage());
		} finally {
			if (scf != null) {
				scf.close();
			}
		}
		return i;

	}

	@SuppressWarnings("resource")
	public static Email getEmail(Integer id) {
		Scanner scf = null;
		Scanner s = null;
		Email email = null;
		try {
			scf = new Scanner(new File("emails.txt"));
			while (scf.hasNextLine()) {
				String tmp = "" + scf.nextLine();
				//System.out.println(tmp);
				s = new Scanner(tmp).useDelimiter("\\s*#\\s*");
				Integer ID ;
				if(s.hasNext()) {
					ID = s.nextInt();
				} else {
					ID = 0;
				}
				
				if(ID.equals(id)==false) {
					continue;
				}
				String topic = s.next();
				LocalDate date = LocalDate.parse(s.next());
				LocalTime hour = LocalTime.parse(s.next());
				String sender = s.next();
				email = new Email(topic, sender, date,hour, ID);
				while (s.hasNext()) {
					email.aggiungereReceiver(s.next());
				}
				break;
			}
		} catch (Exception e) {
			e.printStackTrace();
			//System.out.println("Eccezione: " + e.getClass() + " - " + e.getMessage());
			// return email;
		} finally {
			if (scf != null)
				scf.close();
		}
		return email;

	}

	public static ObservableList<Email> getEmails(ArrayList<Integer> list_id) {
		ObservableList<Email> emails = FXCollections.observableArrayList();
		for (Integer i : list_id) {
			emails.add(getEmail(i));
		}
		return emails;
	}

	public static int counter(int line, String file, char separatore) {

		int count = Integer.MIN_VALUE;
		// 1 Open file
		Scanner scf = null;
		try {
			scf = new Scanner(new File(file));
			String tmp = "";
			while (scf.hasNext()) {
				tmp = tmp + scf.next();
			}
			// 2 place the cursor
			// 3 while char != # : count ++
			int i = 0;
			while (i < tmp.length() && line > 0) {
				if (tmp.charAt(i) == separatore) {
					line--;
				}
				i++;
			}
			if (line <= 0) {
				count = i - 1;
			}
		} catch (Exception e)

		{
			System.out.println("Eccezione: " + e.getClass() + " - " + e.getMessage());
			//e.printStackTrace();
		} finally {
			if (scf != null)
				scf.close();
		}

		// 4 return count-1
		return count;

	}

	public static ArrayList<Email> getEmailList(String name) {
		// ------------temporary--------

		//final Email email1 = new Email("Meeting", "john@mail.com", new ArrayList<String>(),LocalDate.of(2014, Month.MAY, 21), 2);
		

		ArrayList<Email> emailList = new ArrayList<Email>();

		//emailList.add(email1);

		
		Scanner scf = null;
		Scanner s = null;
		try {
			scf = new Scanner(new File(name + ".txt")).useDelimiter("\\s*#\\s*");
			ArrayList<String> tmp = new ArrayList<String>();
			int j=0;
			while (scf.hasNext()) {
				tmp.add(scf.next());
			}
			//System.out.println(tmp.get(0));
			s = new Scanner(tmp.get(0)).useDelimiter("\\s*,\\s*");
			ArrayList<Integer> list = new ArrayList<Integer>();
			while (s.hasNext()) {
				
				list.add(s.nextInt());
				
			}
			
			for (Integer i : list) {
				//System.out.println(i);
				emailList.add(getEmail(i));
				//System.out.println(getEmail(i)); nullPointerException
			}
		} catch (Exception e) {
			System.out.println("Eccezione: " + e.getClass() + " - " + e.getMessage());
			e.printStackTrace();
		} finally {
			if (scf != null)
				scf.close();
		}
		if(emailList.isEmpty()) {
			System.out.println("La lista e' vuota");
		}
		
		return emailList;
	}

}
