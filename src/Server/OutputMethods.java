package Server;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import Model.Email;

public class OutputMethods {
	public static List<String> hlist = new ArrayList<String>();
	private static String emails = "emails.txt";
	// private Lock emails;
	public static boolean writeMessage(String message, Integer ID) {
		PrintWriter p = null;
		FileWriter f = null;
		try {
			f = new FileWriter(ID + ".txt"); // no append mode
			p = new PrintWriter(f);
			p.println(message);
			p.flush();
		} catch (IOException | RuntimeException e) {
			return false;
		} finally {
			if(f != null)
				try {
					f.close();
				} catch (IOException e) {
				}
			if (p != null) // NB: se fallisce la new il PrintWriter e' null!!
				p.close();
			
		}
		return true;
	}

	public static boolean incrementID() {
		int i = InputMethods.getID();
		i++;
		PrintWriter p = null;
		FileWriter f = null;
		try {
			f = new FileWriter("ID.txt");
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
			if(f != null)
				try {
					f.close();
				} catch (IOException e) {
				}
		}
		return true;
	}

	@SuppressWarnings("resource")
	public static boolean removeEmail(Integer id, String client) {
		if(hlist.contains(client) == false) {
		hlist.add(client); 
		}

		synchronized (hlist.get(hlist.indexOf(client))) {

			ArrayList<Integer> emails_received = new ArrayList<Integer>();
			ArrayList<Integer> emails_sent = new ArrayList<Integer>();
			ArrayList<Integer> emails_deleted = new ArrayList<Integer>();
			
			Scanner scf = null;
			Scanner s = null;
			Scanner line = null;
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

				line = new Scanner(received).useDelimiter("\\s*,\\s*");
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
				
				if(line != null)
					line.close();
				if (s != null)
					s.close();
				if (scf != null) // NB: se fallisce la new File lo scanner e' null
					scf.close();
				
			}
			PrintWriter p = null;
			FileWriter f =  null;
			try {
				f = new FileWriter(client + ".txt");
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
				if(f != null)
					try {
						f.close();
					} catch (IOException e) {
					}
				if (p != null) // NB: se fallisce la new File lo scanner e' null
					p.close();
			}
		}
		return true;
	}

	
	@SuppressWarnings("resource")
	public static boolean addEmailClientFile(Integer id, String client, int linenumebr) {
		if(hlist.contains(client) == false) {
		hlist.add(client); 
		}

		synchronized (hlist.get(hlist.indexOf(client))) {

			ArrayList<Integer> emails_received = new ArrayList<Integer>();
			ArrayList<Integer> emails_sent = new ArrayList<Integer>();
			ArrayList<Integer> emails_deleted = new ArrayList<Integer>();
			
			Scanner scf = null;
			Scanner s = null;
			Scanner line = null;
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

				line = new Scanner(received).useDelimiter("\\s*,\\s*");
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

				switch(linenumebr) {
				case 1:
					if(emails_received.contains(id) == false)
					emails_received.add(id);
					break;
				case 2:
					if(emails_sent.contains(id) == false)
					emails_sent.add(id);
					break;
				case 3:
					if(emails_deleted.contains(id) == false)
					emails_deleted.add(id);
					break;
				}

				
			} catch (IOException e) {
				System.out.println("PROBLEMA: " + e.getMessage());
				e.printStackTrace();
				return false;
			} finally {
				
				if(line != null)
					line.close();
				if (s != null)
					s.close();
				if (scf != null) // NB: se fallisce la new File lo scanner e' null
					scf.close();
				
			}
			PrintWriter p = null;
			FileWriter f =  null;
			try {
				f = new FileWriter(client + ".txt");
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
				if(f != null)
					try {
						f.close();
					} catch (IOException e) {
					}
				if (p != null) // NB: se fallisce la new File lo scanner e' null
					p.close();
			}
		}
		return true;
	}
	


	public static void addEmail(Email email, Server server) {
		synchronized (emails) {
			PrintWriter p = null;
			String out = email.toString();
			FileWriter f = null;
			try {
				f = new FileWriter(emails, true);
				p = new PrintWriter(f);
				p.println();
				p.println(out);
				p.flush();

			} catch (Exception e) {
			} finally {
				if (p != null)
					p.close();
			}
			if(f != null) {
				try {
					f.close();
				} catch (IOException e) {
				}
			}

		}

		boolean nameMistaken = false;
		ArrayList<String> mistakenList = new ArrayList<String>();

		Thread t1 = new Thread(() ->  {
			addEmailClientFile(email.getID(), email.getSender(), 2);
		});
		t1.start();
		/*
		try {
			t1.join();
		} catch(Exception e) {
			
		}
		*/
		//update delle tabelle dei receivers
		
		for (String receiver : email.getReceivers()) {
			if (server.egsistsLogin(receiver)) {
				addEmailClientFile(email.getID(), receiver, 1);
			} else {
				nameMistaken = true;
				mistakenList.add(receiver);
			}

		} // update della tabella del cliente sender
		if (nameMistaken) {
			server.informAboutMistakenName(email, mistakenList);
		}

	}
//unused
	public static void changeOpenedStatus(Integer id) {
	}
}
