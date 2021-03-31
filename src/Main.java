import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
	public static ArrayList<Client> clients = new ArrayList<>();
	public static ArrayList<Account> accounts = new ArrayList<>();
	
	public static void main(String[] args) throws IOException {
		Scanner sc0 = new Scanner(System.in);

		readFromFileAccounts();
		readFromFileClients();
		
//	    clients.add(new Client( "Lino", "Hernandez", 45678, "109 Finch", "6473000880" , "1234"));
//	    accounts.add(new Checking(11111, 45678, 400.0, 300.0) );
//	    accounts.add(new Savings(22222, 45678, 150.0, 3, 2.0));
//
//	    clients.add(new Client( "Dina", "Johnson", 78901, "206 Bathurst", "6474500880" , "1234"));
//	    accounts.add(new Checking(44444, 78901, 290.0, 320.0 ));
//	    accounts.add(new Savings(55555, 78901, 110.0, 5,  1.5));
//	    writeToFileClients();
//	    writeToFileAccounts();
		// Initial menu
		do {
			// Decides the type of user
			System.out.println("\nWhat are you?\n");
			System.out.println("1. Admin");
			System.out.println("2. Client\n");
			// menu case
			switch (sc0.nextInt()) {
				case 1: // Admin
					System.out.println("Type your password");
					String pass = sc0.next();
					if(pass.equals("Lambton2021")) { // requests the password to the admin
						adminMenu(); // calls the admin menu
					} else {
						System.out.println("Wrong Password");
					}
					break;
				case 2: // Client
					System.out.println("Enter cliend id:");
					int clientId = sc0.nextInt();
					Client client = getClientById(clientId);
					if(client != null)  { // if client found
						int attempts = 3; // attempts allowed
						// gives 3 attempts to the client to enter the pin
						do { // do-while loop for entering the client pin
							System.out.println("Type your pin");
							String pin = sc0.next();
							// validates the pin
							if(client.cliPin.equals(pin)) {  // successful pin typed
								ArrayList<Account> clientAccs = getClientAccounts(clientId); // gets the client accounts
								System.out.println("Select the index of the account you want to operate: \n");
								// iterates the client accounts
								for (int i = 0; i < clientAccs.size(); i++) {
									Account acc = clientAccs.get(i);
									System.out.println((i+1) +  ". " + acc.getAccNo() + " (" + acc.getClass().getName() + ")"); // print them with an index
								}
								int accInput = sc0.nextInt();
								// validates the index account
								if(accInput > 0 && (accInput) <= clientAccs.size() ){// as the indexes starts in 1 not 0 then array length is a valid value
									Account account = clientAccs.get(accInput - 1); // get the account object
									clientMenu(client, account); // call the client menu
								}else{
									System.out.println("Wrong input");
								}
								break; // do not repeat again the pin request
							}else{ // unsuccessful attempt
								attempts -= 1; // reduce attempts allowed
								System.out.println("Wrong pin, you have " + attempts + " attempts");
							}
						} while (attempts > 0);
						if( attempts == 0){ // not more attempts allowed
							System.out.println("Sorry, your attempts run out");
						}
					} else { // if client not found
						System.out.println("Client id not found");
					}
					break;
				default: // Wrong option
					System.out.println("Wrong Option");
					break;

			}
			
			System.out.println("\n\nDo you want to do another process?y/n");
		}while( sc0.next().equalsIgnoreCase("y"));
		sc0.close();
	}


	/************************************************** ACCOUNT FUNCTIONS**********************************************/
	//Searches for an account by its 'no' and returns the Account object
	public static Account getAccountByNo(int no){
		// iterates all the accounts by object
		for (Account account : accounts) {
			if (account.accNo == no) { // when matching the account number
				return account; // return the Account object
			}
		}

		return null; // if not found return null
	}

	//Searches for an account by its 'no' and returns the 'index' in the accounts array
	public static int getAccountIndex(int no){
		// iterates all the accounts by index
		for (int i = 0; i < accounts.size(); i++) {
			if (accounts.get(i).accNo == no) { // when matching the account number
				return i; // return the array index
			}
		}

		return -1; // if not found return -1
	}

	// creates as many accounts (checking/savings) and appends them into the accounts array
	public static void createAccount(int clientId) {
		Scanner sc1 = new Scanner(System.in);
		if (getClientById(clientId) != null){ // validates the existence of the client
			do {
				System.out.println("Enter account no:");
				int no = sc1.nextInt();
				// validates the existence of the account number
				if( getAccountIndex(no) < 0){ // if not found then create account
					System.out.println("\nEnter account type:\n");
					System.out.println("1. Checking");
					System.out.println("2. Savings\n");

					int type = sc1.nextInt();
					if (type == 1){ // if checking ask for overdraft value
						System.out.println("Enter overdraft fee (default: $300.00) :");
						double overdraftFee = sc1.nextDouble();
						// append Account to the array
						accounts.add(new Checking(no, clientId, overdraftFee));
						System.out.println("Checking account created succesfully");
					}else if (type == 2){ // if savings ask for free transactions limit and transactions cost value
						System.out.println("Enter free transactions limit (default: 5) :");
						int freeTransactions = sc1.nextInt();
						System.out.println("Enter transactions cost (default: $5.00) :");
						double transactionsCost = sc1.nextDouble();
						// append Account to the array
						accounts.add(new Savings(no, clientId, freeTransactions, transactionsCost));
						System.out.println("Savings account created succesfully");
					}else{ // wrong option selected
						System.out.println("Wrong input");
					}
				}else{ // if account already exists
					System.out.println("The account with the number '" + no + "' already exists ");
				}
				// keep adding accounts message
				System.out.println("\n\nDo you want to create another account for this client?y/n");
			} while (sc1.next().equalsIgnoreCase("y"));
			// save file accounts
			try {
				writeToFileAccounts();
			} catch (IOException e) {
				e.printStackTrace();
			} 
		}else{ // if client does not exists
			System.out.println("Client not found");
		}
	}
	
	// edits an account (checking/savings) by its number (no)
	public static void editAccount(int no){
		Scanner sc2 = new Scanner(System.in);
		Account editAccount = getAccountByNo(no);
		if (editAccount != null ) { // if  account found then edit account
			// validate the type of account
			if (editAccount instanceof Savings){ // if savings ask for free transactions limit and transactions cost value
				Savings savAccount = ((Savings) editAccount); // casts the Account object as Savings
				System.out.println("Enter free transactions limit (current value: " + savAccount.getSavFreeTransactions() + ") :");
				savAccount.setSavFreeTransactions( sc2.nextInt());
				System.out.println("Enter transactions cost (current value: $" + String.format("%.2f", savAccount.getSavTransactionsCost() ) +":");
				savAccount.setSavTransactionsCost(sc2.nextDouble());
				System.out.println("Account edited Succesfully\n");
				
			}else if (editAccount instanceof Checking) { // if checking ask for overdraft value
				Checking chkAccount = ((Checking) editAccount); // casts the Account object as Checking
				System.out.println("Enter overdraft fee (current value: $" + String.format("%.2f", chkAccount.getChkOverdraftFee() ) + ":");
				chkAccount.setChkOverdraftFee(sc2.nextDouble()); 
				System.out.println("Account edited Succesfully\n");
				
			}
			// save file accounts
			try {
				writeToFileAccounts();
			} catch (IOException e) {
				e.printStackTrace();
			} 
		}else { // if  account found then print message
			System.out.println("Account number not found\n");
		}
	}
	// deletes an account (checking/savings) by its number (no)
	// in case there is at least 1 account left for the same client, asks for making a deposit to one of the accounts left
	public static void deleteAccount(int no) {
		Scanner sc3 = new Scanner(System.in);
		Account delAccount = getAccountByNo(no);
		if (delAccount != null)   { // if account found then validate decision and get the object
			System.out.println("Do you really want to delete the account no." + no + "? y/n");
			if (sc3.next().equalsIgnoreCase("y")) { // reads the validation answer
				// gets the account balance
				double balance = delAccount.getAccBalance();
				// gets the index of the account to be deleted in the accounts array
				int index = getAccountIndex(no);
				// deletes the account from the accounts array
				accounts.remove(index);
				System.out.println("Account Deleted succesfully!");

				// gets all the client accounts left
				ArrayList<Account> clientAccounts = getClientAccounts(delAccount.accClientId);
				// validates if there is at least 1 account left for this client
				// also, validates if the deleted has money left
				int cliAccsLen = clientAccounts.size();
				if(cliAccsLen > 0 && balance > 0) {
					
					System.out.println("\nThe Client has " + cliAccsLen + "account(s) left");
					System.out.println("Do you want to deposit this account total balance ( $ " + String.format( "%.2f", balance)  + ") to another account? y/n");
					// validates if wants to make a deposit to one of the remaining accounts
					if(sc3.next().equalsIgnoreCase("y")){
						System.out.println("Select the Account");
						// iterates all client's account with an index to select the one who gets the deposit
						for (int i = 0; i < cliAccsLen; i++) {
							System.out.println( (i + 1 ) + ". " + clientAccounts.get(i).accNo);
						}
						int selectedAccount = sc3.nextInt();
						clientAccounts.get(selectedAccount-1).DepositMoney(balance); // deposits the money
						System.out.println("Deposit succesfully done!");
					}
				}
				// save file accounts
				try {
					writeToFileAccounts();
				} catch (IOException e) {
					e.printStackTrace();
				} 
				
			} else {
				System.out.println("Account not Deleted");
			}
		}else { // if  account found then print message
			System.out.println("Account number not found");
		}
	}
	/************************************************** CLIENT FUNCTIONS**********************************************/
	//searchs for a client by its 'id' and returns the Client object
	public static Client getClientById(int id) {
		// iterates all the clients by object
		for (Client client : clients) {
			if( client.getCliId() == id) { // when matching the client id
				return client; // return the Client object
			}
		}
		return null; // if not found return null
	}
	//searchs for a client by its 'id' and returns the 'index' in the clients array
	public static int getClientIndex(int id)  {
		// iterates all the clients by index
		for (int i = 0; i < clients.size(); i++) {
			// when matching the client id
			if (clients.get(i).getCliId() == id) {
				// return the array index
				return i;
			}
		}
		return -1; // if not found return -1
	}
	//gets the all the accounts belonging to a client
	public static ArrayList<Account> getClientAccounts(int cliId)  {
		ArrayList<Account> cliAccs = new ArrayList<>();// empty result Accounts array list
		for (Account account : accounts) { // iterates all the accounts
			if (account.getAccClientId() == cliId) { // if the account client id match with the 'cliId'
				cliAccs.add(account); // append it to the result array
			}
		}
		return cliAccs; // return the result array
	}
	// creates as many clients as the user wants
	public static void createClient(){
		Scanner sc4 = new Scanner(System.in);
		do{
			System.out.println("\nEnter client id:");
			int id = sc4.nextInt();
			// validates if there is a client with the same 'id'
			if(getClientIndex(id) < 0){ // not duplicate client found, create the client
				System.out.println("Enter client first name:");
				String firstName = sc4.next();
				System.out.println("Enter client last name:");
				String lastName = sc4.next();
				sc4.nextLine();
				System.out.println("Enter client address:");
				String address = sc4.nextLine();
				System.out.println("Enter client phone no:");
				String phoneNo = sc4.nextLine();
				clients.add( new Client( firstName, lastName, id, address, phoneNo )); // append client to array
				System.out.println("Client created succesfully!!\n");

				System.out.println("Do you want to create a account for this client? y/n");
				if(sc4.next().equalsIgnoreCase("y")){ // if the user wants to create the accounts
					createAccount(id); // call the account creation method
				}
			}else{// duplicate client found
				System.out.println("There is already a client with this id number\n");
			}
			// keep adding clients message
			System.out.println("Do you want to create another Client? y/n");
		}while( sc4.next().equalsIgnoreCase("y"));
		// save all clients
		try {
			writeToFileClients();
		} catch (IOException e) {
			e.printStackTrace();
		} 
	}
	// edits a client's info
	public static void editClient(int id){
		Scanner sc5 = new Scanner(System.in);
		int clientIndex = getClientIndex(id); // gets the client index
		// validates if there is a client with the same 'id'
		if (clientIndex > -1) { // client found, edit information
			Client client = clients.get(clientIndex); // get the client object from the clients array
			System.out.println("Enter client first name:");
			client.cliFirstName = sc5.next();
			System.out.println("Enter client last name:");
			client.cliLastName = sc5.next();
			sc5.nextLine();
			System.out.println("Enter client address:");
			client.cliAddress = sc5.nextLine();
			System.out.println("Enter client phone no:");
			client.cliPhoneNo = sc5.nextLine();
			System.out.println("Client info succesfully edited\n");
			// save file clients
			try {
				writeToFileClients();
			} catch (IOException e) {
				e.printStackTrace();
			} 
		}else{ // client does not exists
			System.out.println("Client id '" + id + "' not found\n");
		}
		
		
	}
	// changes a client's pin
	public static void changeClientPin(int id){
		Scanner sc6 = new Scanner(System.in);
		int clientIndex = getClientIndex(id);// gets the client index
		// validates if there is a client with the same 'id'
		if (clientIndex > -1) {// client found, edit information
			Client client = clients.get(clientIndex); // get the client object from the clients array
			System.out.println("Enter 4 number pin:");
			String pin1 = sc6.next();
			System.out.println("Re-Enter 4 number pin:");
			String pin2 = sc6.next();
			if(pin1.equalsIgnoreCase(pin2)){ // if pins match then edit the value in the object
				client.setCliPin(pin1);
				System.out.println("Pin changed successfully\n");
				// save file clients
				try {
					writeToFileClients();
				} catch (IOException e) {
					e.printStackTrace();
				} 
			}else{
				System.out.println("The entered pins does not match\n");
			}
		}else{ // client does not exists
			System.out.println("Client id '" + id + "' not found\n");
		}
	}
	//deletes a client and all the accounts belonging to the client
	public static void deleteClient(int id) { // if client found then validate decision and get the object
		Scanner sc7 = new Scanner(System.in);
		Client delClient = getClientById(id);
		if(delClient != null){
			ArrayList<Account> clientAccounts = getClientAccounts(id); // get client's accounts
			System.out.println("Do you really want to delete the client '" + delClient.fullName() + "' and the " + clientAccounts.size() + " account(s) belonging to this client? y/n");
			if( sc7.next().equalsIgnoreCase("y") ){
				// client's accounts iteration
				for (Account acc : clientAccounts) {
					int accIndex = getAccountIndex(acc.getAccNo()); // gets the client's account index in the clients array
					accounts.remove(accIndex); // delete client's account from clients array
				}
				int cliIndex = getClientIndex(id); // gets the client index in the clients array
				clients.remove(cliIndex); // delete client from clients array
				System.out.println("Client and accounts succesfully deleted\n");
				// save all clients
				try {
					writeToFileClients();
					writeToFileAccounts();
				} catch (IOException e) {
					e.printStackTrace();
				} 
			}
		}else{ // client does not exists
			System.out.println("Client id '" + id + "' not found\n");
		}
		
	}

	/************************************************** MENUS FUNCTIONS **********************************************/
	// admin's menu for clients management
	public static void adminClientsManagementMenu(){
		Scanner sc8 = new Scanner(System.in);
		int clientId ;
		int choice = -1;
		do { // infinite loop to do as many operations as the user wants
			System.out.println("\nSelect an option for clients management\n");
			System.out.println("1. View all clients");
			System.out.println("2. Create clients");
			System.out.println("3. Edit an existing client");
			System.out.println("4. Change client's pin");
			System.out.println("5. Delete a client\n");
			System.out.println("0. Return to main menu\n");

			// menu case
			choice = sc8.nextInt();
			switch (choice) {
				case 1: // View all clients
					System.out.println("List of clients\n");
					for (Client client : clients) { // iterates all the clients array
						System.out.println(client.toString()); // print the client detail
					}
					break;
				case 2: // Create clients
					createClient();
					break;
				case 3: // Edit an existing client
					System.out.println("Enter cliend id:");
					clientId = sc8.nextInt();
					editClient(clientId);
					break;
				case 4: // Change client's pin
					System.out.println("Enter cliend id:");
					clientId = sc8.nextInt();
					changeClientPin(clientId);
				case 5: // Delete a client
					System.out.println("Enter cliend id:");
					clientId = sc8.nextInt();
					deleteClient(clientId);
					break;
				case 0: 
					System.out.println("Returning to main menu");
					break;
				default: // wrong choice
					System.out.println("Wrong choice");
					break;
			}
		}while(choice != 0);

	}
	// admin's menu for accounts management
	public static void adminAccountsManagementMenu(){
		Scanner sc9 = new Scanner(System.in);
		int choice = -1;
		int accNo;
		do { // loop to do as many operations as the user wants
			System.out.println("\nSelect an option for accounts management\n");
			System.out.println("1. View all accounts");
			System.out.println("2. Create new accounts");
			System.out.println("3. Edit an existing account");
			System.out.println("4. Delete an account\n");
			System.out.println("0. Return to main menu\n");
			// menu case
			choice = sc9.nextInt();
			switch (choice) {
				case 1: //  View all accounts
					System.out.println("List of accounts\n");
					for (Account acc : accounts) { // iterates all the clients array
						System.out.println(acc.toString()); // print the client detail
					}
					break;
				case 2:// Create new accounts
					System.out.println("\nAdd new accounts\n");
					System.out.println("Enter cliend id:");
					int clientId = sc9.nextInt();
					createAccount(clientId);
					break;
				case 3: // Edit an existing account
					System.out.println("Enter account no:");
					accNo = sc9.nextInt();
					editAccount(accNo);
					break;
				case 4: // Delete an account
					System.out.println("Enter account no:");
					accNo = sc9.nextInt();
					deleteAccount(accNo);
					break;
				case 0: 
					System.out.println("Returning to main menu");
					break;
				default: // wrong choice
					System.out.println("Wrong choice");
					break;
			}
		}while(choice != 0);

	}
	// admin's main menu
	public static void adminMenu (){
		Scanner sc10 = new Scanner(System.in);
		int choice = -1;
		do { // infinite loop to do as many operations as the user wants

			System.out.println("\nWhat do you want to do?\n");
			System.out.println("1. Manage Clients");
			System.out.println("2. Manage Accounts\n");
			System.out.println("0. Log out\n");

			// menu case
			choice = sc10.nextInt();
			switch (choice) {
				case 1: // Manage Clients
					adminClientsManagementMenu();
					break;
				case 2: //  Manage Accounts
					adminAccountsManagementMenu();
					break;
				case 0: 
					System.out.println("Logging out..");
					break;
				default: // wrong choice
					System.out.println("Wrong choice");
					break;
			}
		}while(choice != 0);
	}

	public static void clientMenu (Client clientObj, Account accountObj){
		Scanner sc11 = new Scanner(System.in);
		int choice = -1;
		double amountInput;
		do {
			System.out.println("\nWhat do you want to do?\n");
			System.out.println("1. Display Your current balance");
			System.out.println("2. Deposit money");
			System.out.println("3. Draw money");
			System.out.println("4. Transfer money to other accounts within the bank");
			System.out.println("5. Pay utility bills");
			System.out.println("6. Edit your account Info");
			System.out.println("7. Change your pin\n");
			System.out.println("0. Log out\n");
			// menu case
			choice = sc11.nextInt();
			switch (choice) {
				case 1: // Display Your current balance
					accountObj.printBalance();
					break;
				case 2: // Deposit money
					System.out.println("Enter the amount you want to deposit");
					amountInput = sc11.nextDouble();
					accountObj.DepositMoney(amountInput); // deposits the money
					// save file accounts
					try {
						writeToFileAccounts();
					} catch (IOException e) {
						e.printStackTrace();
					} 
					break;
				case 3: // Draw money
					System.out.println("Enter the amount you want to draw");
					amountInput = sc11.nextDouble();
					if(accountObj.DrawMoney(amountInput)){ // if the draw was successful
						// save file accounts
						try {
							writeToFileAccounts();
						} catch (IOException e) {
							e.printStackTrace();
						} 
					}
					break;
				case 4: // Transfer money to other accounts within the bank
					ArrayList<Account> clientAccs = getClientAccounts(clientObj.getCliId()); // gets the client accounts
					System.out.println("\nSelect the index of the destination account:");
					for (int i = 0; i < clientAccs.size(); i++) { // iterates the client accounts
						// print them with an index
						Account acc =  clientAccs.get(i);
						System.out.println( (i+1) +". " + acc.getAccNo() + " (" + acc.getClass().getName()  + ")"); 
					}
					int destIndex = sc11.nextInt();
					// validates the index account
					if(destIndex > 0 && (destIndex-1) <= clientAccs.size()) {
						Account accountDestination = clientAccs.get(destIndex - 1); // get the account object
						System.out.println("Enter the amount you want to transfer");
						amountInput = sc11.nextDouble();
						if(accountObj.transferToAccount(amountInput, accountDestination)){ // if the transfer was successful
							// save file accounts
							try {
								writeToFileAccounts();
							} catch (IOException e) {
								e.printStackTrace();
							} 
						}
					}else{
						System.out.println("Wrong input");
					}
					break;

				case 5: // Pay utility bills
					// require utility name
					System.out.println("\nEnter the type of the bill (Ex. Wifi, Hydro, etc)");
					String billType = sc11.next();
					// require utility amount
					System.out.println("Enter the amount of your bill");
					amountInput = sc11.nextDouble();
					if (accountObj.DrawMoney(amountInput)) { // if the pay process was successful
						System.out.println("Your bill '" + billType + "' has been paid");
						// save file accounts
						try {
							writeToFileAccounts();
						} catch (IOException e) {
							e.printStackTrace();
						} 
					}else{
						System.out.println("Sorry, it was not possible to pay your '" + billType + "' bill");
					}
					break;
				case 6: // Edit your account Info
					editClient(clientObj.getCliId());
					break;
				case 7: // Change your pin
					changeClientPin(clientObj.getCliId());
					break;
				case 0: 
					System.out.println("Logging out");
					break;
				default: // wrong choice
					System.out.println("Wrong choice");
					break;
			}
		}while(choice != 0);
	}
	/************************************************** FILES FUNCTIONS **********************************************/
	
	public static void readFromFileAccounts() throws IOException
	{
		FileInputStream empFile=new FileInputStream("accounts.txt");
		BufferedReader br=new BufferedReader(new InputStreamReader(empFile));
		
		Account acc;
		String line;
		while((line=br.readLine())!=null) {
			String fields[]=line.split(",");
			if(fields[0].equals("Savings")) {
				acc=new Savings(Integer.parseInt(fields[1]), Integer.parseInt(fields[2]), Double.parseDouble(fields[3]), Integer.parseInt(fields[4]), Double.parseDouble(fields[5]));
				accounts.add(acc);
			}
			else if(fields[0].equals("Checking")) {
				acc=new Checking(Integer.parseInt(fields[1]), Integer.parseInt(fields[2]), Double.parseDouble(fields[3]), Double.parseDouble(fields[4]));
				accounts.add(acc);
			}			
		}
		br.close();
	}
	
	public static void readFromFileClients() throws IOException
	{
		FileInputStream empFile=new FileInputStream("clients.txt");
		BufferedReader br=new BufferedReader(new InputStreamReader(empFile));
		
		Client cli;
		String line;
		while((line=br.readLine())!=null) {
			String fields[]=line.split(",");
			cli=new Client(fields[0], fields[1], Integer.parseInt(fields[2]), fields[3], fields[4], fields[5]);
			clients.add(cli);
		}
		br.close();
	}
	
	public static void writeToFileAccounts() throws IOException {
		//create a new file named .txt, if the file exists will be overwritten
		FileWriter emp = new FileWriter("accounts.txt");
		PrintWriter pw=new PrintWriter(emp);
		
		for (Account account : accounts) {
			pw.println(account.txtFileFormat());
		}

		pw.close();
	}
	
	public static void writeToFileClients() throws IOException {
		//create a new file named .txt, if the file exists will be overwritten
		FileWriter emp = new FileWriter("clients.txt");
		PrintWriter pw=new PrintWriter(emp);
		
		for (Client client : clients) {
			pw.println(client.txtFileFormat());
		}
		
		pw.close();
	}
}	
