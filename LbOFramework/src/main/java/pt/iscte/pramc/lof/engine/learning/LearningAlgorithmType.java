/**
 * Copyright 2011 by Paulo R. Costa 
 * Distributed under the Artistic License.
 * This license appears at LICENSE file on the root of source folder. 
 */
package pt.iscte.pramc.lof.engine.learning;

import java.util.Arrays;
import java.util.List;

import pt.iscte.pramc.lof.engine.learning.ports.AlgorithmConnector;
import pt.iscte.pramc.lof.engine.learning.ports.WekaConnector;
import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.lazy.IBk;
import weka.classifiers.lazy.KStar;
import weka.classifiers.misc.HyperPipes;
import weka.classifiers.rules.NNge;
import weka.classifiers.trees.Id3;

/**
 * @author Paulo Costa (paulo.costa@iste.pt)
 *
 * Represents the several types of learning algorithms that can be used by the learning method
 *
 * @version 0.1
 * @since Nov 14, 2011
 */
public enum LearningAlgorithmType {
	
	W_IBK(new WekaConnector(IBk.class),Library.WEKA),
	W_NBAYES(new WekaConnector(NaiveBayes.class),Library.WEKA),
	W_ID3(new WekaConnector(Id3.class),Library.WEKA),
	W_NNGE(new WekaConnector(NNge.class),Library.WEKA),
	W_HPIPES(new WekaConnector(HyperPipes.class),Library.WEKA),
	W_KSTAR(new WekaConnector(KStar.class),Library.WEKA);
	
	enum Library{
		WEKA,
	};
	
	private AlgorithmConnector connector;
	private Library library;
	
	private LearningAlgorithmType(AlgorithmConnector conn,Library lib) {
		this.connector = conn;
		this.library = lib;
	}
	
	/**
	 * Gets the algorithm connector and sets the properties according to the passed properties files
	 */
	public AlgorithmConnector getAlgorithmConnector(String propsFile){
		connector.setProperties(propsFile);
		return connector;
	}
	
	public boolean isLibrary(Library lib){
		return this.library.equals(lib);
	}
	
	public List<LearningAlgorithmType> getForWeka(){
		return Arrays.asList(W_IBK);
	}
}
