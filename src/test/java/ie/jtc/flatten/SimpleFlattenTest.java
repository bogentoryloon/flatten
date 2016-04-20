package ie.jtc.flatten;

import org.junit.Test;
/**
 * handful of basic tests - see superclass for methodlogy
 * (and consequent weaknesses)
 * @author John
 *
 */
public class SimpleFlattenTest extends BasicFlattenTest {
	
	@Test
	public void oneEmpty(){

		testAllImplementations(new Object[0],new Integer[0]);
	}
	@Test
	public void oneTest(){
		Object oneObject[]={1};
		Integer oneInteger[]={1};
		testAllImplementations(oneObject, oneInteger);
	}
	@Test
	public void flatTest(){
		Object flatArray[]={1,2,1,2,1,2};
		Integer flatInteger[]={1,2,1,2,1,2};
		testAllImplementations(flatArray,flatInteger);
	}
	@Test
	public void nestedTest(){
		// simple nesting {1,{2,{3}}}
		Object nest[]={1,new Object[]{2,new Object[]{3}}};
		Integer flattened[]={1,2,3};
		testAllImplementations(nest,flattened);	
	}

	@Test
	public void raggyTest(){
		// now do the real thing 
		Object raggy[]={1,new Object[]{2,new Object[]{3,4},5},6};
		Integer flattened[]={1,2,3,4,5,6};
		testAllImplementations(raggy,flattened);	
	}
	@Test
	public void raggierTest(){
		// make it work a bit
		Object[] raggier={1,2,new Object[]{3,4},
							new Object[]{5,
									new Object[]{6,7},8},9};
		Integer flattened[]={1,2,3,4,5,6,7,8,9};		
		testAllImplementations(raggier,flattened);
	}
	

}
