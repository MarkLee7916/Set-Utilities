package settheorylib;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Set;
import java.util.HashSet;
import java.util.List;

public class SetUtils {
	// Returns the set of all subsets of a given set
	public static <T> Set<Set<T>> powerSet(Set<T> set) {
		Set<Set<T>> ret = new HashSet<>(Arrays.asList(new HashSet<>()));
		Set<Set<T>> placeholder = new HashSet<>();

		for (T elem : set) {
			for (Set<T> currentTarget : ret) {
				Set<T> newSet = new HashSet<>(currentTarget);
				newSet.add(elem);
				placeholder.add(newSet);
			}
			ret.addAll(placeholder);
			placeholder.clear();
		}

		return ret;
	}

	@SafeVarargs
	// Initialises ArrayList with values
	public static <T> List<T> newList(T... elems) {
		if (elems.length == 0)
			throw new IllegalArgumentException("Doesn't support empty arguments");

		List<T> list = new ArrayList<>();

		for (T elem : elems)
			list.add(elem);

		return list;
	}

	@SafeVarargs
	// Initialises HashSet with values
	public static <T> Set<T> newSet(T... elems) {
		if (elems.length == 0)
			throw new IllegalArgumentException("Doesn't support empty arguments");

		Set<T> set = new HashSet<>();

		for (T elem : elems)
			set.add(elem);

		return set;
	}

	// Roughly equivilant to a simple list comprehension in a language like Python
	public static <I, O> List<O> listComp(FuncInterface<I, O> map, List<I> list, FuncInterface<I, Boolean> pred) {
		List<O> ret = new ArrayList<>();

		for (I elem : list)
			if (pred.function(elem))
				ret.add(map.function((elem)));

		return ret;
	}

	// Roughly equivilant to a simple set comprehension in formal maths
	public static <I, O> Set<O> setComp(FuncInterface<I, O> map, Set<I> list, FuncInterface<I, Boolean> pred) {
		Set<O> ret = new HashSet<>();

		for (I elem : list)
			if (pred.function(elem))
				ret.add(map.function((elem)));

		return ret;
	}

	// Returns set of all combinations of a given set
	public static <T> Set<List<T>> permutationSet(Set<T> set) {
		// Initialise ret with one arraylist, of which has one element
		Set<List<T>> removals = new HashSet<>();
		Set<List<T>> additions = new HashSet<>();
		Set<List<T>> ret = new HashSet<>();

		ret.add(new ArrayList<>());

		for (T elem : set) {
			for (List<T> list : ret) {
				for (int j = 0; j <= list.size(); j++) {
					List<T> newElem = new ArrayList<>(list);
					newElem.add(j, elem);
					additions.add(newElem);
					removals.add(list);
				}
			}
			ret.addAll(additions);
			additions.clear();

			ret.removeAll(removals);
			removals.clear();
		}

		return ret;
	}

	@SafeVarargs
	// For 2 sets A and B, returns the set of elements that are contained in A OR B
	public static <T> Set<T> union(Set<T>... sets) {
		if (sets.length < 2)
			throw new IllegalArgumentException("Must pass in more than one set");

		Set<T> ret = new HashSet<>();

		for (Set<T> set : sets)
			ret.addAll(set);

		return ret;
	}

	// Given a domain, a codomain and a function returns whether function is both an injection and a surjection
	public static <I, O> boolean isBijection(Set<I> domain, Set<O> codomain, FuncInterface<I, O> f) {
		return isInjection(domain, f) && isSurjection(domain, codomain, f);
	}

	// Given a domain, a codomain and a function returns whether every element in the codomain has a mapping
	public static <I, O> boolean isSurjection(Set<I> domain, Set<O> codomain, FuncInterface<I, O> f) {
		Set<O> outputs = new HashSet<>();

		for (I i : domain)
			outputs.add(f.function(i));

		for (O o : codomain)
			if (!outputs.contains(o))
				return false;

		return true;
	}

	// Given a domain and a function returns whether no 2 elements in the codomain are mapped onto twice
	public static <I, O> boolean isInjection(Set<I> domain, FuncInterface<I, O> f) {
		List<O> outputs = new ArrayList<>();

		for (I i : domain)
			outputs.add(f.function(i));

		Set<O> set = new HashSet<>(outputs);

		return set.size() == outputs.size();
	}

	@SafeVarargs
	// For 2 sets A and B, returns the set of elements that are contained in A AND B
	public static <T> Set<T> intersection(Set<T>... sets) {
		if (sets.length < 2)
			throw new IllegalArgumentException("Must pass in more than one set");

		Set<T> ret = new HashSet<>();

		for (Set<T> set : sets)
			for (T elem : set)
				if (allSetsContains(sets, elem))
					ret.add(elem);

		return ret;
	}

	// Helper method for calculating intersections
	private static <T> boolean allSetsContains(Set<T>[] sets, T elem) {
		for (Set<T> set : sets)
			if (!set.contains(elem))
				return false;

		return true;
	}

	// For 2 sets A and B, returns true if B is a subset of A
	public static <T> boolean isSubset(Set<T> superset, Set<T> subset) {
		for (T elem : subset)
			if (!superset.contains(elem))
				return false;

		return true;
	}

	// For 2 sets A and B, returns true if B is a subset of A, but only if A != B
	public static <T> boolean isProperSubset(Set<T> superset, Set<T> subset) {
		return isSubset(superset, subset) && !superset.equals(subset);
	}

	// For 2 sets A and B, returns the set of elements that are in A but not in B
	public static <T> Set<T> difference(Set<T> set1, Set<T> set2) {
		Set<T> ret = new HashSet<>();

		for (T elem : set1)
			if (!set2.contains(elem))
				ret.add(elem);

		return ret;
	}

	// For 2 sets A and B, returns the set of elements that are contained in A XOR B
	public static <T> Set<T> symmetricDifference(Set<T> set1, Set<T> set2) {
		Set<T> ret = new HashSet<>();

		for (T elem : set1)
			if (!set2.contains(elem))
				ret.add(elem);

		for (T elem : set2)
			if (!set1.contains(elem))
				ret.add(elem);

		return ret;
	}

	@SuppressWarnings("rawtypes")
	// Given a set, returns whether set is composed solely of sets i.e doesn't
	// contain any other elements other than other sets
	public static <T> boolean isPureSet(Set set) {
		for (Object elem : set)
			if (!(elem instanceof Set))
				return false;
			else if (!isPureSet((Set) elem))
				return false;

		return true;
	}

	@SafeVarargs
	// Returns the set of all possible ordered tuples where each tuple contains one
	public static <T> Set<List<T>> cartesianProduct(Set<T>... sets) {
		if (sets.length < 2)
			throw new IllegalArgumentException("Must pass in more than one set");

		Set<List<T>> ret = new HashSet<>();

		for (T element : sets[0])
			ret.add(new ArrayList<>(Arrays.asList(element)));

		for (int i = 1; i < sets.length; i++)
			ret = cart2sets(ret, sets[i]);

		return ret;
	}

	// Helper method for calculating cartesian product
	private static <T> Set<List<T>> cart2sets(Set<List<T>> master, Set<T> merger) {
		Set<List<T>> ret = new HashSet<>();

		for (List<T> tuple : master) {
			for (T elem : merger) {
				List<T> newList = new ArrayList<>(tuple);
				newList.add(elem);
				ret.add(newList);
			}
		}
		return ret;
	}
}
