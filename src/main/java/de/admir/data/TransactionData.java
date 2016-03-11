package de.admir.data;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;


/**
 * Author:  Admir Memic
 * Date:    11.03.2016
 * E-Mail:  admir.memic@dmc.de
 */
public class TransactionData
{
	@JsonIgnore
	private Long id;
	private Double amount;
	private String type;
	@JsonProperty("parent_id")
	private Long parentId;

	public TransactionData()
	{
	}

	public TransactionData(Long id, Double amount, String type, Long parentId)
	{
		if (id == null)
			throw new IllegalArgumentException("Transaction id can not be null!");
		this.id = id;
		this.amount = amount;
		this.type = type;
		this.parentId = parentId;
	}

	public Long getId()
	{
		return id;
	}

	public void setId(Long id)
	{
		this.id = id;
	}

	public Double getAmount()
	{
		return amount;
	}

	public void setAmount(Double amount)
	{
		this.amount = amount;
	}

	public String getType()
	{
		return type;
	}

	public void setType(String type)
	{
		this.type = type;
	}

	public Long getParentId()
	{
		return parentId;
	}

	public void setParentId(Long parentId)
	{
		this.parentId = parentId;
	}
}
