package com.revolut.japp.moneytransfer.model;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.concurrent.atomic.AtomicInteger;

public class Account {
	
	private static final AtomicInteger COUNTER = new AtomicInteger();
	
	private final int id;
	
	private String userName;
	
	private BigDecimal balance;
	
	private Currency currency;

	
	
	public Account(String userName, BigDecimal balance, Currency currency) {
		this.id = COUNTER.getAndIncrement();
		this.setUserName(userName);
		this.setBalance(balance);
		this.setCurrency(currency);
		
	}
	
	public Account() {
		this.id = COUNTER.getAndIncrement();
	}
	
	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}
	

	/**
	 * @return the userName
	 */
	public String getUserName() {
		return userName;
	}

	/**
	 * @param userName the userName to set
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}

	/**
	 * @return the balance
	 */
	public BigDecimal getBalance() {
		return balance;
	}

	/**
	 * @param balance the balance to set
	 */
	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}

	/**
	 * @return the currency
	 */
	public Currency getCurrency() {
		return currency;
	}

	/**
	 * @param currency the currency to set
	 */
	public void setCurrency(Currency currency) {
		this.currency = currency;
	}
	
	public boolean equals(Object o) {
		if(this == o) {
			return true;
		}
		if(o == null || getClass() != o.getClass()) {
			return false;
		}
		Account account = (Account)o;
		
		return id == account.id;
	}
	
	public String toString() {
		return "Account{" +
				"id=" + id + ", balance=" + balance + ", name='" + userName + '\'' +
				", currency=" + currency + '}';
		
	}
	
	public void withdraw(BigDecimal amount) {
        this.balance = balance.subtract(amount);
    }

    public void deposit(BigDecimal amount) {
        this.balance = balance.add(amount);
    }

}
