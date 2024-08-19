package dev.algo.parsetest.xtext.tests;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.resource.Resource.Diagnostic;
import org.eclipse.xtext.testing.InjectWith;
import org.eclipse.xtext.testing.extensions.InjectionExtension;
import org.eclipse.xtext.testing.util.ParseHelper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import com.google.inject.Inject;

import dev.algo.parsetest.xtext.arithmeticExpr.Model;

@ExtendWith(InjectionExtension.class)
@InjectWith(ArithmeticExprInjectorProvider.class)
class ArithmeticExprParseTest {

	@Inject
	ParseHelper<Model> parseHelper;
	
	@Test
	void loadModel() throws Exception {
		Model result = parseHelper.parse("(13 * 7) * (2 * 17)");
		assertNotNull(result, "Unexpected null parse result");
		
		EList<Diagnostic> errors = result.eResource().getErrors();
		assertTrue(errors.isEmpty(), "Unexpected errors: " + errors);
	}

}
