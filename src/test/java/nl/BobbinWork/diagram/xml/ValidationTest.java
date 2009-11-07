package nl.BobbinWork.diagram.xml;

import static nl.BobbinWork.testutils.parameterized.TestRunParametersBuilder.run;

import java.io.*;
import java.net.*;
import java.util.*;

import javax.xml.parsers.ParserConfigurationException;

import nl.BobbinWork.diagram.xml.expand.TreeExpander;
import nl.BobbinWork.testutils.parameterized.*;

import org.junit.BeforeClass;
import org.junit.runners.Parameterized.Parameters;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

public class ValidationTest
    extends ParameterizedFixture
{
  private static XmlResources xmlResources;

  private static final String EMPTY = XmlResources.ROOT + "</diagram>";
  private static final URL NEW =
      XmlResources.class.getResource( "newDiagram.xml" );

  private static final Object[] OK = (Object[]) new Object[] {};

  private static final String EOF = "Premature end of file";
  private static final Class<SAXException> SE = SAXException.class;
  private static final Class<NullPointerException> NPE =
      NullPointerException.class;

  @Parameters
  public static Collection<TestRunParameters[]> createParameters()
      throws Exception
  {
    List<TestRunParameters[]> resultList = new ArrayList<TestRunParameters[]>();
    resultList.addAll( Arrays.asList( new TestRunParameters[][] {
        run( "1" ).expects( SE, EOF ).withInput( "" ),
        run( "2" ).expects( NPE ).withInput( (Object) null ),
        run( "new" ).expects( OK ).withInput( NEW.toURI() ),
        run( "empty" ).expects( OK ).withInput( EMPTY ),
        run( "basic" ).expects( OK ).withInput( "", "basicStitches.xml" ),
        run( "include error" ).expects( SE, "[iI]nclude" ).withInput( "",
            "non-existent.xml" ),
    } ) );
    for (Ground g : Ground.values()) {
      resultList.add( run( g.name() + ".diamond" ).expects( OK ).withInput(
      g.diamond() ) );
      resultList.add( run( g.name() + ".square" ).expects( OK ).withInput(
      g.square() ) );
    }
    return resultList;
  }

  @BeforeClass
  public static void setup() throws ParserConfigurationException, SAXException
  {
    xmlResources = new XmlResources();
  }

  public ValidationTest(TestRunParameters parameters)
  {
    super( parameters );
  }

  @Override
  protected Object[] produceActualResults(
      Object[] inputs) throws Exception
  {
    final Document parsed;
    if (inputs.length > 1) {
      String src = XmlResources.ROOT;
      for (int i = 1; i < inputs.length; i++)
        src += String.format( "<xi:include href='%s'/>", inputs[i] );
      parsed = xmlResources.parse( src + "</diagram>" );
    } else if (inputs[0] instanceof String) {
      parsed = xmlResources.parse( (String) inputs[0] );
    } else if (inputs[0] instanceof File) {
      parsed = xmlResources.parse( (File) inputs[0] );
    } else if (inputs[0] instanceof InputStream) {
      parsed = xmlResources.parse( (InputStream) inputs[0] );
    } else if (inputs[0] instanceof URI) {
      parsed = xmlResources.parse( (URI) inputs[0] );
    } else
      throw new IllegalArgumentException( "got: "
          + inputs[0].getClass().getName() );

    XmlResources.validate( parsed );
    TreeExpander.replaceCopyElements( (Element) parsed.getFirstChild() );
    XmlResources.validate( parsed );
    DiagramBuilder.createDiagram( (Element) parsed.getFirstChild() );
    return OK;
  }
}
