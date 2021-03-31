
public class Client {
    protected String cliFirstName; // client First Name
    protected String cliLastName; // client Last Name
    protected int cliId; // client Id
    protected String cliAddress; // client Address
    protected String cliPhoneNo; // client Phone No
    protected String cliPin; // client Pin
    
    
    // Client class constructor
	public Client(String cliFirstName, String cliLastName, int cliId, String cliAddress, String cliPhoneNo, String cliPin) {
		this.cliFirstName = cliFirstName;
		this.cliLastName = cliLastName;
		this.cliId = cliId;
		this.cliAddress = cliAddress;
		this.cliPhoneNo = cliPhoneNo;
		this.cliPin = cliPin;
	}
	// Client class constructor
	public Client(String cliFirstName, String cliLastName, int cliId, String cliAddress, String cliPhoneNo) {
		this.cliFirstName = cliFirstName;
		this.cliLastName = cliLastName;
		this.cliId = cliId;
		this.cliAddress = cliAddress;
		this.cliPhoneNo = cliPhoneNo;
		this.cliPin = "1234";
	}
	
	// Getters and setters
	public String getCliFirstName() {
		return cliFirstName;
	}
	public void setCliFirstName(String cliFirstName) {
		this.cliFirstName = cliFirstName;
	}
	public String getCliLastName() {
		return cliLastName;
	}
	public void setCliLastName(String cliLastName) {
		this.cliLastName = cliLastName;
	}
	public int getCliId() {
		return cliId;
	}
	public void setCliId(int cliId) {
		this.cliId = cliId;
	}
	public String getCliAddress() {
		return cliAddress;
	}
	public void setCliAddress(String cliAddress) {
		this.cliAddress = cliAddress;
	}
	public String getCliPhoneNo() {
		return cliPhoneNo;
	}
	public void setCliPhoneNo(String cliPhoneNo) {
		this.cliPhoneNo = cliPhoneNo;
	}
	public String getCliPin() {
		return cliPin;
	}
	public void setCliPin(String cliPin) {
		this.cliPin = cliPin;
	}
	// function to return the full name (first + last name)
	public String fullName() {
		return cliFirstName + " " +  cliLastName;
	}
	// function to print the client attributes  (except the pin)
	public String toString() {
		return "First Name=" + cliFirstName + ", Last Name=" + cliLastName + ", Id=" + cliId
				+ ", Address=" + cliAddress + ", Phone No=" + cliPhoneNo ;
	}
	
	
}
