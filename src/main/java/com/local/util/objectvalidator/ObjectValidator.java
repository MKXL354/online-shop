package com.local.util.objectvalidator;

import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ConcurrentHashMap;
import java.lang.reflect.Field;
import java.lang.annotation.Annotation;

public class ObjectValidator{
	private ConcurrentMap<String, Validator> validators = new ConcurrentHashMap<>();
	
	public String validate(Object obj) {
		StringBuilder errors = new StringBuilder();
		if(obj == null){
			errors.append("object can't be null").append("\n");
			return errors.toString();
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
							errors.append(message).append("\n");
						}
					}
					catch(ReflectiveOperationException e){
						throw new RuntimeException("object validator error: " + e.getMessage(), e);
					}
                }
            }
        }
        return errors.toString();
	}
}