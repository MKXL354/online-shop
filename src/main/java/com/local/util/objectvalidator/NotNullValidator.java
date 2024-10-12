package com.local.util.objectvalidator;

public class NotNullValidator implements Validator{
	@Override
	public boolean isValid(Object obj){
		return obj != null;
	}
}