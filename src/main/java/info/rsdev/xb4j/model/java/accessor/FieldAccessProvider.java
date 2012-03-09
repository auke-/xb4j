/* Copyright 2012 Red Star Development / Dave Schoorl
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package info.rsdev.xb4j.model.java.accessor;

import info.rsdev.xb4j.exceptions.Xb4jException;

import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Modifier;

import javax.lang.model.SourceVersion;

/**
 * Get or set the value of a class property by accessing it's {@link Field} by fieldname
 * @author Dave Schoorl
 */
public class FieldAccessProvider implements ISetter, IGetter {
	
	private String fieldName = null;
	
	public FieldAccessProvider(String fieldName) {
		this.fieldName = validate(fieldName);
	}
	
	@Override
	public boolean set(Object contextInstance, Object propertyValue) {
		try {
			getField(contextInstance.getClass(), this.fieldName).set(contextInstance, propertyValue);
			return true;
		} catch (Exception e) {
			throw new Xb4jException(String.format("Could not set field '%s' with value '%s' in object '%s'", 
			        fieldName, propertyValue, contextInstance), e);
		}
	}
	
	@Override
	public Object get(Object contextInstance) {
		try {
			return getField(contextInstance.getClass(), this.fieldName).get(contextInstance);
		} catch (Exception e) {
			throw new Xb4jException(String.format("Could not get field '%s' from object %s", fieldName, contextInstance), e);
		}
	}
	
	private Field getField(Class<?> contextType, String fieldName) {
		if (contextType == null) {
			throw new NullPointerException("Type must be provided");
		}
		if (fieldName == null) {
			throw new NullPointerException("The name of the Field must be provided");
		}
		
		Field targetField = null;
		Class<?> candidateClass = contextType;
		while (targetField == null) {
			for (Field candidate: candidateClass.getDeclaredFields()) {
				if (candidate.getName().equals(fieldName)) {
					targetField = candidate;
					break;
				}
			}
			if (targetField ==  null) {
				candidateClass = candidateClass.getSuperclass();
				if (candidateClass == null) {
					throw new IllegalStateException(String.format("Field '%s' is not definied in the entire class hierarchy " +
							"of '%s'.",fieldName, contextType.getName()));
				}
			}
		}
		
		if (!Modifier.isPublic(((Member)targetField).getModifiers()) || !Modifier.isPublic(((Member)targetField).getDeclaringClass().getModifiers())) {
			targetField.setAccessible(true);
		}
		//TODO: check if the field is final? warn if static?
		
		return targetField;
	}
	
	private String validate(String fieldName) {
		if (!SourceVersion.isIdentifier(fieldName)) {
			throw new IllegalArgumentException(String.format("Not a valid name for a field: %s", fieldName));
		}
		return fieldName;
	}
	
	@Override
	public String toString() {
	    return String.format("FieldAccessProvider[field=%s]", fieldName);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((this.fieldName == null) ? 0 : this.fieldName.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		FieldAccessProvider other = (FieldAccessProvider) obj;
		if (this.fieldName == null) {
			if (other.fieldName != null) return false;
		} else if (!this.fieldName.equals(other.fieldName)) return false;
		return true;
	}
	
	

}
