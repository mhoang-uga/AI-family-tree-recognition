import aima.core.logic.fol.domain.DomainFactory;
import aima.core.logic.fol.domain.FOLDomain;
import aima.core.logic.fol.inference.InferenceProcedure;
import aima.core.logic.fol.kb.FOLKnowledgeBase;
/**
 * @author Ciaran O'Reilly
 * 
 */
public class famKnowledgeBaseFactory {

	public static FOLKnowledgeBase createFamKnowledgeBase(
			InferenceProcedure infp) {
		FOLDomain domain = famDomainFactory.famDomain();
		FOLKnowledgeBase kb = new FOLKnowledgeBase(domain,
				infp);
		//Rule
		kb.tell("(Diff(x,y) => Diff(y,x))");
		kb.tell("( (Parent(x,y) AND Female(x)) => Mother(x,y) )");
		kb.tell("( (Parent(x,y) AND Male(x)) => Father(x,y) )");
		
		kb.tell("( (Parent(x,y) AND Parent(y,z)) => Grandparent(x,z) )");
		kb.tell("( (Mother(x,y) AND Parent(y,z)) => Grandmother(x,z) )");
		kb.tell("( (Father(x,y) AND Parent(y,z)) => Grandfather(x,z) )");
		
		//kb.tell("Diff(Minh,Vu)");
		for (String n1: domain.getConstants()) {
			for (String n2: domain.getConstants()) {
				if (!n1.equals(n2)) {
					kb.tell("Diff("+n1+","+n2+")");
				}
			}
		}
		
		kb.tell("( (Diff(y,z) AND Parent(x,y) AND Parent(x,z)) => Sibling(y,z) )");
		kb.tell("( (Sibling(x,y) AND Female(x)) => Sister(x,y) )");
		kb.tell("( (Sibling(x,y) AND Male(x)) => Brother(x,y) )");

		kb.tell("( ( Diff(x,y) AND Brother(x,y) AND Parent(y,z)) => Uncle(x,z) )");
		kb.tell("( ( Diff(x,y) AND Sister(x,y) AND Parent(y,z)) => Aunt(x,z) )");


		//parent and siblings
		kb.tell("Parent(Ngoc, Minh)");
		kb.tell("Parent(Ngoc, Vu)");
		kb.tell("Parent(Ngoc, Lam)");
		kb.tell("Parent(Phong, Minh)");
		kb.tell("Parent(Phong, Vu)");
		kb.tell("Parent(Phong, Lam)");
		kb.tell("Female(Ngoc)");
		kb.tell("Male(Phong)");
		kb.tell("Male(Vu)");
		kb.tell("Female(Lam)");

		//grandparent
		kb.tell("Parent(Long, Phong)");
		kb.tell("Parent(Phuong, Phong)");

		kb.tell("Female(Phuong)");
		kb.tell("Male(Long)");
		
		//Aunt and uncle
		kb.tell("Parent(Long, Yen)");
		kb.tell("Parent(Long, Cuong)");
		kb.tell("Parent(Phuong, Yen)");
		kb.tell("Parent(Phuong, Cuong)");

		kb.tell("Female(Yen)");
		kb.tell("Male(Cuong)");

		return kb;
	}
}
