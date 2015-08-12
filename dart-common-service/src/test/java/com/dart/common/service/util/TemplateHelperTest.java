package com.dart.common.service.util;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

/**
 * @author RMPader
 */
public class TemplateHelperTest {

    @Test
    public void testRender() throws Exception {
        Map<String, String> replacements = new HashMap<>();
        replacements.put("name", "John Doe");
        replacements.put("email", "email@email");
        replacements.put("brackets", "[huehuehuehuhue]");

        String template = "Hello [name]. [brackets] Your email has been identified as [email]. We will stalk you now.";
        String expected = "Hello John Doe. [huehuehuehuhue] Your email has been identified as email@email. We will stalk you now.";

        assertEquals(expected, TemplateHelper.render(template, replacements));

    }
}