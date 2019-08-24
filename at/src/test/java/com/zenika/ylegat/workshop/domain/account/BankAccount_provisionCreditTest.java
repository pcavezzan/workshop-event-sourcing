package com.zenika.ylegat.workshop.domain.account;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.Test;

public class BankAccount_provisionCreditTest extends AbstractBankAccountTesting {

    @Test
    public void should_provision_credit_with_success() {
        // Given
        /**
         * Given a bank account registered
         */
    	final BankAccount bankAccount = new BankAccount(eventStore); 
    	bankAccount.registerBankAccount("bankAccountId");
    	
        // When
        /**
         * When 1 credit is provisioned to the bank account (call BankAccount.provisionCredit)
         */
    	bankAccount.provisionCredit(5);
    	
        // Then
        /**
         * 1. assert that the events associated to te bank account contains exactly a BankAccountRegistered followed by a CreditProvisioned event
         * 2. assert on the state of the bank account (you can use Assertion.assertThat(actualBankAccount).isEqualTo(expectedBankAccount)) :
         * * it's id should be identical to the one created
         * * its credit should be equal to 1
         * * its version should be 2 (2 events have been applied on the bank account)
         */
    	assertThat(bankAccount).isEqualTo(new BankAccount("bankAccountId", eventStore, 5, 2));
    }

}
