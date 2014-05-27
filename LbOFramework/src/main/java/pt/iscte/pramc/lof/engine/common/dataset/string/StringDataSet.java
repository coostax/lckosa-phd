/**
 * Copyright 2011 by Paulo R. Costa 
 * Distributed under the Artistic License.
 * This license appears at LICENSE file on the root of source folder. 
 */
package pt.iscte.pramc.lof.engine.common.dataset.string;

//import java.util.HashSet;
//import java.util.Iterator;
//import java.util.List;
//import java.util.Set;
//
//import pt.iscte.pramc.lof.domain.tools.LbOParser;
//import pt.iscte.pramc.sit.swi.di.Condition;
//import pt.iscte.pramc.sit.swi.di.Snapshot;
//import pt.iscte.pramc.sit.swi.si.Action;

/**
 * @author Paulo Costa (paulo.costa@iste.pt)
 * 
 *         This dataset holds the data as a string representation of object that
 *         stores the data
 * 
 *         NumStorage is simplified as data is represented by a Json string.
 *         This does not allow a correct match between different instances of
 *         the same data. Only string comparison can be used in this case
 * 
 * 
 * 
 * @version 0.1
 * @since Jul 11, 2011
 */
@Deprecated
public class StringDataSet {
//
//	/**
//	 * Represents the dataset
//	 */
//	private final Set<StringDSCell[]> data;
//
//	/**
//	 * The numberInPot of internal attributes of an apprentice
//	 */
//	private final String[] internalAttributeDescription;
//
//	private String[] attributes;
//
//	public StringDataSet(String... internalAtts) {
//		if (internalAtts == null) {
//			this.internalAttributeDescription = new String[0];
//		} else {
//			this.internalAttributeDescription = internalAtts;
//		}
//		this.data = new HashSet<StringDSCell[]>();
//		this.attributes = new String[0];
//	}
//
//	/**
//	 * Adds data to this dataset from an observed snapshot
//	 * 
//	 * @param snp
//	 * @return the snapshot processed as a line of the dataset
//	 */
//	public StringDSCell[] addData(Snapshot snp) {
//		if (attributes.length == 0) {
//			buildAttributeArray(snp);
//		} else if (attributes.length != calculateRowSizeFrom(snp)) {
//			System.err.println("ERROR(StringDataSet):Snapshot " + snp
//					+ " has different numberInPot of attributes");
//			return null;
//		}
//		StringDSCell[] line = buildDataSetLineFrom(snp.getConditions());
//		// add behaviour in last row
//		line[attributes.length - 1] = new StringDSCell(snp.getBehaviour(),
//				attributes.length - 1);
//		// add line to datasets
//		data.add(line);
//		return line;
//	}
//
//	/**
//	 * Builds the attribute array for this dataset
//	 * 
//	 * @param snp
//	 *            the snapshot from witch the array is built
//	 */
//	private void buildAttributeArray(Snapshot snp) {
//		// initialize attribute array
//		this.attributes = new String[calculateRowSizeFrom(snp)];
//		// build attribute list
//		int index = 0;
//		// start with snapshot conditions
//		for (Condition cond : snp.getConditions()) {
//			this.attributes[index] = LbOParser.buildUniqueAttributeDescriptionFor(
//					cond, index);
//			index++;
//		}
//		// put internal attributes
//		for (String rep : internalAttributeDescription) {
//			this.attributes[index] = LbOParser.buildUniqueAttributeDescriptionFor(
//					rep, index);
//			index++;
//		}
//		// finalize with behaviour
//		this.attributes[index] = LbOParser.buildAttributeDescriptionForBehaviour();
//	}
//
//	/**
//	 * @param conditions
//	 *            the list of conditions to build the dataset line
//	 * @return a line of the dataset from the provided list of conditions
//	 */
//	public StringDSCell[] buildDataSetLineFrom(List<Condition> conditions) {
//		StringDSCell[] line = new StringDSCell[attributes.length];
//		int index = 0;
//		// add conditions
//		for (Condition cnd : conditions) {
//			line[index] = new StringDSCell(cnd, index);
//			++index;
//		}
//		// add rest of elements except behaviour
//		while (index != attributes.length - 1) {
//			line[index] = new StringDSCell(getAttributes()[index], index);
//			++index;
//		}
//		return line;
//	}
//
//	/**
//	 * Adds data to this dataset from an observed snapshot
//	 * 
//	 * @param snp
//	 * @return true if data added successfully
//	 */
//	public boolean addData(StringDSCell[] holder) {
//		if (holder.length == attributes.length) {
//			data.add(holder);
//			return true;
//		}
//		System.err.println("ERROR(StringDataSet):Holder with different size");
//		return false;
//	}
//
//	/**
//	 * @return the list of attributes for this dataset.
//	 */
//	public String[] getAttributes() {
//		return this.attributes;
//	}
//
//	/**
//	 * Initializes the row size from the size of the conditions in the snapshot
//	 * 
//	 * @param snp
//	 *            the snapshot from where the size is retrieved
//	 */
//	private int calculateRowSizeFrom(Snapshot snp) {
//		return snp.getConditions().size() + internalAttributeDescription.length
//				+ 1;
//	}
//
//	public Iterator<StringDSCell[]> getData() {
//		return data.iterator();
//	}
//
//	/**
//	 * Assures that the behaviour has already been observed, meaning that it is
//	 * stored in the dataset
//	 * 
//	 * @return a list of behaviours from a string representation
//	 */
//	public List<Action> getBehaviourFrom(String rep) {
//		Iterator<StringDSCell[]> iterator = getData();
//		while (iterator.hasNext()) {
//			// compare with last element of the cell
//			StringDSCell[] line = iterator.next();
//			if (line[line.length - 1].value.equals(rep)) {
//				// found matching behaviour
//				return line[line.length - 1].getBehaviour();
//			}
//		}
//		System.err.println("Behaviour " + rep + " was not observed");
//		return null;
//	}

}
