package info.rsdev.xb4j.model.converter;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;
import info.rsdev.xb4j.model.java.JavaContext;

import org.junit.Test;

public class IntegerConverterTest {
	
	@Test
	public void testToObjectWithPadding() {
    	JavaContext javaContext = mock(JavaContext.class);	//not needed in IntegerConverter implementation
		assertEquals("01", new IntegerConverter(NoValidator.INSTANCE, 2).toText(javaContext, Integer.valueOf(1)));
	}
	
    @Test
    public void nullValuesAreNotValidated() {
        JavaContext javaContext = mock(JavaContext.class);  //not needed in IntegerConverter implementation
        assertNull(new IntegerConverter(NoValidator.INSTANCE, 2).toObject(javaContext, null));
    }
    
	@Test
	public void emptyStringsAreTreatedAsNullValues() {
        JavaContext javaContext = mock(JavaContext.class);  //not needed in IntegerConverter implementation
        assertNull(IntegerConverter.ZERO_OR_POSITIVE.toObject(javaContext, ""));
	}
	
}
