/**
 * Copyright 2010 by Paulo R. Costa 
 * Distributed under the Artistic License.
 * This license appears at LICENSE file on the root of source folder. 
 */
package pt.iscte.pramc.lof.agent;



/*

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Date;
import pt.iscte.pramc.lof.domain.Step;
import pt.iscte.pramc.lof.engine.common.holders.PossibleBehaviour;
import pt.iscte.pramc.lof.engine.memory.SequentialMemoryEngine;
import pt.iscte.pramc.lof.engine.eval.EvaluationEngine;
import pt.iscte.pramc.lof.engine.memory.SequentialStorage;
import pt.iscte.pramc.sit.swi.di.ActionInstance;
import pt.iscte.pramc.sit.ext.Pair;
 */




/**
 * @author Paulo Costa (paulo.costa@iscte.pt)
 *
 * Provides logging facilities to the apprentice agents
 *
 * Commented to prevent slowing down apprentice
 *
 * @since 31 de Jan de 2012
 * @version 0.1 
 */
public aspect ApprenticeInfoCollector {
//
//	private Apprentice apprentice;
//	
//	private SequentialMemoryEngine memEngine;
//	
//	class Holder{
//		
//		public static final String HEAD = "Stage\t Confidence\t isCorrectBh\t " +
//				"Mem size \t Avg Repetitions\t Avg Following\t Mirror wgh\t Recall wgh" +
//				"\t conditions\t behaviour";
//		
//		private String stage = null;
//		private Double confidence = null;
//		private Boolean isCorrectBh = null;
//		private Integer memSize = null;
//		private Double avgReperitions = null;
//		private Double avgFollowing = null;
//		private Double mirrorWg = null;
//		private Double recallWg = null;
//		private String conditions = null;
//		private String behaviour = null;
//		
//		//---------Setters--------------
//		
//		public void setStage(String stage) {
//			this.stage = stage;
//		}
//		
//		public void setConfidence(Double confidence) {
//			this.confidence = confidence;
//		}
//		
//		public void setIsCorrectBh(Boolean correct) {
//			this.isCorrectBh = correct;
//		}
//		
//		public void setMemSize(Integer memSize) {
//			this.memSize = memSize;
//		}
//		
//		public void setAvgReperitions(Double avgReperitions) {
//			this.avgReperitions = avgReperitions;
//		}
//		
//		public void setAvgFollowing(Double avgFollowing) {
//			this.avgFollowing = avgFollowing;
//		}
//		
//		public void setMirrorWg(Double mirrorWg) {
//			this.mirrorWg = mirrorWg;
//		}
//		
//		public void setRecallWg(Double recallWg) {
//			this.recallWg = recallWg;
//		}
//		
//		public void setConditions(String conditions) {
//			this.conditions = conditions;
//		}
//		
//		public void setBehaviour(String behaviour) {
//			this.behaviour = behaviour;
//		}
//		
//		@Override
//		public String toString() {
//			return stage+"\t"
//			+ confidence+"\t"
//			+isCorrectBh+"\t"
//			+memSize+"\t"
//			+avgReperitions+"\t"
//			+avgFollowing+"\t"
//			+mirrorWg+"\t"
//			+recallWg+"\t"
//			+conditions+"\t"
//			+behaviour+"\t"
//			+"\n";
//		}
//	}
//	
//	private Holder line;
//	
//	/**
//	 * Capture apprentice intialization
//	 */
//	pointcut apprenticeInit(Apprentice app): initialization(Apprentice.new(..)) && this(app);
//	
//	pointcut apprenticeCycle(): execution(* Apprentice.learningRoutine());
//	
//	pointcut learningEvaluation(EvaluationEngine method,Step step): execution(boolean EvaluationEngine.evaluateLearning(Step)) && target(method) && args(step);
//	
//	pointcut executionEvaluation(EvaluationEngine method, Pair<List<LbOInstance<?>>,PossibleBehaviour> pbh): execution (* EvaluationEngine.evaluateExecutionOf(*)) && target(method) && args(pbh);
//	
//	/**
//	 * Comparing behaviours
//	 * @param bh1
//	 * @param bh2
//	 */
////	pointcut behaviourComparison(List<ActionInstance> bh1, List<ActionInstance> bh2): 
////		execution(boolean pt.iscte.pramc.lof.engine.common.Tools.doBehavioursMatch(List<ActionInstance>, List<ActionInstance>))
////		&& args(bh1,bh2);
//	
//	/**
//	 * Memory method calls
//	 * @param app
//	 */
//	pointcut memoryStorage(SequentialStorage storage) : execution(* SequentialStorage.add(..)) && target(storage);
//	
//	after(Apprentice app): apprenticeInit(app){
//		apprentice = app;
//		memEngine = app.getMemoryEngine();
//		cleanFile();
//		writeToFile("\n-------"+new Date(System.currentTimeMillis()).toString()+"\n"+Holder.HEAD+"\n");
//	}
//	
//	
//	//---On Learning Stage----
//	
//	/**
//	 * Write info on memory method
//	 */
//	before(EvaluationEngine method, Step step): learningEvaluation(method,step){
//		line = new Holder();
//		line.setStage("Learning");
//		line.setMemSize(memEngine.getMemorySize());
//		line.setAvgReperitions(memEngine.getAvgObservations());
//		line.setAvgFollowing(memEngine.getAvgFollowing());
//	}
//	
//	/**
//	 * Write info on evaluation 
//	 */
//	after(EvaluationEngine method, Step step) returning (boolean result): learningEvaluation(method,step){
//		line.setConfidence(method.getConfidence());
//		line.setIsCorrectBh(result);
//		line.setMirrorWg(method.getClassificationMethodWeight());
//		line.setRecallWg(method.getRecallMethodWeight());
//		line.setConditions(step.getConditions().toString());
//		line.setBehaviour(step.getBehaviour().toString());
//		writeToFile(line.toString());
//	}
//	
//	//---On Execution Stage----
//	/**
//	 * Prepare line
//	 */
//	before(EvaluationEngine method, Pair<List<LbOInstance<?>>,PossibleBehaviour> pbh): executionEvaluation(method,pbh){
//		line = new Holder();
//		line.setStage("Execution");
//		line.setMemSize(memEngine.getMemorySize());
//		line.setAvgReperitions(memEngine.getAvgObservations());
//		line.setAvgFollowing(memEngine.getAvgFollowing());
//	}
//	
//	after(EvaluationEngine method, Pair<List<LbOInstance<?>>,PossibleBehaviour> pbh): executionEvaluation(method,pbh){
//		line.setConfidence(method.getConfidence());
//		line.setIsCorrectBh(method.isCorrectlyExecuted());
//		line.setMirrorWg(method.getClassificationMethodWeight());
//		line.setRecallWg(method.getRecallMethodWeight());
//		line.setConditions(pbh.getFirst().toString());
//		line.setBehaviour(pbh.getSecond().behaviour.toString());
//		writeToFile(line.toString());
//	}
//	
//	//-----HELPER METHODS-----
//	
//	public void cleanFile(){
//		try {
//			BufferedWriter writer = new BufferedWriter(new FileWriter("output/"+apprentice.getClass().getSimpleName()+".log",false));
//			writer.write("") ;
//			writer.close() ;
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}
//	
//	public void writeToFile(String text){
//		try {
//			BufferedWriter writer = new BufferedWriter(new FileWriter("output/"+apprentice.getClass().getSimpleName()+".log",true));
//			writer.write(text) ;
//			writer.close() ;
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}
	
}

