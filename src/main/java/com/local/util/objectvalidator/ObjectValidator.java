package com.local.util.objectvalidator;

import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.HashSet;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.annotation.Annotation;

public class ObjectValidator{
	private ConcurrentMap<String, Validator> validators = new ConcurrentHashMap<>();
	
	public HashSet<String> validate(Object obj) throws ValidatorException{
		HashSet<String> messages = new HashSet<>();
		if(obj == null){
			messages.add("object can't be null");
			return messages;
		}
        Field[] fields = obj.getClass().getDeclaredFields();
        Annotation[] annotations;
        ValidatedBy validatedBy;
        Validator validator;
		String validatorName;
		String message;
        for(Field field : fields){
            annotations = field.getDeclaredAnnotations();
            for(Annotation annotation : annotations){
                if((validatedBy = annotation.annotationType().getAnnotation(ValidatedBy.class)) != null){
					try{
						validatorName = validatedBy.value();
						validator = validators.get(validatorName);
						if(validator == null){
							validator = (Validator) Class.forName(validatedBy.value()).getDeclaredConstructor().newInstance();
							validators.put(validatorName, validator);
						}
						
						field.setAccessible(true);
						if(!validator.isValid(field.get(obj))){
							message = (String) annotation.annotationType().getMethod("message").invoke(annotation);
							messages.add(message);
						}
					}
					catch(ClassNotFoundException | NoSuchMethodException e){
						throw new ValidatorException("cannot find " + e.getMessage(), e);
					}
					catch(InstantiationException e){
						throw new ValidatorException("cannot create objectvalidator: " + e.getMessage(), e);
					}
					catch(IllegalAccessException | InvocationTargetException e){
						throw new ValidatorException("unexpected exception: " + e.getMessage(), e);
					}
                }
            }
        }
        return messages;
	}
}