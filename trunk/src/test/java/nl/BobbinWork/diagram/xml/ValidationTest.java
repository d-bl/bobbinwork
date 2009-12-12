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
  private static final String DIR = "../wiki/diagrams/";

  private static XmlResources xmlResources;

  private static final String EMPTY = XmlResources.ROOT + "</diagram>";
  private static final String MISSING_PAIRS =
      XmlResources.ROOT + XmlResources.INCLUDE
          + "<stitch pairs='1-2'><copy of='C'/></stitch>" + "</diagram>";
  private static final URL NEW =
      XmlResources.class.getResource( "newDiagram.xml" );

  private static final Object[] OK = (Object[]) new Object[] {
    "ok"
  };

  private static final String EOF = "Premature end of file";
  private static final Class<SAXException> SE = SAXException.class;
  private static final Class<NullPointerException> NPE =
      NullPointerException.class;

  @Parameters
  public static Collection<TestRunParameters[]> createParameters()
      throws Exception
  {
    final List<TestRunParameters[]> resultList =
        new ArrayList<TestRunParameters[]>();

    resultList.addAll( Arrays.asList( new TestRunParameters[][] {
        run( "1" ).expects( SE, EOF ).withInput( 777L, "" ),
        run( "2" ).expects( NPE ).withInput( 20L, (Object) null ),
        // new should stay first, caching influences elapse times
        run( "new" ).expects( OK ).withInput( 1000L, NEW.toURI() ),
        run( "empty" ).expects( OK ).withInput( 20L, EMPTY ),
        run( "include error" ).expects( SE, "[iI]nclude" ).withInput( 20L, "",
            "non-existent.xml" ),
        run( MISSING_PAIRS ).expects( OK /* FIXME */).withInput( 1000L,
            NEW.toURI() ),
    } ) );

    for (final Ground g : Ground.values()) {
      resultList.add( run( g.name() + ".diamond" ).expects( OK ).withInput(
          g.diamondLapse, g.diamond() ) );
      resultList.add( run( g.name() + ".square" ).expects( OK ).withInput(
          g.squareLapse, g.square() ) );
    }

    for (final Object[] sample : new Object[][] {
        {
            2000L, new File( DIR + "snow.xml" )
        }, {
            1500L, new File( DIR + "flanders.xml" )
        }, {
            1900L, new File( DIR + "braid-chaos.xml" )
        }, {
            1500L, new File( DIR + "braid-half-stitch.xml" )
        }, {
            1800L, new File( DIR + "braid-row-cloth-row-half-stitch.xml" )
        },

    }) {
      resultList.add( run( "sample" ).expects( OK ).withInput(
          (Long) sample[0], (File) sample[1] ) );
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
    // TODO set elapse to zero to reevaluate
    final long maxElapsed = (Long) inputs[0];
    final Object input = inputs[1];
    final int includeStart = 2;

    final long start = System.currentTimeMillis();
    final Document parsed;
    if (inputs.length > includeStart) {
      String src = XmlResources.ROOT;
      for (int i = includeStart; i < inputs.length; i++)
        src += String.format( "<xi:include href='%s'/>", inputs[i] );
      parsed = xmlResources.parse( src + "</diagram>" );
    } else {
      if (input instanceof String) {
        parsed = xmlResources.parse( (String) input );
      } else if (input instanceof File) {
        parsed = xmlResources.parse( (File) input );
      } else if (input instanceof InputStream) {
        parsed = xmlResources.parse( (InputStream) input );
      } else if (input instanceof URI) {
        parsed = xmlResources.parse( (URI) input );
      } else
        throw new IllegalArgumentException( "got: "
            + input.getClass().getName() );
    }
    final long elapsed = System.currentTimeMillis() - start;
    XmlResources.validate( parsed );
    final long start2 = System.currentTimeMillis();

    TreeExpander.replaceCopyElements( (Element) parsed.getFirstChild() );
    DiagramBuilder.createDiagram( (Element) parsed.getFirstChild() );

    final long elapsedTotal = System.currentTimeMillis() - start2 + elapsed;
    // XmlResources.validate( parsed );
    if (elapsedTotal > maxElapsed) return new String[] {
      "elapse time exceeded. expected: " + maxElapsed + " got: " + elapsedTotal
    };
    return OK;
  }
}
