package Model;

/** This class is where the constructors and getters are kept */
public class Contacts {

    private int contactID;
    private String contactName;
    private String email;

    //constructors
    public Contacts(int contactID, String contactName, String email) {
        this.contactID = contactID;
        this.contactName = contactName;
        this.email = email;
    }

    //getters

    public int getContactID(){
        return contactID;
    }

    public String getContactName() {
        return contactName;
    }

    public String getEmail() {
        return email;
    }


    //this overrides the toString in relation to the combo boxes
    @Override

    public String toString() {
        return getContactID() + "-" + getContactName(); //this returns only contact name
    }
}


