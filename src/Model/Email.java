package Model;


import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Scanner;

public class Email implements Serializable, Comparable <Email> {

	private static final long serialVersionUID = 1L;
	private String topic, sender;
	private ArrayList<String> receivers;
	private LocalDate date;
	private LocalTime time;
	private LocalDate deleted;
	private Integer ID;
	private boolean opened; //0- new 1- read 2- deleted
	private boolean existing;
	
	public Email(String topic, String sender, ArrayList<String> receivers, LocalDate created, LocalTime time) {
		super();
		this.topic = topic;
		this.sender = sender;
		this.receivers = receivers;
		this.date = created;
		this.time = time;
		this.receivers = receivers;
		this.ID = 0;
	}

	public Email(String topic, String sender, ArrayList<String> receivers, LocalDate created, LocalTime time, Integer ID) {
		super();
		this.topic = topic;
		this.sender = sender;
		this.receivers = receivers;
		this.date = created;
		this.time = time;
		this.ID = ID;
	}

	public Email(String topic, String sender,LocalDate created, LocalTime time, Integer ID) {
		super();
		this.topic = topic;
		this.sender = sender;
		this.date = created;
		this.time = time;
		this.ID = ID;
		this.receivers = new ArrayList<>();
	}

	public String getTopic() {
		return topic;
	}

	public void setTopic(String topic) {
		this.topic = topic;
	}

	public String getSender() {
		return sender;
	}

	public boolean isExisting() {
		return existing;
	}
	public boolean isOpened() {
		return opened;
	}
	
	public void setOpened(boolean opened) {
		this.opened = opened;
	}

	public void setExisting(boolean existing) {
		this.existing = existing;
	}

	void setSender(String sender) {
		this.sender = sender;
	}

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	public LocalDate getDeleted() {//
		return deleted;
	}

	public void setDeleted(LocalDate deleted) {
		this.deleted = deleted;
	}

	public LocalTime getTime() {
		return  time.minusNanos(time.getNano());
	}

	
	public void setTime(LocalTime time) {
		this.time = time;
	}

	public int compareTo(Email e) {
		
		if (getDate().compareTo(e.getDate())== 0) {
			return -this.getTime().compareTo(e.getTime());
		} else {
		return -this.getDate().compareTo(e.getDate());
		}
	}

	public Integer getID() {
		return ID;
	}



	public void setID(Integer iD) {
		ID = iD;
	}


	public ArrayList<String> getReceivers() {

		return receivers;
	}

	public void aggiungereReceiver(String r){
	    receivers.add(r);

	}

	public String getStringReceivers() {
		StringBuffer res= new StringBuffer("");
		int i;
		for( i=0; i< this.receivers.size() - 1; i++) {
			res.append(this.receivers.get(i)+ " , ");
		}
		
			if(this.receivers.size() > 0) {
				res.append(this.receivers.get(i));
			}
		return res.toString();
	}



	public String toString() {
		return this.ID+"#"+this.topic+"#"+this.date+"#"+this.time+"#"+this.sender+"#"+this.existing+"#"+this.opened+"#"+this.getStringReceivers()+"#";
	}
	public static ArrayList<String> parseReceivers(String text){
		Scanner scf= null;
		ArrayList<String> list_receivers= new ArrayList<>();
		try {
			scf = new Scanner(text);
			scf.useDelimiter("\\s*;\\s*");
			while (scf.hasNext()) {
				list_receivers.add(scf.next());
			}
		} catch (Exception e) {
			System.out.println("PROBLEMA: " + e.getMessage());
		} finally {
			if (scf != null) {
				scf.close();
			}
		}
		return list_receivers;
	}

	



}
