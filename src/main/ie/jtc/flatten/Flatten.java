package ie.jtc.flatten;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.EmptyStackException;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;
import org.apache.log4j.Logger;


/**
 * implement multiple ways of flattening an array of integers.
 * <p>
 * They are functionally equivalent but are 
 * @author john
 *
 */
public class Flatten {
	static Logger log = Logger.getLogger(Flatten.class);
	/**
	 * <b>The Good</b>
	 * recursive implementation. 
	 * It's simple but constrained by the stack size.
	 * depending on performance metrics (tbd) this might be worthwhile tradeoff
	 * @param oa
	 * @return Integer[]
	 */
	public static Integer[] flattenRecursively(Object[] oa) {
		LinkedList<Object> result=  new LinkedList<Object>(); // decant the result into this 
		recurseFlatten(result,oa);
		// result itself is now functionally the answer, but we convert it something more like
		// form it was passed in to us
		return (Integer[]) result.toArray(new Integer[result.size()]);
	}
	/**
	 * the recursive part of the algorithm.
	 * parses a list, which may be input list or a sublist.
	 * NB no return value, we rely on exhaustion of the input (oa) on each 
	 * stack frame to terminate the recursion
	 * (provided the jvm stack hasn't already terminated prematurely!) 
	 * @param result
	 * @param oa
	 */
	private static void recurseFlatten(LinkedList<Object> result, Object[] oa) {
		for(int i=0;i<oa.length;++i){
			// either we have hit a bare number, so append it to our results and carry on...
			if( oa[i] instanceof Integer){
				result.add(oa[i]);
			}else{
			//... or we have to pop it on the stack
				recurseFlatten(result,(Object[]) oa[i]);
			}
		}		
	}

	/**
	 * Internal helper class.
	 * <p>
	 * Implements a stackframe in the context of parsing nested (primite) arrays.
	 * <p>
	 * As such it has the array and it's current index
	 * @author John
	 *
	 */
	static class WorkUnit{
		private Object[] array;
		private int sofar=0;		
		WorkUnit(Object[] array){
			this.array=array;			
		}
		int howFar(){
			return sofar;
		}
		void setSoFar(int i){
			this.sofar=i;
		}
		Object[] getArray(){
			return array;
		}
	}
	/**
	 * <b>The Ugly</b>
	 * This time we manage our
	 * processing stack explicitly i.e. on the heap.
	 * <p>
	 * NB this is one of the worst pieces of code I have ever written,
	 * but given my solemn covenant not to google for the answer, I'll use
	 * it as a demo of how important testing is
	 * @param oa
	 * @return
	 */
	public static Integer[] flattenIteratively( Object[] oa){		
		   List<Object> result = new ArrayList<>(); // decant the result into this
		   /* we decant input array into an explicit queue 
		    * that will have the same values throughout the program stack
		    */
		    LinkedList<Object> inputq = new LinkedList<>(Arrays.asList(oa));
		    /**
		     * workStack is our equivalent of the program stack in the recursive example,
		     * except we just put the relevant unprocessed array on it.
		     * same deal, except we a) limit the overhead to this 1 object 
		     * b) crucically, use the larger and more configurable resources of the heap.
		     */
		    Stack<WorkUnit> workStack=new Stack<WorkUnit>();
		    WorkUnit sofar=null;
		    while (inputq.size() > 0 || sofar!=null) // terminating case, all input has been processed 
		    {
		    	// step 1 is to see if we have something on our stack i.e. we got here by 
		    	// finishing a prior stack entry
		    	try{
		    		sofar = workStack.peek();
		    		int i=sofar.howFar();
		        	for(;i<sofar.getArray().length;++i){
		        		if( sofar.getArray()[i] instanceof Integer ){
		        			result.add(sofar.getArray()[i]);
		        		}else{
		        			sofar.setSoFar(i+1);
		        			workStack.push(new WorkUnit((Object[])sofar.getArray()[i]));
		        			break;
		        		}
		        	}
		        	// we get here, we've processed a WorkUnit i.e. an array, so pop it
		        	if( i==sofar.array.length){
		        		sofar=null; // complete, don't want to process at end
		        		workStack.pop();
		        	}
		    	}
		        catch(EmptyStackException e){
		    		Object o = inputq.remove();
		    		if( o instanceof Integer){
		    			result.add(o);
		    		}else {
		    			sofar=new  WorkUnit((Object[]) o);		    
		    			workStack.push(sofar);
		    		}		    		
		        }
		    }
		    return (Integer[]) result.toArray(new Integer[result.size()]);
	}
	/**
	 * Render an array of arrays textually.
	 *  <p>This shares the logic of the recursive flattening procedure
	 *  <p>
	 *   Used, with caveats, for development and testing.
	 * @param input
	 * @return
	 */
	protected static String canonicalForm(Object[] input) {
		String s = new String("{");
		for (int i = 0; i < input.length; ++i) {
			if (input[i] instanceof java.lang.Integer) {
				s += (i > 0 ? "," : "") + input[i].toString();
			} else {
				s += "{" + canonicalForm((Object[]) input[i]) + "}";
			}
		}
		return s + "}";
	}
	/**
	 * Render an array of integers textually.
	 * <p>
	 * Used, with caveats, for development and testing.
	 * @param input
	 * @return
	 */
	protected static String canonicalForm(Integer[] input) {
		String s = new String("{");
		for (int i = 0; i < input.length; ++i) {
			s += (i > 0 ? "," : "") + input[i].toString();
		}
		return s + "}";
	}


}
