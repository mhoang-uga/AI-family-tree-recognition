import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Set;

import aima.core.logic.fol.CNFConverter;
import aima.core.logic.fol.StandardizeApartIndexicalFactory;
import aima.core.logic.fol.Unifier;
import aima.core.logic.fol.domain.DomainFactory;
import aima.core.logic.fol.domain.FOLDomain;
import aima.core.logic.fol.inference.Demodulation;
import aima.core.logic.fol.inference.FOLBCAsk;
import aima.core.logic.fol.inference.FOLFCAsk;
import aima.core.logic.fol.inference.FOLModelElimination;
import aima.core.logic.fol.inference.FOLOTTERLikeTheoremProver;
import aima.core.logic.fol.inference.FOLTFMResolution;
import aima.core.logic.fol.inference.InferenceProcedure;
import aima.core.logic.fol.inference.InferenceResult;
import aima.core.logic.fol.inference.Paramodulation;
import aima.core.logic.fol.inference.proof.Proof;
import aima.core.logic.fol.inference.proof.ProofPrinter;
import aima.core.logic.fol.kb.FOLKnowledgeBase;
import aima.core.logic.fol.kb.FOLKnowledgeBaseFactory;
import aima.core.logic.fol.kb.data.CNF;
import aima.core.logic.fol.kb.data.Clause;
import aima.core.logic.fol.kb.data.Literal;
import aima.core.logic.fol.parsing.FOLParser;
import aima.core.logic.fol.parsing.ast.AtomicSentence;
import aima.core.logic.fol.parsing.ast.Constant;
import aima.core.logic.fol.parsing.ast.Predicate;
import aima.core.logic.fol.parsing.ast.Sentence;
import aima.core.logic.fol.parsing.ast.Term;
import aima.core.logic.fol.parsing.ast.TermEquality;
import aima.core.logic.fol.parsing.ast.Variable;

public class Demo {
	public static void main(String[] args) {
		fOL_fcAskDemo();
	}
	private static void fOL_fcAskDemo() {
		System.out.println("---------------------------");
		System.out.println("Forward Chain, Fam Demo 1");
		System.out.println("---------------------------");
		famDemo1(new FOLFCAsk(), "Grandfather");
		famDemo1(new FOLFCAsk(), "Grandmother");
		famDemo1(new FOLFCAsk(), "Father");
		famDemo1(new FOLFCAsk(), "Mother");
		famDemo1(new FOLFCAsk(), "Aunt");
		famDemo1(new FOLFCAsk(), "Uncle");
		famDemo1(new FOLFCAsk(), "Brother");
		famDemo1(new FOLFCAsk(), "Sister");
	}


	/**
	 * To run the proof and get answer from question
	 * @param ip the inference procedure
	 * @param fam the String that get the relationship you want to ask
	*/
	private static void famDemo1(InferenceProcedure ip, String fam) {
		StandardizeApartIndexicalFactory.flush();

		FOLKnowledgeBase kb = famKnowledgeBaseFactory
				.createFamKnowledgeBase(ip);

		//String kbStr = kb.toString();

		List<Term> terms = new ArrayList<Term>();
		terms.add(new Variable("x"));
		terms.add(new Constant("Minh"));

		Predicate query = new Predicate(fam, terms);

		InferenceResult answer = kb.ask(query);

		//System.out.println("Fam Knowledge Base:");
		//System.out.println(kbStr);
		System.out.println("Query: " + query);
		for (Proof p : answer.getProofs()) {
			System.out.print(ProofPrinter.printProof(p));
			System.out.println("");
		}
	}
}