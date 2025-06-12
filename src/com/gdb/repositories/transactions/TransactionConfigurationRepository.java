package com.gdb.repositories.transactions;

import java.util.HashMap;
import java.util.Map;
import com.gdb.models.accounts.AccountPrivilege;

public class TransactionConfigurationRepository
{
    static private Map<AccountPrivilege, Double> transactionLimits = new HashMap<>();
	
	private static void insert() {
		transactionLimits.put(AccountPrivilege.Premium, 100000.0);
		transactionLimits.put(AccountPrivilege.Gold, 50000.0);
		transactionLimits.put(AccountPrivilege.Silver, 25000.0);
	}
	
	public static double getTransferLimit(AccountPrivilege privilege) {
		insert();
		return transactionLimits.get(privilege);
	}
}
