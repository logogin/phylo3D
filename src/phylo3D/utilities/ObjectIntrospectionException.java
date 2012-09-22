/*
 * Created on 18-Aug-2004
 */
package phylo3D.utilities;

/**
 *  Exception that covers the case where the program is not able to introspect an object. In a few cases
 * it is necessary to do introspection on objects. But this exception should really not occur at runtime.
 * 
 * @author Tim Hughes (Computational Biology Unit - University of Bergen - Norway)
 */
public class ObjectIntrospectionException extends FatalException
{
	public ObjectIntrospectionException(String message)
	{
		super(message);
	}
}
