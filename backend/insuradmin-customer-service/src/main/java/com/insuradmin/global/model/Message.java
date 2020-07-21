package com.insuradmin.global.model;

public class Message {

    private MessageType messageType;

    private Object content;

    public Message() {
    }

    public Message(String content) {
        this(content, MessageType.DEFAULT);
    }
    
    public Message(Object content, MessageType messageType) {
        this.content = content;
        this.messageType = messageType;
    }

    public Object getContent() {
        return content;
    }

	public MessageType getMessageType() {
		return messageType;
	}

	@Override
	public String toString() {
		return "Message [messageType=" + messageType + ", content=" + content + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((content == null) ? 0 : content.hashCode());
		result = prime * result + ((messageType == null) ? 0 : messageType.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Message other = (Message) obj;
		if (content == null) {
			if (other.content != null)
				return false;
		} else if (!content.equals(other.content))
			return false;
		if (messageType != other.messageType)
			return false;
		return true;
	}
    
    
    
    

}
