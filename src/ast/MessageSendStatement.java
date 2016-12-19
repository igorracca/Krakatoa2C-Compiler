/*
 * Nome: Igor Racca
 * RA: 511382
 */

package ast;

public class MessageSendStatement extends AssignExprLocalDecStatement { 

   public MessageSendStatement(MessageSend messageSend) {
		this.messageSend = messageSend;
	}

   public void genC( PW pw ) {
   	   pw.printIdent("");
   	   messageSend.genC(pw, false);
	   pw.println(";"); 
   }

	@Override
	public void genKra(PW pw) {
		messageSend.genKra(pw, false);
	}

    private MessageSend messageSend;
}


