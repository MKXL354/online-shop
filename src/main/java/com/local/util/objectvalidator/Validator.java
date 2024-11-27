package com.local.util.objectvalidator;

@FunctionalInterface
public interface Validator{
	boolean isValid(Object obj);
}