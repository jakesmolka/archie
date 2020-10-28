package com.nedap.archie.archetypevalidator;

import com.nedap.archie.aom.Archetype;
import com.nedap.archie.aom.OperationalTemplate;
import com.nedap.archie.aom.terminology.ValueSet;
import com.nedap.archie.flattener.Flattener;
import com.nedap.archie.flattener.FlattenerConfiguration;
import com.nedap.archie.flattener.InMemoryFullArchetypeRepository;
import com.nedap.archie.testutil.TestUtil;
import org.junit.Test;
import org.openehr.referencemodels.BuiltinReferenceModels;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashSet;

import static org.junit.Assert.*;

public class TermCodeSpecializationTest {

    @Test
    public void validRequiredBindingStrength() throws IOException {
        Archetype parent = TestUtil.parseFailOnErrors("/com/nedap/archie/archetypevalidator/primitives/openEHR-EHR-CLUSTER.constraint_strength_parent.v1.0.0.adls");
        Archetype child = TestUtil.parseFailOnErrors("/com/nedap/archie/archetypevalidator/primitives/openEHR-EHR-CLUSTER.constraint_strength_valid_child.v1.0.0.adls");
        InMemoryFullArchetypeRepository repo = new InMemoryFullArchetypeRepository();
        repo.addArchetype(parent);
        repo.addArchetype(child);
        ArchetypeValidator archetypeValidator = new ArchetypeValidator(BuiltinReferenceModels.getMetaModels());
        repo.compile(archetypeValidator);
        for(ValidationResult validationResult:repo.getAllValidationResults()) {
            assertTrue(validationResult.toString(), validationResult.passes());
        }
        Flattener flattener = new Flattener(repo, BuiltinReferenceModels.getMetaModels(), FlattenerConfiguration.forOperationalTemplate());
        OperationalTemplate opt = (OperationalTemplate) flattener.flatten(child);
        ValueSet valueSet = opt.getTerminology().getValueSets().get("ac0.2");
        assertEquals(new LinkedHashSet(Arrays.asList("at1", "at2", "at3", "at0.1")), valueSet.getMembers());
    }

    @Test
    public void invalidRequiredBindingStrength() throws IOException {
        Archetype parent = TestUtil.parseFailOnErrors("/com/nedap/archie/archetypevalidator/primitives/openEHR-EHR-CLUSTER.constraint_strength_parent.v1.0.0.adls");
        Archetype child = TestUtil.parseFailOnErrors("/com/nedap/archie/archetypevalidator/primitives/openEHR-EHR-CLUSTER.constraint_strength_invalid_child.v1.0.0.adls");
        InMemoryFullArchetypeRepository repo = new InMemoryFullArchetypeRepository();
        repo.addArchetype(parent);
        repo.addArchetype(child);
        ArchetypeValidator archetypeValidator = new ArchetypeValidator(BuiltinReferenceModels.getMetaModels());
        repo.compile(archetypeValidator);
        ValidationResult validationResult = repo.getValidationResult("openEHR-EHR-CLUSTER.constraint_strength_invalid_child.v1.0.0");
        assertFalse(validationResult.toString(), validationResult.passes());
        assertTrue("VPOV error should be present", validationResult.getErrors().stream().filter(e -> e.getType() == ErrorType.VPOV).findFirst().isPresent());
    }

    @Test
    public void valueSetCodeChanges() throws IOException {
        Archetype parent = TestUtil.parseFailOnErrors("/com/nedap/archie/archetypevalidator/primitives/openEHR-EHR-CLUSTER.constraint_strength_parent.v1.0.0.adls");
        Archetype child = TestUtil.parseFailOnErrors("/com/nedap/archie/archetypevalidator/primitives/openEHR-EHR-CLUSTER.constraint_strength_change_valueset_code.v1.0.0.adls");
        InMemoryFullArchetypeRepository repo = new InMemoryFullArchetypeRepository();
        repo.addArchetype(parent);
        repo.addArchetype(child);
        ArchetypeValidator archetypeValidator = new ArchetypeValidator(BuiltinReferenceModels.getMetaModels());
        repo.compile(archetypeValidator);
        ValidationResult validationResult = repo.getValidationResult("openEHR-EHR-CLUSTER.constraint_strength_change_valueset_code.v1.0.0");
        assertFalse(validationResult.toString(), validationResult.passes());

        assertEquals(ErrorType.VPOV, validationResult.getErrors().get(0).getType());
        assertEquals("/items[id2]/value[id3]/defining_code", validationResult.getErrors().get(0).getPathInArchetype());
        //assertEquals(ErrorType.VPOV, validationResult.getErrors().get(1).getType());
        //assertEquals("/items[id4]/value[id5]/defining_code", validationResult.getErrors().get(1).getPathInArchetype());
        assertEquals(validationResult.getErrors().size(), 1);
    }

    @Test
    public void invalidConstraintStrenghKeyword() throws IOException {
        TestUtil.parseExpectErrorCode("/com/nedap/archie/archetypevalidator/primitives/openEHR-EHR-CLUSTER.constraint_strength_wrong_name.v1.0.0.adls", "Constraint status incorrect");
    }

    @Test
    public void invalidValueSetRedefinition() throws IOException {
        Archetype parent = TestUtil.parseFailOnErrors("/com/nedap/archie/archetypevalidator/primitives/openEHR-EHR-CLUSTER.constraint_strength_parent.v1.0.0.adls");
        Archetype child = TestUtil.parseFailOnErrors("/com/nedap/archie/archetypevalidator/primitives/openEHR-EHR-CLUSTER.constraint_strength_invalid_redefined_value-set.v1.0.0.adls");
        InMemoryFullArchetypeRepository repo = new InMemoryFullArchetypeRepository();
        repo.addArchetype(parent);
        repo.addArchetype(child);
        ArchetypeValidator archetypeValidator = new ArchetypeValidator(BuiltinReferenceModels.getMetaModels());
        repo.compile(archetypeValidator);
        ValidationResult validationResult = repo.getValidationResult("openEHR-EHR-CLUSTER.constraint_strength_invalid_redefined_value-set.v1.0.0");
        assertFalse(validationResult.toString(), validationResult.passes());
        assertTrue("VPOV error should be present in " + validationResult, validationResult.getErrors().stream().filter(e -> e.getType() == ErrorType.VALUESET_REDEFINITION_ERROR).findFirst().isPresent());
    }
}