package com.opensymphony.xwork2.validator.validators;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.opensymphony.xwork2.CompositeTextProvider;
import com.opensymphony.xwork2.TextProvider;
import com.opensymphony.xwork2.StrutsTextProviderFactory;
import com.opensymphony.xwork2.XWorkTestCase;
import com.opensymphony.xwork2.validator.DelegatingValidatorContext;
import com.opensymphony.xwork2.validator.ValidatorContext;
import com.opensymphony.xwork2.validator.VisitorValidatorTestAction;
import com.opensymphony.xwork2.validator.validators.VisitorFieldValidator.AppendingValidatorContext;

public class AppendingValidatorContextTest extends XWorkTestCase {

    private static final String FIRST_NAME = "first";
    private static final String SECOND_NAME = "second";
    private static final String FIELD_NAME = "fieldName";
    private static final String FULL_FIELD_NAME = FIRST_NAME + "." + SECOND_NAME + "." + FIELD_NAME;

    private VisitorValidatorTestAction action;
    private VisitorFieldValidator.AppendingValidatorContext validatorContext;

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        action = container.inject(VisitorValidatorTestAction.class);
        StrutsTextProviderFactory tpf = container.getInstance(StrutsTextProviderFactory.class);
        ValidatorContext vc1 = new DelegatingValidatorContext(action, tpf);

        VisitorFieldValidator.AppendingValidatorContext vc2 = new AppendingValidatorContext(vc1, createTextProvider(action, vc1), FIRST_NAME, "");
        validatorContext = new AppendingValidatorContext(vc2, createTextProvider(action, vc2), SECOND_NAME, "");
    }

    public void testGetFullFieldName() throws Exception {
        String fullFieldName = validatorContext.getFullFieldName(FIELD_NAME);
        assertEquals(FULL_FIELD_NAME, fullFieldName);
    }

    public void testAddFieldError() throws Exception {
        validatorContext.addFieldError(FIELD_NAME, "fieldError");
        assertTrue(action.hasFieldErrors());

        Map<String, List<String>> fieldErrors = action.getFieldErrors();
        assertEquals(1, fieldErrors.size());
        assertTrue(fieldErrors.containsKey(FULL_FIELD_NAME));
    }

    private CompositeTextProvider createTextProvider(Object o, ValidatorContext parent) {
        List<TextProvider> textProviders = new LinkedList<>();
        if (o instanceof TextProvider) {
            textProviders.add((TextProvider) o);
        }
        textProviders.add(parent);

        return new CompositeTextProvider(textProviders);
    }

}
