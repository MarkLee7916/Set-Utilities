package settheorylib;

// Used to allow caller to pass functions as arguments
interface FuncInterface<I, O> {
	O function(I argument);
}
