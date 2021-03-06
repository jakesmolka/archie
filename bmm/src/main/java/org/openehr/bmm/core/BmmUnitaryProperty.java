package org.openehr.bmm.core;

/*
 * #%L
 * OpenEHR - Java Model Stack
 * %%
 * Copyright (C) 2020 openEHR Foundation
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 * Author: Thomas Beale
 */

/**
 * Property whose type is unitary.
 *
 */
public class BmmUnitaryProperty extends BmmProperty<BmmUnitaryType> {

    public BmmUnitaryProperty (String name, BmmUnitaryType type, String aDocumentation, boolean isMandatoryFlag, boolean isComputedFlag) {
        super(name, type, aDocumentation, isMandatoryFlag, isComputedFlag);
    }

    public BmmUnitaryProperty(BmmUnitaryProperty other) {
        super (other);
    }

    public BmmUnitaryProperty() {
        super();
    }

}
