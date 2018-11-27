package org.openehr.bmm.v2.validation.validators;

import org.apache.commons.lang.StringUtils;

import org.openehr.bmm.persistence.validation.BmmMessageIds;
import org.openehr.bmm.v2.persistence.*;
import org.openehr.bmm.v2.validation.BmmValidation;
import org.openehr.bmm.v2.validation.BmmValidationResult;
import org.openehr.bmm.v2.validation.BmmRepository;
import org.openehr.utils.message.MessageCode;
import org.openehr.utils.message.MessageLogger;

import javax.xml.validation.Validator;
import java.util.Map;

public class ClassesValidator extends ValidatorBase implements BmmValidation {

    private BmmValidationResult validationResult;
    private BmmRepository repository;
    private PBmmSchema schema;
    private PropertyValidator propertyValidator;

    @Override
    public void validate(BmmValidationResult validationResult, BmmRepository repository, MessageLogger logger, PBmmSchema schema) {

        setLogger(logger);
        this.validationResult = validationResult;
        this.repository = repository;
        this.schema = schema;
        propertyValidator = new PropertyValidator(logger, schema);

        schema.doAllClasses( pBmmClass -> {
            validateClass(pBmmClass);
        });
    }

    public void validateClass(PBmmClass pBmmClass) {
        //check that all ancestors exist
        pBmmClass.getAncestors().forEach(ancestorClassName -> {
            if(StringUtils.isEmpty(ancestorClassName)) {
                addValidityError(schema, pBmmClass.getSourceSchemaId(), BmmMessageIds.ec_BMM_ANCE, pBmmClass.getSourceSchemaId(), pBmmClass.getName());
            }
        });

        if(!logger.hasErrors()) {
            validateGenericParameters(pBmmClass);
        }

        // validate the properties
        for(PBmmProperty property:pBmmClass.getProperties().values()) {
            propertyValidator.validateProperty(pBmmClass, property);
        }
    }

    private void validateGenericParameters(PBmmClass pBmmClass) {
        //check that all generic parameter.conforms_to_type exist exists
        if(pBmmClass.isGeneric()) {
            Map<String, PBmmGenericParameter> genericParameterDefinitions = pBmmClass.getGenericParameterDefs();

            for(PBmmGenericParameter pBmmGenericParameter: genericParameterDefinitions.values()) {

                String conformsToType = pBmmGenericParameter.getConformsToType();
                if(conformsToType != null && !schema.hasClassOrPrimitiveDefinition(conformsToType)) {

                    addValidityError(schema, pBmmClass.getSourceSchemaId(), BmmMessageIds.ec_BMM_GPCT,
                            pBmmClass.getSourceSchemaId(),
                            pBmmClass.getName(),
                            pBmmGenericParameter.getName(),
                            conformsToType);

                }
            }

        }

    }

}
