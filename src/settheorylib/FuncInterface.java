package settheorylib;

// Used to pass as arguments by allowed caller to pass a llamda expression
interface FuncInterface<I, O> {
	O function(I argument);
}
