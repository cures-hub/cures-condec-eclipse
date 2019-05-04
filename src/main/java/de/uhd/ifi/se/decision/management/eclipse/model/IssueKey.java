package de.uhd.ifi.se.decision.management.eclipse.model;

import java.util.HashMap;
import java.util.Map;

public class IssueKey {
	private static Map<String, IssueKey> instances = new HashMap<String, IssueKey>();
	private int  keynumber = 0;
	private String issueKeyBase = "<not set>";
	
	
	public static IssueKey getOrCreate(String issueKey) {
		if(instances.containsKey(issueKey.toUpperCase())) {
			return instances.get(issueKey);
		} else {
			IssueKey ik = new IssueKey();
			if(ik.setIssueKey(issueKey)) {
				instances.put(ik.getFullIssueKey(), ik);
				return ik;
			}
			return null;
		}
	}
	
	private IssueKey() { /* Default-Constructor */ }
	
	private IssueKey(String issuekey) {
		this.setIssueKey(issuekey);
	}
	
	@Override
	public String toString() {
		return this.getFullIssueKey();
	}
	
	public String getFullIssueKey() {
		return this.issueKeyBase + "-" + this.keynumber;
	}
	
	public void setKeynumber(int keynumber) {
		if(keynumber == 0) {
			//throw new IssueKeyZeroExeption("The Issue-Key-Number cannot be zero.");
		}
		if(keynumber < 0) {
			this.keynumber = -keynumber;
		} else {
			this.keynumber = keynumber;
		}
	}
	
	public int getKeynumber() {
		return this.keynumber;
	}
	
	/**
	 * @param issuekey Expecting an Issue-Key-Base like "EXAMPLE" instead of "EXAMPLE-123"
	 * @return Returns true, if the Issue-Key-Base was changed successfully, otherwise false.
	 */
	public boolean setIssuekeyBase(String issuekey) {
		if(issuekey.contains("-")
			|| issuekey.contains(":")
			|| issuekey.contains("0")
			|| issuekey.contains("1")
			|| issuekey.contains("2")
			|| issuekey.contains("3")
			|| issuekey.contains("4")
			|| issuekey.contains("5")
			|| issuekey.contains("6")
			|| issuekey.contains("7")
			|| issuekey.contains("8")
			|| issuekey.contains("9")
			) {
			// Fails, because the IssueKeyBase should never contain ":", "-" or any number.
			return false;
		}
		// Otherwise apply IssueKeyBase as Upper-Case
		this.issueKeyBase = issuekey.toUpperCase();
		return true;
	}
	
	public String getIssuekeyBase() {
		return this.issueKeyBase;
	}
	
	/**
	 * @param issuekey Expecting a full Issue-Key with pattern "EXAMPLE-123"
	 * @return Returns true, if the IssueKey was changed successfully, otherwise false.
	 */
	public boolean setIssueKey(String issuekey) {
		String extraction = "";
		if(issuekey.contains(":")) {
			extraction = issuekey.substring(0, issuekey.indexOf(":"));
		} else {
			extraction = issuekey;
		}
		if(extraction.contains("-")) {
			String[] words = extraction.split("-");
			if(words.length != 2) {
				return false;
			}
			try {
				int key = Integer.parseInt(words[1]);
				setKeynumber(key);
				setIssuekeyBase(words[0]);
			} catch(Exception ex) {
				return false;
			}
		}
		return true;
	}
}
