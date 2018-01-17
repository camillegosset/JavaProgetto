package Server;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

import Model.Email;

public class OutputMethods {
	public static HashMap<String, String> hmap = new HashMap<String, String>();

	// private Lock emails;
	public static boolean writeMessage(String message, Integer ID) {
		PrintWriter p = null;
		try {
			FileWriter f = new FileWriter(ID + ".txt"); // no append mode
			p = new PrintWriter(f);
			p.println(message);
			p.flush();
		} catch (IOException | RuntimeException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
			return false;
		} finally {
			if (p != null) // NB: se fallisce la new il PrintWriter e' null!!
				p.close();
		}
		return true;
	}

	public static boolean incrementID() {
		int i = InputMethods.getID();
		i++;
		PrintWriter p = null;
		try {
			FileWriter f = new FileWriter("ID.txt");
			p = new PrintWriter(f);
			p.println(i);
			p.flush();
		} catch (IOException | RuntimeException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
			return false;

		} finally {
			if (p != null) // NB: se fallisce la new il PrintWriter e' null!!
				p.close();
		}
		return true;
	}

	@SuppressWarnings("resource")
	public static boolean removeEmail(Integer id, String client) {
		hmap.put(client.substring(0, 1), client.substring(0, 1)); // la prima lettera

		synchronized (hmap.get(client.substring(0, 1))) {

			ArrayList<Integer> emails_received = new ArrayList<Integer>();
			ArrayList<Integer> emails_sent = new ArrayList<Integer>();
			ArrayList<Integer> emails_deleted = new ArrayList<Integer>();
			Scanner scf = null;
			Scanner s = null;
			try {
				scf = new Scanner(new File(client + ".txt"));
				String l = "";
				while (scf.hasNext()) {
					l = l + scf.nextLine();
				}
				s = new Scanner(l).useDelimiter("\\s*#\\s*");
				String received = s.next();
				String sent = s.next();
				String deleted = s.next();

				Scanner line = new Scanner(received).useDelimiter("\\s*,\\s*");
				while (line.hasNext()) {
					emails_received.add(line.nextInt());
				}

				line = null;
				line = new Scanner(sent).useDelimiter("\\s*,\\s*");
				while (line.hasNext()) {
					int i = line.nextInt();
					// System.out.println(i);
					emails_sent.add(i);
				}

				line = null;
				line = new Scanner(deleted).useDelimiter("\\s*,\\s*");
				while (line.hasNext()) {
					emails_deleted.add(line.nextInt());
				}

				boolean b1 = emails_received.remove(id);
				boolean b2 = emails_sent.remove(id);

				if (b1 || b2) {
					emails_deleted.add(id);
				}
			} catch (IOException e) {
				System.out.println("PROBLEMA: " + e.getMessage());
				e.printStackTrace();
				return false;
			} finally {
				if (scf != null) // NB: se fallisce la new File lo scanner e' null
					scf.close();
				if (s != null)
					s.close();
			}
			PrintWriter p = null;// Penso di aver trovato l'errore.
			try {
				FileWriter f = new FileWriter(client + ".txt");
				p = new PrintWriter(f);
				String l = ",";
				for (int i : emails_received) {
					l = l + i + ", ";
				}
				l = l + "#";
				p.println(l);
				l = ",";
				for (int i : emails_sent) {
					l = l + i + ", ";
				}
				l = l + "#";
				p.println(l);
				l = ",";
				for (int i : emails_deleted) {
					l = l + i + ", ";
				}
				l = l + "#";
				p.println(l);
				p.flush();
			} catch (IOException e) {
				System.out.println("PROBLEMA: " + e.getMessage());
				e.printStackTrace();
				return false;
			} finally {
				if (p != null) // NB: se fallisce la new File lo scanner e' null
					p.close();
			}
		}
		return true;
	}

	public static void addEmailClientFile(Email email, String client, int line) {
		hmap.put(client.substring(0, 1), client.substring(0, 1)); // la prima lettera

		synchronized (hmap.get(client.substring(0, 1))) {
			int count = InputMethods.counter(line, client + ".txt", '#');
			// System.out.println(count);
			// PrintWriter p= null;
			Scanner scanner = null;
			FileWriter writer = null;
			String out = email.getID() + ",";
			try {

				File file = new File(client + ".txt");
				scanner = new Scanner(file);

				String str = "";
				while (scanner.hasNext()) {
					str = str + scanner.nextLine();
				}
				String newStr = str.substring(0, count) + out + str.substring(count);
				writer = new FileWriter(file);
				writer.write(newStr);
			} catch (Exception e) {
				System.out.println("Eccezione: " + e.getClass() + " - " + e.getMessage());
				e.printStackTrace();
			} finally {//
				if (scanner != null) {
					scanner.close();
				}
				
				 if(writer != null) { try {
					writer.close();
				} catch (IOException e) {
					e.printStackTrace();
				}}
			}

		}

	}

	private static String emails = "emails.txt";

	public static void addEmail(Email email, Server server) throws RemoteException {
		synchronized (emails) {
			// udpate del FileSystem emails.txt
			PrintWriter p = null;
			String out = email.toString();
			try {
				FileWriter f = new FileWriter(emails, true);
				p = new PrintWriter(f);
				p.println();
				p.println(out);
				p.flush();

			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("Eccezione: " + e.getClass() + " - " + e.getMessage());
			} finally {
				if (p != null)
					p.close();
			}

		}

		boolean nameMistaken = false;
		ArrayList<String> mistakenList = new ArrayList<String>();

		
		Thread t1 = new Thread(() ->  {
			addEmailClientFile(email, email.getSender(), 2);
		});
		//if (email.getReceivers().contains(email.getSender()) == false) {// se hai sbagliato non ricevi il tuo messaggio
			
		//}
		t1.start();
		try {
			t1.join();
		} catch(Exception e) {
			
		}
		// update delle tabelle dei receivers
		
		for (String receiver : email.getReceivers()) {
			if (server.egsistsLogin(receiver)) {
				addEmailClientFile(email, receiver, 1);
			} else {
				nameMistaken = true;
				mistakenList.add(receiver);
			}

		} // update della tabella del cliente sender
		if (nameMistaken) {
			server.informAboutMistakenName(email, mistakenList);
		}

	}

	public static void changeOpenedStatus(Integer id) {
	}
}
