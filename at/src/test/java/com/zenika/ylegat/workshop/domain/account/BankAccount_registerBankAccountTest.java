package com.zenika.ylegat.workshop.domain.account;

import org.assertj.core.api.Assertions;
import org.junit.Test;

import com.zenika.ylegat.workshop.domain.common.ConflictingEventException;

public class BankAccount_registerBankAccountTest extends AbstractBankAccountTesting {

    @Test
    public void should_register_bank_account_with_success() {
        // When
        /**
         * when bank account is registered (instantiate a bank account and call BankAccount.registerBankAccount)
         */
    	final BankAccount bankAccount = new BankAccount(eventStore);
    	bankAccount.registerBankAccount("bankAccountId");
        // Then
        /**
         * 1. assert that the events associated to the bank account contains exactly one BankAccountRegistered event (use assertThatEvents method defined in the superclass)
         * 2. assert on the state of the bank account (you can use Assertion.assertThat(actualBankAccount).isEqualTo(expectedBankAccount)) :
         * * it's id should be identical to the one created
         * * its credit should be equal to 0
         * * its version should be 1 (one event has been applied on the bank account)
         */
    	assertThatEvents("bankAccountId").containsExactly(new BankAccountRegistered("bankAccountId"));
    	Assertions.assertThat(bankAccount).isEqualTo(new BankAccount("bankAccountId", eventStore, 0, 1));
    }

    @Test
    public void should_fail_registering_bank_account_with_already_used_id() {
        // Given
        /**
         * Given a bank account registered (instantiate a bank account and call BankAccount.registerBankAccount)
         */
    	final BankAccount bankAccount = new BankAccount(eventStore);
    	bankAccount.registerBankAccount("bankAccountId");
        // When
        /**
         * When a bank account with the same id is registered (use Assertions.catchThrowable(() -> call_registerBankAccount_here() to catch the exception)
         */
    	final BankAccount bankAccount2 = new BankAccount(eventStore);
    	final Throwable throwable = Assertions.catchThrowable(() -> bankAccount2.registerBankAccount("bankAccountId"));
        // Then
        /**
         * 1. assert that the command thrown a ConflictingEventException exception
         * 2. assert that the events associated to the bank account contains exactly one BankAccountRegistered event
         */
    	Assertions.assertThat(throwable).isInstanceOfAny(ConflictingEventException.class);
    	
    }

}
