package life.totl.totlback.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

@Entity
public class EmailConfirmTokenEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long tokenId;
    private String confirmationToken;
    private long userId;
    private Date expiryDate;

    public EmailConfirmTokenEntity() {}

    public EmailConfirmTokenEntity(String confirmationToken, long userId, Date expiryDate) {
        this.confirmationToken = confirmationToken;
        this.userId = userId;
        this.expiryDate = expiryDate;
    }

    private Date calculateExpiryDate() {
        Calendar cal = Calendar.getInstance();
        cal.setTimeZone(TimeZone.getTimeZone("America/Chicago"));
        cal.setTime(new Timestamp(cal.getTime().getTime()));
        cal.add(Calendar.MINUTE, 20);
        return new java.sql.Date(cal.getTime().getTime());
    }

    public long getTokenId() {
        return tokenId;
    }

    public String getConfirmationToken() {
        return confirmationToken;
    }

    public void setConfirmationToken(String confirmationToken) {
        this.confirmationToken = confirmationToken;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public Date getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate() {
        this.expiryDate = calculateExpiryDate();
    }
}
