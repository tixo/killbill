/*
 * Copyright 2014 The Billing Project, LLC
 *
 * The Billing Project licenses this file to you under the Apache License, version 2.0
 * (the "License"); you may not use this file except in compliance with the
 * License.  You may obtain a copy of the License at:
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */

package org.killbill.billing.catalog;

import java.net.URI;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

import org.killbill.billing.catalog.api.BillingPeriod;
import org.killbill.billing.catalog.api.PlanPhase;
import org.killbill.billing.catalog.api.Recurring;
import org.killbill.xmlloader.ValidatingConfig;
import org.killbill.xmlloader.ValidationError;
import org.killbill.xmlloader.ValidationErrors;

@XmlAccessorType(XmlAccessType.NONE)
public class DefaultRecurring extends ValidatingConfig<StandaloneCatalog> implements Recurring {

    @XmlElement(required = true)
    private BillingPeriod billingPeriod;

    @XmlElement(required = false)
    private DefaultInternationalPrice recurringPrice;

    // Not exposed in xml.
    private PlanPhase phase;

    @Override
    public BillingPeriod getBillingPeriod() {
        return billingPeriod;
    }

    @Override
    public DefaultInternationalPrice getRecurringPrice() {
        return recurringPrice;
    }

    @Override
    public void initialize(final StandaloneCatalog root, final URI uri) {
        if (recurringPrice != null) {
            recurringPrice.initialize(root, uri);
        }
    }

    @Override
    public ValidationErrors validate(final StandaloneCatalog catalog, final ValidationErrors errors) {

        //Validation: check for nulls
        if (billingPeriod == null) {
            errors.add(new ValidationError(String.format("Recurring section of Phase %s of plan %s has a recurring price but no billing period", phase.getPhaseType().toString(), phase.getPlan().getName()),
                                           catalog.getCatalogURI(), DefaultPlanPhase.class, phase.getPhaseType().toString()));
        }

        //Validation: if there is a recurring price there must be a billing period
        if ((recurringPrice != null) && (billingPeriod == null || billingPeriod == BillingPeriod.NO_BILLING_PERIOD)) {
            errors.add(new ValidationError(String.format("Recurring section of Phase %s of plan %s has a recurring price but no billing period", phase.getPhaseType().toString(), phase.getPlan().getName()),
                                           catalog.getCatalogURI(), DefaultPlanPhase.class, phase.getPhaseType().toString()));
        }

        //Validation: if there is no recurring price there should be no billing period
        if ((recurringPrice == null) && billingPeriod != BillingPeriod.NO_BILLING_PERIOD) {
            errors.add(new ValidationError(String.format("Recurring section of Phase %s of plan %s has no recurring price but does have a billing period. The billing period should be set to '%s'",
                                                         phase.getPhaseType().toString(), phase.getPlan().getName(), BillingPeriod.NO_BILLING_PERIOD),
                                           catalog.getCatalogURI(), DefaultPlanPhase.class, phase.getPhaseType().toString()));
        }
        return errors;
    }

    public DefaultRecurring setBillingPeriod(final BillingPeriod billingPeriod) {
        this.billingPeriod = billingPeriod;
        return this;
    }

    public DefaultRecurring setRecurringPrice(final DefaultInternationalPrice recurringPrice) {
        this.recurringPrice = recurringPrice;
        return this;
    }

    public DefaultRecurring setPhase(final PlanPhase phase) {
        this.phase = phase;
        return this;
    }
}
