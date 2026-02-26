package org.jtrace;

import org.jtrace.geometry.GeometricObject;

/**
 * Represents a section of a {@link GeometricObject} that a {@link Jay} goes
 * through.
 * 
 * A {@link Section} always have a entryHit and a exitHit, even if the
 * {@link GeometricObject} is planar, in that case, the entry {@link Hit} and
 * the exit {@link Hit} should be the same.
 * 
 * @author raphaelpaiva
 * 
 */
public class Section {

	private Hit entryHit;
	private Hit exitHit;

	/**
	 * Creates a section based on the {@link Hit}s passed as argument. The entry
	 * and exit {@link Hit}s are determined by the {@link Hit#getT()}. The
	 * lowest is the entry {@link Hit}.
	 * 
	 * @param hit1
	 * @param hit2
	 * 
	 * @throws IllegalStateException if one of the hits is instance of {@link NotHit}.
	 */
	public Section(Hit hit1, Hit hit2) {
		if (hit1.getT() < hit2.getT()) {
			entryHit = hit1;
			exitHit = hit2;
		}else{
			entryHit = hit2;
			exitHit = hit1;
		}
	}

	public Hit getEntryHit() {
		return entryHit;
	}

	public Hit getExitHit() {
		return exitHit;
	}

}
