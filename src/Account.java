
public abstract class Account {
    protected int accNo; // account number
    protected int accClientId; // account client identification
    protected double accBalance; // account balance
	
    
    public Account(int accNo, int accClientId) {
		this.accNo = accNo;
		this.accClientId = accClientId;
		accBalance = 0.0;
	}
    
    

	public Account(int accNo, int accClientId, double accBalance) {
		super();
		this.accNo = accNo;
		this.accClientId = accClientId;
		this.accBalance = accBalance;
	}



	public int getAccNo() {
		return accNo;
	}



	public void setAccNo(int accNo) {
		this.accNo = accNo;
	}



	public int getAccClientId() {
		return accClientId;
	}



	public void setAccClientId(int accClientId) {
		this.accClientId = accClientId;
	}



	public double getAccBalance() {
		return accBalance;
	}


	// function to draw money from the account
    // returns a boolean value to indicate whether the process had success or not 
    public abstract Boolean DrawMoney(double moneyTotal);

    // function to print the account attributes
	public String toString() {
		return "Account No=" + accNo + ", Client Id=" + accClientId + ", Balance=" + String.format("%.2f", accBalance);
	}

	// function to deposit money into the account
    public abstract void DepositMoney(double moneyTotal);

    // function to print the account balance
    public abstract void printBalance();

    // function to transfer an amount of money from the account to a destination account 
    // returns a boolean value to indicate whether the process had success or not 
    public Boolean transferToAccount(double moneyTotal, Account desAccount ) {
        if(accBalance >= moneyTotal){ // if there is enough money to do the transfer
            accBalance -= moneyTotal; // deduct from accout
            desAccount.accBalance += moneyTotal; // add to destination
            printBalance(); // print accout balance 
            desAccount.printBalance(); // print destination balance
            return true;
        } else {
            System.out.println("There is not enough funds for this operation");
            return false;
        }
    }
}
