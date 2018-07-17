package org.dufy.util;

import java.util.LinkedHashMap;

/**
 * Split a string into substrings with a given separator,
 * and hold the substrings into a linkedHashMap with offset as keys.
 * <b>Unlike</b> {@link String#split(String)}, multiple adjacent separator won't be ignored,
 * and the separator is treated as literal string, not regular expression pattern.
 * 
 * <p>{@code new StringSplitHelper("a,b,,,c", ",")} will generate "a","b","","","c" 5 substrings.
 * 
 * <p>An example:
 * <code><pre>
 *    String current = null;
 *    StringSplitHelper helper = new StringSplitHelper("a,b,,,c", ",");
 *    while( (current = helper.getNext()) != null) {
 *        System.out.println(current);
 *    }
 * </pre></code>
 * 
 * @author Charlie
 *
 */
public class StringSplitHelper {

	private String original;
	private String separator;
	private int offset = 0;
	private int total = 0;
	private LinkedHashMap<Integer, String> holderMap = new LinkedHashMap<Integer, String>();
	
	/**
	 * Construction function, to general a {@link StringSplitHelper}
	 * 
	 * @param orgiginal string to be split
	 * @param separator in the {@code original} string
	 */
	public StringSplitHelper(String orgiginal, String separator) {
		this.original = orgiginal;
		this.separator = separator;
		initialize();
	}
	
	/**
	 * Split the given string, and hold all substrings in a linkedHashMap
	 */
	private final void initialize() {
		int start = 0, next = -1, index = 0, span = separator.length();
		while((next = original.indexOf(separator, start)) != -1) {
			holderMap.put(index, original.substring(start, next));
			start = next + span;
			total = ++index;
		}
		if(start <= original.length()) {
			holderMap.put(index, original.substring(start));
			total += 1;
		}
	}
	
	/**
	 * Get the first substring, a.k.a. the substring with offset 0
	 * 
	 * <p>It has same effect to call {@link #get(int)} with 0 as parameter,
	 * or call {@link #getNext()} after construct the class
	 * 
	 * @return the first substring
	 */
	public String getFirst() {
		return get(0);
	}
	
	/**
	 * Get the next substring, and increment the offset by 1
	 * 
	 * @return the next substring
	 */
	public String getNext() {
		return get(offset);
	}
	
	/**
	 * Get the {@code offset} -nd substring, and increment the offset by 1
	 * 
	 * <p>If offset exceeds the total, return {@code null},
	 * and if offset is a negative number, means count reversely ( start from the end ),
	 * the actual offset is {@code total + offset}
	 * 
	 * @param offset of the wanted substring, start by 0
	 * @return
	 */
	public String get(int offset) {
		if(offset < 0) {
			if(offset < -this.total) {
				return null;
			}
			this.offset = this.total + offset;
			return holderMap.get(this.offset++);
		}
		else {
			this.offset = offset;
			if(this.offset >= this.total) {
				return null;
			}
		}
		return holderMap.get(this.offset++);
	}
	
	/**
	 * Get the {@code offset} -nd substring, and <b>don't</b> increment the offset
	 * 
	 *  <p>If offset exceeds the total, return {@code null}
	 *  
	 * @param offset offset of the wanted substring, start by 0
	 * @return
	 */
	public String getAbitrary(int offset) {
		if(offset >= this.total) {
			return null;
		}
		return holderMap.get(offset);
	}
	
	/**
	 * Get the last substring
	 * 
	 * <p>It has same effect to call {@link #get(int)} with -1 as parameter,
	 * 
	 * @return the last substring
	 */
	public String getLast() {
		return get(-1);
	}
	
	/**
	 * @return the total occurrence of substrings
	 */
	public int getTotal() {
		return total;
	}
}
