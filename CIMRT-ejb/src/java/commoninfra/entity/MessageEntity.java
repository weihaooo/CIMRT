/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package commoninfra.entity;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.sql.Timestamp;

/**
 *
 * @author FABIAN
 */
@Entity
public class MessageEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long messageId;
    private Timestamp messageDate;
    private String msgContent;
    private String receiver;
    private String receiverName;
    private String sender;
    private String senderName;
    
    public MessageEntity() {
    }
    
    public MessageEntity(String msgContent, Timestamp messageDate, String receiver,String receiverName, String sender, String senderName){
        this.setMsgContent(msgContent);
        this.setMessageDate(messageDate);
        this.setReceiver(receiver);
        this.setReceiverName(receiverName);
        this.setSender(sender);
        this.setSenderName(senderName);
              
    }

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }
       
    
    
    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }
    
    public Timestamp getMessageDate() {
        return messageDate;
    }

    public void setMessageDate(Timestamp messageDate) {
        this.messageDate = messageDate;
    }

    public String getMsgContent() {
        return msgContent;
    }

    public void setMsgContent(String msgContent) {
        this.msgContent = msgContent;
    }
    
    
    public Long getMessageId() {
        return messageId;
    }

    public void setId(Long messageId) {
        this.messageId = messageId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (messageId != null ? messageId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof MessageEntity)) {
            return false;
        }
        MessageEntity other = (MessageEntity) object;
        if ((this.messageId == null && other.messageId != null) || (this.messageId != null && !this.messageId.equals(other.messageId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.MessageEntity[ messageId=" + messageId + " ]";
    }
    
}
