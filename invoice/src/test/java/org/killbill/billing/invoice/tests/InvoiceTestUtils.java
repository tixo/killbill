/*
 * Copyright 2010-2013 Ning, Inc.
 *
 * Ning licenses this file to you under the Apache License, version 2.0
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

package org.killbill.billing.invoice.tests;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.joda.time.DateTime;
import org.mockito.Mockito;
import org.testng.Assert;

import org.killbill.billing.catalog.api.Currency;
import org.killbill.billing.invoice.api.Invoice;
import org.killbill.billing.invoice.api.InvoiceApiException;
import org.killbill.billing.invoice.api.InvoiceItem;
import org.killbill.billing.invoice.api.InvoicePayment;
import org.killbill.billing.invoice.api.InvoicePaymentType;
import org.killbill.billing.invoice.dao.InvoiceDao;
import org.killbill.billing.invoice.dao.InvoiceItemModelDao;
import org.killbill.billing.invoice.dao.InvoiceModelDao;
import org.killbill.billing.invoice.model.FixedPriceInvoiceItem;
import org.killbill.billing.callcontext.InternalCallContext;
import org.killbill.clock.Clock;
import org.killbill.billing.entity.EntityPersistenceException;
import org.killbill.billing.invoice.api.InvoiceInternalApi;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

public class InvoiceTestUtils {

    private InvoiceTestUtils() {}

    public static Invoice createAndPersistInvoice(final InvoiceDao invoiceDao,
                                                  final Clock clock,
                                                  final BigDecimal amount,
                                                  final Currency currency,
                                                  final InternalCallContext internalCallContext) {
        try {
            return createAndPersistInvoice(invoiceDao, clock, ImmutableList.<BigDecimal>of(amount),
                                           currency, internalCallContext);
        } catch (EntityPersistenceException e) {
            Assert.fail(e.getMessage());
            return null;
        }
    }

    public static Invoice createAndPersistInvoice(final InvoiceDao invoiceDao,
                                                  final Clock clock,
                                                  final List<BigDecimal> amounts,
                                                  final Currency currency,
                                                  final InternalCallContext internalCallContext) throws EntityPersistenceException {
        final Invoice invoice = Mockito.mock(Invoice.class);
        final UUID invoiceId = UUID.randomUUID();
        final UUID accountId = UUID.randomUUID();

        Mockito.when(invoice.getId()).thenReturn(invoiceId);
        Mockito.when(invoice.getAccountId()).thenReturn(accountId);
        Mockito.when(invoice.getInvoiceDate()).thenReturn(clock.getUTCToday());
        Mockito.when(invoice.getTargetDate()).thenReturn(clock.getUTCToday());
        Mockito.when(invoice.getCurrency()).thenReturn(currency);
        Mockito.when(invoice.isMigrationInvoice()).thenReturn(false);

        final List<InvoiceItem> invoiceItems = new ArrayList<InvoiceItem>();
        final List<InvoiceItemModelDao> invoiceModelItems = new ArrayList<InvoiceItemModelDao>();
        for (final BigDecimal amount : amounts) {
            final InvoiceItem invoiceItem = createInvoiceItem(clock, invoiceId, accountId, amount, currency);
            invoiceModelItems.add(new InvoiceItemModelDao(invoiceItem));
            invoiceItems.add(invoiceItem);
        }
        Mockito.when(invoice.getInvoiceItems()).thenReturn(invoiceItems);

        invoiceDao.createInvoice(new InvoiceModelDao(invoice), invoiceModelItems, true, ImmutableMap.<UUID, List<DateTime>>of(), internalCallContext);

        return invoice;
    }

    public static InvoiceItem createInvoiceItem(final Clock clock, final UUID invoiceId, final UUID accountId, final BigDecimal amount, final Currency currency) {
        return new FixedPriceInvoiceItem(invoiceId, accountId, UUID.randomUUID(), UUID.randomUUID(),
                                         "charge back test", "charge back phase", clock.getUTCToday(), amount, currency);
    }

    public static InvoicePayment createAndPersistPayment(final InvoiceInternalApi invoicePaymentApi,
                                                         final Clock clock,
                                                         final UUID invoiceId,
                                                         final BigDecimal amount,
                                                         final Currency currency,
                                                         final InternalCallContext callContext) throws InvoiceApiException {
        final InvoicePayment payment = Mockito.mock(InvoicePayment.class);
        Mockito.when(payment.getId()).thenReturn(UUID.randomUUID());
        Mockito.when(payment.getType()).thenReturn(InvoicePaymentType.ATTEMPT);
        Mockito.when(payment.getInvoiceId()).thenReturn(invoiceId);
        Mockito.when(payment.getPaymentId()).thenReturn(UUID.randomUUID());
        Mockito.when(payment.getPaymentCookieId()).thenReturn(UUID.randomUUID().toString());
        Mockito.when(payment.getPaymentDate()).thenReturn(clock.getUTCNow());
        Mockito.when(payment.getAmount()).thenReturn(amount);
        Mockito.when(payment.getCurrency()).thenReturn(currency);
        Mockito.when(payment.getProcessedCurrency()).thenReturn(currency);

        invoicePaymentApi.notifyOfPayment(payment, callContext);

        return payment;
    }
}
