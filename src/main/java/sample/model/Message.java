package sample.model;

import java.util.Date;

/*
 * Message received from client.
 *
 * @Author Jay Sridhar
 */
public class Message
{
    private String from;
    private String to;
    private String text;
    private Date time = new Date();

    public Message() {}

    public Message(String from,String text)
    {
	this.from = from;
	this.text = text;
    }

    public Message(String from, String to, String text) {
        this.from = from;
        this.to = to;
        this.text = text;
    }

    public String getFrom()
    {
        return from;
    }

    public void setFrom(String from)
    {
        this.from = from;
    }

    public String getText()
    {
        return text;
    }

    public void setText(String text)
    {
        this.text = text;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "Message{" +
                "from='" + from + '\'' +
                ", to='" + to + '\'' +
                ", text='" + text + '\'' +
                '}';
    }
}
