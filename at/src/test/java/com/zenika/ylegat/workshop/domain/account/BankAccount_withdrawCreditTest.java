package com.zenika.ylegat.workshop.domain.account;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import org.junit.Test;
import com.zenika.ylegat.workshop.domain.common.InvalidCommandException;

public class BankAccount_withdrawCreditTest extends AbstractBankAccountTesting {

    @Test
    public void should_fail_withdrawing_more_credit_than_provisioned() {
        // Given
        BankAccount bankAccount = new BankAccount(eventStore);
        bankAccount.registerBankAccount("bankAccountId");

        // When
        Throwable invalidCommandException = catchThrowable(() -> bankAccount.withdrawCredit(1));

        // Then
        assertThat(invalidCommandException).isInstanceOf(InvalidCommandException.class);
        assertThatEvents("bankAccountId").containsExactly(new BankAccountRegistered("bankAccountId"));
    }

    @Test
    public void should_succeed_withdrawing_less_credit_than_provisioned() {
        // Given
        BankAccount bankAccount = new BankAccount(eventStore);
        bankAccount.registerBankAccount("bankAccountId");
        bankAccount.provisionCredit(1);

        // When
        bankAccount.withdrawCredit(1);

        // Then
        assertThatEvents("bankAccountId").containsExactly(new BankAccountRegistered("bankAccountId"),
                                                          new CreditProvisioned("bankAccountId", 1, 1),
                                                          new CreditWithdrawn("bankAccountId", 1, 0));

        assertThat(bankAccount).isEqualTo(new BankAccount("bankAccountId", eventStore, 0, 3));
    }

}
