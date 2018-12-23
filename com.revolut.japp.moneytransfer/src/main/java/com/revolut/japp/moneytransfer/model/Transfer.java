package com.revolut.japp.moneytransfer.model;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.concurrent.atomic.AtomicInteger;

public class Transfer {
	
	private static final AtomicInteger COUNTER	= new AtomicInteger();
	
	private final int id;
	
	private int fromAccountId;
	
	private int toAccountId;
	
	private BigDecimal amount;
	
	private double discount = 0.0;
	
	private double feeRate = 0.0;
	
	private Currency currency;
	
	private String comment;
	
	private TransferStatus status;
	
	public Transfer(int fromAccountId, int toAccountId, BigDecimal amount, Currency currency, String comment) {
        this.id = COUNTER.getAndIncrement();
        this.fromAccountId = fromAccountId;
        this.toAccountId = toAccountId;
        this.amount = amount;
        this.currency = currency;
        this.comment = comment;
        this.status = TransferStatus.PENDING;
    }
	
	public Transfer() {
		this.id = COUNTER.getAndIncrement();
		this.status = TransferStatus.PENDING;
	}

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}


	/**
	 * @return the fromAccountId
	 */
	public int getFromAccountId() {
		return fromAccountId;
	}


	/**
	 * @param fromAccountId the fromAccountId to set
	 */
	public void setFromAccountId(int fromAccountId) {
		this.fromAccountId = fromAccountId;
	}


	/**
	 * @return the toAccountId
	 */
	public int getToAccountId() {
		return toAccountId;
	}


	/**
	 * @param toAccountId the toAccountId to set
	 */
	public void setToAccountId(int toAccountId) {
		this.toAccountId = toAccountId;
	}


	/**
	 * @return the amount
	 */
	public BigDecimal getAmount() {
		return amount;
	}


	/**
	 * @param amount the amount to set
	 */
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}


	/**
	 * @return the discount
	 */
	public double getDiscount() {
		return discount;
	}


	/**
	 * @param discount the discount to set
	 */
	public void setDiscount(double discount) {
		this.discount = discount;
	}


	/**
	 * @return the feeRate
	 */
	public double getFeeRate() {
		return feeRate;
	}


	/**
	 * @param feeRate the feeRate to set
	 */
	public void setFeeRate(double feeRate) {
		this.feeRate = feeRate;
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


	/**
	 * @return the comment
	 */
	public String getComment() {
		return comment;
	}


	/**
	 * @param comment the comment to set
	 */
	public void setComment(String comment) {
		this.comment = comment;
	}


	/**
	 * @return the status
	 */
	public TransferStatus getStatus() {
		return status;
	}


	/**
	 * @param status the status to set
	 */
	public void setStatus(TransferStatus status) {
		this.status = status;
	}

}
