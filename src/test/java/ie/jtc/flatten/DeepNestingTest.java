package ie.jtc.flatten;

import org.junit.Test;
import static org.junit.Assert.assertArrayEquals;

import org.apache.log4j.Logger;
/**
 * let's push the testing to the limit to see 
 * when recursion fails
 * @author John
 *
 */
public class DeepNestingTest extends BasicFlattenTest {
	static Logger log = Logger.getLogger(DeepNestingTest.class);
	/**
	 * test that fully nested equals fully flat
	 * our nested object looks like:
	 * <p>
	 * {0,{1,{2,{3...}}}
	 * <p>
	 * NB testing revealed that e.g.
	 * <p>
	  * {1,{2,{null,null}}} crashed
	 * the diagnostic log messages.
	 * <p>
	 * At this time I will contend 
	 * this is not a bug - converting null
	 * is undefined, so  the test
	* avoids this (at the expense of horror code)
	 */
	private Object[] deeplyNested(int depth){
		assert  depth>= 2 ;
		Object deepAndNarrow[]=new Object[2];
		Object assignee[]=deepAndNarrow;
		for(int i=0;i<depth;++i){			
			/**
			 * we want to avoid
			 * nulls in the topmost array
			 * (see above), so check we're
			 * not on the 2nd last number
			 */
			Object newArray[]=null; 
			if( i<depth-2){
				newArray=new Object[2];
			}else if( i<depth-1){
				/**
				 *  ...otherwise size down in
				 *  preparation for the end
				 */				
				newArray=new Object[1];
			}			
			/**
			 * not at the end
			 */
			if( i<depth-1) {			
				// put the number in first slot 
				assignee[0]=i;
				// new empty array in preparation for further expansion
				assignee[1]=newArray;
				assignee=newArray;
			}else{
				// fill in the single slot  in the last array with a number 
				assignee[0]=i;
			}
		}
		return deepAndNarrow;
	}
	@Test
	public void deepNesting(){
		int depth=100;
		Integer expected[]=new Integer[depth];
		for(int i=0;i<depth;++i){
			expected[i]=i;
		}
		Object[] deepAndNarrow=deeplyNested(depth);
		testAllImplementations(deepAndNarrow, expected);
	}
	@Test
	public void tooDeeplyNesting(){
		int depth=5000;
		Integer expected[]=new Integer[depth];
		for(int i=0;i<depth;++i){
			expected[i]=i;
		}
		Object[] deepAndNarrow=deeplyNested(depth);
		// check iteration works
		Integer[] actual = Flatten.flattenIteratively(deepAndNarrow);
		assertArrayEquals(expected,actual);

		// see if we get a stackoverflow for the craic
		try{
			actual = Flatten.flattenRecursively(deepAndNarrow);
			assertArrayEquals(expected,actual);
		}catch(StackOverflowError e){
			log.info("stackoverflow(expected) with depth of "+depth);
		}
	}
}
