/*
 * Copyright 2010-2011 Ning, Inc.
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

package com.ning.billing.jaxrs.json;

import java.math.BigDecimal;

import org.joda.time.DateTime;

import com.ning.billing.payment.api.Refund;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class RefundJson {

    private final String refundId;
    private final String paymentId;
    private final BigDecimal refundAmount;
    private final Boolean isAdjusted;
    private final DateTime requestedDate;
    private final DateTime effectiveDate;

    public RefundJson(final Refund input) {
        this(input.getId().toString(), input.getPaymentId().toString(), input.getRefundAmount(), input.isAdjusted(),
             input.getEffectiveDate(), input.getEffectiveDate());
    }

    @JsonCreator
    public RefundJson(@JsonProperty("refund_id") final String refundId,
                      @JsonProperty("paymentId") final String paymentId,
                      @JsonProperty("refundAmount") final BigDecimal refundAmount,
                      @JsonProperty("adjusted") final Boolean isAdjusted,
                      @JsonProperty("requestedDate") final DateTime requestedDate,
                      @JsonProperty("effectiveDate") final DateTime effectiveDate) {
        this.refundId = refundId;
        this.paymentId = paymentId;
        this.refundAmount = refundAmount;
        this.isAdjusted = isAdjusted;
        this.requestedDate = requestedDate;
        this.effectiveDate = effectiveDate;
    }

    public String getRefundId() {
        return refundId;
    }

    public String getPaymentId() {
        return paymentId;
    }

    public BigDecimal getRefundAmount() {
        return refundAmount;
    }

    public boolean isAdjusted() {
        return isAdjusted;
    }

    public DateTime getRequestedDate() {
        return requestedDate;
    }

    public DateTime getEffectiveDate() {
        return effectiveDate;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("RefundJson");
        sb.append("{refundId='").append(refundId).append('\'');
        sb.append(", paymentId='").append(paymentId).append('\'');
        sb.append(", refundAmount=").append(refundAmount);
        sb.append(", isAdjusted=").append(isAdjusted);
        sb.append(", requestedDate=").append(requestedDate);
        sb.append(", effectiveDate=").append(effectiveDate);
        sb.append('}');
        return sb.toString();
    }

    @Override
    public int hashCode() {
        int result = refundId != null ? refundId.hashCode() : 0;
        result = 31 * result + (paymentId != null ? paymentId.hashCode() : 0);
        result = 31 * result + (refundAmount != null ? refundAmount.hashCode() : 0);
        result = 31 * result + (isAdjusted != null ? isAdjusted.hashCode() : 0);
        result = 31 * result + (requestedDate != null ? requestedDate.hashCode() : 0);
        result = 31 * result + (effectiveDate != null ? effectiveDate.hashCode() : 0);
        return result;
    }

    @Override
    public boolean equals(final Object obj) {
        if (!this.equalsNoIdNoDates(obj)) {
            return false;
        } else {
            final RefundJson other = (RefundJson) obj;
            if (refundId == null) {
                if (other.getRefundId() != null) {
                    return false;
                }
            } else if (!refundId.equals(other.getRefundId())) {
                return false;
            }

            if (requestedDate == null) {
                if (other.getRequestedDate() != null) {
                    return false;
                }
            } else if (requestedDate.compareTo(other.getRequestedDate()) != 0) {
                return false;
            }

            if (effectiveDate == null) {
                if (other.getEffectiveDate() != null) {
                    return false;
                }
            } else if (effectiveDate.compareTo(other.getEffectiveDate()) != 0) {
                return false;
            }

            return true;
        }
    }

    public boolean equalsNoIdNoDates(final Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj == null) {
            return false;
        }

        if (getClass() != obj.getClass()) {
            return false;
        }

        final RefundJson other = (RefundJson) obj;
        if (isAdjusted == null) {
            if (other.isAdjusted != null) {
                return false;
            }
        } else if (!isAdjusted.equals(other.isAdjusted)) {
            return false;
        }

        if (paymentId == null) {
            if (other.paymentId != null) {
                return false;
            }
        } else if (!paymentId.equals(other.paymentId)) {
            return false;
        }

        if (refundAmount == null) {
            if (other.refundAmount != null) {
                return false;
            }
        } else if (!refundAmount.equals(other.refundAmount)) {
            return false;
        }

        return true;
    }
}