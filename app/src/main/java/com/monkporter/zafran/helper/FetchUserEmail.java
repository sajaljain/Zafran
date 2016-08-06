package com.monkporter.zafran.helper;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;

/**
 * Created by vabs on 31/7/16.
 */
public class FetchUserEmail {
   public static String getEmail(Context context) {
        AccountManager accountManager = AccountManager.get(context);
        Account account = getAccount(accountManager);

        if (account == null) {
            return null;
        } else {
            return account.name;
        }
    }

    private static Account getAccount(AccountManager accountManager) {
        Account[] accounts = accountManager.getAccountsByType("com.google");
        Account account;
        if (accounts.length > 0) {
            account = accounts[0];
        } else {
            account = null;
        }
        return account;
    }
}

