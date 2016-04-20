package ie.jtc.flatten;

import static org.junit.Assert.assertArrayEquals;

import org.apache.log4j.Logger;

/**
 * Common functionality for simple comparison tests
 * so we can add in logging, timing, etc.
 * <p>
 * Plus we allow for the scope to include multiple
 * implementations
 * @author john
 *
 */
public abstract class BasicFlattenTest {

	static Logger logger = Logger.getLogger(BasicFlattenTest.class);

	/**
	 * test all flatten implementations, logging time for each
	 * @param testvalue
	 */
	protected void testAllImplementations(Object[] input,Integer[] expected){
		logger.debug("Input: "+Flatten.canonicalForm(input));
		long start = System.currentTimeMillis();
		Integer[] actual = Flatten.flattenIteratively(input);
		logger.debug("Output: "+Flatten.canonicalForm(actual));
		long tmIter=System.currentTimeMillis()-start;
		assertArrayEquals( expected,actual);		
		start = System.currentTimeMillis();
		actual = Flatten.flattenRecursively(input);
		logger.debug("Output: "+Flatten.canonicalForm(actual));
		long tmRecur=System.currentTimeMillis()-start;
		assertArrayEquals( expected,actual);
		logger.debug("Iterating took "+tmIter+", recursing took "+tmRecur);
	}
}