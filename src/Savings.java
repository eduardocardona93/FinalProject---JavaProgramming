
public class Savings extends Account {
    private int savFreeTransactions; // savings account number of free transactions
    private double savTransactionsCost; // savings account transactions cost
 // Savings class constructor  
	public Savings(int accNo, int accClientId, int savFreeTransactions, double savTransactionsCost) {
		super(accNo, accClientId); // Account class constructor call
		this.savFreeTransactions = savFreeTransactions;
		this.savTransactionsCost = savTransactionsCost;
	}
	
	
	
	public Savings(int accNo, int accClientId, double balance, int savFreeTransactions, double savTransactionsCost) {
	super(accNo, accClientId,balance);
	this.savFreeTransactions = savFreeTransactions;
	this.savTransactionsCost = savTransactionsCost;
}



	// Getters and setters
	public int getSavFreeTransactions() {
		return savFreeTransactions;
	}

	public void setSavFreeTransactions(int savFreeTransactions) {
		this.savFreeTransactions = savFreeTransactions;
	}

	public double getSavTransactionsCost() {
		return savTransactionsCost;
	}

	public void setSavTransactionsCost(double savTransactionsCost) {
		this.savTransactionsCost = savTransactionsCost;
	}
	

	@Override
    // function to draw money from the account
    // returns a boolean value to indicate whether the process had success or not 
	public Boolean DrawMoney(double moneyTotal) {
        double totalDeduct = moneyTotal;
        if  (savFreeTransactions > 0 ) { // if the user has free transactions left
            savFreeTransactions -= 1; // reduce the free transactions number
            System.out.println("Free transactions left: " + savFreeTransactions);
        }else{ // if the user run out of free transactions
            System.out.println("Service Cost: $" + String.format( "%.2f", savTransactionsCost)); // shows the cost
            totalDeduct += savTransactionsCost;  // add the transactions cost to the total deduction
        }
        if(accBalance - totalDeduct >= 0 ) { // if the balance is enough to draw the total deduction
            accBalance -= totalDeduct;  // deduct the money
            System.out.println("Savings account deduction successfully made");
            
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
	public void printBalance() {
		System.out.println("Savings account "+ accNo + ", balance: $"+ String.format( "%.2f", accBalance));
	}



	@Override
	// function to return a formatted savings account info to save in external txt file 
	public String txtFileFormat() {
		return "Savings," + accNo + "," + accClientId + "," + accBalance + "," + savFreeTransactions + "," + savTransactionsCost;
	}


}
