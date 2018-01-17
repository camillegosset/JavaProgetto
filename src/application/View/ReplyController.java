package application.View;

import java.rmi.RemoteException;
import java.util.ArrayList;

import Model.Email;

public class ReplyController extends SendNewMessageController {


	private Email email;
	private int type;
	
	public Email getEmail() {
		return email;
	}

	public void setEmail(Email email) {
		this.email = email;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getReceiversReplyAll() {
		ArrayList<String> listReceivers= email.getReceivers();
		String stringReceivers= "";
		for(String r: listReceivers) {
			if(!r.equals(email.getSender()) && !r.equals(account.getClient().getName())) {
				stringReceivers= stringReceivers  + "; " + r + "@mail.com";
			}
		}
		return stringReceivers;
	}
	public void initializeR() throws RemoteException {
		type = account.getType();
		switch(type) {
		case 1:  //reply
			
			topic.setText("Re: " + email.getTopic());
			receivers.setText(email.getSender() + "@mail.com");
			message.setText("\n \n \n \n On " + email.getDate() + " at " + email.getTime() + ", " + email.getSender() + " wrote: \n" + account.getMessage(email.getID()));
			break;
		
	case 2:  //replyAll
		
		topic.setText("Re: " + email.getTopic());
		receivers.setText(email.getSender() + "@mail.com" + getReceiversReplyAll());
		message.setText("\n \n \n \n On " + email.getDate() + " at " + email.getTime() + ", " + email.getSender() + " wrote: \n" + account.getMessage(email.getID()));
		break;
	case 3:  //forward
		
		topic.setText("Fwd: " + email.getTopic());
		message.setText("\n \n \n \n On " + email.getDate() + " at " + email.getTime() + ", " + email.getSender() + " wrote: \n" + account.getMessage(email.getID()));
		break;
	}
	
}

}
