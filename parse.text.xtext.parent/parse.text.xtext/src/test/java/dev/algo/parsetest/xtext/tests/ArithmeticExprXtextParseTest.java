package dev.algo.parsetest.xtext.tests;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import com.google.inject.Inject;
import dev.algo.parsetest.xtext.arithmeticExpr.Model;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.xtext.testing.InjectWith;
import org.eclipse.xtext.testing.extensions.InjectionExtension;
import org.eclipse.xtext.testing.util.ParseHelper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(InjectionExtension.class)
@InjectWith(ArithmeticExprInjectorProvider.class)
public class ArithmeticExprXtextParseTest {

    @Inject
    private ParseHelper<Model> parseHelper;

    @Test
    public void testLoadModel() throws Exception {
        String input = "(13 * 7) * (2 * 17)";
        Model result = parseHelper.parse(input);

        assertNotNull(result, "Parsed model should not be null");

        EList<Resource.Diagnostic> errors = result.eResource().getErrors();
        assertTrue(errors.isEmpty(), "Unexpected errors: " + String.join(", ", errors.stream()
                .map(Object::toString)
                .toArray(String[]::new)));
    }
}
