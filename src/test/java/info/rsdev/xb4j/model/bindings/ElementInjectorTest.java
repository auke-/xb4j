package info.rsdev.xb4j.model.bindings;

import static org.junit.Assert.assertEquals;
import info.rsdev.xb4j.exceptions.Xb4jMarshallException;
import info.rsdev.xb4j.model.bindings.action.IMarshallingAction;
import info.rsdev.xb4j.model.java.JavaContext;
import info.rsdev.xb4j.test.FixedValueTestAction;
import info.rsdev.xb4j.test.NullValueTestAction;
import info.rsdev.xb4j.test.ObjectA;
import info.rsdev.xb4j.util.SimplifiedXMLStreamWriter;

import java.io.StringWriter;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLOutputFactory;

import org.junit.Before;
import org.junit.Test;

public class ElementInjectorTest {
	
	private IMarshallingAction action = null;
	
	private StringWriter writer = null;
	
	private SimplifiedXMLStreamWriter staxWriter = null;	
	
	@Before
	public void setUp() throws Exception {
		action = new FixedValueTestAction();
        writer = new StringWriter();
        staxWriter = new SimplifiedXMLStreamWriter(XMLOutputFactory.newInstance().createXMLStreamWriter(writer));
	}
	
	@Test
	public void testInjectText() throws Exception {
		ElementInjector xmlInjector = new ElementInjector(new QName("Injected"), action);
		xmlInjector.toXml(staxWriter, new JavaContext(new ObjectA("true")));
		staxWriter.close();
		
		assertEquals("<Injected>Fixed value</Injected>", this.writer.toString());
	}
	
	@Test
	public void testInjectOptionalElementNoText() throws Exception {
		ElementInjector xmlInjector = new ElementInjector(new QName("Injected"), new NullValueTestAction());
		xmlInjector.setOptional(true);
		xmlInjector.toXml(staxWriter, new JavaContext(new ObjectA("true")));
		staxWriter.close();
		
		assertEquals("", this.writer.toString());
	}
	
	@Test(expected=Xb4jMarshallException.class)
	public void testInjectMandatoryElementNoText() throws Exception {
		ElementInjector xmlInjector = new ElementInjector(new QName("Injected"), new NullValueTestAction());
		xmlInjector.setOptional(false);	//default value is mandatory, for clarity, set it explicity
		xmlInjector.toXml(staxWriter, new JavaContext(new ObjectA("true")));
	}
	
	@Test
	public void testInjectMandatoryElementNoTextWithAttributes() throws Exception {
		ElementInjector xmlInjector = new ElementInjector(new QName("Injected"), new NullValueTestAction());
		xmlInjector.addAttribute(new Attribute(new QName("attributes")), "name");
		xmlInjector.setOptional(false);	//default value is mandatory, for clarity, set it explicity
		xmlInjector.toXml(staxWriter, new JavaContext(new ObjectA("true")));
		staxWriter.close();
		
		assertEquals("<Injected attributes=\"true\"/>", this.writer.toString());
	}
}
