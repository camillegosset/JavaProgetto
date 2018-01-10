package Model;


import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;

public class Email implements Serializable, Comparable <Email> {

	private static final long serialVersionUID = 1L;
	private String topic, sender;
	// deve diventare una Lista!!!!!
	private ArrayList<String> receivers;// da cambiare!!!!!
	private LocalDate date;
	private LocalDate deleted;
	private Integer ID;
	// Myenum status;

	public Email(String topic, String sender, LocalDate created, Integer ID) {
		super();
		this.topic = topic;
		this.sender = sender;
		this.receivers = new ArrayList<>();
		this.date = created;

		this.ID = ID;
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

	public void setSender(String sender) {
		this.sender = sender;
	}

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	public LocalDate getDeleted() {
		return deleted;
	}

	public void setDeleted(LocalDate deleted) {
		this.deleted = deleted;
	}

	public int compareTo(Email e) {
		return this.getDate().compareTo(e.getDate());
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
		String res="";
		int i=0;
		for( i=0; i< this.receivers.size() - 1; i++) {
			res= res+ this.receivers.get(i)+ " , ";
		}
		if(i>0) {
			res= res+ this.receivers.get(i);
		}
		return res;
	}

	public String toString() {
		return this.ID+"#"+this.topic+"#"+this.date+"#"+this.getStringReceivers()+"#";
	}

	/*public Object getReceivers() {
		// TODO Auto-generated method stub
		return null;
	}*/




}
