package com.zenika.ylegat.workshop.domain.account;

import static org.assertj.core.api.Assertions.assertThat;

import org.assertj.core.api.Assertions;
import org.junit.Test;

import com.zenika.ylegat.workshop.domain.common.InvalidCommandException;

public class BankAccount_transferInitializedTest extends AbstractBankAccountTesting {

    @Test
    public void should_fail_initializing_transfer_when_destination_is_same_than_initializer() {
        // Given
        /**
         * Given a bank account registered with 1 credit
         */
    	final BankAccount bankAccount = new BankAccount(eventStore);
    	bankAccount.registerBankAccount("bankAccountId");
    	bankAccount.provisionCredit(1);
        // When
        /**
         * when a transfer of 1 credit is initialized (BankAccount.initializeTransfer) at the same destination than the origin account
         */
    	final Throwable throwable = Assertions.catchThrowable(() ->  bankAccount.initializeTransfer("bankAccountId", 1));
    	
        // Then
        /**
         * then assert that:
         * 1. the command throws an invalidCommandException exception
         * 2. the events associated to the account only contains one BankAccountRegistered event
         */
    	assertThat(throwable).isInstanceOfAny(InvalidCommandException.class);
    	assertThatEvents("bankAccountId").contains(new BankAccountRegistered("bankAccountId"), new CreditProvisioned("bankAccountId", 1, 1));
    }

    @Test
    public void should_fail_initializing_transfer_when_credit_to_transfer_greater_than_available_credit() {
        // Given
        /**
         * Given a bank account registered with 0 credit
         */
    	final BankAccount bankAccount = new BankAccount(eventStore);
    	bankAccount.registerBankAccount("bankAccountId");

        // When
        /**
         * when a transfer of 1 credit is initialized (BankAccount.initializeTransfer) at destination of another bank account
         */
    	final Throwable throwable = Assertions.catchThrowable(() ->  bankAccount.initializeTransfer("bankAccountId", 1));
    	
        // Then
        /**
         * then assert that:
         * 1. the command throws an invalidCommandException exception
         * 2. the events associated to the account only contains one BankAccountRegistered event
         */
    	assertThat(throwable).isInstanceOfAny(InvalidCommandException.class);
    	assertThatEvents("bankAccountId").contains(new BankAccountRegistered("bankAccountId"));

    }

    @Test
    public void should_initialize_transfer() {
        // Given
        /**
         * Given a bank account registered and provisionoined with 1 credit
         */
    	final BankAccount bankAccount = new BankAccount(eventStore);
    	bankAccount.registerBankAccount("bankAccountId");
    	bankAccount.provisionCredit(1);
    	
    	final BankAccount destAccount = new BankAccount(eventStore);
    	destAccount.registerBankAccount("destAccountId");


        // When
        /**
         * when a transfer of 1 credit is initialized (BankAccount.initializeTransfer) at destination of another bank account
         */
    	final String transfertId = bankAccount.initializeTransfer("destAccountId", 1);
    	
        // Then
        /**
         * then assert that:
         * 1. the transfer id returned by the command is not null
         * 2. the events associated to the account only contains a BankAccountRegistered, followed by a CreditProvisioned followed by a TransferInitialized event
         * 3. the state of the bank account is:
         * * 0 credit
         * * a version of 3
         * * a pending event map containing the transfer initialized event
         */
    	assertThat(transfertId).isNotNull();
    	assertThatEvents("bankAccountId").contains(new BankAccountRegistered("bankAccountId"), new CreditProvisioned("bankAccountId", 1, 1), new TransferInitialized("bankAccountId", transfertId, "destAccountId", 1, 1));
    }

}
