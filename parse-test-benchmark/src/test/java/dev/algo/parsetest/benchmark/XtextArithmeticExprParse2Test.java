package dev.algo.parsetest.benchmark;

import com.google.inject.Injector;
import dev.algo.parsetest.xtext.ArithmeticExprStandaloneSetup;
import dev.algo.parsetest.xtext.arithmeticExpr.Model;
import org.eclipse.xtext.testing.util.ParseHelper;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class XtextArithmeticExprParse2Test {

    @Test
    void myTest(){
        ArithmeticExprStandaloneSetup setup = new ArithmeticExprStandaloneSetup();
        Injector injector = setup.createInjectorAndDoEMFRegistration();
        ParseHelper<Model> parseHelper = (ParseHelper<Model>) injector.getInstance(ParseHelper.class);

        assertNotNull(parseHelper, "Ita was null");
    }
}
