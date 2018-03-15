package org.clyze.persistent.model

/**
 * A persistent element of a clue analysis
 */
abstract class Element extends ItemImpl {

	/**
	 * The element's id. Set by either a subclass or by external code
	 */
	String id

	/**
	 * The root element id -- the id of the "root element" this element belong to.
	 * Either the analysis or the bundle, depending on the type of this element.	 
	 */
	String rootElemId	

	Element() {}
}
