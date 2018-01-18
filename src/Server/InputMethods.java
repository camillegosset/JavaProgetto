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

	private static Scanner scanner;
	private static Scanner scanner2;
	private static Scanner scanner3;

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
			scanner3 = new Scanner(new File("logins.txt"));
			scf = scanner3.useDelimiter("\\s*,\\s*");
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

	@SuppressWarnings("finally")
	public static String getMessage(Integer ID) {
		StringBuffer message = new StringBuffer("");
		Scanner scf = null;
		try {
			scf = new Scanner(new File(ID + ".txt"));
			while (scf.hasNext()) {
				message.append(scf.nextLine() + "\n");
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
				// System.out.println(tmp);
				s = new Scanner(tmp).useDelimiter("\\s*#\\s*");
				Integer ID;
				if (s.hasNext()) {
					ID = s.nextInt();
				} else {
					ID = 0;
				}

				if (ID.equals(id) == false) {
					continue;
				}
				String topic = s.next();
				LocalDate date = LocalDate.parse(s.next());
				LocalTime hour = LocalTime.parse(s.next());
				String sender = s.next();
				boolean existing = s.nextBoolean();
				boolean opened = s.nextBoolean();
				email = new Email(topic, sender, date, hour, ID);
				email.setExisting(existing);
				email.setOpened(opened);
				Scanner last = new Scanner(s.next()).useDelimiter("\\s*,\\s*");
				while (last.hasNext()) {
					email.aggiungereReceiver(last.next());
				}
				break;
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Eccezione: " + e.getClass() + " - " + e.getMessage());
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

	public static ArrayList<Email> getEmailList(String name, int line) {
		ArrayList<Email> emailList = new ArrayList<Email>();

		Scanner scf = null;
		Scanner s = null;
		try {
			scanner = new Scanner(new File(name + ".txt"));
			scf = scanner.useDelimiter("\\s*#\\s*");
			ArrayList<String> tmp = new ArrayList<String>();
			while (scf.hasNext()) {
				tmp.add(scf.next());
			}
			scanner2 = new Scanner(tmp.get(line));
			s = scanner2.useDelimiter("\\s*,\\s*");
			ArrayList<Integer> list = new ArrayList<Integer>();
			while (s.hasNext()) {// int?

				try {
					list.add(s.nextInt());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			for (Integer i : list) {
				emailList.add(getEmail(i));
			}
		} catch (Exception e) {
			System.out.println("Eccezione: " + e.getClass() + " - " + e.getMessage());
			e.printStackTrace();
		} finally {
			if (scf != null)
				scf.close();
			if (scanner != null)
				scanner.close();
			if (scanner2 != null)
				scanner2.close();
			if (s != null)
				s.close();
		}

		return emailList;
	}

}
