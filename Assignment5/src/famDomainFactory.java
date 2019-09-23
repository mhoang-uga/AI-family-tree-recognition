
import aima.core.logic.fol.domain.FOLDomain;

public class famDomainFactory {
	public static FOLDomain famDomain() {
		FOLDomain domain = new FOLDomain();
		//parents
		domain.addConstant("Minh");
		domain.addConstant("Ngoc");
		domain.addConstant("Phong");
		//siblings
		domain.addConstant("Vu");
		domain.addConstant("Lam");

		//grandparents
		domain.addConstant("Long");
		domain.addConstant("Phuong");
		
		//Uncle and Aunt
		domain.addConstant("Yen");
		domain.addConstant("Cuong");

		//predicate
		domain.addPredicate("Parent");
		domain.addPredicate("Male");
		domain.addPredicate("Female");
		domain.addPredicate("Father");
		domain.addPredicate("Mother");
		domain.addPredicate("Sibling");
		domain.addPredicate("Brother");
		domain.addPredicate("Sister");
		domain.addPredicate("Grandparent");
		domain.addPredicate("Grandmother");
		domain.addPredicate("Grandfather");
		domain.addPredicate("Aunt");
		domain.addPredicate("Uncle");
		domain.addPredicate("Diff");


		
		return domain;
	}
}