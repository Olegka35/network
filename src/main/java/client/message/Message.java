package client.message;

import java.io.Serializable;

public class Message implements Serializable {
    private MES_TYPE type;
    private Object data;

    public Message() {
    }

    public Message(MES_TYPE type, Object data) {
        this.type = type;
        this.data = data;
    }

    public MES_TYPE getType() {
        return type;
    }

    public void setType(MES_TYPE type) {
        this.type = type;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "Message{" +
                "type=" + type +
                ", data=" + data +
                '}';
    }
}
