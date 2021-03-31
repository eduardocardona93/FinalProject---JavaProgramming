
public class Checking extends Account {
	private double chkOverdraftFee;
	// Checking class constructor 
	public Checking(int accNo, int accClientId, double chkOverdraftFee) {
		super(accNo, accClientId); // Account class constructor call
		this.chkOverdraftFee = chkOverdraftFee;
	}
	
	
	
	public Checking(int accNo, int accClientId, double balance, double chkOverdraftFee) {
		super(accNo, accClientId, balance);
		this.chkOverdraftFee = chkOverdraftFee;
	}



	// getters and setters
	public double getChkOverdraftFee() {
		return chkOverdraftFee;
	}

	public void setChkOverdraftFee(double chkOverdraftFee) {
		this.chkOverdraftFee = chkOverdraftFee;
	}

	@Override
    // function to draw money from the account
    // returns a boolean value to indicate whether the process had success or not 
	public Boolean DrawMoney(double moneyTotal) {
        if(accBalance - moneyTotal >= 0) { // if the balance is enough to draw the money
            accBalance -= moneyTotal; // deduct the money
            System.out.println("Checking account deduction successfully made");
            printBalance(); // prints the balance
            return true;
        }else if ((accBalance + chkOverdraftFee) - moneyTotal  >= 0)  { // if the balance plus the overdraft value is enough to draw the money
            double overdraft = accBalance + chkOverdraftFee - moneyTotal; // update the overdraft available value
            accBalance -= moneyTotal; // deduct the money
            System.out.println("Checking account deduction successfully made");
            System.out.println("OverDraft money left: $"  + String.format( "%.2f", overdraft) );
            printBalance(); // prints the balance
            return true;
        }else{ // not enough money
            System.out.println("There is not enough funds for this operation");
            return false;
        }
	}
	
	@Override
    // function to deposit money into the account
	public void DepositMoney(double moneyTotal) {
        accBalance += moneyTotal; // add the money to the balance
        System.out.println("Savings account deposit successfully made");
        printBalance(); // prints the balance
	}

	@Override
    // function to print the account balance
	public void printBalance() {
		System.out.println("Checking account "+ accNo + ", balance: $"+ String.format( "%.2f", accBalance));
	}


}
